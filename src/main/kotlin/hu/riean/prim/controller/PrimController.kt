package hu.riean.prim.controller

import hu.riean.prim.data.Settings
import hu.riean.prim.dto.ActionFail
import hu.riean.prim.error.SearchingException
import hu.riean.prim.bl.NumberStore
import hu.riean.prim.dto.PrimListResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.math.BigInteger

/**
 * Primek listázásáért felelős kontroller
 */
@RestController
@RequestMapping("prim")
class PrimController {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var numberStore: NumberStore

    @Autowired
    lateinit var settings: Settings

    /**
     * Prímek listája
     *
     * megtalált prímszámokat kilistázó rest-es szolgáltatás
     *
     * ◦ két paramétert vár: minimum érték, maximum érték, a két érték között lévő összes megtalált prímszámot adja vissza. A lekérdezésben legyen védelem a túl sok találat kikérése ellen
     *
     * az én olvasatomban ez inkluzív
     *
     * ◦ amennyiben egy olyan intervallumot ad meg a felhasználó, ahol még nem futott a prímszámkeresés, akkor adjon vissza hibaüzenetet a lekérdezés
     *
     * ◦ a lekérdezésnek akkor is működnie kell, ha a prímszámkeresés éppen fut és akkor is, miután le lett állítva
     */
    @GetMapping("/list/{from}/{to}")
    @Throws(SearchingException::class)
    fun list(@PathVariable("from") from: BigInteger, @PathVariable("to") to: BigInteger): PrimListResponse {
        logger.info("call prim list")
        if (from.signum() != 1) {
            throw SearchingException("Not positive number $from. ");
        }
        if (to.signum() != 1) {
            throw SearchingException("Not positive number $to. ");
        }
        if (from.compareTo(to) == 1) {
            throw SearchingException("Negative interval [$from - $to].");
        }
        return numberStore.getList(from, to, settings.maxResponseCount);
    }

    @ExceptionHandler(SearchingException::class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun handleException(searchingException: SearchingException): ActionFail {
        return ActionFail(searchingException.message)
    }
}