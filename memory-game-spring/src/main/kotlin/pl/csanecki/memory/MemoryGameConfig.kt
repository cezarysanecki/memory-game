package pl.csanecki.memory

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.cezarysanecki.memory.engine.MemoryGameApp

@Configuration
class MemoryGameConfig {

    @Bean
    fun memoryGameApp(): MemoryGameApp = MemoryGameApp.inMemory()
}