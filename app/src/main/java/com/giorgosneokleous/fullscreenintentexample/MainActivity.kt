/*
 * MIT License
 *
 * Copyright (c) 2020 Giorgos Neokleous
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.giorgosneokleous.fullscreenintentexample

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.giorgosneokleous.fullscreenintentexample.service.ApkDownloadService

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.showFullScreenIntentButton).setOnClickListener {
            showNotificationWithFullScreenIntent()
        }

        findViewById<Button>(R.id.showFullScreenIntentWithDelayButton).setOnClickListener {
            scheduleNotification(false)
        }

        findViewById<Button>(R.id.showFullScreenIntentLockScreenWithDelayButton).setOnClickListener {
            scheduleNotification(true)
        }

        with(findViewById<Button>(R.id.showVideoCallNotification)) {
            setOnClickListener{
                postDelayed({ startVideoCallActivity() }, 3000)
            }
        }


        findViewById<Button>(R.id.gotoAppNotificationSetting).setOnClickListener {
            this.gotoNotificationSettings(null)
        }

        findViewById<Button>(R.id.particularChannel).setOnClickListener {
            this.gotoNotificationSettings(CHANNEL_ID)
        }


        findViewById<Button>(R.id.isOpenNotificationPermission).setOnClickListener {
            Toast.makeText(this,if (this.isOpenedNotificationPermission()) "通知权限开启了" else "通知权限没开",Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.downloadApk).setOnClickListener {
            ApkDownloadService.startApkDownloadService(this@MainActivity);
        }

    }



    private fun startVideoCallActivity() {

        val fullScreenIntent = Intent(this, VideoCallActivity::class.java)
        val fullScreenPendingIntent = PendingIntent.getActivity(
            this, 0,
            fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder = NotificationCompat.Builder(this, "channelId")
            .setSmallIcon(R.drawable.abc_vector_test)
            .setContentTitle("Incoming call")
            .setContentText("(919) 555-1234") //以下为关键的3行
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setFullScreenIntent(fullScreenPendingIntent, true)

        var notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        with(notificationManager) {
            buildChannel()
        }

        notificationManager.notify(java.util.Random().nextInt(1000), notificationBuilder.build())

    }

}

