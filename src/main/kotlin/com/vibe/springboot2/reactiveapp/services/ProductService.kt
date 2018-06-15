package com.vibe.springboot2.reactiveapp.services

import com.vibe.springboot2.reactiveapp.dto.Product
import com.vibe.springboot2.reactiveapp.repositories.ProductRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Service
class ProductService(@Autowired val productRepo: ProductRepo) {

    fun getAllProducts(): Flux<Product> {
        return productRepo.getAllProducts()
    }

    fun getProduct(id: UUID): Mono<Product> {
        return productRepo.getProduct(id)
    }

    fun createProduct(product: Mono<Product>): UUID? {
        return productRepo.createProduct(product)
    }



}