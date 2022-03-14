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

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import java.util.concurrent.TimeUnit

/**
 * Created by fuyuguang on 2022/3/11 9:26 上午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 * 参考：
    [关于使用AlarmManager的注意事项](https://www.jianshu.com/p/d69a90bc44c0)
    [Android中的AlarmManager的使用Android中的AlarmManager的使用](https://blog.csdn.net/wangxingwu_314/article/details/8060312)
    [如果您的应用使用 AlarmManager](https://developer.android.google.cn/about/versions/android-4.4.html#BehaviorAlarms)
    [低电耗模式限制](https://developer.android.google.cn/training/monitoring-device-state/doze-standby.html#restrictions)


    AlarmManager的常用方法有三个：
    （1）set(int type，long startTime，PendingIntent pi)；
            该方法用于设置【一次性闹钟】，第一个参数表示闹钟类型，第二个参数表示闹钟执行时间，第三个参数表示闹钟响应动作。
    （2）setRepeating(int type，long startTime，long intervalTime，PendingIntent pi)；
            该方法用于设置【重复闹钟】，第一个参数表示闹钟类型，第二个参数表示闹钟首次执行时间，第三个参数表示闹钟两次执行的间隔时间，第三个参数表示闹钟响应动作。
    （3）setInexactRepeating（int type，long startTime，long intervalTime，PendingIntent pi）；
            该方法也用于设置【重复闹钟】，与第二个方法相似，不过其两个闹钟执行的间隔时间不是固定的而已。

    方法1和方法2在SDK_INT 19以前是精确的闹钟,19以后为了节能省电（减少系统唤醒和电池使用）。
    使用Alarm.set()和Alarm.setRepeating()已经不能保证精确性,不过还好Google又提供了两个精确的Alarm方法setWindow()和setExact(),
    如果您需要设置在设备处于低电耗模式时触发的闹钟，请使用 setAndAllowWhileIdle() 或 setExactAndAllowWhileIdle()。

    三个方法各个参数详悉：
    （1）int type： 闹钟的类型，常用的有5个值

        AlarmManager.ELAPSED_REALTIME表示闹钟在手机睡眠状态下不可用，该状态下闹钟使用相对时间（相对于系统启动开始），状态值为3；
        AlarmManager.RTC表示闹钟在睡眠状态下不可用，该状态下闹钟使用绝对时间，即当前系统时间，状态值为1；
        AlarmManager.POWER_OFF_WAKEUP表示闹钟在手机关机状态下也能正常进行提示功能，所以是5个状态中用的最多的状态之一，该状态下闹钟也是用绝对时间，状态值为4；不过本状态好像受SDK版本影响，某些版本并不支持；

        AlarmManager.ELAPSED_REALTIME_WAKEUP表示闹钟在睡眠状态下会唤醒系统并执行提示功能，该状态下闹钟也使用相对时间，状态值为2；
        AlarmManager.RTC_WAKEUP表示闹钟在睡眠状态下会唤醒系统并执行提示功能，该状态下闹钟使用绝对时间，状态值为0；

    （2）long startTime： 闹钟的第一次执行时间，以毫秒为单位，可以自定义时间，不过一般使用当前时间。需要注意的是，本属性与第一个属性（type）密切相关，如果第一个参数对 应的闹钟使用的是相对时间（ELAPSED_REALTIME和ELAPSED_REALTIME_WAKEUP），那么本属性就得使用相对时间（相对于 系统启动时间来说），比如当前时间就表示为：SystemClock.elapsedRealtime()；如果第一个参数对应的闹钟使用的是绝对时间 （RTC、RTC_WAKEUP、POWER_OFF_WAKEUP），那么本属性就得使用绝对时间，比如当前时间就表示 为：System.currentTimeMillis()。

    （3）long intervalTime：对于后两个方法来说，存在本属性，表示两次闹钟执行的间隔时间，也是以毫秒为单位。

    （4）PendingIntent pi： 绑定了闹钟的执行动作，比如发送一个广播、给出提示等等。PendingIntent是Intent的封装类。需要注意的是，如果是通过启动服务来实现闹钟提 示的话，PendingIntent对象的获取就应该采用Pending.getService(Context c,int i,Intent intent,int j)方法；如果是通过广播来实现闹钟提示的话，PendingIntent对象的获取就应该采用 PendingIntent.getBroadcast(Context c,int i,Intent intent,int j)方法；如果是采用Activity的方式来实现闹钟提示的话，PendingIntent对象的获取就应该采用 PendingIntent.getActivity(Context c,int i,Intent intent,int j)方法。如果这三种方法错用了的话，虽然不会报错，但是看不到闹钟提示效果。
 */
fun Context.scheduleNotification(isLockScreen: Boolean) {
    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    /**
    TimeUnit.SECONDS.toMillis(1)     1秒转换为毫秒数
    TimeUnit.SECONDS.toMinutes(60)   60秒转换为分钟数
    TimeUnit.SECONDS.sleep(5)  线程休眠5秒
    TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES) 1分钟转换为秒数
     */
    val timeInMillis = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(SCHEDULE_TIME) //延迟5秒

    with(alarmManager) {

        //AlarmManager.RTC_WAKEUP 表示闹钟在睡眠状态下会唤醒系统并执行提示功能，该状态下闹钟使用绝对时间，状态值为0
        //安排在规定时间准确发送警报，精准的闹钟定时任务使用 setExact() 方法设置
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //[低电耗模式限制](https://developer.android.google.cn/training/monitoring-device-state/doze-standby.html#restrictions)
            //从 Android 6.0（API 级别 23）开始，Android 引入了两项省电功能
            //标准 AlarmManager 闹钟（包括 setExact() 和 setWindow()）推迟到下一个维护期。
            //如果您需要设置在设备处于低电耗模式时触发的闹钟，请使用 setAndAllowWhileIdle() 或 setExactAndAllowWhileIdle()。
            //使用 setAlarmClock() 设置的闹钟将继续正常触发，系统会在这些闹钟触发之前不久退出低电耗模式。
            setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, getReceiver(isLockScreen));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //[如果您的应用使用 AlarmManager](https://developer.android.google.cn/about/versions/android-4.4.html#BehaviorAlarms)
            //如果您的闹铃必须固定到一个精确的时钟时间（例如，日历事件提醒），那么您可以使用新的 setExact() 方法。
            //这个精确的批处理行为仅适用于更新后的应用。如果您已将 targetSdkVersion 设置为“18”或更低版本，那么在 Android 4.4 上运行时，您的闹铃的行为方式和在以前版本上一样。
            setExact(AlarmManager.RTC_WAKEUP, timeInMillis, getReceiver(isLockScreen))
        }
    }
}

private fun Context.getReceiver(isLockScreen: Boolean): PendingIntent {
    // for demo purposes no request code and no flags
//    return PendingIntent.getBroadcast(this, 0, NotificationReceiver.build(this, isLockScreen), 0)
    /**
    PendingIntent 主要可以通过以下三种方式获取:
        //获取一个用于启动 Activity 的 PendingIntent 对象
        public static PendingIntent getActivity(Context context, int requestCode, Intent intent, int flags);

        //获取一个用于启动 Service 的 PendingIntent 对象
        public static PendingIntent getService(Context context, int requestCode, Intent intent, int flags);

        //获取一个用于向 BroadcastReceiver 广播的 PendingIntent 对象
        public static PendingIntent getBroadcast(Context context, int requestCode, Intent intent, int flags)

    PendingIntent 具有以下几种 flag：
    1.FLAG_CANCEL_CURRENT: 如果当前系统中已经存在一个相同的 PendingIntent 对象，那么就将先将已有的 PendingIntent 取消，然后重新生成一个 PendingIntent 对象。
    2.FLAG_NO_CREATE: 如果当前系统中不存在相同的 PendingIntent 对象，系统将不会创建该 PendingIntent 对象而是直接返回 null 。
    3.FLAG_ONE_SHOT: 该 PendingIntent 只作用一次。
    4.FLAG_UPDATE_CURRENT : 如果系统中已存在该 PendingIntent 对象，那么系统将保留该 PendingIntent 对象，但是会使用新的 Intent 来更新之前 PendingIntent 中的 Intent 对象数据，例如更新 Intent 中的 Extras 。
     */
    return PendingIntent.getBroadcast(this, 0, NotificationReceiver.build(this, isLockScreen), PendingIntent.FLAG_UPDATE_CURRENT)
}

private const val SCHEDULE_TIME = 3L
