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

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.io.File

fun Context.showNotificationWithFullScreenIntent(
    isLockScreen: Boolean = false,
    channelId: String = CHANNEL_ID,
    title: String = "Title",
    description: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit."

) {
    val builder = NotificationCompat.Builder(this, channelId)
        //声音
        .setSound(Uri.fromFile(File("system/media/audio/notifications/Argon.ogg")))
        //默认铃声
        //.setDefaults(NotificationCompat.DEFAULT_SOUND)
        /**  它的参数为一个 long 的数组，以 ms 为单位，如果我们想让手机震动1s，延时一秒，再震动一次，当然，它需要向申请权限
            <uses-permission android:name="android.permission.VIBRATE"/>
         */
        //.setVibrate(longArrayOf(1000L,0L,1000L,1000L))
        /**  默认震动效果  */
        .setDefaults(NotificationCompat.DEFAULT_VIBRATE)

        .setSmallIcon(android.R.drawable.arrow_up_float)
        .setContentTitle(title)
        .setContentText(description)
        /**
        显示一段长文字
        想显示一段比较长的文字，或者一个比较大的图片，使用默认的 setContentText 或者 setLargeIcon 是不行的，
        但是可以使用 setStyle ，setStyle 可以让通知栏变大来显示通知。 会覆盖setContentText方法
         */
        .setStyle(NotificationCompat.BigTextStyle().bigText("我是内容，我是demo\n我不是测试\n 我是长文字啊"))
        /**  大图片  */
        .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.wine))
        .setStyle(NotificationCompat.BigPictureStyle()
            .bigPicture(BitmapFactory.decodeResource(
                resources,
                R.mipmap.wine)).bigLargeIcon(null)
        )
        /**  一个通知最多可以提供三个操作按钮  */
        .addAction(R.mipmap.bottle,"酒",getFullScreenIntent(isLockScreen))

        .setProgress(100, 0, false)


        /**
         优先级在 7.1 及以下有用，8.0 则使用 channel 来设置。
         通知栏是可以设置优先级的，它有5个常亮可以选
         PRIORITY_DEFAULT 默认优先级
         PRIORITY_MIN 优先级最低，某个特定场合才显示
         PRIORITY_LOW 优先级较低，系统可能将这类通知缩小，或者改变它的显示位置，比如靠后的位置
         PRIORITY_HIGH 表示较高程度，系统可能对它进行方法，或者改变位置，比如靠前的位置
         PRIORITY_MAX 优先级最高，这类通知会让用户立刻看到
          */
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setCategory(NotificationCompat.CATEGORY_ALARM)

        /**
        还可以通过设置 setVisibility(int visibility) 来设置锁屏屏幕的范围，共有三种选择：
        VISIBILITY_PUBLIC 显示通知的完整内容。
        VISIBILITY_SECRET 不在锁定屏幕上显示该通知的任何部分。
        VISIBILITY_PRIVATE 显示基本信息，例如通知图标和内容标题，但隐藏通知的完整内容。你如，显示"您有三条新短信"，但是发件人和内容不可见。
        */
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        //setFullScreenIntent(getFullScreenIntent(isLockScreen), true) 和
        //setContentIntent(getFullScreenIntent(isLockScreen)) 的区别 ？
        /**
        意图启动而不是将通知发布到状态栏。仅用于要求用户的极高优先级通知
        <strong>立即</strong>注意，例如用户明确设置为特定时间的来电或闹钟。如果此功能用于其他用途，请为用户提供关闭它并使用正常通知的选项，因为这可能会造成极大的破坏。
        <p> 在某些平台上，系统 UI 可能会选择在用户使用设备时显示提示通知，而不是启动此意图。 </p>
        @param intent 待启动的意图。 @param highPriority 传递 true 将导致发送此通知，即使其他通知被抑制。
         */
        .setFullScreenIntent(getFullScreenIntent(isLockScreen), true)
        /**
        提供一个 {@link PendingIntent} 在点击通知时发送。如果您不提供意图，您现在可以通过调用
        {
        @link RemoteViews#setOnClickPendingIntent
        RemoteViews.setOnClickPendingIntent(int,PendingIntent)
        }
        将 PendingIntents 添加到要在单击时启动的单个视图。
        请务必阅读 {@link Notification#contentIntent Notification.contentIntent} 以了解如何正确使用它。
         */
//        .setContentIntent(getFullScreenIntent(isLockScreen))
        .setAutoCancel(true);

    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    with(notificationManager) {
        buildChannel()

        val notification = builder.build()
        notify(0, notification)
    }
}

 fun NotificationManager.buildChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Example Notification Channel"
        val descriptionText = "This is used to demonstrate the Full Screen Intent"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        createNotificationChannel(channel)
    }
}

private fun Context.getFullScreenIntent(isLockScreen: Boolean): PendingIntent {
    val destination = if (isLockScreen)
        LockScreenActivity::class.java
    else
        FullScreenActivity::class.java
    val intent = Intent(this, destination)

    // flags and request code are 0 for the purpose of demonstration
//    return PendingIntent.getActivity(this, 0, intent, 0)
    return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
}

 const val CHANNEL_ID = "channelId"


/**
 * areNotificationsEnabled方法的有效性官方只最低支持到API 19，低于19的仍可调用此方法不过只会返回true，即默认为用户已经开启了通知。
 * @return
 */
fun Context.isOpenedNotificationPermission(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        val manager = NotificationManagerCompat.from(this)
        manager.areNotificationsEnabled()
    } else {
        true
    }
}

/**
 * Send intent to load system Notification Settings UI for a particular channel.
 *
 * @param channelId Name of channel to configure
 */
fun Context.gotoNotificationSettings(channelId: String?) {

    val intent: Intent = if (TextUtils.isEmpty(channelId)) {
        Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
    } else {
        Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
    }
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.putExtra(Settings.EXTRA_APP_PACKAGE, this.packageName)
    intent.putExtra(Settings.EXTRA_CHANNEL_ID, channelId)
    this.startActivity(intent)
}
