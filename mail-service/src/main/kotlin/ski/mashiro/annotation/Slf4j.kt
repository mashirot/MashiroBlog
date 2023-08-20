package ski.mashiro.annotation

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author MashiroT
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Slf4j {
    companion object {
        val <reified T> T.log: Logger
            inline get() = LoggerFactory.getLogger(T::class.java)
    }
}