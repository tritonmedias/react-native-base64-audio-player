package com.base64audio

import android.util.Base64
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ProgressiveMediaSource

class Base64AudioModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    private var player: ExoPlayer? = null

    override fun getName(): String = "Base64Audio"

    private fun sendEvent(eventName: String, params: WritableMap?) {
        reactApplicationContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit(eventName, params)
    }

    @ReactMethod
    fun play(base64: String, mimeType: String, promise: Promise) {
        try {
            val bytes = Base64.decode(base64, Base64.DEFAULT)
            val context = reactApplicationContext

            if (player != null) {
                player?.stop()
                player?.release()
                player = null
            }

            player = ExoPlayer.Builder(context).build()
            player?.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    val event = Arguments.createMap()
                    when (playbackState) {
                        Player.STATE_ENDED -> sendEvent("Base64AudioEvent", event.apply { putString("event", "onPlaybackCompleted") })
                        Player.STATE_READY -> sendEvent("Base64AudioEvent", event.apply { putString("event", "onPlaybackStarted") })
                    }
                }

                override fun onPlayerError(error: com.google.android.exoplayer2.PlaybackException) {
                    val event = Arguments.createMap()
                    event.putString("event", "onError")
                    event.putString("message", error.localizedMessage)
                    sendEvent("Base64AudioEvent", event)
                }
            })

            val factory = ByteArrayDataSourceFactory(bytes)
            val mediaSource = ProgressiveMediaSource.Factory(factory).createMediaSource(MediaItem.fromUri("memory://audio"))
            player?.setMediaSource(mediaSource)
            player?.prepare()
            player?.play()
            promise.resolve(null)
        } catch (e: Exception) {
            promise.reject("E_PLAYBACK", e.localizedMessage, e)
        }
    }

    @ReactMethod fun pause(promise: Promise) { player?.pause(); sendEvent("Base64AudioEvent", Arguments.createMap().apply { putString("event", "onPlaybackPaused") }); promise.resolve(null) }
    @ReactMethod fun resume(promise: Promise) { player?.play(); sendEvent("Base64AudioEvent", Arguments.createMap().apply { putString("event", "onPlaybackResumed") }); promise.resolve(null) }
    @ReactMethod fun stop(promise: Promise) { player?.stop(); player?.release(); player = null; sendEvent("Base64AudioEvent", Arguments.createMap().apply { putString("event", "onPlaybackStopped") }); promise.resolve(null) }
    @ReactMethod fun setVolume(value: Double, promise: Promise) { player?.volume = value.toFloat(); promise.resolve(null) }
    @ReactMethod fun unload(promise: Promise) { player?.stop(); player?.release(); player = null; sendEvent("Base64AudioEvent", Arguments.createMap().apply { putString("event", "onPlaybackStopped") }); promise.resolve(null) }

    @ReactMethod
    fun addListener(eventName: String) {
        // Keep: Required for RN built in Event Emitter Calls.
    }

    @ReactMethod
    fun removeListeners(count: Int) {
        // Keep: Required for RN built in Event Emitter Calls.
    }
}