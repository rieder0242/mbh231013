package hu.riean.prim

import hu.riean.prim.data.Settings
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import java.util.*
import kotlin.io.path.*

/**
 * Az alkalmazás konfigja spring értelemben is, és itt olvassuk fel a conf.properties filet is.
 */
@Configuration
@EnableAsync
class Config {
    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * kiolvassa a conf.properties tartalmát, kezel default értékeket
     */
    @Bean
    fun settings(): Settings {
        var maxResponseCount = 100
        var maxThreadCount = 10
        var path = Path("./conf.properties")

        if (path.isReadable()) {
            path = path.absolute()
            logger.info("Read config $path");
            val prop = Properties()
            prop.load(path.reader())
            maxResponseCount = readIntFromProp(prop, "maxResponseCount", maxResponseCount)
            maxThreadCount = readIntFromProp(prop, "maxThreadCount", maxThreadCount)
        }

        return Settings(maxResponseCount, maxThreadCount);
    }

    /**
     *  egy Properties-ből kiolvas egy értéket, ha az nincs, vagy nem szám, akkor defaultValue-val tér vissza
     */
    private fun readIntFromProp(prop: Properties, name: String, defaultValue: Int): Int {
        if (!prop.containsKey(name)) {
            return defaultValue;
        }
        return prop.getProperty(name).toIntOrNull() ?: defaultValue
    }
}