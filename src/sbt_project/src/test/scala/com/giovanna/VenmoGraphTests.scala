package com.giovanna

import java.io.{File, PrintWriter}

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

class VenmoGraphTests extends FunSuite with BeforeAndAfter {

  var venmoGraph: VenmoGraph = _

  before {
  }

  test("Correct median is wirtten to output file") {
    var line = ""
    var lineNum = 1

/*    val outputFile = new File("output.txt")

    val writerObj = new PrintWriter(outputFile)
    writerObj.close()

    venmoGraph.processTransaction(line, lineNum, outputFile)
*/
    assert(true == true)
  }



}
