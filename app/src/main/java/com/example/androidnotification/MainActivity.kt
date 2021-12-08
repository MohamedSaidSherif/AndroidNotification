package com.example.androidnotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import com.example.androidnotification.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.media.RingtoneManager
import android.net.Uri


class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val CHANNEL_ID = "Test_Channel_ID"
        private const val NOTIFICATION_ID = 150
        const val OK_NOTIFICATION_ACTION = "OK Notification Action"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        createNotificationChannel()

        // Create an explicit intent for an Activity in your app
//        val intent = Intent(this, SecondActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
//        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

//        val okIntent = Intent(this, CustomBroadCastReceiver::class.java).apply {
//            action = OK_NOTIFICATION_ACTION
//        }
//        val okPendingIntent: PendingIntent = PendingIntent.getBroadcast(this, 0, okIntent, 0)

//        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
//            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
//            .setContentTitle("Notification Test")
//            .setContentText("Notification body: Hi this is mohamed sherif from integrant")
//            .setStyle(
//                NotificationCompat.BigTextStyle()
//                    .bigText("BigSty -> Notification body: Hi this is mohamed sherif from integrant")
//            )
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            .setContentIntent(pendingIntent)
//            .setAutoCancel(true)
//            .addAction(R.drawable.ic_baseline_check_24, getString(R.string.ok), okPendingIntent)
//
//        with(NotificationManagerCompat.from(this)) {
//            notify(NOTIFICATION_ID, builder.build())
//        }

        GlobalScope.launch {
            delay(3000)
            val fullScreenIntent = Intent(this@MainActivity, SecondActivity::class.java)
            val fullScreenPendingIntent =
                PendingIntent.getActivity(
                    this@MainActivity,
                    0,
                    fullScreenIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            val bitmap = AppCompatResources.getDrawable(
                this@MainActivity,
                R.drawable.ic_baseline_notifications_24
            )?.toBitmap()
            Log.d(TAG, "Bitmap: $bitmap")
            launch(Dispatchers.Main) {
                binding.activityMainImageView.setImageBitmap(bitmap)
            }
            val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            val builder = NotificationCompat.Builder(this@MainActivity, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle("Notification Using Full Screen Intent")
                .setContentText("Full Screen Intent Description, Full Screen Intent Description, Full Screen Intent Description")
                .setLargeIcon(bitmap)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText("Full Screen Intent Description, Full Screen Intent Description, Full Screen Intent Description")
                )
                .setStyle(
                    NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null)
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setFullScreenIntent(fullScreenPendingIntent, true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSound(uri)
                .setNumber(6)

            with(NotificationManagerCompat.from(this@MainActivity)) {
                notify(NOTIFICATION_ID, builder.build())
            }
            Log.d(TAG, "--------------DONE--------")
            delay(2000)
            //use constant ID for notification used as group summary
            val SUMMARY_ID = 0
            val GROUP_KEY_WORK_EMAIL = "com.android.example.WORK_EMAIL"

            val newMessageNotification1 = NotificationCompat.Builder(this@MainActivity, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notify_email_status)
                .setContentTitle("emailObject1.getSummary()")
                .setContentText("You will not believe...")
                .setGroup(GROUP_KEY_WORK_EMAIL)
                .build()

            val newMessageNotification2 = NotificationCompat.Builder(this@MainActivity, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notify_email_status)
                .setContentTitle("emailObject2.getSummary()")
                .setContentText("Please join us to celebrate the...")
                .setGroup(GROUP_KEY_WORK_EMAIL)
                .build()

            val summaryNotification = NotificationCompat.Builder(this@MainActivity, CHANNEL_ID)
                .setContentTitle("emailObject.getSummary()")
                //set content text to support devices running API level < 24
                .setContentText("Two new messages")
                .setSmallIcon(R.drawable.ic_notify_summary_status)
                //build summary info into InboxStyle template
                .setStyle(NotificationCompat.InboxStyle()
                    .addLine("Alex Faarborg Check this out")
                    .addLine("Jeff Chang Launch Party")
                    .setBigContentTitle("2 new messages")
                    .setSummaryText("janedoe@example.com"))
                //specify which group this notification belongs to
                .setGroup(GROUP_KEY_WORK_EMAIL)
                //set this notification as the summary for the group
                .setGroupSummary(true)
                .setNumber(8)
                .build()

            NotificationManagerCompat.from(this@MainActivity).apply {
                notify(1, newMessageNotification1)
                notify(2, newMessageNotification2)
                notify(SUMMARY_ID, summaryNotification)
            }
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            channel.enableLights(true)
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}