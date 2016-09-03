package com.github.ericytsang.lib.constrainedlist

/**
 * Created by surpl on 8/15/2016.
 */
interface Constraint<in Constrained>
{
    companion object
    {
        fun <Constrained> new():SimpleConstraint<Constrained>
        {
            return SimpleConstraint()
        }
    }

    /**
     * returns true if [Constrained] is consistent and does not violate this
     * [Constraint]; returns false if [Constrained] violates this [Constraint].
     */
    val isConsistent:Predicate<Change<Constrained>>

    /**
     * string describing what this [Constraint] reinforces in [Constrained].
     */
    val description:String

    class Change<out Constrained>(val oldValue:Constrained,val newValue:Constrained)

    interface Predicate<in Subject>
    {
        companion object
        {
            fun <Subject> new(tester:(Subject)->Boolean):Predicate<Subject>
            {
                return object:Predicate<Subject>
                {
                    override fun test(subject:Subject):Boolean = tester(subject)
                }
            }
        }

        fun test(subject:Subject):Boolean
    }

    class SimpleConstraint<Constrained> internal constructor():Constraint<Constrained>
    {
        override var isConsistent = Predicate.new<Change<Constrained>>()
        {
            return@new true
        }
        override var description:String = ""
    }
}
