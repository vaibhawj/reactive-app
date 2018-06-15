package com.vibe.springboot2.reactiveapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ReactiveAppApplication

fun main(args: Array<String>) {
    runApplication<ReactiveAppApplication>(*args)
}
