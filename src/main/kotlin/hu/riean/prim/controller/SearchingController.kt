package hu.riean.prim.controller

import hu.riean.prim.data.Settings
import hu.riean.prim.dto.ActionFail
import hu.riean.prim.dto.SearchingAction
import hu.riean.prim.error.SearchingException
import hu.riean.prim.bl.RunningState
import hu.riean.prim.bl.Searcher
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

/**
 * A kereső szolgáltatás leállításáért/újraindításáért felelős Controller
 */
@RestController
@RequestMapping("searching")
class SearchingController {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var searcher: Searcher

    @Autowired
    lateinit var settings: Settings

    /**
     * Prímszám keresés indítása szolgáltatás:
     *
     * ◦ amennyiben többszálú prímszámkeresést valósítunk meg, akkor paraméterben meg lehessen adni a szálak számát, a megadott értéket ilyenkor le kell vizsgálni, hogy a nem lépi túl a konfigurációban megadott maximum szálak számát (pl.: 5)
     *
     * az én olvasatomban nem lehet "ráindítani" a szolgáltatásra
     *
     * ◦ elindítja háttérben a prímszámkeresést és azonnal visszaválaszol, hogy elindult a folyamat
     *
     * ◦ amennyiben már fut a prímszámkeresés, hibaüzenettel azonnal válaszol, hogy már fut a folyamat
     */
    @PostMapping("/start/{threadCount}")
    @Throws(SearchingException::class)
    fun start(@PathVariable("threadCount") threadCount: Int): SearchingAction {
        logger.info("call searching start")
        if (threadCount > settings.maxThreadCount) {
            throw SearchingException("Too big value!");
        }
        if (threadCount <= 0) {
            throw SearchingException("Too small value!");
        }
        val prevState: RunningState = searcher.start(threadCount)
        return SearchingAction(true, prevState)

    }

    /**
     *  Prímszámkereső szolgáltatás leállítása
     *
     *  ◦ állítsa le a háttérben a prímszámkereséshez tartozó szálakat
     */
    @DeleteMapping("/stop")
    @Throws(SearchingException::class)
    fun stop(): SearchingAction {
        logger.info("call searching stop")
        val prevState: RunningState = searcher.stop()
        return SearchingAction(true, prevState)
    }

    @ExceptionHandler(SearchingException::class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun handleException(searchingException: SearchingException): ActionFail {
        return ActionFail(searchingException.message)
    }
}