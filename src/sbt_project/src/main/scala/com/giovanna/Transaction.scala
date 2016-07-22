package com.giovanna

import org.json4s._
import org.json4s.DefaultFormats
import org.json4s.native.JsonMethods._

/** ----------------------------------------------------------------------------- */
/** Class Transaction - An instance corresponds to an individual transaction line */
/** ----------------------------------------------------------------------------- */
class Transaction(val line: String, lineNum: Int) {

  var created_time: String = ""
  var target: String = ""
  var actor: String = ""
  private var valid: Boolean = false

  def isValid(): Boolean = {
    return valid
  }

  def parseLine(): Boolean = {

    /** Validate line is in correct format and all required values are present */
    var validJSON = true
    var validData = true
    implicit val formats = DefaultFormats // for JSON parsing

    //transactionObject = parse(line).extract[transaction]

    var jsonInputObj = parse("")

    try {
      jsonInputObj = parse(line)
    }
    catch {
      case _: Throwable => validJSON = false
    }

    if (!validJSON) {
      println("Line #" + lineNum + " - Is not a valid JSON format")
      validData = false

    } else {

      if (((jsonInputObj \ "actor") == JNothing)) {
        println("Line #" + lineNum + " - Actor was not found")
        validData = false
      }
      else {
        this.actor = (jsonInputObj \ "actor").extract[String]
      }

      if (((jsonInputObj \ "target") == JNothing)) {
        println("Line #" + lineNum + " - Target was not found")
        validData = false
      }
      else {
        this.target = (jsonInputObj \ "target").extract[String]
      }

      if (((jsonInputObj \ "created_time") == JNothing)) {
        println("Line #" + lineNum + " - Created time was not found")
        validData = false
      }
      else {
        this.created_time = (jsonInputObj \ "created_time").extract[String]
      }
    }

    valid = validData
    return validData

  }

  // Parse Input when instantiating the class
  parseLine()

}
