# frozen_string_literal: true

require 'spec_helper'

RSpec.describe MemoryGame::CLI do
  let(:io) { StringIO.new }

  it 'prints usage without command' do
    exit_code = described_class.run([], io: io, client: double('client'))
    expect(exit_code).to eq(1)
    expect(io.string).to include('Usage: memory-game')
  end

  it 'starts a game' do
    client = double('client')
    allow(client).to receive(:start_game).and_return('memoryGameId' => '123', 'cards' => [])

    exit_code = described_class.run(['start'], io: io, client: client)

    expect(exit_code).to eq(0)
    expect(io.string).to include('New game started')
    expect(io.string).to include('Game ID: 123')
  end
end

