package com.srnyndrs.jobfinder.repositories

import com.srnyndrs.jobfinder.domain.entities.CompanyEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CompanyRepository: JpaRepository<CompanyEntity, Long?>