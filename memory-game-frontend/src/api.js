const BASE = '/memory-game'

export async function startGame() {
  const res = await fetch(BASE, { method: 'POST' })
  if (!res.ok) throw new Error(`Failed to start game: ${res.status}`)
  return res.json()
}

export async function turnCard(gameId, cardId) {
  const res = await fetch(`${BASE}/${gameId}/turn-card/${cardId}`, { method: 'POST' })
  if (!res.ok) throw new Error(`Failed to turn card: ${res.status}`)
  return res.json()
}

export async function getEvents(gameId) {
  const res = await fetch(`${BASE}/${gameId}/events`)
  if (!res.ok) throw new Error(`Failed to fetch events: ${res.status}`)
  return res.json()
}
