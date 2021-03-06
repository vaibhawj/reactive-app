package com.vibe.springboot2.reactiveapp.routes

import com.vibe.springboot2.reactiveapp.dto.Product
import com.vibe.springboot2.reactiveapp.errors.ProductNotFoundException
import com.vibe.springboot2.reactiveapp.services.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.BodyInserters.fromPublisher
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import java.net.URI
import java.util.*

@Configuration
class ProductHandler(@Autowired val productService: ProductService) {

    fun getProducts(): HandlerFunction<ServerResponse> {

        return HandlerFunction { request ->
            ok().contentType(MediaType.APPLICATION_JSON)
                .body(fromPublisher(productService.getAllProducts(), Product::class.java))
        }
    }

    fun getProduct(): HandlerFunction<ServerResponse> {

        return HandlerFunction { request ->
            val product = productService.getProduct(UUID.fromString(request.pathVariable("id")))

            product.flatMap { p ->
                ok().contentType(MediaType.APPLICATION_JSON)
                    .body(fromObject(p))
            }.switchIfEmpty(ServerResponse.notFound().build())

        }
    }

    fun createProduct(): HandlerFunction<ServerResponse> {

        return HandlerFunction { request ->
            val productMono = request.bodyToMono(Product::class.java)

            val id = productService.createProduct(productMono)


            id?.let { ServerResponse.created(URI.create("/api/products/$it")).build() }
                    ?: ServerResponse.status(500).build()

        }
    }

    fun updateProduct(): HandlerFunction<ServerResponse> {

        return HandlerFunction { request ->
            val productMono = request.bodyToMono(Product::class.java)

            try {
                productService.updateProduct(productMono)
                ServerResponse.ok().build()
            } catch (e: RuntimeException) {
                when (e.cause) {
                    is ProductNotFoundException -> ServerResponse.notFound().build()
                    else -> ServerResponse.status(500).build()
                }
            }
        }
    }

}
