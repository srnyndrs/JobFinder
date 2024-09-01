package com.srnyndrs.jobfinder.domain.dto

data class JobSummaryDto (
    val id: Long?,
    val title: String,
    val description: String,
    val location: String,
    val created: String,
    val jobType: String,
    val remote: String,
    val company: CompanySummaryDto
)