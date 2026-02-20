import { useState } from 'react'

const EVENT_COLORS = {
  Initialized: '#388bfd',
  Continued:   '#8b949e',
  Guessed:     '#3fb950',
  Missed:      '#f85149',
  Finished:    '#ffd700',
}

function shortId(uuid) {
  return uuid ? uuid.slice(0, 8) + '…' : '?'
}

function formatTime(ts) {
  try {
    // Kotlin LocalDateTime -> "2024-01-15T14:23:05.123456"
    const d = new Date(ts)
    if (isNaN(d.getTime())) return ts
    return d.toLocaleTimeString('pl-PL', { hour: '2-digit', minute: '2-digit', second: '2-digit' })
  } catch {
    return ts
  }
}

function CardEvent({ event }) {
  return (
    <div className="al-card-event">
      <span className="al-tree-icon">└</span>
      <span className="al-card-type">{event.type}</span>
      <span className="al-card-id">{shortId(event.flatItemId)}</span>
    </div>
  )
}

function GroupEvent({ event }) {
  const [open, setOpen] = useState(false)
  return (
    <div className="al-group-event">
      <div className="al-group-header" onClick={() => setOpen(o => !o)}>
        <span className="al-tree-icon">└</span>
        <span className="al-toggle">{open ? '▾' : '▸'}</span>
        <span className="al-group-type">{event.type}</span>
        <span className="al-group-id">{shortId(event.flatItemGroupId)}</span>
      </div>
      {open && (
        <div className="al-group-children">
          {event.events.map(ce => (
            <CardEvent key={ce.eventId} event={ce} />
          ))}
        </div>
      )}
    </div>
  )
}

function EventEntry({ event, index }) {
  const [open, setOpen] = useState(false)
  const color = EVENT_COLORS[event.type] || '#8b949e'
  const hasChildren = event.events.length > 0

  return (
    <div className="al-entry">
      <div className="al-entry-header" onClick={() => hasChildren && setOpen(o => !o)}>
        <span className="al-index">#{index}</span>
        <span className="al-badge" style={{ background: color }}>{event.type}</span>
        <span className="al-time">{formatTime(event.timestamp)}</span>
        {hasChildren && (
          <span className="al-toggle al-toggle-main">{open ? '▾' : '▸'}</span>
        )}
      </div>
      {open && hasChildren && (
        <div className="al-entry-children">
          {event.events.map(ge => (
            <GroupEvent key={ge.eventId} event={ge} />
          ))}
        </div>
      )}
    </div>
  )
}

export default function AuditLog({ events }) {
  return (
    <aside className="audit-panel">
      <div className="audit-panel-header">
        <h2 className="audit-title">Event Log</h2>
        {events.length > 0 && (
          <span className="audit-count">{events.length}</span>
        )}
      </div>

      {events.length === 0 ? (
        <p className="audit-empty">Zagraj, aby zobaczyć eventy</p>
      ) : (
        <div className="audit-list">
          {[...events].reverse().map((event, i) => (
            <EventEntry
              key={event.eventId}
              event={event}
              index={events.length - i}
            />
          ))}
        </div>
      )}
    </aside>
  )
}
