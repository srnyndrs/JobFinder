package com.srnyndrs.jobfinder.services.impl

import com.srnyndrs.jobfinder.domain.entities.CompanyEntity
import com.srnyndrs.jobfinder.domain.entities.JobEntity
import com.srnyndrs.jobfinder.repositories.CompanyRepository
import com.srnyndrs.jobfinder.services.CompanyService
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class CompanyServiceImpl(
    private val companyRepository: CompanyRepository
): CompanyService {

    override fun createCompany(companyEntity: CompanyEntity): CompanyEntity {
        // The given company must not already have an ID
        // Else throws IllegalArgumentException
        require(null == companyEntity.id)

        return companyRepository.save(companyEntity)
    }

    override fun readCompanies(): List<CompanyEntity> {
        return companyRepository.findAll()
    }

    override fun readCompany(id: Long): CompanyEntity? {
        return companyRepository.findByIdOrNull(id)
    }

    @Transactional
    override fun updateCompany(id: Long, companyEntity: CompanyEntity): CompanyEntity {
        // Check if the company exist in the database
        // If not -> Throws IllegalStateException
        check(companyRepository.existsById(id))
        // Making sure the ids are equivalent
        val normalisedCompany = companyEntity.copy(id = id)
        return companyRepository.save(normalisedCompany)
    }

    override fun deleteCompany(id: Long) {
        companyRepository.deleteById(id)
    }
}