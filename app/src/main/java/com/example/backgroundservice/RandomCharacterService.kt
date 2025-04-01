package com.example.backgroundservice

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast

class RandomCharacterService : Service() {
    private var isRunning = false;

    private var TAG = "RandomCharacterService";
    private var charArr: List<Char> = ('a'..'z').toList()


    fun startRandomGenerator() {
        while (isRunning) {
            try {
                Thread.sleep(1000);
                if (isRunning) {
                    val min = 0;
                    val max = charArr.size - 1;
                    val randomIndex = (min..max).random();
                    val randomChar = charArr[randomIndex];
                    Log.i(TAG, "Random Character: $randomChar");
                    val broadCastIntent = Intent()
                    broadCastIntent.setAction("com.example.backgroundservice")
                    broadCastIntent.putExtra("randomChar", randomChar)
                    sendBroadcast(broadCastIntent)
                }
            } catch (e: Exception) {
                e.printStackTrace();
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(applicationContext, "Service started", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Service started");
        isRunning = true;
        Thread {
            startRandomGenerator();
        }.start()

        return START_STICKY;
    }

    private fun stopRandomGenerator() {
        isRunning = false;
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRandomGenerator()
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show()
        Log.i(TAG, "Service got destroyed")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null;
    }

}