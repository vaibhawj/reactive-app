package com.vibe.springboot2.reactiveapp

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import com.vibe.springboot2.reactiveapp.dto.Product
import com.vibe.springboot2.reactiveapp.services.ProductService
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import java.util.*


@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class ReactiveAppApplicationTests {

    @Autowired
    private var webTestClient: WebTestClient? = null

    @MockBean
    private var productService: ProductService? = null

    @Test
    fun contextLoads() {
    }

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testUpdateProduct() {
        val appleMono = Mono.just(Product("apple", 3.5))

        whenever(productService!!.createOrUpdateProduct(any())).thenReturn(null)

        webTestClient!!.post().uri("/api/products")
                .contentType(APPLICATION_JSON)
                .body(appleMono, Product::class.java)
                .exchange()
                .expectStatus().isOk
                .expectBody().isEmpty
    }

    @Test
    fun testCreateProduct() {
        val appleMono = Mono.just(Product("apple", 3.5))

        whenever(productService!!.createOrUpdateProduct(any())).thenReturn(UUID.randomUUID())

        webTestClient!!.post().uri("/api/products")
                .contentType(APPLICATION_JSON)
                .body(appleMono, Product::class.java)
                .exchange()
                .expectStatus().isCreated
                .expectBody().isEmpty
    }

}
