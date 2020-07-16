package com.example.penyimpangan_idm

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseFCM : FirebaseMessagingService() {

    val TAG = String::class.java.simpleName

     override fun onMessageReceived(p0: RemoteMessage) {
        Log.d(TAG,"From: ${p0?.from}")

        p0?.data?.isNotEmpty()?.let {
            Log.d(TAG,"Message data payload " + p0.data)

            if(!p0.data.isNullOrEmpty()){
                val msg:String = p0.data.get("message").toString()
            }
        }

        p0?.notification?.let {
            sendNotification(p0.notification?.body)
        }
    }

    private fun sendNotification(messageBody:String?){
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this,0, intent, PendingIntent.FLAG_ONE_SHOT)
        val chanelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, chanelId)
            .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
//            .setContentTitle(getString(R.string.fcm_message))
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(chanelId,"Channel Title", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0,notificationBuilder.build())
    }
}
