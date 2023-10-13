package hu.riean.prim.bl

import hu.riean.prim.data.Range
import hu.riean.prim.dto.PrimListResponse
import hu.riean.prim.error.SearchingException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import java.math.BigInteger
import kotlin.math.min

/**
 * Itt tároljuk a prímszámokat.
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
class NumberStore {
    private val logger = LoggerFactory.getLogger(javaClass)
    var readyRanges: ArrayList<Range> = ArrayList()
    val step: BigInteger = BigInteger.valueOf(100000)
    var ceiling: BigInteger = BigInteger.ZERO

    /**
     * Ad egy új még nem használt Range-et, hogy lehessen prímeket keresni rajta
     */
    fun getRange(): Range {
        synchronized(this) {
            val min = ceiling.inc()
            ceiling = ceiling.add(step)
            return Range(min, ceiling)
        }
    }

    /**
     * Befogadja a már végigkeresett Range-et
     */
    fun givesBack(range: Range) {
        synchronized(this) {

            readyRanges.add(range)
            for (i in readyRanges.size - 1 downTo 1) { //  buborékrendezem az új elemet, általában jó helyen van, nem fog vissza performanciában
                if (readyRanges[i - 1].min.compareTo(readyRanges[i].min) == -1) {
                    return
                } else {
                    readyRanges[i] = readyRanges[i - 1]
                    readyRanges[i - 1] = range
                }
            }
        }
    }

    /**
     * Visszaadja annak a Range-nek az indexét, ami tartalmazza a searchedNumber-t, ha az beleesik a [low, high] tartományba
     */
    @Throws(SearchingException::class)
    fun getContainingRange(searchedNumber: BigInteger, low: Int, high: Int): Int {
        var myLow = low
        var myHigh = high
        var first = true;
        while (myLow <= myHigh) {
            val mid = if (first) {
                min(searchedNumber.dec().divide(step).toInt(), myHigh)
            } else { // csak akkor kerülhetünk ide, ha nem sorrendben végeztek a prím tesztelő szálak
                (myLow + myHigh) / 2  // innentől binary search
            }

            val midRange = readyRanges[mid]
            when (midRange.compareToBigint(searchedNumber)) {
                -1 -> myHigh = mid - 1
                1 -> myLow = mid + 1
                else -> return mid
            }
            first = false;
        }
        throw SearchingException("Out of processed range");
    }

    /**
     * Visszaadja az adott tartományba [from, to] eső prímeket, maximum count darabot
     */
    @Throws(SearchingException::class)
    fun getList(from: BigInteger, to: BigInteger, count: Int): PrimListResponse {
        val min: Int = getContainingRange(from, 0, readyRanges.size - 1);
        val max: Int = getContainingRange(to, min, readyRanges.size - 1);

        var remaining = count;
        val resp = PrimListResponse()
        var prevRange: Range? = null;
        for (i in min..max) {
            var range = readyRanges.get(i)
            if (prevRange != null) {
                if (!prevRange.isNextRang(range)) {
                    throw SearchingException("Out of processed range");
                }
            }
            remaining = range.getPrims(resp.list, from, to, remaining)
            if (remaining == -1) {
                resp.truncated = true;
                break
            }
        }
        return resp;
    }
}