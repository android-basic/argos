package redroid.log.argos

import android.os.Build
import android.util.Log
import timber.log.Timber
import java.util.regex.Pattern

/**
 * Timber Console Log Tree.
 * Inspired By: https://xiazdong.github.io/2017/05/17/Timber/
 *
 * @author RobinVanYang
 * @since 2021-05-30 23:40
 *
 * @property consoleLoggable
 * when you build your app in release mode, and you want to see log in logcat,
 * but you are lazy to enable LOGGABLE_TAG prop using adb shell,
 * then set this to true.
 */
open class ConsoleTree(
    private val consoleLoggable: Boolean = false,
    vararg ignoredClasses: Class<*>
) : Timber.DebugTree(*ignoredClasses) {
    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return consoleLoggable || BuildConfig.DEBUG || Log.isLoggable(LOGGABLE_TAG, Log.VERBOSE)
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (!isLoggable(tag, priority)) {
            return
        }

        var shadowTag = tag
        var shadowMessage = message

        val threadName = Thread.currentThread().name
        shadowMessage = "thread@$threadName: $shadowMessage"

        if (DEFAULT_TAG != shadowTag && Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            shadowMessage = "$shadowTag,$shadowMessage"
            shadowTag = DEFAULT_TAG
        }

        super.log(priority, shadowTag, shadowMessage, t)
    }

    override fun createStackElementTag(element: StackTraceElement): String? {
        var tag = getDefaultStackElementTag(element)
        val superTag = tag
        tag = if (null == superTag) {
            DEFAULT_TAG
        } else {
            if (element.fileName.contains(".")) {
                val begin = "("
                val suffix = element.fileName.substring(element.fileName.lastIndexOf("."))
                val end = ":" + element.lineNumber + ")"
                begin + superTag + suffix + end
            } else {
                superTag
            }
        }
        return tag
    }

    private fun getDefaultStackElementTag(element: StackTraceElement): String? {
        var tag = element.className
        val m = ANONYMOUS_CLASS.matcher(tag)
        if (m.find()) {
            tag = m.replaceAll("")
        }
        tag = tag.substring(tag.lastIndexOf('.') + 1)
        return tag
    }

    fun getCallStackIndex(): Int {
        return CALL_STACK_INDEX
    }

    companion object {
        private const val LOGGABLE_TAG = "AHS_LOG"
        private const val CALL_STACK_INDEX = 6
        private const val MAX_TAG_LENGTH = 23

        //static final String DEFAULT_TAG = "⇢";
        const val DEFAULT_TAG = ">"

        private val ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$")
    }
}