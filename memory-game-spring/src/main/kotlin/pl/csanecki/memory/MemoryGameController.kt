package pl.csanecki.memory

import org.springframework.web.bind.annotation.*
import pl.cezarysanecki.memory.engine.MemoryGameApp
import pl.cezarysanecki.memory.engine.MemoryGameEvent
import pl.cezarysanecki.memory.engine.api.FlatItemId
import pl.cezarysanecki.memory.engine.api.MemoryGameId
import pl.cezarysanecki.memory.engine.api.MemoryGameState
import pl.cezarysanecki.memory.engine.db.MemoryGameEventStore
import java.util.*

@RestController
@RequestMapping("/memory-game")
class MemoryGameController(
    private val memoryGameApp: MemoryGameApp,
    private val memoryGameEventStore: MemoryGameEventStore,
) {

    @PostMapping
    fun start(): MemoryGameStateResponse {
        val gameState = memoryGameApp.start(12, 3)
        return gameState.toResponse()
    }

    @PostMapping("/{memoryGameId}/turn-card/{cardId}")
    fun turnCard(
        @PathVariable memoryGameId: String,
        @PathVariable cardId: String,
    ): TurningCardResponse {
        val memoryGameIdValue = MemoryGameId(UUID.fromString(memoryGameId))

        val turnResult = memoryGameApp.turnCard(memoryGameIdValue, FlatItemId(UUID.fromString(cardId)))
        val gameState = memoryGameApp.getState(memoryGameIdValue)

        return turnResult.map {
            when (it) {
                is MemoryGameEvent.Initialized -> TurningCardResponse(
                    result = "Ongoing",
                    state = gameState.toResponse()
                )

                is MemoryGameEvent.Continued -> TurningCardResponse(
                    result = "Ongoing",
                    state = gameState.toResponse()
                )

                is MemoryGameEvent.Finished -> TurningCardResponse(
                    result = "Finished",
                    state = gameState.toResponse()
                )

                is MemoryGameEvent.Guessed -> TurningCardResponse(
                    result = "Guessed",
                    state = gameState.toResponse()
                )

                is MemoryGameEvent.Missed -> TurningCardResponse(
                    result = "Missed",
                    state = gameState.toResponse()
                )
            }
        }.orElseGet {
            TurningCardResponse(
                result = "Ongoing",
                state = gameState.toResponse()
            )
        }
    }

    @GetMapping("/{memoryGameId}")
    fun getCurrentState(@PathVariable memoryGameId: String): MemoryGameStateResponse = memoryGameApp.getState(
        MemoryGameId(UUID.fromString(memoryGameId))
    ).toResponse()

    @GetMapping("/{memoryGameId}/events")
    fun getEvents(@PathVariable memoryGameId: String): List<MemoryGameEventResponse> =
        memoryGameEventStore.load(MemoryGameId(UUID.fromString(memoryGameId))).toResponse()

}

fun MemoryGameState.toResponse(): MemoryGameStateResponse = MemoryGameStateResponse(
    memoryGameId = this.memoryGameId.value().toString(),
    cards = this.flatItems.map {
        MemoryGameStateResponse.Card(
            id = it.flatItemId.value.toString(),
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

fun List<MemoryGameEvent>.toResponse(): List<MemoryGameEventResponse> = this.map {
    MemoryGameEventResponse(
        eventId = it.eventId().toString(),
        type = it::class.simpleName ?: "Unknown",
        timestamp = it.`when`().toString(),
        memoryGameId = it.memoryGameId().value.toString(),
        events = it.events().map { flatItemsGroupEvent ->
            FlatItemsGroupEventResponse(
                eventId = flatItemsGroupEvent.eventId().toString(),
                type = flatItemsGroupEvent::class.simpleName ?: "Unknown",
                timestamp = flatItemsGroupEvent.`when`().toString(),
                flatItemGroupId = flatItemsGroupEvent.flatItemsGroupId().value.toString(),
                events = flatItemsGroupEvent.events().map { flatItemEvent ->
                    FlatItemEventResponse(
                        eventId = flatItemEvent.eventId().toString(),
                        type = flatItemEvent::class.simpleName ?: "Unknown",
                        timestamp = flatItemEvent.`when`().toString(),
                        flatItemId = flatItemEvent.flatItemId().value.toString()
                    )
                }
            )
        }
    )
}

data class MemoryGameEventResponse(
    val eventId: String,
    val type: String,
    val timestamp: String,
    val memoryGameId: String,
    val events: List<FlatItemsGroupEventResponse>,
)

data class FlatItemsGroupEventResponse(
    val eventId: String,
    val type: String,
    val timestamp: String,
    val flatItemGroupId: String,
    val events: List<FlatItemEventResponse>,
)

data class FlatItemEventResponse(
    val eventId: String,
    val type: String,
    val timestamp: String,
    val flatItemId: String,
)