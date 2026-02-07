# frozen_string_literal: true

require 'json'
require 'net/http'
require 'uri'

module MemoryGame
  class Error < StandardError; end

  # Prosty klient HTTP do backendu memory-game-spring
  class Client
    DEFAULT_BASE_URL = 'http://localhost:8080'.freeze

    attr_reader :base_url

    def initialize(base_url: ENV.fetch('MEMORY_GAME_BASE_URL', DEFAULT_BASE_URL))
      @base_url = base_url.chomp('/')</n    end

    def start_game
      post('/memory-game')
    end

    def turn_card(memory_game_id:, card_id:)
      post("/memory-game/#{memory_game_id}/turn-card/#{card_id}")
    end

    def state(memory_game_id:)
      get("/memory-game/#{memory_game_id}")
    end

    private

    def get(path)
      uri = URI.parse(base_url + path)
      req = Net::HTTP::Get.new(uri)
      perform_request(uri, req)
    end

    def post(path)
      uri = URI.parse(base_url + path)
      req = Net::HTTP::Post.new(uri)
      perform_request(uri, req)
    end

    def perform_request(uri, request)
      http = Net::HTTP.new(uri.host, uri.port)
      http.use_ssl = uri.scheme == 'https'

      response = http.request(request)

      unless response.is_a?(Net::HTTPSuccess)
        raise Error, "HTTP #{response.code}: #{response.body}"
      end

      return nil if response.body.nil? || response.body.empty?

      JSON.parse(response.body)
    rescue SocketError, Errno::ECONNREFUSED => e
      raise Error, "Connection error: #{e.message}"
    rescue JSON::ParserError => e
      raise Error, "Invalid JSON response: #{e.message}"
    end
  end
end

