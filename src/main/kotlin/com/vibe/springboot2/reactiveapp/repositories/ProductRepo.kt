package com.vibe.springboot2.reactiveapp.repositories

import com.vibe.springboot2.reactiveapp.dto.Product
import com.vibe.springboot2.reactiveapp.errors.ProductNotFoundException
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*
import java.util.function.Consumer

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

    fun create(productMono: Mono<Product>): UUID? {
        var id: UUID? = null
        productMono.subscribe {
            it.id = UUID.randomUUID()
            (productBackend as MutableList).add(it)
            id = it.id
        }
        return id
    }

    fun update(productMono: Mono<Product>) {

        productMono
            .subscribe {
                val product = productBackend.filter { p -> p.id == it.id }.firstOrNull()
                        ?: throw ProductNotFoundException()

                if (it.name != null) product.name = it.name
                if (it.price != null) product.price = it.price
            }
    }
}