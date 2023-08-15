package ski.mashiro

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * @author MashiroT
 */
@SpringBootApplication
class MailServiceApplication

fun main(args: Array<String>) {
    runApplication<MailServiceApplication>(*args)
}
