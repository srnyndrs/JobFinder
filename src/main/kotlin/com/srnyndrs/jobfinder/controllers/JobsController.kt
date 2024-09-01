package com.srnyndrs.jobfinder.controllers

import com.srnyndrs.jobfinder.domain.dto.JobDto
import com.srnyndrs.jobfinder.domain.dto.JobSummaryDto
import com.srnyndrs.jobfinder.exceptions.InvalidCompanyException
import com.srnyndrs.jobfinder.exceptions.InvalidJobException
import com.srnyndrs.jobfinder.services.JobService
import com.srnyndrs.jobfinder.toJobDto
import com.srnyndrs.jobfinder.toJobSummary
import com.srnyndrs.jobfinder.toJobSummaryDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin(value = ["http://localhost:3000"])
@RestController
@RequestMapping(path = ["/v1/jobs"])
@Tag(name = "Jobs")
class JobsController(
    private val jobService: JobService
) {

    @PostMapping(consumes = ["application/json"], produces = ["application/json"])
    @Operation(summary = "Create a new job")
    fun createJob(
        @RequestBody jobSummaryDto: JobSummaryDto
    ): ResponseEntity<JobSummaryDto> {
        return try {
            val savedJob = jobService
                .createJob(jobSummaryDto.toJobSummary())
                .toJobSummaryDto()
            ResponseEntity(savedJob, HttpStatus.CREATED)
        } catch (exception: IllegalArgumentException) {
            // Job ID is not null
            ResponseEntity(HttpStatus.BAD_REQUEST)
        } catch (exception: IllegalStateException) {
            // Company is not found
            ResponseEntity(HttpStatus.BAD_REQUEST)
        } catch (exception: InvalidJobException) {
            // Job ID is null
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (exception: InvalidCompanyException) {
            // Company ID is null
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping
    @Operation(summary = "Read jobs by a given company")
    fun readManyJobs(
        @Parameter(description = "The id of the company")
        @RequestParam("companyId") companyId: Long? = null
    ): List<JobSummaryDto> {
        return jobService.readJobs(companyId).map { it.toJobSummaryDto() }
    }

    @GetMapping(path = ["/{id}"])
    @Operation(summary = "Read a specific job by id")
    fun readOneJob(
        @Parameter(description = "The id of the job")
        @PathVariable("id") id: Long
    ): ResponseEntity<JobDto> {
        return jobService.readJob(id)?.let {
            ResponseEntity(it.toJobDto(), HttpStatus.OK)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @PutMapping(path = ["/{id}"], consumes = ["application/json"], produces = ["application/json"])
    @Operation(summary = "Update a job")
    fun updateJob(
        @Parameter(description = "The id of the job")
        @PathVariable("id") id: Long,
        @RequestBody jobSummaryDto: JobSummaryDto
    ): ResponseEntity<JobDto> {
        return try {
            val updatedJob = jobService.updateJob(id, jobSummaryDto.toJobSummary())
            ResponseEntity(updatedJob.toJobDto(), HttpStatus.OK)
        } catch (exception: IllegalArgumentException) {
            // Job not exist
            ResponseEntity(HttpStatus.BAD_REQUEST)
        } catch (exception: IllegalStateException) {
            // Company not exist
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @DeleteMapping(path = ["/{id}"])
    @Operation(summary = "Delete a job by id")
    fun deleteJob(
        @Parameter(description = "The id of the job")
        @PathVariable("id") id: Long
    ): ResponseEntity<Unit> {
        jobService.deleteJob(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

}