package pl.csanecki.memory

import org.springframework.web.bind.annotation.*
import pl.cezarysanecki.memory.engine.MemoryGameApp
import pl.cezarysanecki.memory.engine.api.FlatItemId
import pl.cezarysanecki.memory.engine.api.MemoryGameId
import pl.cezarysanecki.memory.engine.api.MemoryGameState
import java.util.*

@RestController
@RequestMapping("/memory-game")
class MemoryGameController(
    private val memoryGameApp: MemoryGameApp
) {

    @PostMapping
    fun start(): MemoryGameStateResponse {
        val gameState = memoryGameApp.start(12, 3)
        return gameState.toResponse()
    }

    @PostMapping("/{memoryGameId}/turn-card/{cardId}")
    fun turnCard(
        @PathVariable memoryGameId: String,
        @PathVariable cardId: Int,
    ): TurningCardResponse {
        val memoryGameIdValue = MemoryGameId(UUID.fromString(memoryGameId))
        val guessResult = memoryGameApp.turnCard(memoryGameIdValue, FlatItemId.of(cardId))
        return TurningCardResponse(
            result = guessResult.actionResult.name,
            state = guessResult.state.toResponse()
        )
    }

    @GetMapping("/{memoryGameId}")
    fun getCurrentState(@PathVariable memoryGameId: String): MemoryGameStateResponse = memoryGameApp.getState(
        MemoryGameId(UUID.fromString(memoryGameId))
    ).toResponse()

}

fun MemoryGameState.toResponse(): MemoryGameStateResponse = MemoryGameStateResponse(
    memoryGameId = this.memoryGameId.value().toString(),
    cards = this.flatItems.map {
        MemoryGameStateResponse.Card(
            id = it.flatItemId.id.toString(),
            obverse = it.obverseUp()
        )
    }
)

data class MemoryGameStateResponse(
    val memoryGameId: String,
    val cards: List<Card>
) {
    data class Card(
        val id: String,
        val obverse: Boolean
    )
}

data class TurningCardResponse(
    val result: String,
    val state: MemoryGameStateResponse
)