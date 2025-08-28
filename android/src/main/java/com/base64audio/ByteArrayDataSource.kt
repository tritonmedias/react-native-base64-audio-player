package com.base64audio
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DataSpec
import java.io.IOException


class ByteArrayDataSource(private val data: ByteArray) : DataSource {
  private var readPosition = 0
  private var opened = false
  override fun addTransferListener(transferListener: TransferListener?) {}
  @Throws(IOException::class)
  override fun open(dataSpec: DataSpec): Long {
    opened = true
    readPosition = dataSpec.position.toInt()
    return (data.size - readPosition).toLong()
  }
  @Throws(IOException::class)
  override fun read(buffer: ByteArray, offset: Int, readLength: Int): Int {
    if (!opened || readPosition >= data.size) return -1
    val toRead = kotlin.math.min(readLength, data.size - readPosition)
    System.arraycopy(data, readPosition, buffer, offset, toRead)
    readPosition += toRead
    return toRead
  }
  override fun getUri() = null
  override fun close() { opened = false }
}