package com.vibe.springboot2.reactiveapp.repositories

import com.vibe.springboot2.reactiveapp.dto.Product
import org.junit.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.*

class ProductRepoTest {

    @Test fun should_return_empty() {

        val productRepo = ProductRepo()

        val allProducts: Flux<Product> = productRepo.getAllProducts()

        StepVerifier.create(allProducts)
                .expectComplete()
                .verify()
    }

    @Test fun should_return_all_products() {

        val productRepo = ProductRepo(listOf(Product("apple", 1.0), Product("banana", 2.0)))

        val allProducts: Flux<Product> = productRepo.getAllProducts()

        StepVerifier.create(allProducts)
                .expectNext(Product("apple", 1.0))
                .expectNext(Product("banana", 2.0))
                .expectComplete()
                .verify()
    }

    @Test fun should_return_one_product() {
        val appleId = UUID.randomUUID()
        val apple = Product("apple", 1.0, id = appleId)
        val bananaId = UUID.randomUUID()
        val banana = Product("banana", 2.0, id = bananaId)

        val productRepo = ProductRepo(listOf(apple, banana))

        val product: Mono<Product> = productRepo.getProduct(banana.id)

        StepVerifier.create(product)
                .expectNext(banana)
                .expectComplete()
                .verify()
    }

    @Test fun should_return_null() {

        val apple = Product("apple", 1.0)
        val productRepo = ProductRepo(listOf(apple))

        val product: Mono<Product> = productRepo.getProduct(UUID.randomUUID())

        StepVerifier.create(product)
                .expectComplete()
                .verify()
    }

    @Test fun should_add_one_product_to_list() {
        val productRepo = ProductRepo()

        val id = productRepo.createProduct(Mono.just(Product("chess", 4.5)))

        val allProducts: Flux<Product> = productRepo.getAllProducts()



        StepVerifier.create(allProducts)
                .expectNext(Product("chess", 4.5, id))
                .expectComplete()
                .verify()
    }


}