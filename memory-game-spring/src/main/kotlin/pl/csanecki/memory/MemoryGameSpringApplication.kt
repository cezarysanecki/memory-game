package pl.csanecki.memory

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MemoryGameSpringApplication

fun main(args: Array<String>) {
    runApplication<MemoryGameSpringApplication>(*args)
}
