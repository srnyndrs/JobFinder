package com.srnyndrs.jobfinder.domain.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "jobs")
data class JobEntity(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "job_id_seq")
    @SequenceGenerator(name = "job_id_seq", sequenceName = "job_id_seq", allocationSize = 1)
    val id: Long?,

    @Column(name = "title")
    val title: String,

    @Column(name = "description", columnDefinition = "TEXT")
    val description: String,

    @Column(name = "location")
    val location: String,

    @Column(name = "created")
    val created: String,

    @Column(name = "job_type")
    val jobType: String,

    @Column(name = "remote")
    val remote: String,

    @ManyToOne(cascade = [(CascadeType.DETACH)])
    @JoinColumn(name = "company_id")
    val companyEntity: CompanyEntity
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JobEntity

        if (id != other.id) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (location != other.location) return false
        if (created != other.created) return false
        if (jobType != other.jobType) return false
        if (remote != other.remote) return false

        //
        //if (companyEntity != other.companyEntity) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + location.hashCode()
        result = 31 * result + created.hashCode()
        result = 31 * result + jobType.hashCode()
        result = 31 * result + remote.hashCode()
        result = 31 * result + companyEntity.hashCode()
        return result
    }
}
