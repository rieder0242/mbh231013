package hu.riean.prim.bl

import hu.riean.prim.data.Range
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.math.BigInteger
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
class PrimTest {
    @Volatile
    var isRun: Boolean = true

    private val logger = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var numberStore: NumberStore

    /**
     * Prím-tesztelő folyamat, addig fut Range-eken, amíg disable()-t meg nem hívják.
     */
    @Async
    fun run(): Future<Void> {
        logger.info("PrimTest start")
        while (isRun) {
            val range: Range = numberStore.getRange()
            logger.debug("Start range $range")
            testRange(range);
            numberStore.givesBack(range)
        }
        logger.info("PrimTest stop")
        return CompletableFuture.completedFuture(null)
    }

    /**
     * Tiltja az új Range feldolgozását, ezzel leállítja a feldolgozást.
     * @see enable
     */
    fun disable() {
        isRun = false
    }

    /**
     * Engedélyezi az új Range feldolgozását, ezzel újra lehet indítani a feldolgozást.
     * @see disable
     */
    fun enable() {
        isRun = true
    }

    /**
     * Egy Range-t (prím)tesztel végig.
     */
    fun testRange(range: Range) {
        for (current in range) {
            //logger.info("test: $current!")
            if (testPrim(current)) {
                range.addPrim(current)
            }
        }
    }

    /**
     * Egy számot prímtesztel.
     *
     * Valószínűségi alapon dolgozik - lásd Miller Rabin.
     *
     * 1/(2^1000) az esélye a hibázásra. Ne fogadj ellene :)
     */
    fun testPrim(current: BigInteger): Boolean {
        return current.isProbablePrime(1000) // ki lehetne vezetni config-ba az értékét
    }
}