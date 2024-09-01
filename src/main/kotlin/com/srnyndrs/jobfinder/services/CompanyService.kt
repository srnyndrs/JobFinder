package com.srnyndrs.jobfinder.services

import com.srnyndrs.jobfinder.domain.entities.CompanyEntity
import com.srnyndrs.jobfinder.domain.entities.JobEntity

interface CompanyService {
    fun createCompany(companyEntity: CompanyEntity): CompanyEntity
    fun readCompanies(): List<CompanyEntity>
    fun readCompany(id: Long): CompanyEntity?
    fun updateCompany(id: Long, companyEntity: CompanyEntity): CompanyEntity
    fun deleteCompany(id: Long)
}