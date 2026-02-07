# frozen_string_literal: true

module MemoryGame
  class CLI
    def self.run(argv = ARGV, io: $stdout, client: Client.new)
      new(argv, io, client).run
    end

    def initialize(argv, io, client)
      @argv = argv
      @io = io
      @client = client
    end

    def run
      command = @argv.shift

      case command
      when 'start'
        handle_start
      when 'state'
        handle_state
      when 'turn'
        handle_turn
      else
        print_usage
        return 1
      end

      0
    rescue MemoryGame::Error => e
      @io.puts "Error: #{e.message}"
      1
    end

    private

    def handle_start
      state = @client.start_game
      print_state(state, header: 'New game started')
    end

    def handle_state
      game_id = @argv.shift
      unless game_id
        @io.puts 'Usage: memory-game state <memory_game_id>'
        return
      end

      state = @client.state(memory_game_id: game_id)
      print_state(state, header: "Game state for #{game_id}")
    end

    def handle_turn
      game_id = @argv.shift
      card_id = @argv.shift

      unless game_id && card_id
        @io.puts 'Usage: memory-game turn <memory_game_id> <card_id>'
        return
      end

      result = @client.turn_card(memory_game_id: game_id, card_id: card_id)
      @io.puts "Result: #{result['result']}" if result.is_a?(Hash) && result['result']

      state = result.is_a?(Hash) ? result['state'] : nil
      print_state(state, header: "Game state after turning card #{card_id}") if state
    end

    def print_state(state, header: nil)
      return unless state

      @io.puts header if header
      game_id = state['memoryGameId'] || state['memory_game_id']
      @io.puts "Game ID: #{game_id}" if game_id

      cards = state['cards'] || []
      @io.puts 'Cards:'
      cards.each do |card|
        id = card['id'] || card['cardId']
        obverse = card['obverse']
        @io.puts "  - id=#{id}, obverse=#{obverse}"
      end
    end

    def print_usage
      @io.puts <<~USAGE
        Usage: memory-game <command> [options]

          start                       Start a new game
          state <memory_game_id>      Show current state of a game
          turn <memory_game_id> <id>  Turn a card in a game
      USAGE
    end
  end
end

