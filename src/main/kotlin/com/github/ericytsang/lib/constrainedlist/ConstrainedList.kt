package com.github.ericytsang.lib.constrainedlist

import java.util.AbstractList
import java.util.ArrayList
import java.util.LinkedHashSet
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Created by surpl on 8/15/2016.
 */
class ConstrainedList<E>(val wrapee:MutableList<E>):MutableList<E>,AbstractList<E>()
{
    val constraints = LinkedHashSet<Constraint<List<E>>>()

    private val mutationLock = ReentrantLock()

    fun <R> doTransaction(mutate:()->R) = mutationLock.withLock()
    {
        // save the state of the list in case we need to roll back
        val originalList = ArrayList(wrapee)

        // perform the mutation
        try
        {
            return@withLock mutate()
        }
        finally
        {
            // check which constraints have been violated
            val violatedConstraints = constraints
                .filter {!it.isConsistent.test(Constraint.Change(originalList,wrapee))}
                .toSet()

            // roll back and throw exception if constraints are violated
            if (violatedConstraints.isNotEmpty())
            {
                wrapee.clear()
                wrapee.addAll(originalList)
                throw ConstraintViolationException(violatedConstraints)
            }
        }
    }

    override val size:Int get() = wrapee.size

    override fun get(index:Int):E = wrapee[index]

    override fun add(index:Int,element:E)
    {
        if (mutationLock.isHeldByCurrentThread)
        {
            wrapee.add(index,element)
        }
        else doTransaction()
        {
            wrapee.add(index,element)
        }
    }

    override fun set(index:Int,element:E):E
    {
        return if (mutationLock.isHeldByCurrentThread)
        {
            wrapee.set(index,element)
        }
        else doTransaction()
        {
            wrapee.set(index,element)
        }
    }

    override fun removeAt(index:Int):E
    {
        return if (mutationLock.isHeldByCurrentThread)
        {
            wrapee.removeAt(index)
        }
        else doTransaction()
        {
            wrapee.removeAt(index)
        }
    }
}
