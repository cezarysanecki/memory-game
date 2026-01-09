package pl.csanecki.memory

import org.springframework.web.bind.annotation.*
import pl.csanecki.memory.engine.FlatItemId
import pl.csanecki.memory.engine.GuessResult
import pl.csanecki.memory.engine.MemoryGame
import pl.csanecki.memory.engine.state.MemoryGameCurrentState

@RestController
@RequestMapping("/memory-game")
class MemoryGameController(
    private val memoryGame: MemoryGame
) {

    @GetMapping
    fun getCurrentState(): CurrentStateResponse = memoryGame.currentState().asResponse()

    @PostMapping("/turn-card/{cardId}")
    fun turnCard(@PathVariable cardId: Int): GuessResult = memoryGame.turnCard(FlatItemId.of(cardId))

}

data class CurrentStateResponse(
    val cards: List<Card>,
    val finished: Boolean
) {
    data class Card(
        val id: String,
        val obverse: Boolean
    )
}

fun MemoryGameCurrentState.asResponse() = CurrentStateResponse(
    cards = groupOfFlatItems.flatMap { it.flatItems }
        .map { CurrentStateResponse.Card(it.flatItemId.toString(), it.obverse()) },
    finished = isFinished
)