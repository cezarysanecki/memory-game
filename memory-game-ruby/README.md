# memory-game-ruby

Ruby client and command-line interface for the `memory-game-spring` HTTP API.

## Wymagania

- Ruby 3.0+ (rbenv, rvm lub systemowy)
- Bundler
- Działający backend `memory-game-spring` (np. na `http://localhost:8080`)

## Instalacja

```bash
cd memory-game-ruby
bundle install
```

## Uruchomienie backendu Spring

W katalogu głównym repozytorium:

```bash
./gradlew :memory-game-spring:bootRun
```

Domyślnie aplikacja nasłuchuje na `http://localhost:8080`.

## Użycie CLI

Przykłady z katalogu `memory-game-ruby`:

```bash
bundle exec bin/memory-game start
bundle exec bin/memory-game state <memory_game_id>
bundle exec bin/memory-game turn <memory_game_id> <card_id>
```

Możesz zmienić adres backendu ustawiając zmienną środowiskową:

```bash
MEMORY_GAME_BASE_URL="http://localhost:8081" bundle exec bin/memory-game start
```

## Testy

```bash
bundle exec rspec
```

## Użycie jako biblioteka

```ruby
require 'memory_game'

client = MemoryGame::Client.new # domyślnie http://localhost:8080
state = client.start_game
puts state['memoryGameId']
```
