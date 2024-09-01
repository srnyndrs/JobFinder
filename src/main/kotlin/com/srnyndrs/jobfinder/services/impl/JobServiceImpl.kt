package com.srnyndrs.jobfinder.services.impl

import com.srnyndrs.jobfinder.domain.JobSummary
import com.srnyndrs.jobfinder.domain.entities.JobEntity
import com.srnyndrs.jobfinder.repositories.CompanyRepository
import com.srnyndrs.jobfinder.repositories.JobRepository
import com.srnyndrs.jobfinder.services.JobService
import com.srnyndrs.jobfinder.toJobEntity
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class JobServiceImpl (
    private val jobRepository: JobRepository,
    private val companyRepository: CompanyRepository
): JobService {

    @Transactional
    override fun createJob(jobSummary: JobSummary): JobEntity {
        // The given job must not already have an ID
        // Else throws IllegalArgumentException
        require(null == jobSummary.id)

        // Find the job's company
        val companyEntity = companyRepository.findByIdOrNull(jobSummary.companySummary.id)

        // The company must exist in the database
        // Else throws IllegalStateException
        checkNotNull(companyEntity)

        // Save the job
        return jobRepository.save(jobSummary.toJobEntity(companyEntity))
    }

    override fun readJobs(companyId: Long?): List<JobEntity> {
        // If companyId is not null -> find jobs by its id
        return companyId?.let {
            jobRepository.findByCompanyEntityId(it)
        } ?: jobRepository.findAll()
    }

    override fun readJob(id: Long): JobEntity? {
        return jobRepository.findByIdOrNull(id)
    }

    @Transactional
    override fun updateJob(id: Long, jobSummary: JobSummary): JobEntity {
        val normalisedJob = jobSummary.copy(id = id)

        val isExist = jobRepository.existsById(id)
        require(isExist) // IllegalArgumentException

        val company = companyRepository.findByIdOrNull(normalisedJob.companySummary.id)
        checkNotNull(company) // IllegalStateException

        return jobRepository.save(normalisedJob.toJobEntity(company))
    }

    override fun deleteJob(id: Long) {
        jobRepository.deleteById(id)
    }
}