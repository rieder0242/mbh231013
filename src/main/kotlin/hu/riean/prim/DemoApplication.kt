package hu.riean.prim

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DemoApplication

/**
 * Ez indítja az alkalmazást
 */
fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}
