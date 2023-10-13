package hu.riean.prim.data

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.math.BigInteger

class RangeTest {

    @Test
    operator fun next() {
        val rang = Range(BigInteger.ONE, BigInteger.TWO)
        assertEquals(true, rang.hasNext())
        assertEquals(BigInteger.ONE, rang.current)
        assertEquals(BigInteger.ONE, rang.next())
        assertEquals(BigInteger.TWO, rang.next())
        assertEquals(false, rang.hasNext())
    }

    @Test
    fun isNextRang() {
        val rang1To2 = Range(BigInteger.ONE, BigInteger.TWO)
        val rang3To10 = Range(BigInteger.valueOf(3), BigInteger.TEN)
        val rang0To3 = Range(BigInteger.TWO, BigInteger.valueOf(3))

        assertEquals(false, rang1To2.isNextRang(rang1To2))
        assertEquals(false, rang1To2.isNextRang(rang1To2))
        assertEquals(true, rang1To2.isNextRang(rang3To10))
        assertEquals(true, rang1To2.isNextRang(rang3To10))
        assertEquals(false, rang1To2.isNextRang(rang0To3))
        assertEquals(false, rang1To2.isNextRang(rang0To3))

        assertEquals(false, rang3To10.isNextRang(rang1To2))
        assertEquals(false, rang3To10.isNextRang(rang1To2))
        assertEquals(false, rang3To10.isNextRang(rang3To10))
        assertEquals(false, rang3To10.isNextRang(rang3To10))
        assertEquals(false, rang3To10.isNextRang(rang0To3))
        assertEquals(false, rang3To10.isNextRang(rang0To3))

        assertEquals(false, rang0To3.isNextRang(rang1To2))
        assertEquals(false, rang0To3.isNextRang(rang1To2))
        assertEquals(false, rang0To3.isNextRang(rang3To10))
        assertEquals(false, rang0To3.isNextRang(rang3To10))
        assertEquals(false, rang0To3.isNextRang(rang0To3))
        assertEquals(false, rang0To3.isNextRang(rang0To3))
    }

    @Test
    fun iteratea() {
        val min: Long = 1
        val max: Long = 100000
        val range = Range(BigInteger.valueOf(min), BigInteger.valueOf(max))
        var count: Long = 1;
        for (bigInt in range) {
            assertEquals(bigInt, BigInteger.valueOf(count))
            count++;
        }
        assertEquals(max, count)
    }

    @Test
    fun getPrims() {
        val rang = Range(BigInteger.TWO, BigInteger.TEN)
        val list = ArrayList<BigInteger>();
        val bi25 = BigInteger.valueOf(25)
        val bi2 = BigInteger.TWO
        val bi3 = BigInteger.valueOf(3)
        val bi5 = BigInteger.valueOf(5)
        val bi7 = BigInteger.valueOf(7)
        list.add(bi25)
        assertEquals(25, rang.getPrims(list, BigInteger.TWO, BigInteger.TEN, 25))
        assertArrayEquals(arrayOf(bi25), list.toArray())

        rang.addPrim(bi2)
        rang.addPrim(bi3)
        rang.addPrim(bi5)
        rang.addPrim(bi7)

        assertEquals(21, rang.getPrims(list, BigInteger.TWO, BigInteger.TEN, 25))
        assertArrayEquals(arrayOf(bi25, bi2, bi3, bi5, bi7), list.toArray())

        list.clear()
        assertEquals(-1, rang.getPrims(list, BigInteger.TWO, BigInteger.TEN, 2))
        assertArrayEquals(arrayOf(bi2, bi3), list.toArray())

    }

    @Test
    fun compareToBigint() {
        val rang = Range(BigInteger.TWO, BigInteger.TEN)
        assertEquals(-1, rang.compareToBigint(BigInteger.valueOf(1)))
        assertEquals(0, rang.compareToBigint(BigInteger.valueOf(2)))
        assertEquals(0, rang.compareToBigint(BigInteger.valueOf(3)))
        assertEquals(0, rang.compareToBigint(BigInteger.valueOf(9)))
        assertEquals(0, rang.compareToBigint(BigInteger.valueOf(10)))
        assertEquals(1, rang.compareToBigint(BigInteger.valueOf(11)))
        assertEquals(1, rang.compareToBigint(BigInteger.valueOf(1987981)))

    }
}