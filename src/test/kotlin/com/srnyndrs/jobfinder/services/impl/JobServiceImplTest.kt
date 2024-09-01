package com.srnyndrs.jobfinder.services.impl

import com.srnyndrs.jobfinder.*
import com.srnyndrs.jobfinder.domain.CompanySummary
import com.srnyndrs.jobfinder.repositories.CompanyRepository
import com.srnyndrs.jobfinder.repositories.JobRepository
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
@Transactional
class JobServiceImplTest @Autowired constructor(
    val underTest: JobServiceImpl,
    private val jobRepository: JobRepository,
    private val companyRepository: CompanyRepository
) {

    @Test
    fun `test that createJob throws IllegalArgumentException when book id already exists`() {
        // Create summary objects
        val companySummary = CompanySummary(id = 1)
        val jobSummary = testJobSummaryA(id = 999L, companySummary = companySummary)

        assertThrows<IllegalArgumentException> {
            // The book id is not null -> IllegalArgumentException should be thrown
            underTest.createJob(jobSummary)
        }
    }

    @Test
    fun `test that createJob throws IllegalStateException when company does not exist`() {
        // Create summary objects
        val companySummary = CompanySummary(id = 999L)
        val jobSummary = testJobSummaryA(companySummary = companySummary)

        assertThrows<IllegalStateException> {
            // The company does not exist yet -> IllegalStateException should be thrown
            underTest.createJob(jobSummary)
        }
    }

    @Test
    fun `test that createJob persists the job in the database`() {
        // Save company
        val savedCompany = companyRepository.save(testCompanyEntityA())
        assertThat(savedCompany).isNotNull()

        // Create summary objects
        val companySummary = savedCompany.toCompanySummaryDto().toCompanySummary()
        val jobSummary = testJobSummaryA(companySummary = companySummary)

        // Save job
        val savedJob = underTest.createJob(jobSummary)

        // The returned job should have an id now
        assertThat(savedJob.id).isNotNull()

        // Expected job
        val expected = testJobSummaryA(id = savedJob.id!!, companySummary)

        // Recall the saved job
        val recalledJob = jobRepository.findByIdOrNull(savedJob.id)

        // The job should be in the database
        assertThat(recalledJob).isNotNull()

        // The job should be the expected
        assertThat(recalledJob!!.toJobSummaryDto().toJobSummary()).isEqualTo(expected)
    }

    @Test
    fun `test that readJobs returns empty list when there are no jobs in the database`() {
        // Initially it should return with an empty list
        val result = underTest.readJobs()
        assertThat(result).isEmpty()
    }

    @Test
    fun `test that readJobs returns jobs when there are jobs in the database`() {
        // Save company
        val savedCompany = companyRepository.save(testCompanyEntityA())
        assertThat(savedCompany).isNotNull()

        // Create job entity
        val jobEntity = testJobEntityA(companyEntity = savedCompany)

        // Save job
        val savedJob = jobRepository.save(jobEntity)
        assertThat(savedJob).isNotNull()

        // Expected job
        val expected = testJobEntityA(id = savedJob.id, companyEntity = testCompanyEntityA(id = savedCompany.id))

        // Read jobs
        val result = underTest.readJobs()

        // Check if it has only one element as expected
        assertThat(result).hasSize(1)

        // Check if the returned job equals the expected one
        assertThat(result[0]).isEqualTo(expected)
    }

     @Test
     fun `test that readJobs returns empty list when company ID does not match`() {
         // Save company
         val savedCompany = companyRepository.save(testCompanyEntityA())
         assertThat(savedCompany).isNotNull()

         // Create job entity
         val jobEntity = testJobEntityA(companyEntity = savedCompany)

         // Save job
         val savedJob = jobRepository.save(jobEntity)
         assertThat(savedJob).isNotNull()

         // Read jobs with a different company ID
         val result = underTest.readJobs(companyId = (savedCompany.id!! + 1L))

         // Check if the returned list is empty
         assertThat(result).hasSize(0)
     }

    @Test
    fun `test that readJobs returns jobs when company ID does match`() {
        // Save company
        val savedCompany = companyRepository.save(testCompanyEntityA())
        assertThat(savedCompany).isNotNull()

        // Create job entity
        val jobEntity = testJobEntityA(companyEntity = savedCompany)

        // Save job
        val savedJob = jobRepository.save(jobEntity)
        assertThat(savedJob).isNotNull()

        // Expected job
        val expected = testJobEntityA(id = savedJob.id, companyEntity = testCompanyEntityA(id = savedCompany.id))

        // Read jobs with the saved company ID
        val result = underTest.readJobs(companyId = savedCompany.id)

        // Check if the returned list has only one element
        assertThat(result).hasSize(1)

        // Check if the returned job is the expected one
        assertThat(result[0]).isEqualTo(expected)
    }

    @Test
    fun `test that readJob returns null when job not found in the database`() {
        // Initially job with id = 1 does not exist
        val result = underTest.readJob(id = 1)
        // Result should be null
        assertThat(result).isNull()
    }

    @Test
    fun `test that readJob returns job when the job is found in the database`() {
        // Save company
        val savedCompany = companyRepository.save(testCompanyEntityA())
        assertThat(savedCompany).isNotNull()

        // Create job entity
        val jobEntity = testJobEntityA(companyEntity = savedCompany)

        // Save job
        val savedJob = jobRepository.save(jobEntity)
        assertThat(savedJob).isNotNull()
        assertThat(savedJob.id).isNotNull()

        // Expected job
        val expected = testJobEntityA(id = savedJob.id, companyEntity = testCompanyEntityA(id = savedCompany.id))

        // Read job with the saved ID
        val result = underTest.readJob(savedJob.id!!)

        // The returned job should be equal
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `test that updateJob successfully updates the job in the database`() {
        // Save company
        val savedCompany = companyRepository.save(testCompanyEntityA())
        assertThat(savedCompany).isNotNull()

        // Create job entity
        val jobEntity = testJobEntityA(companyEntity = savedCompany)

        // Save job
        val existingJob = jobRepository.save(jobEntity)
        assertThat(existingJob).isNotNull()

        // Create update request
        val existingJobId = existingJob.id!!
        val updatedJob = testJobEntityB(id = existingJobId, companyEntity = savedCompany)
        val updatedJobSummary = updatedJob.toJobSummaryDto().toJobSummary()

        // Update job
        val result = underTest.updateJob(existingJobId, updatedJobSummary)
        assertThat(result).isEqualTo(updatedJob)

        // Recall job
        val recalledJob = jobRepository.findByIdOrNull(existingJobId)

        // Returned job should exist
        assertThat(recalledJob).isNotNull()

        // Return job should be the same as expected
        assertThat(recalledJob).isEqualTo(updatedJob)
    }

    @Test
    fun `test that updateJob throws IllegalArgumentException when the job does not exist`() {
        // Save company
        val savedCompany = companyRepository.save(testCompanyEntityA())
        assertThat(savedCompany).isNotNull()

        // Create update request to a non existing job
        val nonExistingJobId = 999L
        val updatedJob = testJobEntityB(id = nonExistingJobId, companyEntity = savedCompany)
        val updatedJobSummary = updatedJob.toJobSummaryDto().toJobSummary()

        assertThrows<IllegalArgumentException> {
            // The job does not exist -> IllegalArgumentException should be thrown
            underTest.updateJob(nonExistingJobId, updatedJobSummary)
        }
    }

    @Test
    fun `test that updateJob throws IllegalStateException when the updated company does not exist in the database`() {
        // Save company
        val savedCompany = companyRepository.save(testCompanyEntityA())
        assertThat(savedCompany).isNotNull()

        // Create job entity
        val jobEntity = testJobEntityA(companyEntity = savedCompany)

        // Save job
        val existingJob = jobRepository.save(jobEntity)
        assertThat(existingJob).isNotNull()

        // Create update request
        val nonExistingCompanyId = 999L
        val updatedCompanySummary = CompanySummary(id = nonExistingCompanyId)
        val jobSummary = jobEntity.toJobSummaryDto().toJobSummary()
        val updatedJobSummary = jobSummary.copy(companySummary = updatedCompanySummary)

        assertThrows<IllegalStateException> {
            // Company in the update request does not exist -> IllegalStateException should be thrown
            underTest.updateJob(id = existingJob.id!!, jobSummary = updatedJobSummary)
        }
    }

    @Test
    fun `test that deleteJob deletes an existing job in the database`() {
        // Save company
        val savedCompany = companyRepository.save(testCompanyEntityA())
        assertThat(savedCompany).isNotNull()

        // Create job entity
        val jobEntity = testJobEntityA(companyEntity = savedCompany)

        // Save job
        val existingJob = jobRepository.save(jobEntity)
        assertThat(existingJob).isNotNull()

        // Delete job
        val existingJobId = existingJob.id!!
        underTest.deleteJob(existingJobId)

        // The job should not exist
        assertThat(jobRepository.existsById(existingJobId)).isFalse()
    }

    @Test
    fun `test that deleteJob deletes a non-existing job in the database`() {
        // Delete job with a non-existing id
        val nonExistingJobId = 999L
        underTest.deleteJob(nonExistingJobId)
        // It should not exist
        assertThat(jobRepository.existsById(nonExistingJobId)).isFalse()
    }
}