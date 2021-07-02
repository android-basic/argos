package redroid.log.argos

import android.util.Log
import com.dianping.logan.Logan

/**
 * Release mode log tree.
 * Inspired By: https://xiazdong.github.io/2017/05/17/Timber/
 *
 * @author RobinVanYang
 * @since 2021-05-31 01:25
 */
class ReleaseTree(consoleLoggable: Boolean = false, vararg ignoredClasses: Class<*>) :
    ConsoleTree(consoleLoggable, *ignoredClasses) {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, tag, message, t)

        if (isLoggableToFile(priority)) {
            write(tag, message, priority)
        }
    }

    /**
     * 级别大于DEBUG级的日志，都需保存到日志文件.
     *
     * @param priority 日志级别.
     */
    private fun isLoggableToFile(priority: Int): Boolean {
        return priority > Log.DEBUG
    }

    /**
     * write to mmap or java heap.
     */
    private fun write(tag: String?, message: String, priority: Int) {
        if (DEFAULT_TAG != tag) {
            Logan.w("$tag:$message", priority)
        } else {
            Logan.w(message, priority)
        }
    }
}