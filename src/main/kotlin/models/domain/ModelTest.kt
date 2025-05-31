package com.daccvo.models.domain

import kotlinx.serialization.Serializable


@Serializable
data class CVRequest(
    val personalInfo: PersonalInfo,
    val education: List<Education>,
    val experience: List<Experience>,
    val skills: List<String>,
    val languages: List<Language>,
    val certifications: List<Certification> = emptyList(),
    val projects: List<Project> = emptyList()
)

@Serializable
data class PersonalInfo(
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val address: String,
    val city: String,
    val postalCode: String,
    val country: String,
    val linkedin: String? = null,
    val github: String? = null,
    val website: String? = null,
    val summary: String? = null
)

@Serializable
data class Education(
    val institution: String,
    val degree: String,
    val fieldOfStudy: String,
    val startDate: String,
    val endDate: String,
    val gpa: String? = null,
    val description: String? = null
)

@Serializable
data class Experience(
    val company: String,
    val position: String,
    val location: String,
    val startDate: String,
    val endDate: String,
    val description: List<String>,
    val technologies: List<String> = emptyList()
)

@Serializable
data class Language(
    val name: String,
    val level: String // Débutant, Intermédiaire, Avancé, Courant, Natif
)

@Serializable
data class Certification(
    val name: String,
    val issuer: String,
    val date: String,
    val expirationDate: String? = null
)

@Serializable
data class Project(
    val name: String,
    val description: String,
    val technologies: List<String>,
    val url: String? = null,
    val date: String
)