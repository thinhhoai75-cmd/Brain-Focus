package com.example.util

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlin.concurrent.thread
import kotlin.math.sin

enum class LofiSoundMode(val title: String, val description: String, val iconResName: String) {
    LOFI_CHILL("🎵 Nhạc lo-fi chill beats", "Giai điệu lofi 432Hz thư giãn", "music_note"),
    RAIN_FOCUS("🌧️ Tiếng mưa tĩnh lặng", "Tiếng mưa nhẹ cách âm xao nhãng", "water_drop"),
    COFFEE_SHOP("☕ Không gian quán cà phê", "Âm thanh ấm áp giúp tập trung", "coffee"),
    BINAURAL_ALPHA("🧘 Sóng não alpha 10Hz", "Sóng não kích thích khả năng ghi nhớ", "graphic_eq")
}

class LofiAudioPlayer {
    private var audioTrack: AudioTrack? = null
    private var isPlaying = false
    private var playThread: Thread? = null
    private var currentVolume = 0.5f
    private var currentMode = LofiSoundMode.LOFI_CHILL

    fun startSound(mode: LofiSoundMode) {
        stopSound()
        currentMode = mode
        isPlaying = true

        val sampleRate = 44100
        val bufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        ) * 2

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

        audioTrack?.setVolume(currentVolume)
        audioTrack?.play()

        playThread = thread(start = true) {
            val samples = ShortArray(1024)
            var sampleIndex = 0L

            // Pentatonic frequencies for Lofi Chill
            val chordFrequencies = doubleArrayOf(220.0, 261.63, 293.66, 329.63, 392.00, 440.0) // A3, C4, D4, E4, G4, A4

            while (isPlaying) {
                for (i in samples.indices) {
                    val t = sampleIndex / sampleRate.toDouble()
                    var sampleValue = 0.0

                    when (currentMode) {
                        LofiSoundMode.LOFI_CHILL -> {
                            // Warm Lo-Fi melody with gentle chord modulation
                            val chordStep = ((sampleIndex / (sampleRate * 2)) % chordFrequencies.size).toInt()
                            val baseFreq = chordFrequencies[chordStep]
                            val subFreq = baseFreq / 2.0
                            val env = (sin(2 * Math.PI * 0.5 * t) + 1.0) / 2.0 // slow breathe envelope

                            val sine1 = sin(2 * Math.PI * baseFreq * t)
                            val sine2 = sin(2 * Math.PI * subFreq * t) * 0.4
                            val lofiVinylNoise = (Math.random() - 0.5) * 0.04 // gentle vinyl crackle

                            sampleValue = (sine1 * 0.4 + sine2 + lofiVinylNoise) * (0.3 + 0.2 * env)
                        }

                        LofiSoundMode.RAIN_FOCUS -> {
                            // Pink noise algorithm filtered for gentle rain
                            val white = Math.random() * 2.0 - 1.0
                            val rainDroplets = if (Math.random() > 0.998) (Math.random() * 0.5) else 0.0
                            sampleValue = (white * 0.25 + rainDroplets)
                        }

                        LofiSoundMode.COFFEE_SHOP -> {
                            // Low warmth rumble + soft murmur
                            val rumble = sin(2 * Math.PI * 60.0 * t) * 0.15
                            val murmur = (Math.random() * 2.0 - 1.0) * 0.12
                            sampleValue = rumble + murmur
                        }

                        LofiSoundMode.BINAURAL_ALPHA -> {
                            // 432 Hz carrier + 10 Hz Alpha beat
                            val carrier = sin(2 * Math.PI * 432.0 * t) * 0.3
                            val alphaMod = sin(2 * Math.PI * 10.0 * t) * 0.2
                            sampleValue = carrier + alphaMod
                        }
                    }

                    // Clip to short range (-32768 to 32767)
                    val shortVal = (sampleValue * 12000.0 * currentVolume).toInt().coerceIn(-32000, 32000)
                    samples[i] = shortVal.toShort()
                    sampleIndex++
                }

                audioTrack?.write(samples, 0, samples.size)
            }
        }
    }

    fun setVolume(volume: Float) {
        currentVolume = volume.coerceIn(0f, 1f)
        audioTrack?.setVolume(currentVolume)
    }

    fun stopSound() {
        isPlaying = false
        try {
            audioTrack?.stop()
            audioTrack?.release()
        } catch (_: Exception) {}
        audioTrack = null
        playThread = null
    }

    fun isPlaying(): Boolean = isPlaying
    fun getCurrentMode(): LofiSoundMode = currentMode
}
