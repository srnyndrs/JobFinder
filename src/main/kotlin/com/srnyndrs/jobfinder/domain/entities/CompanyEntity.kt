package com.srnyndrs.jobfinder.domain.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*

@Entity
@Table(name = "companies")
data class CompanyEntity(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_id_seq")
    @SequenceGenerator(name = "company_id_seq", sequenceName = "company_id_seq", allocationSize = 1)
    val id: Long?,

    @Column(name = "name")
    val name: String,

    @Column(name = "description", columnDefinition = "TEXT")
    val description: String,

    @Column(name = "image")
    val image: String,

    @OneToMany(mappedBy = "companyEntity", cascade = [(CascadeType.REMOVE)])
    val jobEntities: List<JobEntity> = emptyList()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CompanyEntity

        if (id != other.id) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (image != other.image) return false

        // Two empty list will cause false
        //if (jobEntities != other.jobEntities) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + image.hashCode()
        result = 31 * result + jobEntities.hashCode()
        return result
    }
}
