package com.srnyndrs.jobfinder.controllers

import com.srnyndrs.jobfinder.domain.dto.CompanyDto
import com.srnyndrs.jobfinder.services.CompanyService
import com.srnyndrs.jobfinder.toCompanyDto
import com.srnyndrs.jobfinder.toCompanyEntity
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin(value = ["http://localhost:3000"])
@RestController
@RequestMapping(path = ["/v1/companies"])
@Tag(name = "Companies")
class CompaniesController(
    private val companyService: CompanyService
) {

    @PostMapping(consumes = ["application/json"], produces = ["application/json"])
    @Operation(summary = "Create a new company")
    fun createCompany(
        @RequestBody companyDto: CompanyDto
    ): ResponseEntity<CompanyDto> {
        return try {
            val createdCompany = companyService
                .createCompany(companyDto.toCompanyEntity())
                .toCompanyDto()
            ResponseEntity(createdCompany, HttpStatus.CREATED)
        } catch (exception: IllegalArgumentException) {
            // If ID is not null
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping
    @Operation(summary = "Read all companies")
    fun readManyCompanies(): List<CompanyDto> {
        return companyService.readCompanies().map { it.toCompanyDto() }
    }

    @GetMapping(path = ["/{id}"])
    @Operation(summary = "Read a specific company by id")
    fun readOneCompany(
        @Parameter(description = "The id of the company")
        @PathVariable("id") id: Long
    ): ResponseEntity<CompanyDto> {
        val foundCompany = companyService.readCompany(id)?.toCompanyDto()
        return foundCompany?.let {
            ResponseEntity(foundCompany, HttpStatus.OK)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @PutMapping(path = ["/{id}"], consumes = ["application/json"], produces = ["application/json"])
    @Operation(summary = "Update a company")
    fun updateCompany(
        @Parameter(description = "The id of the company")
        @PathVariable("id") id: Long,
        @RequestBody companyDto: CompanyDto
    ): ResponseEntity<CompanyDto> {
        return try {
            val updatedCompany = companyService.updateCompany(
                id = id,
                companyEntity = companyDto.toCompanyEntity()
            )
            ResponseEntity(updatedCompany.toCompanyDto(), HttpStatus.OK)
        } catch (exception: IllegalStateException) {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @DeleteMapping(path = ["/{id}"])
    @Operation(summary = "Delete a company by id")
    fun deleteCompany(
        @Parameter(description = "The id of the company")
        @PathVariable("id") id: Long
    ): ResponseEntity<Unit> {
        companyService.deleteCompany(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}