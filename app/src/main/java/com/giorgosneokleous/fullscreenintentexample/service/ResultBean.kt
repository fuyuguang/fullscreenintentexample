package com.giorgosneokleous.fullscreenintentexample.service

import java.io.Serializable

/**
 * Created by fuyuguang on 2022/3/11 4:03 下午.
 * E-Mail ：2355245065@qq.com
 * Wechat :fyg13522647431
 * Tel : 13522647431
 * 修改时间：
 * 类描述：
 * 备注：
    []()
    []()
    state 0 error
    state 1 success

 */
class ResultBean(var state: Int = 0) :
    Serializable {

    companion object{
        const val DOWNLOAD_STATE = "download_state"
        const val DOWNLOAD_STATE_ERROR = 0
        const val DOWNLOAD_STATE_SUCCESS =1
    }


}