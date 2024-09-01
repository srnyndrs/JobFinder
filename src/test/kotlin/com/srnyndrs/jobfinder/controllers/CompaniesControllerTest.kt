package com.srnyndrs.jobfinder.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.srnyndrs.jobfinder.COMPANIES_BASE_URL
import com.srnyndrs.jobfinder.services.CompanyService
import com.srnyndrs.jobfinder.testCompanyDtoA
import com.srnyndrs.jobfinder.testCompanyEntityA
import io.mockk.every
import org.hamcrest.CoreMatchers.equalTo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*
import kotlin.test.Test

@SpringBootTest
@AutoConfigureMockMvc
class CompaniesControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    @MockkBean val companyService: CompanyService
) {

    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `test that createCompany returns HTTP 201 on successful create`() {
        // Service will return the given parameter
        // In this test the company ID does not matter
        every {
            companyService.createCompany(any())
        } answers {
            firstArg()
        }

        // Post request
        mockMvc.post(COMPANIES_BASE_URL) {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(testCompanyDtoA())
        }.andExpect {
            // Controller should return HTTP 201
            status { isCreated() }
        }
    }

    @Test
    fun `test that createCompany returns HTTP 400 when IllegalArgumentException is thrown`() {
        // Service will throw an IllegalArgumentException
        every {
            companyService.createCompany(any())
        } throws IllegalArgumentException()

        // Post request
        mockMvc.post(COMPANIES_BASE_URL) {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(testCompanyDtoA())
        }.andExpect {
            // Controller should return HTTP 400
            status { isBadRequest() }
        }
    }

    @Test
    fun `test that readCompanies returns empty list and HTTP 200 status when no companies in the database`() {
        // Service will return with an empty list
        every {
            companyService.readCompanies()
        } answers {
            emptyList()
        }

        // Get request
        mockMvc.get(COMPANIES_BASE_URL) {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            // Controller should return HTTP 200
            status { isOk() }
            // Content should be an empty tuple
            content { json("[]") }
        }
    }

    @Test
    fun `test that readCompanies returns companies and HTTP 200 status when there are companies in the database`() {
        // Service will return with one test element
        every {
            companyService.readCompanies()
        } answers {
            listOf(
                testCompanyEntityA(id = 1)
            )
        }

        // Get request
        mockMvc.get(COMPANIES_BASE_URL) {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            // Controller should return HTTP 200
            status { isOk() }
            // Content fields should be equal to the testCompanyEntity fields with an ID of 1
            content { jsonPath("$[0].id", equalTo(1)) }
            content { jsonPath("$[0].name", equalTo("Company A")) }
            content { jsonPath("$[0].description", equalTo("An amazing company")) }
            content { jsonPath("$[0].image", equalTo("company-image.jpeg")) }
        }
    }

    @Test
    fun `test that readCompany returns HTTP 404 status when company not found in the database`() {
        // Service will return null -> the company does not exist
        every {
            companyService.readCompany(any())
        } answers {
            null
        }

        // Get request
        mockMvc.get("$COMPANIES_BASE_URL/999") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            // Controller should return HTTP 404
            status { isNotFound() }
        }
    }

    @Test
    fun `test that readCompany returns HTTP 200 and company when company found in the database`() {
        // Service will return test company with an id of 999
        every {
            companyService.readCompany(any())
        } answers {
            testCompanyEntityA(id = 999)
        }

        // Get request
        mockMvc.get("$COMPANIES_BASE_URL/999") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            // Controller should return HTTP 200
            status { isOk() }
            // Content fields should be the same as the test company
            content { jsonPath("$.id", equalTo(999)) }
            content { jsonPath("$.name", equalTo("Company A")) }
            content { jsonPath("$.description", equalTo("An amazing company")) }
            content { jsonPath("$.image", equalTo("company-image.jpeg")) }
        }
    }

    @Test
    fun `test that updateCompany returns HTTP 200 and updated company on a successful call`() {
        // Service will return with the second parameter which is the given entity
        every {
            companyService.updateCompany(any(), any())
        } answers {
            secondArg()
        }

        // Put request
        mockMvc.put("$COMPANIES_BASE_URL/999") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(testCompanyDtoA(id = 999))
        }.andExpect {
            // Controller should return HTTP 200
            status { isOk() }
            // Content fields should be the same as the test company
            content { jsonPath("$.id", equalTo(999)) }
            content { jsonPath("$.name", equalTo("Company A")) }
            content { jsonPath("$.description", equalTo("An amazing company")) }
            content { jsonPath("$.image", equalTo("company-image.jpeg")) }
        }
    }

    @Test
    fun `test that updateCompany returns HTTP 400 status on IllegalStateException`() {
        // Service will throw IllegalStateException -> the company does not exist
        every {
            companyService.updateCompany(any(), any())
        } throws IllegalStateException()

        // Put request
        mockMvc.put("$COMPANIES_BASE_URL/999") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(testCompanyDtoA(id = 999))
        }.andExpect {
            // Controller should return HTTP 400
            status { isBadRequest() }
        }
    }

    @Test
    fun `test that delete company returns HTTP 204 on successful delete`() {
        // Service will return with empty content
        every {
            companyService.deleteCompany(any())
        } answers {
            // Empty content
        }

        // Delete request
        mockMvc.delete("$COMPANIES_BASE_URL/999") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            // Controller should return HTTP 204
            status { isNoContent() }
        }
    }
}