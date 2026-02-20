import { useState } from 'react'
import { startGame, turnCard, getEvents } from './api'
import AuditLog from './AuditLog'

const SYMBOLS = ['🌸', '🌊', '🌙', '⭐', '🍀', '🌈', '🦋', '🎯']

/**
 * Z pełnej listy eventów buduje:
 *  - cardGroupMap:  { flatItemId -> flatItemGroupId }
 *  - groupSymbols:  { flatItemGroupId -> emoji }
 * Kolejność symboli = kolejność pierwszego pojawienia się grupy w eventach.
 */
function buildGroupMaps(events) {
  const cardGroupMap = {}
  const groupOrder = [] // kolejność odkrywania grup

  for (const event of events) {
    for (const groupEvent of event.events) {
      const gid = groupEvent.flatItemGroupId
      if (!groupOrder.includes(gid)) groupOrder.push(gid)
      for (const cardEvent of groupEvent.events) {
        cardGroupMap[cardEvent.flatItemId] = gid
      }
    }
  }

  const groupSymbols = {}
  groupOrder.forEach((gid, i) => {
    groupSymbols[gid] = SYMBOLS[i % SYMBOLS.length]
  })

  return { cardGroupMap, groupSymbols }
}

export default function App() {
  const [game, setGame] = useState(null)
  const [cardOrder, setCardOrder] = useState([])
  const [cardGroupMap, setCardGroupMap] = useState({})  // cardId -> groupId
  const [groupSymbols, setGroupSymbols] = useState({})  // groupId -> emoji
  const [currentTurn, setCurrentTurn] = useState([])
  const [lastResult, setLastResult] = useState(null)
  const [isLocked, setIsLocked] = useState(false)
  const [isFinished, setIsFinished] = useState(false)
  const [guessedGroupsCount, setGuessedGroupsCount] = useState(0)
  const [error, setError] = useState(null)
  const [events, setEvents] = useState([])

  const fetchAndApplyEvents = async (gameId) => {
    try {
      const data = await getEvents(gameId)
      setEvents(data)
      const { cardGroupMap: newMap, groupSymbols: newSymbols } = buildGroupMaps(data)
      setCardGroupMap(newMap)
      setGroupSymbols(newSymbols)
    } catch (e) {
      console.error('Failed to fetch events', e)
    }
  }

  const handleStart = async () => {
    setError(null)
    try {
      const state = await startGame()
      setGame(state)
      setCardOrder(state.cards.map(c => c.id))
      setCardGroupMap({})
      setGroupSymbols({})
      setCurrentTurn([])
      setLastResult(null)
      setIsLocked(false)
      setIsFinished(false)
      setGuessedGroupsCount(0)
      setEvents([])
    } catch (e) {
      setError('Nie można połączyć z serwerem. Czy Spring działa na porcie 8080?')
    }
  }

  const handleCardClick = async (cardId) => {
    if (isLocked) return
    if (currentTurn.includes(cardId)) return
    const card = game.cards.find(c => c.id === cardId)
    if (card?.obverse) return

    setIsLocked(true)
    const newTurn = [...currentTurn, cardId]
    setCurrentTurn(newTurn)

    try {
      const response = await turnCard(game.memoryGameId, cardId)

      // Fetch events sekwencyjnie po ruchu – buduje mapę grup i symboli
      await fetchAndApplyEvents(game.memoryGameId)

      if (response.result === 'Guessed' || response.result === 'Finished') {
        setGuessedGroupsCount(c => c + 1)
        setCurrentTurn([])
        setGame(response.state)
        setLastResult(response.result === 'Finished' ? null : 'Guessed')
        if (response.result === 'Finished') setIsFinished(true)
        setIsLocked(false)
      } else if (response.result === 'Missed') {
        setGame(response.state)
        setLastResult('Missed')
        setTimeout(() => {
          setCurrentTurn([])
          setLastResult(null)
          setIsLocked(false)
        }, 1500)
      } else {
        setGame(response.state)
        setLastResult(null)
        setIsLocked(false)
      }
    } catch (e) {
      console.error(e)
      setCurrentTurn([])
      setIsLocked(false)
    }
  }

  if (!game) {
    return (
      <div className="app centered">
        <h1>Memory Game</h1>
        <p className="subtitle">Znajdź wszystkie grupy 3 pasujących kart</p>
        {error && <p className="error">{error}</p>}
        <button className="btn" onClick={handleStart}>Nowa gra</button>
      </div>
    )
  }

  const totalGroups = game.cards.length / 3

  return (
    <div className="page-layout">
      <div className="game-area">
        <h1>Memory Game</h1>

        {isFinished ? (
          <div className="finished-screen">
            <div className="trophy">🏆</div>
            <p className="finished-title">Brawo! Ukończyłeś grę!</p>
            <p className="finished-sub">Znalazłeś wszystkie {totalGroups} grupy</p>
            <button className="btn" onClick={handleStart}>Zagraj ponownie</button>
          </div>
        ) : (
          <>
            <div className="stats">
              <span>Grupy: {guessedGroupsCount} / {totalGroups}</span>
            </div>

            <div className="message-area">
              {lastResult === 'Guessed' && (
                <span className="message guessed">Trafienie!</span>
              )}
              {lastResult === 'Missed' && (
                <span className="message missed">Nie ta grupa...</span>
              )}
            </div>

            <div className="game-board">
              {cardOrder.map(id => game.cards.find(c => c.id === id)).filter(Boolean).map(card => {
                const isInTurn = currentTurn.includes(card.id)
                const isGuessed = card.obverse && !isInTurn
                const isFaceUp = isInTurn || card.obverse
                const isMissed = isInTurn && lastResult === 'Missed'

                // Symbol pochodzi wyłącznie z mapy grup zbudowanej z eventów
                const groupId = cardGroupMap[card.id]
                const symbol = isFaceUp && groupId ? groupSymbols[groupId] : null

                return (
                  <div
                    key={card.id}
                    className={[
                      'card-wrapper',
                      isFaceUp  ? 'face-up'     : '',
                      isGuessed ? 'guessed'     : '',
                      isInTurn  ? 'in-turn'     : '',
                      isMissed  ? 'missed-card' : '',
                    ].join(' ')}
                    onClick={() => handleCardClick(card.id)}
                    role="button"
                    aria-label={isFaceUp ? `Karta: ${symbol ?? '?'}` : 'Zakryta karta'}
                  >
                    <div className="card-inner">
                      <div className="card-face card-back" />
                      <div className="card-face card-front">{symbol}</div>
                    </div>
                  </div>
                )
              })}
            </div>

            <button className="btn btn-secondary" onClick={handleStart}>Nowa gra</button>
          </>
        )}
      </div>

      <AuditLog events={events} />
    </div>
  )
}
