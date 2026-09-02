package com.example.util

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.sin
import kotlin.random.Random

enum class LofiSoundType(val displayName: String, val description: String) {
    NONE("Tắt âm thanh", "Yên tĩnh tuyệt đối"),
    GAMMA_40HZ("Sóng não Gamma 40Hz", "Binaural beats 40Hz kích hoạt vỏ não trước"),
    WHITE_NOISE("Tiếng ồn trắng (White Noise)", "Tạo màn chắn âm thanh chống phân tâm"),
    RAIN_SOUNDS("Mưa rơi êm dịu (Raindrops)", "Âm thanh thư giãn nhịp sinh học"),
    LOFI_CHILL_BEATS("Lofi Chill Synth Loop", "Giai điệu chậm rãi thư thái 70 BPM")
}

class LofiAudioPlayer(private val context: Context) {
    private var audioTrack: AudioTrack? = null
    private var playJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    var currentSoundType: LofiSoundType = LofiSoundType.NONE
        private set

    fun play(soundType: LofiSoundType) {
        if (currentSoundType == soundType && isPlaying()) return
        stop()
        currentSoundType = soundType
        if (soundType == LofiSoundType.NONE) return

        playJob = scope.launch {
            try {
                val sampleRate = 22050
                val minBufferSize = AudioTrack.getMinBufferSize(
                    sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                )
                val bufferSize = (minBufferSize * 2).coerceAtLeast(sampleRate / 2)

                audioTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(bufferSize)
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .build()

                audioTrack?.play()

                val buffer = ShortArray(1024)
                var sampleIndex = 0L

                while (isActive && currentSoundType == soundType) {
                    for (i in buffer.indices) {
                        val t = (sampleIndex + i).toDouble() / sampleRate
                        val sampleValue: Double = when (soundType) {
                            LofiSoundType.GAMMA_40HZ -> {
                                // 40Hz modulation on a carrier wave (200Hz)
                                val carrier = sin(2.0 * Math.PI * 216.0 * t)
                                val beat = (1.0 + sin(2.0 * Math.PI * 40.0 * t)) / 2.0
                                carrier * beat * 0.4
                            }
                            LofiSoundType.WHITE_NOISE -> {
                                // Filtered soft white noise
                                (Random.nextDouble(-1.0, 1.0) * 0.18)
                            }
                            LofiSoundType.RAIN_SOUNDS -> {
                                // Raindrop texture (soft noise + sporadic drops)
                                val baseNoise = Random.nextDouble(-0.15, 0.15)
                                val dropMod = if (Random.nextInt(1000) < 5) sin(2.0 * Math.PI * 600.0 * t) * 0.25 else 0.0
                                (baseNoise + dropMod) * 0.8
                            }
                            LofiSoundType.LOFI_CHILL_BEATS -> {
                                // Relaxing ambient chord synthesizer loop (Pentatonic chord progression)
                                val chordFreq1 = 220.0 // A3
                                val chordFreq2 = 277.18 // C#4
                                val chordFreq3 = 329.63 // E4
                                val lfo = (1.0 + sin(2.0 * Math.PI * 0.5 * t)) / 2.0 // 0.5Hz gentle breathe
                                val synth = (sin(2.0 * Math.PI * chordFreq1 * t) +
                                        0.8 * sin(2.0 * Math.PI * chordFreq2 * t) +
                                        0.6 * sin(2.0 * Math.PI * chordFreq3 * t)) / 3.0
                                synth * lfo * 0.35
                            }
                            LofiSoundType.NONE -> 0.0
                        }
                        buffer[i] = (sampleValue * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                    }
                    audioTrack?.write(buffer, 0, buffer.size)
                    sampleIndex += buffer.size
                }
            } catch (_: Exception) {
                // Audio track safely closed
            }
        }
    }

    fun stop() {
        playJob?.cancel()
        playJob = null
        try {
            audioTrack?.pause()
            audioTrack?.flush()
            audioTrack?.stop()
            audioTrack?.release()
        } catch (_: Exception) {}
        audioTrack = null
        currentSoundType = LofiSoundType.NONE
    }

    fun isPlaying(): Boolean {
        return audioTrack != null && currentSoundType != LofiSoundType.NONE
    }
}
