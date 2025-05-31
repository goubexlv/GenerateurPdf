package com.daccvo.models.domain

data class Gaz(
    val uuid: String,
    val name : String,
    val stock : Set<GazDetail> = emptySet()
)

data class GazDetail (
    val type: String,
    val sizeGaz: Int,
    val quantity: Int = 0
)