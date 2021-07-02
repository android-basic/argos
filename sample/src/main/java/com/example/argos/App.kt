package com.example.argos

import android.app.Application
import redroid.log.argos.Argos
import redroid.log.argos.ArgosConfig
import java.io.File

/**
 * @author RobinVanYang
 * @since 2021-06-06 14:49
 */
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        initArgos()
    }

    /**
     * 初始化日志系统配置
     */
    private fun initArgos() {
        val argosConfigBuilder = ArgosConfig.Builder(false).let {
            it.setCachePath(cacheDir.absolutePath)
            it.setPath(filesDir.absolutePath + File.separator + "logan_v1")
            it.setEncryptKey16("party123456789".toByteArray())
            it.setEncryptIV16("party123456789".toByteArray())
            it.setLogFileRetainDay(7)
            it.setMaxFileSize(50)
            it.setMinSDCard(50)
        }
        val argosConfig = argosConfigBuilder.build()
        Argos.init(argosConfig)
    }
}