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

    fun createOrUpdate(productMono: Mono<Product>): UUID? {

        var id: UUID? = null;
        productMono.subscribe {
            val product = productBackend.filter { p -> p.id == it.id }.firstOrNull()
            if(product == null) {
                it.id = UUID.randomUUID()
                id = it.id
                (productBackend as MutableList).add(it)
            } else {
                product.price = it.price
                product.name = it.name
            }
        }
        return id
    }

}