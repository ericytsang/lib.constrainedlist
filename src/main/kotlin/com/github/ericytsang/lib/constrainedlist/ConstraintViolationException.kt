package com.github.ericytsang.lib.constrainedlist

class ConstraintViolationException(val violatedConstraints:Set<Constraint<*>>)
:IllegalStateException("The operation violates the following constraints: ${violatedConstraints.map {"\n    - ${it.description}"}.joinToString(separator = "")}")
