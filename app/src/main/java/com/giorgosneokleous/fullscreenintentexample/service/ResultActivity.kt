package com.giorgosneokleous.fullscreenintentexample.service

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.giorgosneokleous.fullscreenintentexample.R
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        setupView()
        restoreArguments()
    }

    private fun setupView() {}

    private fun restoreArguments() {
        val bundle = intent.extras
        if (bundle != null) {
            val resultBean: ResultBean? = bundle.getSerializable(ResultBean.DOWNLOAD_STATE) as ResultBean?
            setDownloadApkState(resultBean)
        }
    }

    private fun setDownloadApkState(resultBean: ResultBean?) {
        resultBean?.apply {
            downloadApkState?.text = when(resultBean.state){
                0 -> "失败"
                1 -> "成功"
                2 -> "异常"
                else -> {""}
            }
        }
    }


}