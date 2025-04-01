package com.example.backgroundservice

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    private var editTextView: EditText? = null

    private var broadcastReceiver: BroadcastReceiver? = null

    private var serviceIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        editTextView = findViewById(R.id.editTextText)
        serviceIntent = Intent(this, RandomCharacterService::class.java)

        findViewById<Button>(R.id.startBtn).setOnClickListener {
            startService(serviceIntent)
        }

        findViewById<Button>(R.id.stopBtn).setOnClickListener {
            stopService(serviceIntent)
        }
    }


    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onStart() {
        super.onStart()
        broadcastReceiver = MyBroadcastReceiver { message ->
            runOnUiThread {
                Log.d("MainActivity", "Got broadcast: $message")
                editTextView?.setText(message)
            }
        }

        val intentFilter = IntentFilter("com.example.backgroundservice")
        Log.d("MainActivity", "Registering broadcast receiver")
        registerReceiver(broadcastReceiver, intentFilter, Context.RECEIVER_NOT_EXPORTED)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcastReceiver)
    }

    class MyBroadcastReceiver(private val updateText: (String) -> Unit) : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            try {
                val message = intent?.getCharExtra("randomChar", '?') ?: '?'
                updateText(message.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
