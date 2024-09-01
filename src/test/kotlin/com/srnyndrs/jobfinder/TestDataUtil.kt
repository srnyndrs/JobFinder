package com.srnyndrs.jobfinder

import com.srnyndrs.jobfinder.domain.CompanySummary
import com.srnyndrs.jobfinder.domain.JobSummary
import com.srnyndrs.jobfinder.domain.dto.CompanyDto
import com.srnyndrs.jobfinder.domain.dto.CompanySummaryDto
import com.srnyndrs.jobfinder.domain.dto.JobSummaryDto
import com.srnyndrs.jobfinder.domain.entities.CompanyEntity
import com.srnyndrs.jobfinder.domain.entities.JobEntity

const val COMPANIES_BASE_URL = "/v1/companies"
const val JOBS_BASE_URL = "/v1/jobs"

/**
 * CompanyDto
 */
fun testCompanyDtoA(id: Long? = null) = CompanyDto(
    id = id,
    name = "Company A",
    description = "An amazing company",
    image = "company-image.jpeg"
)


/**
 * CompanyEntity
 */
fun testCompanyEntityA(id: Long? = null) = CompanyEntity(
    id = id,
    name = "Company A",
    description = "An amazing company",
    image = "company-image.jpeg"
)

fun testCompanyEntityB(id: Long? = null) = CompanyEntity(
    id = id,
    name = "Company B",
    description = "Another amazing company",
    image = "another-company-image.jpeg"
)

/**
 * JobEntity
 */
fun testJobEntityA(id: Long? = null, companyEntity: CompanyEntity) = JobEntity(
    id = id,
    title = "Job Summary A",
    description = "An amazing job",
    location = "Budapest",
    created = "2024-08-16",
    jobType = "Full-Time",
    remote = "Remote",
    companyEntity = companyEntity,
)

fun testJobEntityB(id: Long? = null, companyEntity: CompanyEntity) = JobEntity(
    id = id,
    title = "Job Summary B",
    description = "Another amazing job",
    location = "Debrecen",
    created = "2024-08-20",
    jobType = "Contract",
    remote = "Hybrid",
    companyEntity = companyEntity,
)

/**
 * CompanySummaryDto
 */
fun testCompanySummaryDtoA(id: Long) = CompanySummaryDto(
    id = id,
    name = "Company A",
    image = "company-image.jpeg"
)

/**
 * JobSummaryDto
 */
fun testJobSummaryDtoA(id: Long? = null, companySummaryDto: CompanySummaryDto) = JobSummaryDto(
    id = id,
    title = "Job Summary A",
    description = "An amazing job",
    location = "job-location-image.jpeg",
    created = "2024-08-16",
    jobType = "Full-time",
    remote = "Remote",
    company = companySummaryDto
)

/**
 * JobSummary
 */
fun testJobSummaryA(id: Long? = null, companySummary: CompanySummary) = JobSummary(
    id = id,
    title = "Job Summary A",
    description = "An amazing job",
    location = "job-location-image.jpeg",
    created = "2024-08-16",
    jobType = "Full-time",
    remote = "Remote",
    companySummary = companySummary,
)