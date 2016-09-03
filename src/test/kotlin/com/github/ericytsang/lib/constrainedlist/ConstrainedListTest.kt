package com.github.ericytsang.lib.constrainedlist

import org.junit.Test
import java.util.ArrayList

/**
 * Created by surpl on 8/15/2016.
 */
class ConstrainedListTest
{
    val testList = ConstrainedList<Int>(ArrayList()).apply()
    {
        constraints += Constraint.new<List<Int>>().apply()
        {
            description = "all elements are positive"
            isConsistent = Constraint.Predicate.new()
            {
                it.newValue.all {it >= 0}
            }
        }
    }

    @Test
    fun doTransaction1()
    {
        testList.doTransaction()
        {
            testList.add(0)
            testList.add(1)
            testList.add(2)
            testList.add(3)
        }
    }

    @Test
    fun doTransaction2()
    {
        try
        {
            testList.doTransaction()
            {
                testList.add(-1)
            }
        }
        catch (ex:ConstraintViolationException)
        {
            ex.printStackTrace()
            return
        }
        assert(false)
    }

    @Test
    fun size()
    {
        assert(testList.size == 0)
        testList.add(0)
        assert(testList.size == 1)
        testList[0] = 7
        assert(testList.size == 1)
        testList.add(2)
        assert(testList.size == 2)
        testList.removeAt(1)
        assert(testList.size == 1)
    }

    @Test
    fun get()
    {
        testList.add(2)
        testList.add(1)
        testList.add(3)
        testList.add(0)
        assert(testList[0] == 2)
        assert(testList[1] == 1)
        assert(testList[2] == 3)
        assert(testList[3] == 0)
    }

    @Test
    fun add()
    {
        testList.add(2)
        testList.add(1)
        testList.add(3)
        testList.add(0)
        assert(testList[0] == 2)
        assert(testList[1] == 1)
        assert(testList[2] == 3)
        assert(testList[3] == 0)
        assert(testList.size == 4)
    }

    @Test
    fun addAll1()
    {
        testList.addAll(listOf(2,5,7,4))
        assert(testList[0] == 2)
        assert(testList[1] == 5)
        assert(testList[2] == 7)
        assert(testList[3] == 4)
        assert(testList.size == 4)
    }

    @Test
    fun addAll2()
    {
        try
        {
            testList.addAll(listOf(2,5,7,-4))
        }
        catch (ex:ConstraintViolationException)
        {
            ex.printStackTrace()
            return
        }
        assert(false)
    }

    @Test
    fun set()
    {
        testList.add(0)
        testList.add(0)
        testList.add(0)
        testList.add(0)

        assert(testList[0] == 0)
        testList[0] = 2
        assert(testList[0] == 2)

        assert(testList[1] == 0)
        testList[1] = 5
        assert(testList[1] == 5)

        assert(testList[2] == 0)
        testList[2] = 7
        assert(testList[2] == 7)

        assert(testList[3] == 0)
        testList[3] = 4
        assert(testList[3] == 4)
    }

    @Test
    fun removeAt()
    {
        testList.add(2)
        testList.add(1)
        testList.add(3)
        testList.add(0)
        testList.removeAt(2)
        assert(testList[0] == 2)
        assert(testList[1] == 1)
        assert(testList[2] == 0)
        assert(testList.size == 3)
    }

}