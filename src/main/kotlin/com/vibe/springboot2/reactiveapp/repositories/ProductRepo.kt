package com.vibe.springboot2.reactiveapp.repositories

import com.vibe.springboot2.reactiveapp.dto.Product
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Repository
class ProductRepo(var productBackend: List<Product>) {

    constructor() : this(mutableListOf())

    fun getAll(): Flux<Product> {
        return Flux.fromIterable(productBackend)
    }

    fun get(id: UUID?): Mono<Product> {
        val filteredProducts: List<Product> = productBackend.filter { product -> id == product.id }
        return Mono.justOrEmpty(filteredProducts.firstOrNull())
    }

    fun createOrUpdate(product: Mono<Product>): UUID? {
        var id: UUID? = null
        product.subscribe { p ->
            run {
                id = UUID.randomUUID()
                p.id = id
                (productBackend as MutableList).add(p)
            }
        }
        return id
    }

}