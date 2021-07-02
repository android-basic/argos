package redroid.log.argos

import com.dianping.logan.LoganConfig


/**
 * 日志库配置
 *
 * @author RobinVanYang
 * @since 2021-05-30 16:58
 */
class ArgosConfig private constructor(
    var loganConfig: LoganConfig,
    var onlyPrintToConsole: Boolean = true
) {
    class Builder(val onlyPrintToConsole: Boolean) {
        private var mCachePath: String? = null //mmap缓存路径
        private var mPath: String? = null //file文件路径
        private var mMaxFileSize: Long = DEFAULT_FILE_SIZE //删除文件最大值
        private var mRetainDay: Long = DEFAULT_DAY //删除天数

        private lateinit var mEncryptKey16: ByteArray //128位ase加密Key
        private lateinit var mEncryptIv16: ByteArray //128位aes加密IV

        private var mMinSDCard: Long = DEFAULT_MIN_SDCARD_SIZE

        fun setCachePath(cachePath: String?): Builder {
            mCachePath = cachePath
            return this
        }

        fun setPath(path: String?): Builder {
            mPath = path
            return this
        }

        fun setMaxFileSize(maxFile: Long): Builder {
            mMaxFileSize = maxFile * M
            return this
        }

        fun setLogFileRetainDay(day: Long): Builder {
            mRetainDay = day * DAYS
            return this
        }

        fun setEncryptKey16(encryptKey16: ByteArray): Builder {
            mEncryptKey16 = encryptKey16
            return this
        }

        fun setEncryptIV16(encryptIv16: ByteArray): Builder {
            mEncryptIv16 = encryptIv16
            return this
        }

        fun setMinSDCard(minSDCard: Long): Builder {
            mMinSDCard = minSDCard
            return this
        }

        fun build(): ArgosConfig {
            val loganConfig = LoganConfig.Builder()
                .setCachePath(mCachePath)
                .setPath(mPath)
                .setMaxFile(mMaxFileSize)
                .setMinSDCard(mMinSDCard)
                .setDay(mRetainDay)
                .setEncryptKey16(mEncryptKey16)
                .setEncryptIV16(mEncryptIv16)
                .build()
            return ArgosConfig(loganConfig, onlyPrintToConsole)
        }
    }

    companion object {
        private const val DAYS = (24 * 60 * 60 * 1000).toLong() //天
        private const val M = (1024 * 1024).toLong() //M
        private const val DEFAULT_DAY = 7 * DAYS //默认删除天数

        private const val DEFAULT_FILE_SIZE = 10 * M
        private const val DEFAULT_MIN_SDCARD_SIZE = 50 * M //最小的SD卡小于这个大小不写入

        private const val DEFAULT_QUEUE = 500
    }
}