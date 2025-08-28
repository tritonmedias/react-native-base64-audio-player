require 'json'

package = JSON.parse(File.read('package.json'))

Pod::Spec.new do |s|
  s.name         = package['name']
  s.version      = package['version']
  s.summary      = package['description']
  s.homepage     = package['repository']['url']
  s.license      = package['license']
  s.authors      = { package['author'] => '' }
  s.platform     = :ios, "11.0"
  s.source       = { :git => "https://github.com/tritonmedias/react-native-base64-audio-player.git", :tag => "v#{s.version}" }
  s.source_files = "ios/**/*.{h,m,swift}"
  s.requires_arc = true
  s.dependency 'React-Core'
  s.swift_version = '5.0'
end
