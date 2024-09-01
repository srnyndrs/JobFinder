package com.srnyndrs.jobfinder.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.srnyndrs.jobfinder.*
import com.srnyndrs.jobfinder.services.JobService
import io.mockk.every
import org.hamcrest.CoreMatchers.equalTo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*
import kotlin.test.Test

@SpringBootTest
@AutoConfigureMockMvc
class JobsControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    @MockkBean val jobService: JobService
) {

    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `test that createJob returns HTTP 201 when successfully created`() {
        // Create entities
        val company = testCompanyEntityA(id = 1)
        val job = testJobEntityA(id = null, companyEntity = company)

        // Create summary dto objects
        val companySummaryDto = testCompanySummaryDtoA(id = 1)
        val jobSummaryDto = testJobSummaryDtoA(id = null, companySummaryDto = companySummaryDto)

        // Service will return the job
        every {
            jobService.createJob(any())
        } answers {
            job.copy(id = 1)
        }

        // Post request
        mockMvc.post(JOBS_BASE_URL) {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(jobSummaryDto)
        }.andExpect {
            // Controller should return HTTP 201
            status { isCreated() }
        }
    }

    @Test
    fun `test that createJob returns HTTP 400 when IllegalArgumentException is thrown`() {
        // Create summary dto objects
        val companySummaryDto = testCompanySummaryDtoA(id = 1)
        val jobSummaryDto = testJobSummaryDtoA(id = 1, companySummaryDto = companySummaryDto)

        // Service will throw IllegalArgumentException
        every {
            jobService.createJob(any())
        } throws IllegalArgumentException()

        // Post request
        mockMvc.post(JOBS_BASE_URL) {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(jobSummaryDto)
        }.andExpect {
            // Controller should return HTTP 400
            status { isBadRequest() }
        }
    }

    @Test
    fun `test that createJob returns HTTP 400 when IllegalStateException is thrown`() {
        // Create summary dto objects
        val companySummaryDto = testCompanySummaryDtoA(id = 999)
        val jobSummaryDto = testJobSummaryDtoA(id = 1, companySummaryDto = companySummaryDto)

        // Service will throw IllegalStateException
        every {
            jobService.createJob(any())
        } throws IllegalStateException()

        // Post request
        mockMvc.post(JOBS_BASE_URL) {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(jobSummaryDto)
        }.andExpect {
            // Controller should return HTTP 400
            status { isBadRequest() }
        }
    }

    @Test
    fun `test that readManyJobs returns with an empty list and HTTP 200 if there are no jobs in the database`() {
        // Service will return with an empty list
        every {
            jobService.readJobs()
        } answers {
            emptyList()
        }

        // Get request
        mockMvc.get(JOBS_BASE_URL) {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            // Controller should return HTTP 200
            status { isOk() }
            // Content should be an empty tuple
            content { json("[]") }
        }
    }

    @Test
    fun `test that readManyJobs returns with jobs and HTTP 200 if there are jobs in the database`() {
        // Service will return with one test element
        every {
            jobService.readJobs()
        } answers {
            listOf(testJobEntityA(id = 1, testCompanyEntityA(id = 1)))
        }

        // Get request
        mockMvc.get(JOBS_BASE_URL) {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            // Controller should return HTTP 200
            status { isOk() }
            // Content fields should be equal to the testJobEntity fields with an ID of 1
            content { jsonPath("$[0].id", equalTo(1)) }
            content { jsonPath("$[0].title", equalTo("Job Summary A")) }
            content { jsonPath("$[0].description", equalTo("An amazing job")) }
            content { jsonPath("$[0].location", equalTo("Budapest")) }
            content { jsonPath("$[0].created", equalTo("2024-08-16")) }
            content { jsonPath("$[0].jobType", equalTo("Full-Time")) }
            content { jsonPath("$[0].remote", equalTo("Remote")) }
            content { jsonPath("$[0].company.id", equalTo(1)) }
            content { jsonPath("$[0].company.name", equalTo("Company A")) }
            content { jsonPath("$[0].company.image", equalTo("company-image.jpeg")) }
        }
    }

    @Test
    fun `test that readOneJob returns with HTTP 404 when job not found in the database`() {
        // Service will return null -> the job does not exist
        every {
            jobService.readJob(any())
        } answers {
            null
        }

        // Get request
        mockMvc.get("$JOBS_BASE_URL/999") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            // Controller should return HTTP 404
            status { isNotFound() }
        }
    }

    @Test
    fun `test that readOneJob returns with a job and HTTP 200 when successfully found in the database`() {
        // Service will return test company with an id of 999
        every {
            jobService.readJob(any())
        } answers {
            testJobEntityA(id = 999, testCompanyEntityA(id = 1))
        }

        // Get request
        mockMvc.get("$JOBS_BASE_URL/999") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            // Controller should return HTTP 200
            status { isOk() }
            // Content fields should be the same as the test job
            content { jsonPath("$.id", equalTo(999)) }
            content { jsonPath("$.title", equalTo("Job Summary A")) }
            content { jsonPath("$.description", equalTo("An amazing job")) }
            content { jsonPath("$.location", equalTo("Budapest")) }
            content { jsonPath("$.created", equalTo("2024-08-16")) }
            content { jsonPath("$.jobType", equalTo("Full-Time")) }
            content { jsonPath("$.remote", equalTo("Remote")) }
            content { jsonPath("$.company.id", equalTo(1)) }
            content { jsonPath("$.company.name", equalTo("Company A")) }
            content { jsonPath("$.company.description", equalTo("An amazing company")) }
            content { jsonPath("$.company.image", equalTo("company-image.jpeg")) }
        }
    }

    @Test
    fun `test that updateJob returns HTTP 200 and updated job on a successful call`() {
        // Create entities
        val company = testCompanyEntityA(id = 1)
        val job = testJobEntityA(id = 1, company)

        // Create job summary dto object
        val jobSummaryDto = job.toJobSummaryDto()

        // Service will return with the second parameter which is the given entity
        every {
            jobService.updateJob(any(), any())
        } answers {
            job.copy(id = 1)
        }

        // Put request
        mockMvc.put("$JOBS_BASE_URL/1") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(jobSummaryDto)
        }.andExpect {
            // Controller should return HTTP 200
            status { isOk() }
            // Content fields should be the same as the test job
            content { jsonPath("$.id", equalTo(1)) }
            content { jsonPath("$.title", equalTo("Job Summary A")) }
            content { jsonPath("$.description", equalTo("An amazing job")) }
            content { jsonPath("$.location", equalTo("Budapest")) }
            content { jsonPath("$.created", equalTo("2024-08-16")) }
            content { jsonPath("$.jobType", equalTo("Full-Time")) }
            content { jsonPath("$.remote", equalTo("Remote")) }
            content { jsonPath("$.company.id", equalTo(1)) }
            content { jsonPath("$.company.name", equalTo("Company A")) }
            content { jsonPath("$.company.image", equalTo("company-image.jpeg")) }
        }
    }

    @Test
    fun `test that updateJob returns HTTP 400 when IllegalArgumentException is thrown if the job does not exist`() {
        // Create summary dto objects
        val companySummaryDto = testCompanySummaryDtoA(id = 1)
        val jobSummaryDto = testJobSummaryDtoA(id = null, companySummaryDto = companySummaryDto)

        // Service will throw IllegalArgumentException -> the job does not exist
        every {
            jobService.updateJob(any(), any())
        } throws IllegalArgumentException()

        // Put request
        mockMvc.put("$JOBS_BASE_URL/1") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(jobSummaryDto)
        }.andExpect {
            // Controller should return HTTP 400
            status { isBadRequest() }
        }
    }

    @Test
    fun `test that updateJob returns HTTP 400 when IllegalStateException is thrown if the company does not exist`() {
        // Create summary dto objects
        val companySummaryDto = testCompanySummaryDtoA(id = 999)
        val jobSummaryDto = testJobSummaryDtoA(id = 1, companySummaryDto = companySummaryDto)

        // Service will throw IllegalStateException -> the company does not exist
        every {
            jobService.updateJob(any(), any())
        } throws IllegalStateException()

        // Put request
        mockMvc.put("$JOBS_BASE_URL/1") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(jobSummaryDto)
        }.andExpect {
            // Controller should return HTTP 400
            status { isBadRequest() }
        }
    }

    @Test
    fun `test that deleteJob returns HTTP 204 on successful delete`() {
        // Service will return with empty content
        every {
            jobService.deleteJob(any())
        } answers {
            // Empty content
        }

        // Delete request
        mockMvc.delete("$JOBS_BASE_URL/999") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            // Controller should return HTTP 204
            status { isNoContent() }
        }
    }
}