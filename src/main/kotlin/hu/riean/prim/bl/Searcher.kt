package hu.riean.prim.bl

import hu.riean.prim.error.SearchingException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import java.util.concurrent.Future

/**
 * A kereső szálak menedzselését végzi
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
class Searcher {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val runnings = ArrayList<Future<Void>>()

    /**
     * a futási állapota
     * @see RunningState
     *
     */
    var runningState: RunningState = RunningState.STOPPED;

    @Autowired
    lateinit var primTest: PrimTest

    /**
     * elindítja a feldolgozást
     */
    @Throws(SearchingException::class)
    fun start(threadCount: Int): RunningState {
        synchronized(this) {
            if (runningState != RunningState.STOPPED) {
                throw SearchingException("Service state is not \"STOPPED\".")
            }
            runningState = RunningState.STARTING;
        }
        primTest.enable()
        for (i in 1..threadCount) {
            logger.debug("Startin async#{$i}")
            val run = primTest.run()
            runnings.add(run)
        }
        synchronized(this) {
            runningState = RunningState.RUN;
        }
        return RunningState.RUN;
    }

    /**
     * leállítja a feldolgozást, és megvárja az utolsó Range-ek feldolgozását
     */
    @Throws(SearchingException::class)
    fun stop(): RunningState {
        synchronized(this) {
            if (runningState == RunningState.STOPPED) {
                return RunningState.STOPPED;
            }
            if (runningState != RunningState.RUN) {
                throw SearchingException("Service is in swapping state.")
            }
            runningState = RunningState.SHUTDOWN;
        }
        primTest.disable()
        for (run in runnings) {
            run.cancel(false);
        }
        runnings.clear()
        synchronized(this) {
            runningState = RunningState.STOPPED;
        }
        return RunningState.STOPPED;
    }
}