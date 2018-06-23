package com.vibe.springboot2.reactiveapp

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import com.vibe.springboot2.reactiveapp.dto.Product
import com.vibe.springboot2.reactiveapp.errors.ProductNotFoundException
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
import reactor.core.publisher.Flux
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
    fun testUpdateProductForNotFound() {
        val appleMono = Mono.just(Product("apple", 3.5))

        val exception = RuntimeException(ProductNotFoundException())
        whenever(productService!!.updateProduct(any())).thenThrow(exception)

        webTestClient!!.post().uri("/api/products")
                .contentType(APPLICATION_JSON)
                .body(appleMono, Product::class.java)
                .exchange()
                .expectStatus().isNotFound
                .expectBody().isEmpty
    }

    @Test
    fun testUpdateProductForAnyError() {
        val appleMono = Mono.just(Product("apple", 3.5))

        val exception = RuntimeException()
        whenever(productService!!.updateProduct(any())).thenThrow(exception)

        webTestClient!!.post().uri("/api/products")
            .contentType(APPLICATION_JSON)
            .body(appleMono, Product::class.java)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody().isEmpty
    }

    @Test
    fun testUpdateProduct() {
        val appleMono = Mono.just(Product("apple", 3.5))

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

        val uuid = UUID.randomUUID()
        whenever(productService!!.createProduct(any())).thenReturn(uuid)

        webTestClient!!.put().uri("/api/products")
                .contentType(APPLICATION_JSON)
                .body(appleMono, Product::class.java)
                .exchange()
                .expectStatus().isCreated
                .expectBody().isEmpty
    }

    @Test
    fun testCreateProductForError() {
        val appleMono = Mono.just(Product("apple", 3.5))

        whenever(productService!!.createProduct(any())).thenReturn(null)

        webTestClient!!.put().uri("/api/products")
            .contentType(APPLICATION_JSON)
            .body(appleMono, Product::class.java)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody().isEmpty
    }

    @Test
    fun testGetProducts() {

        whenever(productService!!.getAllProducts())
                .thenReturn(Flux.just(Product("apple", 2.5, UUID.fromString("f87a0969-e71e-4a26-abd9-770831cfb920"))))

        webTestClient!!.get().uri("/api/products")
                .exchange()
                .expectStatus().isOk
                .expectBody().json("[{\"name\":\"apple\",\"price\":2.5,\"id\":\"f87a0969-e71e-4a26-abd9-770831cfb920\"}]")

    }

    @Test
    fun testGetProduct() {
        val productId = UUID.fromString("f87a0969-e71e-4a26-abd9-770831cfb920")
        whenever(productService!!.getProduct(productId))
                .thenReturn(Mono.just(Product("apple", 2.5, productId)))

        webTestClient!!.get().uri("/api/products/${productId.toString()}")
                .exchange()
                .expectStatus().isOk
                .expectBody().json("{\"name\":\"apple\",\"price\":2.5,\"id\":\"f87a0969-e71e-4a26-abd9-770831cfb920\"}")
    }

}
