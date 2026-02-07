# frozen_string_literal: true

require 'spec_helper'

RSpec.describe MemoryGame::Client do
  let(:base_url) { 'http://example.com' }
  subject(:client) { described_class.new(base_url: base_url) }

  before do
    stub_request(:post, %r{#{base_url}/memory-game}).to_return(
      status: 200,
      body: '{"memoryGameId":"123","cards":[]}',
      headers: { 'Content-Type' => 'application/json' }
    )

    stub_request(:get, %r{#{base_url}/memory-game/123}).to_return(
      status: 200,
      body: '{"memoryGameId":"123","cards":[{"id":"c1","obverse":true}]}',
      headers: { 'Content-Type' => 'application/json' }
    )
  end

  it 'starts a game' do
    state = client.start_game
    expect(state['memoryGameId']).to eq('123')
  end

  it 'fetches game state' do
    state = client.state(memory_game_id: '123')
    expect(state['cards'].first['id']).to eq('c1')
  end

  it 'raises error on HTTP failure' do
    stub_request(:get, %r{#{base_url}/memory-game/404}).to_return(status: 404, body: 'Not found')

    expect {
      client.state(memory_game_id: '404')
    }.to raise_error(MemoryGame::Error, /HTTP 404/)
  end
end

