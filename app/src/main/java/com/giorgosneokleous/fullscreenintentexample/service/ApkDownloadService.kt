package com.giorgosneokleous.fullscreenintentexample.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.giorgosneokleous.fullscreenintentexample.R
import kotlin.random.Random

class ApkDownloadService : Service() {

    var mHandler = Handler(Looper.getMainLooper())
    companion object {
        const val NOTIFICATION_ID_PROGRESS = 1512
        const val NOTIFICATION_ID_RESULT = 1513

        const val CHANNEL_ID = "content_loading"
        const val CHANNEL_NAME = "Content Loading"
        const val ACTION_START = "fullscreenintentexample"

        fun startApkDownloadService(context : Context) {
            val intent = Intent(context, ApkDownloadService::class.java)
            intent.action = ApkDownloadService.ACTION_START
            context.startService(intent)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            val action = intent.action
            if (ACTION_START == action) {
                startDownloadApk()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationGroupIfNeed()
    }

    override fun onBind(intent: Intent): IBinder? = null


    private fun startDownloadApk() {

        var process = 0
        Thread {
            while (process < 100){

                mHandler.post {
                    showProgressNotification(process)
                }
                Thread.sleep(1000)
                process += Random.nextInt(25)
                if (process in 89..92){
                    stop()

                    if (isAppForeground()) {
                        openSuccessResultScreen(ResultBean(ResultBean.DOWNLOAD_STATE_ERROR))
                    } else {
                        showServiceFailedNotification(ResultBean(ResultBean.DOWNLOAD_STATE_ERROR))
                        showFailureResultMessage()
                    }

                    return@Thread
                }
            }

            stop()
            if (isAppForeground()) {
                openSuccessResultScreen(ResultBean(ResultBean.DOWNLOAD_STATE_SUCCESS))
            } else {
                showServiceSucceedNotification(ResultBean(ResultBean.DOWNLOAD_STATE_SUCCESS))
            }

        }.start()

    }


    private fun showServiceSucceedNotification(resultBean: ResultBean) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultBean.DOWNLOAD_STATE, resultBean)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.arrow_up_float)
                .setContentTitle(getString(R.string.notification_your_request_is_done))
                .setContentText(getString(R.string.notification_your_request_is_success))
                .setTicker(getString(R.string.notification_download))
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .build()
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID_RESULT, notification)
    }

    private fun showServiceFailedNotification(resultBean: ResultBean) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultBean.DOWNLOAD_STATE, resultBean)
        intent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.arrow_up_float)
                .setContentTitle(getString(R.string.notification_your_request_is_done))
                .setContentText(getString(R.string.notification_your_request_is_fail))
                .setTicker(getString(R.string.notification_your_request_is_fail))
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .build()
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID_RESULT, notification)
    }

    private fun showProgressNotification(process:Int) {
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.arrow_up_float)
            .setContentTitle(getString(R.string.notification_downloading))
                .setContentTitle(getString(R.string.notification_downloading))
                .setTicker(getString(R.string.notification_downloading))
                /**
                功能：设置带进度条的通知，可以在下载中使用。
                描述：max:进度条最大数值 、progress:当前进度、indeterminate:表示进度是否不确定，true为不确定。
                注意：此方法在4.0及以后版本才有用，如果为早期版本：需要自定义通知布局，其中包含ProgressBar视图
                使用：如果为确定的进度条：调用setProgress(max, progress, false)来设置通知，在更新进度的时候在此发起通知更新progress，并且在下载完成后要移除进度条，通过调用setProgress(0, 0, false)既可。
                如果为不确定（持续活动）的进度条，这是在处理进度无法准确获知时显示活动正在持续，所以调
                用setProgress(0, 0, true) ，操作结束时，调用setProgress(0, 0, false)并更新通知以移除指示条
                 */
                .setProgress(100, process, false)
                /** 设置该属性以后，用户点击该通知时会清除它  */
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

        startForeground(NOTIFICATION_ID_PROGRESS, notification)
    }

    private fun stop() {
        stopForeground(true)
    }

    private fun openSuccessResultScreen(resultBean: ResultBean) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra(ResultBean.DOWNLOAD_STATE, resultBean)
        startActivity(intent)
    }


    private fun showFailureResultMessage() {
        mHandler.post {
            Toast.makeText(this, R.string.notification_your_request_is_fail, Toast.LENGTH_SHORT).show()
        }

    }

    private fun isAppForeground(): Boolean {
        val appProcessInfo = ActivityManager.RunningAppProcessInfo()
        ActivityManager.getMyMemoryState(appProcessInfo)
        return appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND || appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE
    }

    private fun createNotificationGroupIfNeed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NotificationManager::class.java)
            if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
                channel.enableVibration(true)
                notificationManager.createNotificationChannel(channel)
            }
        }
    }
}
