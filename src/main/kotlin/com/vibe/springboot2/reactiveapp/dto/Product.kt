package com.vibe.springboot2.reactiveapp.dto

import java.util.UUID

data class Product(var name: String?, var price: Double?, var id: UUID? = null)