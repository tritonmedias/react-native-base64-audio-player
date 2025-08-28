import Foundation
import AVFoundation
import React


@objc(Base64Audio)
class Base64Audio: NSObject {
  private var player: AVAudioPlayer?

  @objc
  func play(_ base64: String, mimeType: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
    DispatchQueue.global(qos: .userInitiated).async {
      guard let data = Data(base64Encoded: base64) else { reject("E_INVALID_BASE64", "Invalid base64", nil); return }
      do {
        try AVAudioSession.sharedInstance().setCategory(.playback, mode: .default)
        try AVAudioSession.sharedInstance().setActive(true)
        self.player = try AVAudioPlayer(data: data, fileTypeHint: mimeType)
        self.player?.prepareToPlay()
        self.player?.play()
        resolve(nil)
      } catch let err { reject("E_PLAYBACK", err.localizedDescription, err) }
    }
  }

  @objc func pause(_ resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) { player?.pause(); resolve(nil) }
  @objc func resume(_ resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) { player?.play(); resolve(nil) }
  @objc func stop(_ resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) { player?.stop(); player = nil; resolve(nil) }
  @objc func setVolume(_ value: NSNumber, resolver resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) { player?.volume = Float(truncating: value); resolve(nil) }
  @objc func unload(_ resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) { player?.stop(); player = nil; resolve(nil) }
  @objc static func requiresMainQueueSetup() -> Bool { return false }
}