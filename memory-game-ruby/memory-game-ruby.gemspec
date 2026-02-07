# frozen_string_literal: true

require_relative 'lib/memory_game/version'

Gem::Specification.new do |spec|
  spec.name          = 'memory-game-ruby'
  spec.version       = MemoryGame::VERSION
  spec.authors       = ['Cezary Sanecki']
  spec.email         = ['example@example.com']

  spec.summary       = 'Ruby HTTP client and CLI for the Memory Game Spring backend'
  spec.description   = 'A small Ruby client and command-line interface that communicates with the memory-game-spring HTTP API to start games, turn cards and inspect game state.'
  spec.homepage      = 'https://example.com/memory-game-ruby'
  spec.license       = 'MIT'

  spec.required_ruby_version = '>= 3.0'

  spec.files         = Dir.chdir(File.expand_path(__dir__)) do
    Dir['lib/**/*', 'bin/*', 'README.md']
  end
  spec.bindir        = 'bin'
  spec.executables   = ['memory-game']
  spec.require_paths = ['lib']

  spec.metadata['homepage_uri'] = spec.homepage
  spec.metadata['source_code_uri'] = spec.homepage

  spec.add_development_dependency 'rspec', '~> 3.13'
  spec.add_development_dependency 'webmock', '~> 3.20'
end

