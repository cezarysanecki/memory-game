package pl.csanecki.memory

import org.springframework.web.bind.annotation.*
import pl.cezarysanecki.memory.engine.MemoryGameApp
import pl.cezarysanecki.memory.engine.api.FlatItemId
import pl.cezarysanecki.memory.engine.api.MemoryGameId
import pl.cezarysanecki.memory.engine.api.MemoryGameView
import java.util.*

@RestController
@RequestMapping("/memory-game")
class MemoryGameController(
    private val memoryGameApp: MemoryGameApp
) {

    @PostMapping
    fun start(): MemoryGameResponse {
        val memoryGameId = memoryGameApp.start(12, 3)
        return memoryGameApp.getState(memoryGameId).toResponse()
    }

    @PostMapping("/{memoryGameId}/turn-card/{cardId}")
    fun turnCard(
        @PathVariable memoryGameId: String,
        @PathVariable cardId: Int,
    ): MemoryGameResponse {
        val memoryGameIdValue = MemoryGameId(UUID.fromString(memoryGameId))
        memoryGameApp.turnCard(memoryGameIdValue, FlatItemId.of(cardId))
        return memoryGameApp.getState(memoryGameIdValue).toResponse()
    }

    @GetMapping("/{memoryGameId}")
    fun getCurrentState(@PathVariable memoryGameId: String): MemoryGameResponse = memoryGameApp.getState(
        MemoryGameId(UUID.fromString(memoryGameId))
    ).toResponse()

}

fun MemoryGameView.toResponse(): MemoryGameResponse = MemoryGameResponse(
    memoryGameId = this.memoryGameId.value().toString(),
    cards = this.flatItems.map {
        MemoryGameResponse.Card(
            id = it.flatItemId.id.toString(),
            obverse = it.obverseUp()
        )
    }
)

data class MemoryGameResponse(
    val memoryGameId: String,
    val cards: List<Card>
) {
    data class Card(
        val id: String,
        val obverse: Boolean
    )
}