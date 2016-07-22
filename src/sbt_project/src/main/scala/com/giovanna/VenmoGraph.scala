package com.giovanna

import java.io.{File, FileOutputStream, PrintWriter}
import com.github.nscala_time.time.Imports._
import scala.collection.mutable.ArrayBuffer


/** ----------------------------------------------------------------------------- */
/** Class VenmoGraph - Holds the collection of current transactions               */
/** ----------------------------------------------------------------------------- */
class VenmoGraph() {

  var transactionsArr = ArrayBuffer[Transaction]()  /** empty array that holds all transaction bbjects */
  var maxTimeStamp:Option[DateTime] = None  /** holds the maximum payment transaction timestamp */


  def calcMedian(arr: ArrayBuffer[Int]): Double = {

    var middle = arr.length/2
    var result: Double = 0
    if (arr.length % 2 != 0) {  /** Array length is odd */

      result = arr(Math.floor(middle).toInt)
      result = BigDecimal(result).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
    }
    else { /**  middle is even */
      result = (arr(middle - 1).toDouble + arr(middle).toDouble) / 2
      //println("Inside median method - arr(middle - 1):" + arr(middle - 1) + ", arr(middle):" + arr(middle) + ", result:" + result)
      result = BigDecimal(result).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
    }
    return result
  }


  def processTransaction(line: String, lineNum: Int, outputFile: File): Unit = {

    var transactionObj = new Transaction(line, lineNum)

    if (transactionObj.isValid ) {

      /** Convert transaction created_time (String) to nscala-time DateTime */
      var transTime = DateTime.parse(transactionObj.created_time)

      var newMaxTimeStamp = maxTimeStamp

      /** Determine max timestamp */
      if (newMaxTimeStamp.isEmpty) {
        newMaxTimeStamp = Some(transTime)
      } else {

        if (transTime > newMaxTimeStamp.get) {
          newMaxTimeStamp = Some(transTime)
        }
      }

      //println("----------------------------------------------------------------")
      //println("Process Transaction: a.target:" + o.target + ", a.actor:" + o.actor + ", o.created_time:" + o.created_time + ", transTime:" + transTime + ", newMaxTimeStamp:" + newMaxTimeStamp)
      //println("newMaxTimeStamp.get:" + newMaxTimeStamp.get + ", newMaxTimeStamp.get - 60.seconds:" + (newMaxTimeStamp.get - 60.seconds).toString  )

      /** Add transaction object to array of transactions  */
      transactionsArr += transactionObj

      /** create an empty map - adjacency list - to keep track of connections for each vertex (actor or target) */
      //var vertexMap = scala.collection.mutable.Map[String, Int]()
      var vertexMap = scala.collection.mutable.Map[String, ArrayBuffer[String]]()

      /** Loop through last transactions, only consider the ones that are within the 60-seconds window */
      var transTempArr = ArrayBuffer[Transaction]()
      for (a <- transactionsArr) {

        var lineTransTime = DateTime.parse(a.created_time)
        //println("a.target:" + a.target + ", a.actor:" + a.actor + ", a.created_time:" + a.created_time + ", lineTransTime:" + lineTransTime)

        if (lineTransTime > newMaxTimeStamp.get - 60.seconds) {
          /** Transaction time is within 60-secs window, then copy it to temporary array */
          transTempArr += a
          //println("Transaction will be copied")

          /** Increment connections for the actor */
          if (vertexMap.contains(a.actor)) {
            /** If user already exists in the Adjacency List (HashMap), then increment its connections */
            //vertexMap(a.actor) += 1
            //vertexMap(a.actor) += a.target

            if (!vertexMap(a.actor).contains(a.target)) {
              vertexMap(a.actor) += a.target
            }
          }
          else {
            //vertexMap += (a.actor -> 1)
            vertexMap += (a.actor -> ArrayBuffer(a.target))
          }

          /** Increment connections for the target */
          if (vertexMap.contains(a.target)) {
            /** If user already exists in the Adjacency List (HashMap), then increment its connections */
            //vertexMap(a.target) += 1
            //vertexMap(a.target) += a.actor

            if (!vertexMap(a.target).contains(a.actor)) {
              vertexMap(a.target) += a.actor
            }

          }
          else {
            //vertexMap += (a.target -> 1)
            vertexMap += (a.target -> ArrayBuffer(a.actor))
          }
        }
      }


      /** Copy HasMap (containing number of connections for each individual) to Array, sort and calculate new median degree */
      var medianArray = ArrayBuffer[Int]()
      for ((k, v) <- vertexMap) {
        //medianArray += v
        medianArray += v.length
      }
      medianArray = medianArray.sorted
      //var medianValStr = median(medianArray).toString
      var medianValStr = "%.2f".format(calcMedian(medianArray)).toString

      /** Write new media degree to output file */
      //println("medianArray:" + medianArray + ", map:" + vertexMap)
      println("MEDIAN: " + medianValStr)
      val write = new PrintWriter(new FileOutputStream(outputFile, true))
      write.write(medianValStr + "\n")
      write.close()


      /** Update maxTimeStamp */
      maxTimeStamp = newMaxTimeStamp

      /** Update transactions array */
      transactionsArr = transTempArr
    }

    else {

      println("Transaction # " + lineNum + " is not valid, it will be ignored")
    }
  }

}
