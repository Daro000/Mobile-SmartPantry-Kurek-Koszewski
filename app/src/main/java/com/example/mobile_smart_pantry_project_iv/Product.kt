package com.example.mobile_smart_pantry_project_iv

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: String,
    val nazwa: String,
    val ilosc: Int,
    val jednostka: String,
    val kategoria: String,
    val imageRef: String = "ic_default"
) : java.io.Serializable