package com.luisenricke.helpyourself.service

import android.app.*
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.luisenricke.androidext.preferenceGet
import com.luisenricke.helpyourself.Constraint
import com.luisenricke.helpyourself.MainActivity
import com.luisenricke.helpyourself.R
import com.luisenricke.helpyourself.alert.AlertFacade
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.abs

// https://expertise.jetruby.com/how-to-implement-motion-sensor-in-a-kotlin-app-b70db1b5b8e5
// https://stackoverflow.com/a/55607504/12923478
// TODO customize the notification
@Suppress("unused")
class SensorForeground : Service(), SensorEventListener {

    companion object {
        private const val SERVICE_ID = 1010

        private const val CHANNEL_ID = "SensorAlertChannelID"
        private const val CHANNEL_NAME = "SensorAlert Notification Channel"
        private const val NOTIFICATION_TITLE = R.string.alert_service_notification_title
        private const val NOTIFICATION_CONTENT = R.string.alert_service_notification_content
        private const val NOTIFICATION_ICON = R.drawable.ic_siren

        private var isCycleTriggered = false
        private const val CYCLE_TIME_DURATION = 1000 * 5 // milliseconds * seconds
        private const val CYCLE_TIME_PAUSE = 200L // milliseconds
        private const val CYCLE_ACTIVATION_LIMIT = 5

        private var sensitivity: Float = 15f

        @JvmStatic
        fun startService(context: Context) {
            val startIntent = Intent(context, SensorForeground::class.java)
            ContextCompat.startForegroundService(context, startIntent)
        }

        @JvmStatic
        fun stopService(context: Context) {
            val stopIntent = Intent(context, SensorForeground::class.java)
            context.stopService(stopIntent)
        }
    }

    private var manager: SensorManager? = null
    private var accelerometer: Sensor? = null

    // region Service
    override fun onCreate() {
        super.onCreate()
        manager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = manager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        manager?.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)

        val sensitivityPreference = preferenceGet(Constraint.ALERT_SENSITIVITY, String::class)
        val sensitivities = resources.getStringArray(R.array.settings_category_alert_sensitivity_list)
        sensitivity = when (sensitivityPreference) {
            sensitivities[0] -> 13f
            sensitivities[1] -> 15f
            sensitivities[2] -> 17f
            else             -> 14f
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = buildNotification()
        startForeground(SERVICE_ID, notification)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        manager?.unregisterListener(this)
        // SendSMS.getInstance(this).unregisterReceivers()
    }

    override fun onBind(intent: Intent): IBinder? =
            null

    private fun buildNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE_HIGH)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(serviceChannel)
        }

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        return NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(NOTIFICATION_TITLE))
                .setContentText(getString(NOTIFICATION_CONTENT))
                .setSmallIcon(NOTIFICATION_ICON)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
    }
    // endregion Service

    // region Sensor
    override fun onSensorChanged(event: SensorEvent?) {
        val gravity = floatArrayOf(0.0f, 0.0f, 0.0f)
        val acceleration = floatArrayOf(0.0f, 0.0f, 0.0f)
        val x: Float

        when (event?.sensor?.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                val alpha = 0.8f
                gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0]
                acceleration[0] = event.values[0] - gravity[0]
                x = abs(acceleration[0])

                if (x >= sensitivity && !isCycleTriggered) sensorCycleCheck(x)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) =
            Unit

    private fun sensorCycleCheck(xAxis: Float) {
        val startTime = System.currentTimeMillis()

        CoroutineScope(Dispatchers.Default).launch {
            isCycleTriggered = true

            var count = 0
            var totalElapsed = 0L
            while (totalElapsed < CYCLE_TIME_DURATION) {
                totalElapsed = System.currentTimeMillis() - startTime
                count = if (xAxis >= sensitivity) count + 1 else count
                // Timber.d("total: $totalElapsed, xAxis: $xAxis, count: $count")
                delay(CYCLE_TIME_PAUSE)
            }

            if (count > CYCLE_ACTIVATION_LIMIT) {
                try {
                    AlertFacade().alertTriggeredBackgroundThreat(this@SensorForeground)
                } catch (e: Exception) { // TODO Check who is the correct exception
                    Timber.e("Exceeded request")
                }
            }

            isCycleTriggered = false
        }

    }
    // endregion Sensor
}
