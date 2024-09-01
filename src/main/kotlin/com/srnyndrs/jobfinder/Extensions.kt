package com.srnyndrs.jobfinder

import com.srnyndrs.jobfinder.domain.CompanySummary
import com.srnyndrs.jobfinder.domain.JobSummary
import com.srnyndrs.jobfinder.domain.dto.CompanyDto
import com.srnyndrs.jobfinder.domain.dto.CompanySummaryDto
import com.srnyndrs.jobfinder.domain.dto.JobDto
import com.srnyndrs.jobfinder.domain.dto.JobSummaryDto
import com.srnyndrs.jobfinder.domain.entities.CompanyEntity
import com.srnyndrs.jobfinder.domain.entities.JobEntity
import com.srnyndrs.jobfinder.exceptions.InvalidCompanyException
import com.srnyndrs.jobfinder.exceptions.InvalidJobException

fun CompanyEntity.toCompanyDto() = CompanyDto (
    id = this.id,
    name = this.name,
    description = this.description,
    image = this.image
)

fun CompanyDto.toCompanyEntity() = CompanyEntity(
    id = this.id,
    name = this.name,
    description = this.description,
    image = this.image
)

fun JobEntity.toJobDto() = JobDto(
    id = this.id,
    title = this.title,
    description = this.description,
    created = this.created,
    location = this.location,
    jobType = this.jobType,
    remote = this.remote,
    company = this.companyEntity.toCompanyDto()
)

fun JobDto.toJobEntity() = JobEntity(
    id = this.id,
    title = this.title,
    description = this.description,
    created = this.created,
    location = this.location,
    jobType = this.jobType,
    remote = this.remote,
    companyEntity = this.company.toCompanyEntity()
)

fun CompanyEntity.toCompanySummaryDto(): CompanySummaryDto {
    val companyId = this.id ?: throw InvalidCompanyException()

    return CompanySummaryDto(
        id = companyId,
        name = this.name,
        image = this.image
    )
}

fun JobEntity.toJobSummaryDto(): JobSummaryDto {
    val jobId = this.id ?: throw InvalidJobException()

    return JobSummaryDto(
        id = jobId,
        title = this.title,
        description = this.description,
        created = this.created,
        location = this.location,
        jobType = this.jobType,
        remote = this.remote,
        company = this.companyEntity.toCompanySummaryDto()
    )
}

fun CompanySummaryDto.toCompanySummary() = CompanySummary(
    id = this.id,
    name = this.name,
    image = this.image
)

fun JobSummaryDto.toJobSummary() = JobSummary(
    id = this.id,
    title = this.title,
    description = this.description,
    created = this.created,
    location = this.location,
    jobType = this.jobType,
    remote = this.remote,
    companySummary = this.company.toCompanySummary()
)

fun JobSummary.toJobEntity(companyEntity: CompanyEntity) = JobEntity(
    id = this.id,
    title = this.title,
    description = this.description,
    created = this.created,
    location = this.location,
    jobType = this.jobType,
    remote = this.remote,
    companyEntity = companyEntity
)