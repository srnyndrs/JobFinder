package com.srnyndrs.jobfinder.services

import com.srnyndrs.jobfinder.domain.JobSummary
import com.srnyndrs.jobfinder.domain.entities.JobEntity

interface JobService {
    fun createJob(jobSummary: JobSummary): JobEntity
    fun readJobs(companyId: Long? = null): List<JobEntity>
    fun readJob(id: Long): JobEntity?
    fun updateJob(id: Long, jobSummary: JobSummary): JobEntity
    fun deleteJob(id: Long)
}