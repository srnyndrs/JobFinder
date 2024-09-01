package com.srnyndrs.jobfinder.domain

data class JobSummary(
    val id: Long?,
    val title: String,
    val description: String,
    val location: String,
    val created: String,
    val jobType: String,
    val remote: String,
    val companySummary: CompanySummary
)
