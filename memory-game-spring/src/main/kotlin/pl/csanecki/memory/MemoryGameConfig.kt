package pl.csanecki.memory

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.cezarysanecki.memory.engine.MemoryGameApp
import pl.cezarysanecki.memory.engine.db.MemoryGameEventStore
import pl.cezarysanecki.memory.engine.db.MemoryGameReadModel
import pl.cezarysanecki.memory.infrastructure.InMemoryMemoryGameEventStore
import pl.cezarysanecki.memory.infrastructure.InMemoryMemoryGameReadModel

@Configuration
class MemoryGameConfig {

    @Bean
    fun memoryGameReadModel(): MemoryGameReadModel = InMemoryMemoryGameReadModel()

    @Bean
    fun memoryGameEventStore(memoryGameReadModel: MemoryGameReadModel): MemoryGameEventStore =
        InMemoryMemoryGameEventStore(memoryGameReadModel)

    @Bean
    fun memoryGameApp(
        memoryGameEventStore: MemoryGameEventStore,
        memoryGameReadModel: MemoryGameReadModel
    ): MemoryGameApp = MemoryGameApp(memoryGameEventStore, memoryGameReadModel)
}