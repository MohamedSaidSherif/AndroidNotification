package com.example.androidnotification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class CustomBroadCastReceiver : BroadcastReceiver() {
    companion object {
        private val TAG = CustomBroadCastReceiver::class.java.simpleName
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceive")
        intent?.let {
            Log.d(TAG, "received intent: $intent")
            if (it.action == MainActivity.OK_NOTIFICATION_ACTION) {
                Toast.makeText(context, "OK Button Is Clicked", Toast.LENGTH_SHORT).show()
            }
        }
    }
}