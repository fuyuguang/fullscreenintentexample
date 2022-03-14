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

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
/**
 * Created by fuyuguang on 2022/3/10 4:52 下午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
 * 1，当单击 【3秒后模拟微信视频来电】按钮后，把应用切到后台，然后锁屏，，3秒后，该页面会直接在锁屏的上层界面显示，该页面关闭后，会显示锁屏页。
 * 2，在该页面，可以实现永不锁屏的效果（锁屏按钮无效）setShowWhenLocked(true)
 */
class VideoCallActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        turnScreenOnAndKeyguardOff()
        Log.e("log","VideoCallActivity onCreate")
        setContentView(TextView(this).also {
            it.text = "VideoCallActivity"
            it.layoutParams= ViewGroup.LayoutParams(300,300)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                it.setBackgroundColor(getColor(R.color.colorAccent))
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        turnScreenOffAndKeyguardOn()
    }
}
