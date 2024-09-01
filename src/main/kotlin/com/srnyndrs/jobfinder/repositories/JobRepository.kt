package com.srnyndrs.jobfinder.repositories

import com.srnyndrs.jobfinder.domain.entities.JobEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface JobRepository: JpaRepository<JobEntity, Long?> {
    fun findByCompanyEntityId(companyEntityId : Long) : List<JobEntity>
}