package com.test.websocket

import me.jinuo.imf.websocket.anno.EnableWebSocket
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
@EnableWebSocket("/ws")
class TestApplication

fun main(args: Array<String>) {
    SpringApplication.run(TestApplication::class.java, *args)
}