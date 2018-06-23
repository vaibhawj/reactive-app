package com.vibe.springboot2.reactiveapp.routes

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.RequestPredicates.*
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions.nest
import org.springframework.web.reactive.function.server.RouterFunctions.route
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class ProductRouter {

    @Bean
    fun productRoutes(productHandler: ProductHandler): RouterFunction<ServerResponse> {
        return nest(path("/api"),
                route(GET("/products").and(accept(APPLICATION_JSON)), productHandler::getProducts.invoke())
                .andRoute(GET("/products/{id}").and(accept(APPLICATION_JSON)), productHandler::getProduct.invoke())
                .andRoute(POST("/products").and(accept(APPLICATION_JSON)), productHandler::updateProduct.invoke())
                .andRoute(PUT("/products").and(accept(APPLICATION_JSON)), productHandler::createProduct.invoke())
        )
    }
}