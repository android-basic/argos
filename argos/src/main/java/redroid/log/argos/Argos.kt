package redroid.log.argos

import android.annotation.SuppressLint
import android.util.Log
import com.dianping.logan.Logan
import org.jetbrains.annotations.NonNls
import timber.log.Timber


/**
 * 日志封装
 * 包括终端输出(使用Timber)及日志上报(美团Logan)
 *
 * @author RobinVanYang
 * @since 2021-05-17 19:56
 */
object Argos {
    private const val TAG = "Argos"
    private lateinit var argosConfig: ArgosConfig

    fun init(config: ArgosConfig) {
        argosConfig = config

        initLogan()
        initTimber()
    }

    @SuppressLint("LogNotTimber")
    private fun initLogan() {
        Logan.init(argosConfig.loganConfig)
        // TODO: 2021/5/30
        Logan.setDebug(false)
        Logan.setOnLoganProtocolStatus { cmd, code ->
            Log.d(TAG, "clogan > cmd : $cmd | code: $code")
        }

        //this library normally init at Application begin, so flush the nmap data to file immediately.
        flushLogToFile()
    }

    private fun flushLogToFile() {
        Logan.f()
    }

    private fun initTimber() {
        //if remove all trees in the forest.
        if (Timber.forest().isNotEmpty()) {
            Timber.uprootAll()
        }

        Timber.plant(
            if (argosConfig.onlyPrintToConsole) ConsoleTree(
                argosConfig.onlyPrintToConsole,
                Argos::class.java
            )
            else ReleaseTree(false, Argos::class.java)
        )
    }

    /**
     * Log a verbose message with optional format args.
     */
    fun v(@NonNls message: String?, vararg args: Any?) {
        checkForestTree()
        Timber.v(message, args)
    }

    /**
     * Log a verbose exception and a message with optional format args.
     */
    fun v(t: Throwable?, @NonNls message: String? = null, vararg args: Any?) {
        checkForestTree()
        Timber.v(t, message, args)
    }

    /**
     * Log a verbose exception.
     */
    fun v(t: Throwable?) {
        checkForestTree()
        Timber.v(t)
    }

    /**
     * Log a debug message with optional format args.
     */
    fun d(@NonNls message: String?, vararg args: Any?) {
        checkForestTree()
        Timber.d(message, args)
    }

    /**
     * Log a debug exception and a message with optional format args.
     */
    fun d(t: Throwable?, @NonNls message: String?, vararg args: Any?) {
        checkForestTree()
        Timber.d(t, message, args)
    }

    /**
     * Log a debug exception.
     */
    fun d(t: Throwable?) {
        checkForestTree()
        Timber.d(t)
    }

    /**
     * Log an info message with optional format args.
     */
    fun i(@NonNls message: String?, vararg args: Any?) {
        checkForestTree()
        Timber.i(message, args)
    }

    /**
     * Log an info exception and a message with optional format args.
     */
    fun i(t: Throwable?, @NonNls message: String?, vararg args: Any?) {
        checkForestTree()
        Timber.i(t, message, args)
    }

    /**
     * Log an info exception.
     */
    fun i(t: Throwable?) {
        checkForestTree()
        Timber.i(t)
    }

    /**
     * Log a warning message with optional format args.
     */
    fun w(@NonNls message: String?, vararg args: Any?) {
        checkForestTree()
        Timber.w(message, args)
    }

    /**
     * Log a warning exception and a message with optional format args.
     */
    fun w(t: Throwable?, @NonNls message: String?, vararg args: Any?) {
        checkForestTree()
        Timber.w(t, message, args)
    }

    /**
     * Log a warning exception.
     */
    fun w(t: Throwable?) {
        checkForestTree()
        Timber.w(t)
    }

    /**
     * Log an error message with optional format args.
     */
    fun e(@NonNls message: String?, vararg args: Any?) {
        checkForestTree()
        Timber.e(message, args)
    }

    /**
     * Log an error exception and a message with optional format args.
     */
    fun e(t: Throwable?, @NonNls message: String?, vararg args: Any?) {
        checkForestTree()
        Timber.e(t, message, args)
    }

    /**
     * Log an error exception.
     */
    fun e(t: Throwable?) {
        checkForestTree()
        Timber.e(t)
    }

    fun tag(tag: String): Timber.Tree {
        checkForestTree()
        return Timber.tag(tag)
    }

    @SuppressLint("LogNotTimber")
    private fun checkForestTree() {
        if (Timber.forest().isEmpty()) {
            Log.d(TAG, "plantDefaultTree")
            Timber.plant(ConsoleTree(argosConfig.onlyPrintToConsole, Argos::class.java))
        }
    }

    /**
     * 上传日志到服务器
     */
    // TODO: 2021/5/31 上传日志到服务器
    @SuppressLint("LogNotTimber")
    fun uploadLogToServer(logDate: String) {
        val url = "https://openlogan.inf.test.sankuai.com/logan/upload.json"
        Logan.s(
            url,
            logDate,
            "testAppId",
            "testUnionId",
            "testDeviceId",
            "testBuildVersion",
            "testAppVersion"
        ) { statusCode, data ->
            val resultData = data?.let { String(it) } ?: ""
            Log.d(TAG, "upload result, httpCode: $statusCode, details: $resultData")
        }
    }
}