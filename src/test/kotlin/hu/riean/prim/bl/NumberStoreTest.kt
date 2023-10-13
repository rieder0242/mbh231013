package hu.riean.prim.bl

import hu.riean.prim.data.Range
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.math.BigInteger

class NumberStoreTest {

    @Test
    fun getRange() {
        val numberStore = NumberStore()
        var range: Range = numberStore.getRange()
        assertEquals(BigInteger.ONE, range.min)
        assertEquals(numberStore.step, range.max)
        range = numberStore.getRange()
        assertEquals(numberStore.step.inc(), range.min)
        assertEquals(numberStore.step.multiply(BigInteger.TWO), range.max)
    }

    @Test
    fun givesBack() {
        val numberStore = NumberStore()
        val range1 = numberStore.getRange()
        assertEquals(0, numberStore.readyRanges.size)
        numberStore.givesBack(range1)
        assertArrayEquals(arrayOf(range1), numberStore.readyRanges.toTypedArray())
        val range2 = numberStore.getRange()
        assertArrayEquals(arrayOf(range1), numberStore.readyRanges.toTypedArray())
        numberStore.givesBack(range2)
        assertArrayEquals(arrayOf(range1, range2), numberStore.readyRanges.toTypedArray())
        val range3 = numberStore.getRange()
        val range4 = numberStore.getRange()
        numberStore.givesBack(range4)
        numberStore.givesBack(range3)
        assertArrayEquals(arrayOf(range1, range2, range3, range4), numberStore.readyRanges.toTypedArray())
    }

    @Test
    fun getContainingRange() {
        val count = 20
        val numberStore = NumberStore()
        for (i in 0..count) {
            val range = numberStore.getRange()
            numberStore.givesBack(range)
        }
        for ((index, range) in numberStore.readyRanges.withIndex()) {
            assertEquals(index, numberStore.getContainingRange(range.min, 0, count))
            assertEquals(index, numberStore.getContainingRange(range.max, 0, count))
            val med = range.max.add(range.min).divide(BigInteger.TWO)
            assertEquals(index, numberStore.getContainingRange(med, 0, count))
            assertEquals(index, numberStore.getContainingRange(med, index, count))
            assertEquals(index, numberStore.getContainingRange(med, 0, index))

        }
    }

    @Test
    fun getList() {
        val count = 20
        val numberStore = NumberStore()
        for (i in 0..count) {
            val range = numberStore.getRange()
            range.addPrim(range.min)
            range.addPrim(range.min.add(BigInteger.TWO))
            range.addPrim(range.max)

            numberStore.givesBack(range)
        }
        for (i in 0..count) {
            val range = numberStore.readyRanges[i]
            val list = ArrayList<BigInteger>();
            var ret = numberStore.getList(range.min, range.max, 5)
            assertArrayEquals(arrayOf(range.min, range.min.add(BigInteger.TWO), range.max), ret.list.toArray())
            assertEquals(false, ret.truncated)

            list.clear()
            ret = numberStore.getList(range.min, range.max, 2)
            assertArrayEquals(arrayOf(range.min, range.min.add(BigInteger.TWO)), ret.list.toArray())
            assertEquals(true, ret.truncated)

        }
        for (i in 1..count) {
            val rangeA = numberStore.readyRanges[i - 1]
            val rangeB = numberStore.readyRanges[i]
            val ret = numberStore.getList(rangeA.max, rangeB.min, 5)
            assertArrayEquals(arrayOf(rangeA.max, rangeB.min), ret.list.toArray())

        }

    }
}