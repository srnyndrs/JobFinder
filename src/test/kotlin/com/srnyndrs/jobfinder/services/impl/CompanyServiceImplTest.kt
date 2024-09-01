package com.srnyndrs.jobfinder.services.impl

import com.srnyndrs.jobfinder.repositories.CompanyRepository
import com.srnyndrs.jobfinder.testCompanyEntityA
import com.srnyndrs.jobfinder.testCompanyEntityB
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import kotlin.test.Test

@SpringBootTest
@Transactional
class CompanyServiceImplTest @Autowired constructor(
    val underTest: CompanyServiceImpl,
    private val companyRepository: CompanyRepository
) {

    @Test
    fun `test that createCompany persists the company in the database`() {
        // Save company
        val savedCompany = underTest.createCompany(testCompanyEntityA())

        // The returned company should have an ID by now
        assertThat(savedCompany.id).isNotNull()

        // Recall the saved company
        val recalledCompany = companyRepository.findByIdOrNull(savedCompany.id)

        // The company should be in the database
        assertThat(recalledCompany).isNotNull()

        // The company should be the same before save with a new ID
        assertThat(recalledCompany).isEqualTo(testCompanyEntityA(id = savedCompany.id))
    }

    @Test
    fun `test that createCompany with an ID throws an IllegalArgumentException`() {
        // If the given companyEntity already has an ID -> IllegalArgumentException should be thrown
        assertThrows<IllegalArgumentException> {
            val existingCompany = testCompanyEntityA(id = 1)
            underTest.createCompany(existingCompany)
        }
    }

    @Test
    fun `test that readCompanies returns empty list when there are no companies in the database`() {
        // Initially it should return with an empty list
        val result = underTest.readCompanies()
        assertThat(result).isEmpty()
    }

    @Test
    fun `test that readCompanies returns companies when there are companies in the database`() {
        // Save company to the database
        val savedCompany = underTest.createCompany(testCompanyEntityA())

        // Expect that only that company will return
        val expected = listOf(savedCompany)

        // Read companies
        val result = underTest.readCompanies()

        // Check if the returned list equals the expected one
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `test that readCompany returns null when company not found in the database`() {
        // Initially the company should not exist
        val result = underTest.readCompany(id = 999)
        assertThat(result).isNull()
    }

    @Test
    fun `test that readCompany returns company when company exists in database`() {
        // Save company to the database
        val savedCompany = underTest.createCompany(testCompanyEntityA())

        // The returned company should have an ID by now
        assertThat(savedCompany.id).isNotNull()

        // Read saved company by its new id
        val result = underTest.readCompany(savedCompany.id!!)

        // The result should be match with the saved object
        assertThat(result).isEqualTo(testCompanyEntityA(id = savedCompany.id))
    }

    @Test
    fun `test that updateCompany successfully updates the company in the database`() {
        // Save company to the database
        val savedCompany = companyRepository.save(testCompanyEntityA())

        // Get the saved company's id
        val savedCompanyId = savedCompany.id!!

        // Create updated company object
        val updatedCompany = testCompanyEntityB(id = savedCompanyId)

        // Update company
        val result = underTest.updateCompany(savedCompanyId, updatedCompany)

        // The returned company should be the expected
        assertThat(result).isEqualTo(updatedCompany)

        // Recall the company
        val recalledCompany = companyRepository.findByIdOrNull(savedCompanyId)

        // Company should exist in the database
        assertThat(recalledCompany).isNotNull()

        // Company should be the updated one
        assertThat(recalledCompany).isEqualTo(updatedCompany)
    }

    @Test
    fun `test that updateCompany throws IllegalStateException when company does not exist in the database`() {
        assertThrows<IllegalStateException> {
            val nonExistingCompanyId = 999L
            val updatedCompany = testCompanyEntityB(id = nonExistingCompanyId)
            // Company does not exist yet -> IllegalStateException should be thrown
            underTest.updateCompany(nonExistingCompanyId, updatedCompany)
        }
    }

    @Test
    fun `test that deleteCompany deletes an existing company in the database`() {
        // Save company
        val existingCompany = companyRepository.save(testCompanyEntityA())

        // Delete company
        val existingCompanyId = existingCompany.id!!
        underTest.deleteCompany(existingCompanyId)

        // Company should not exist
        assertThat(companyRepository.existsById(existingCompanyId)).isFalse()
    }

    @Test
    fun `test that deleteCompany deletes a non-existing company in the database`() {
        // Delete company
        val nonExistingCompanyId = 999L
        underTest.deleteCompany(nonExistingCompanyId)

        // It should not exist
        assertThat(companyRepository.existsById(nonExistingCompanyId)).isFalse()
    }
}