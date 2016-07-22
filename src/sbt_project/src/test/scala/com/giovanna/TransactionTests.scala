package com.giovanna

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class TransactionTests extends FunSuite with BeforeAndAfter {

  var transaction: Transaction = _

  before {
  }

  test("Empty Transaction line is not valid") {
    transaction = new Transaction("", 1)
    assert(transaction.isValid() == false)
  }

  test("Transaction without actor is not valid") {
    transaction = new Transaction("{" + "created_time" + ": " + "2016-04-07T03:33:19Z" + ", " + "target" + ": " +  "Jamie-Korn" + "}", 1)
    assert(transaction.isValid() == false)
  }

  test("Transaction without target is not valid") {
    transaction = new Transaction("{" + "created_time" + ": " + "2016-04-07T03:33:19Z" + ", " + "actor" + ": " +  "Jamie-Korn" + "}", 1)
    assert(transaction.isValid() == false)
  }

  test("Transaction without created_time is not valid") {
    transaction = new Transaction("{" + "target" + ": " +  "Jamie-Korn" + "}", 1)
    assert(transaction.isValid() == false)
  }

}
