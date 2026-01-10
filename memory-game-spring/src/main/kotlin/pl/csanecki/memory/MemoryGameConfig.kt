package pl.csanecki.memory

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.csanecki.memory.engine.game.MemoryGame

@Configuration
class MemoryGameConfig {

    @Bean
    fun memoryGame() = MemoryGame.create(20, 2)
}