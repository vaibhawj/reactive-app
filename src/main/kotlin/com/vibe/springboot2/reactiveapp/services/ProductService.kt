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
        return productRepo.getAll()
    }

    fun getProduct(id: UUID): Mono<Product> {
        return productRepo.get(id)
    }

    fun createOrUpdateProduct(product: Mono<Product>): UUID? {
        return productRepo.createOrUpdate(product)
    }
}