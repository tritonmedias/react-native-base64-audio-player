package com.base64audio
import com.google.android.exoplayer2.upstream.DataSource

class ByteArrayDataSourceFactory(private var data: ByteArray) : DataSource.Factory {
  override fun createDataSource(): DataSource {
    return ByteArrayDataSource(data)
  }
}