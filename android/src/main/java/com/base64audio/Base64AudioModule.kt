package com.base64audio
import android.util.Base64
import com.facebook.react.bridge.*
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ProgressiveMediaSource

class Base64AudioModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
  private var player: ExoPlayer? = null
  override fun getName(): String = "Base64Audio"

  @ReactMethod
  fun play(base64: String, mimeType: String, promise: Promise) {
    try {
      val bytes = Base64.decode(base64, Base64.DEFAULT)
      val context = reactApplicationContext
      if (player == null) player = ExoPlayer.Builder(context).build() else { player?.stop(); player?.clearMediaItems() }
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

  @ReactMethod fun pause(promise: Promise) { player?.pause(); promise.resolve(null) }
  @ReactMethod fun resume(promise: Promise) { player?.play(); promise.resolve(null) }
  @ReactMethod fun stop(promise: Promise) { player?.stop(); player?.release(); player = null; promise.resolve(null) }
  @ReactMethod fun setVolume(value: Double, promise: Promise) { player?.volume = value.toFloat(); promise.resolve(null) }
  @ReactMethod fun unload(promise: Promise) { player?.stop(); player?.release(); player = null; promise.resolve(null) }
}