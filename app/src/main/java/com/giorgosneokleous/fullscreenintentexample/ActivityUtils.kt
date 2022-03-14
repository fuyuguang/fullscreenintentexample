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

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import android.view.WindowManager

fun Activity.turnScreenOnAndKeyguardOff() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
        /**
         * 退到后台后锁屏，在锁定屏幕顶部可以显示该activity
         */
        setShowWhenLocked(true)
        //亮屏
        setTurnScreenOn(true)
    } else {
        window.addFlags(
            /**
             * 窗口标志：只要该窗口对用户可见，就保持设备的屏幕打开且明亮。
             */
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    /**
                     * 窗口标志：只要该窗口对用户可见，就允许在屏幕打开时激活锁定屏幕。这可以单独使用，
                    也可以与 FLAG_KEEP_SCREEN_ON 和/或 FLAG_SHOW_WHEN_LOCKED 结合使用
                     */
                    WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
        )
    }

    with(getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /**
            如果设备当前被锁定（参见 isKeyguardLocked()，请求解除 Keyguard。
            如果 Keyguard 不安全或设备当前处于受信任状态，调用此方法将立即关闭 Keyguard，无需任何用户交互。
            如果 Keyguard 是安全的并且设备不处于受信任状态，这将打开 UI，以便用户可以输入他们的凭据。
            如果为 Activity attr android.R.attr.turnScreenOn 设置的值为 true，则在解除键盘保护时屏幕将打开。
             */
            requestDismissKeyguard(this@turnScreenOnAndKeyguardOff, null)
        }
    }
}

fun Activity.turnScreenOffAndKeyguardOn() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
        setShowWhenLocked(false)
        setTurnScreenOn(false)
    } else {
        window.clearFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
        )
    }
}
