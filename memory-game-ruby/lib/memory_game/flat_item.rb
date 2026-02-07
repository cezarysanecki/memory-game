# frozen_string_literal: true

module MemoryGame
  class FlatItem
    module Side
      REVERSE = :reverse
      OBVERSE = :obverse
    end

    attr_reader :flat_item_id

    private_class_method :new

    def self.restore(flat_item_id, obverse_up)
      side = obverse_up ? Side::OBVERSE : Side::REVERSE
      new(flat_item_id, side)
    end

    def self.obverse_up(flat_item_id)
      new(flat_item_id, Side::OBVERSE)
    end

    def self.reverse_up(flat_item_id)
      new(flat_item_id, Side::REVERSE)
    end

    def initialize(flat_item_id, side)
      @flat_item_id = flat_item_id
      @side = side
    end

    def flip
      @side = obverse_up? ? Side::REVERSE : Side::OBVERSE
    end

    def turn_obverse_up
      @side = Side::OBVERSE
    end

    def turn_reverse_up
      @side = Side::REVERSE
    end

    def obverse_up?
      @side == Side::OBVERSE
    end

    def reverse_up?
      @side == Side::REVERSE
    end

    def ==(other)
      return false unless other.is_a?(FlatItem)

      flat_item_id == other.flat_item_id
    end

    alias eql? ==

    def hash
      flat_item_id.hash
    end
  end
end

