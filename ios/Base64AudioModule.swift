import Foundation
import AVFoundation
import React

@objc(Base64Audio)
class Base64Audio: RCTEventEmitter, AVAudioPlayerDelegate {
    private var player: AVAudioPlayer?

    override func supportedEvents() -> [String]! {
        return ["Base64AudioEvent"]
    }

    @objc
    func play(_ base64: String, mimeType: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        DispatchQueue.global(qos: .userInitiated).async {
            guard let data = Data(base64Encoded: base64) else {
                reject("E_INVALID_BASE64", "Invalid base64", nil)
                return
            }
            do {
                try AVAudioSession.sharedInstance().setCategory(.playback, mode: .default)
                try AVAudioSession.sharedInstance().setActive(true)
                self.player = try AVAudioPlayer(data: data, fileTypeHint: mimeType)
                self.player?.delegate = self
                self.player?.prepareToPlay()
                self.player?.play()
                self.sendEvent(withName: "Base64AudioEvent", body: ["event": "onPlaybackStarted"])
                resolve(nil)
            } catch let err {
                self.sendEvent(withName: "Base64AudioEvent", body: ["event": "onError", "message": err.localizedDescription])
                reject("E_PLAYBACK", err.localizedDescription, err)
            }
        }
    }

    @objc func pause(_ resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) {
        player?.pause()
        sendEvent(withName: "Base64AudioEvent", body: ["event": "onPlaybackPaused"])
        resolve(nil)
    }

    @objc func resume(_ resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) {
        player?.play()
        sendEvent(withName: "Base64AudioEvent", body: ["event": "onPlaybackResumed"])
        resolve(nil)
    }

    @objc func stop(_ resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) {
        player?.stop()
        player = nil
        sendEvent(withName: "Base64AudioEvent", body: ["event": "onPlaybackStopped"])
        resolve(nil)
    }

    @objc func setVolume(_ value: NSNumber, resolver resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) {
        player?.volume = Float(truncating: value)
        resolve(nil)
    }

    @objc func unload(_ resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) {
        player?.stop()
        player = nil
        sendEvent(withName: "Base64AudioEvent", body: ["event": "onPlaybackStopped"])
        resolve(nil)
    }

    func audioPlayerDidFinishPlaying(_ player: AVAudioPlayer, successfully flag: Bool) {
        sendEvent(withName: "Base64AudioEvent", body: ["event": "onPlaybackCompleted"])
    }

    override static func requiresMainQueueSetup() -> Bool {
        return false
    }

    @objc override func addListener(_ eventName: String!) {
        super.addListener(eventName)
    }

    @objc override func removeListeners(_ count: Int) {
        super.removeListeners(count)
    }
}