# frozen_string_literal: true

require 'spec_helper'

RSpec.describe MemoryGame::FlatItem do
  let(:id1) { 'c1' }
  let(:id2) { 'c2' }

  describe '.obverse_up' do
    it 'creates item with obverse side up' do
      item = described_class.obverse_up(id1)
      expect(item.obverse_up?).to be true
      expect(item.reverse_up?).to be false
      expect(item.flat_item_id).to eq(id1)
    end
  end

  describe '.reverse_up' do
    it 'creates item with reverse side up' do
      item = described_class.reverse_up(id1)
      expect(item.reverse_up?).to be true
      expect(item.obverse_up?).to be false
    end
  end

  describe '.restore' do
    it 'restores obverse side when obverse_up is true' do
      item = described_class.restore(id1, true)
      expect(item.obverse_up?).to be true
    end

    it 'restores reverse side when obverse_up is false' do
      item = described_class.restore(id1, false)
      expect(item.reverse_up?).to be true
    end
  end

  describe '#flip' do
    it 'flips from obverse to reverse and back' do
      item = described_class.obverse_up(id1)
      item.flip
      expect(item.reverse_up?).to be true
      item.flip
      expect(item.obverse_up?).to be true
    end
  end

  describe '#turn_obverse_up and #turn_reverse_up' do
    it 'forces side regardless of previous state' do
      item = described_class.reverse_up(id1)
      item.turn_obverse_up
      expect(item.obverse_up?).to be true
      item.turn_reverse_up
      expect(item.reverse_up?).to be true
    end
  end

  describe 'equality and hash' do
    it 'considers two items equal when flat_item_id is the same' do
      a = described_class.obverse_up(id1)
      b = described_class.reverse_up(id1)

      expect(a).to eq(b)
      expect(a.eql?(b)).to be true
      expect(a.hash).to eq(b.hash)
    end

    it 'considers two items different when flat_item_id differs' do
      a = described_class.obverse_up(id1)
      b = described_class.obverse_up(id2)

      expect(a).not_to eq(b)
    end

    it 'works as Hash key' do
      a = described_class.obverse_up(id1)
      b = described_class.reverse_up(id1)

      hash = { a => 'value' }
      expect(hash[b]).to eq('value')
    end
  end
end

