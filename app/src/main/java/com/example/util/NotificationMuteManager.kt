package com.example.util

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast

object NotificationMuteManager {
    private const val TAG = "NotificationMuteManager"
    
    // Store previous states to restore correctly
    private var previousRingerMode: Int = AudioManager.RINGER_MODE_NORMAL
    private var previousNotificationVolume: Int = -1
    private var previousRingVolume: Int = -1
    private var isDndCurrentlyEnabled: Boolean = false

    fun isPermissionGranted(context: Context): Boolean {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        return notificationManager?.isNotificationPolicyAccessGranted == true
    }

    fun isDndActive(): Boolean {
        return isDndCurrentlyEnabled
    }

    fun openDndSettings(context: Context) {
        try {
            val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
            Toast.makeText(
                context,
                "Vui lòng cấp quyền 'Truy cập Không làm phiền' cho Brain Focus để tự động tắt thông báo khi học!",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to open DND settings", e)
            try {
                val fallbackIntent = Intent(Settings.ACTION_SOUND_SETTINGS).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(fallbackIntent)
            } catch (_: Exception) {}
        }
    }

    /**
     * Mutes all notifications, calls, and ringers to create a deep focus shield.
     * Uses DND system filter if granted, plus AudioManager stream muting.
     */
    fun enableDoNotDisturb(context: Context): Boolean {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        var success = false

        try {
            audioManager?.let { am ->
                previousRingerMode = am.ringerMode
                previousNotificationVolume = am.getStreamVolume(AudioManager.STREAM_NOTIFICATION)
                previousRingVolume = am.getStreamVolume(AudioManager.STREAM_RING)
            }

            // Method 1: System Do Not Disturb (DND)
            if (notificationManager != null && notificationManager.isNotificationPolicyAccessGranted) {
                notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_PRIORITY)
                isDndCurrentlyEnabled = true
                success = true
                Log.d(TAG, "System DND enabled via NotificationManager")
            } else {
                Log.d(TAG, "Notification policy access not granted. Applying AudioManager silent mute fallback.")
            }

            // Method 2: AudioManager Silent Mute Fallback
            audioManager?.let { am ->
                try {
                    am.ringerMode = AudioManager.RINGER_MODE_SILENT
                } catch (_: Exception) {
                    try {
                        am.ringerMode = AudioManager.RINGER_MODE_VIBRATE
                    } catch (_: Exception) {}
                }

                try {
                    am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0, 0)
                    am.setStreamVolume(AudioManager.STREAM_RING, 0, 0)
                } catch (_: Exception) {}
                isDndCurrentlyEnabled = true
                success = true
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error enabling Do Not Disturb", e)
        }

        return success
    }

    /**
     * Restores notifications, ringers, and system interruptions to normal.
     */
    fun disableDoNotDisturb(context: Context): Boolean {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        var success = false

        try {
            // Restore System DND
            if (notificationManager != null && notificationManager.isNotificationPolicyAccessGranted) {
                notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
                Log.d(TAG, "System DND restored to INTERRUPTION_FILTER_ALL")
            }

            // Restore Audio Manager Ringer & Volumes
            audioManager?.let { am ->
                try {
                    am.ringerMode = if (previousRingerMode != AudioManager.RINGER_MODE_SILENT) {
                        previousRingerMode
                    } else {
                        AudioManager.RINGER_MODE_NORMAL
                    }
                } catch (_: Exception) {}

                try {
                    val maxNotif = am.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION)
                    val targetNotifVol = if (previousNotificationVolume > 0) previousNotificationVolume else (maxNotif / 2)
                    am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, targetNotifVol, 0)

                    val maxRing = am.getStreamMaxVolume(AudioManager.STREAM_RING)
                    val targetRingVol = if (previousRingVolume > 0) previousRingVolume else (maxRing / 2)
                    am.setStreamVolume(AudioManager.STREAM_RING, targetRingVol, 0)
                } catch (_: Exception) {}
            }

            isDndCurrentlyEnabled = false
            success = true
        } catch (e: Exception) {
            Log.e(TAG, "Error disabling Do Not Disturb", e)
        }

        return success
    }
}
