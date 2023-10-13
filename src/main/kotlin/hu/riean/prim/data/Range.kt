package hu.riean.prim.data

import hu.riean.prim.dto.PrimListResponse
import java.math.BigInteger

/**
 * Számok tartományát reprezentálja, ide lehet gyüjteni a megtalált prímeket.
 */
class Range(var min: BigInteger, var max: BigInteger) : Iterator<BigInteger> {
    var list: List<BigInteger> = ArrayList()
    var current: BigInteger = min
    var next: Range? = null

    /**
     * @see Iterator.hasNext
     *
     * Végig lehet iterálni vele a tartomány (nem csak prím) értékein
     */
    override fun hasNext(): Boolean {
        return current.compareTo(max) == -1
    }

    /**
     * @see Iterator.next
     *
     * Végig lehet iterálni vele a tartomány (nem csak prím) értékein
     */
    override fun next(): BigInteger {
        val old = current
        current = current.inc();
        return old
    }

    /**
     * a megtalált prímszámot adjuk oda
     */
    fun addPrim(current: BigInteger) {
        list += current;
    }

    fun compareToBigint(number: BigInteger): Int {
        if (min.compareTo(number) == 1) {
            return -1
        } else if (number.compareTo(max) == 1) {
            return 1
        } else {
            return 0
        }
    }

    override fun toString(): String {
        return "Range [$min - $max]"
    }

    /**
     * Ellenőrzi, hogy két Range folytatása-e egymásnak
     *
     * Ha megtalálta a következőt, el cache-eli.
     */
    fun isNextRang(other: Range): Boolean {
        if (next == null) {
            if (max.inc().compareTo(other.min) == 0) {
                next = other;
                return true;
            }
            return false;
        } else {
            return next == other;
        }
    }

    /**
     * A Range-bent tárolt primeket berakja toAdd-be from és to között maximum count darabot
     *
     */
    fun getPrims(toAdd: ArrayList<BigInteger>, from: BigInteger, to: BigInteger, count: Int): Int {
        var remain = count
        var inrange = false
        for (prim in list) {
            if (!inrange) {
                inrange = from.compareTo(prim) != 1
            }
            if (inrange) {

                if (prim.compareTo(to) == 1) {
                    break;
                }
                toAdd.add(prim);
                remain--
                if (remain <= 0) {
                    return -1
                }

            }
        }
        return remain;
    }
}