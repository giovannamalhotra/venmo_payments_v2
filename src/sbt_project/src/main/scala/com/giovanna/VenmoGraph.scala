package com.giovanna

import java.io.{File, FileOutputStream, PrintWriter}
import com.github.nscala_time.time.Imports._
import scala.collection.mutable.ArrayBuffer

/** ----------------------  Summary of VenmoGraph main method: processTransaction --------------------------------------
  *
  *   1. If new transaction's time stamp is greater than previous one:
  *     a. Update current max time stamp
  *     b. Evict transactions that are out of the 60 secs window
  *
  *   2. If transaction is within new 60 secs window:
  *     a. Add new transaction to array of transactions
  *     b. Update  graph adjacency list (vertexMap) with the connections for the actor and target
  *     c. Update  median array (remove previous median and insert new median value for the actor and target)
  *     d. Calculate new median degree
  *     e. Write new median degree to output file
*/


/** ----------------------------------------------------------------------------- */
/** Class VenmoGraph - Holds the collection of current transactions               */
/** ----------------------------------------------------------------------------- */
class VenmoGraph() {

  var transactionsArr = ArrayBuffer[Transaction]()  /** empty array that holds all transaction bbjects */
  var maxTimeStamp:Option[DateTime] = None  /** holds the maximum payment transaction timestamp */

  /** Graph User's Map - to keep track of connections for each vertex (actor or target) */
  var vertexMap = scala.collection.mutable.Map[String, ArrayBuffer[String]]()

  /** Sorted Array of user's median values */
  var sortedGraphDegreesArray = ArrayBuffer[Int]()



  /** ------------------------------------------- */
  /** "Find Index With Binary Search" method      */
  /** ------------------------------------------- */
  def findIndexWithBinarySearch(degree: Int, arr: ArrayBuffer[Int]): Int = { // Returns index where new degree should be inserted

    if (arr.length == 0) { return 0}
    else {
      var minIndex = 0
      var maxIndex = arr.length - 1

      while (minIndex <= maxIndex) {

        var middleIndex = minIndex + Math.floor((maxIndex - minIndex) / 2).toInt
        //println("degree:" + degree + ", minIndex:" + minIndex + ", maxIndex:" + maxIndex + ", middleIndex:" + middleIndex)

        if (degree == arr(middleIndex)) {

          if ((middleIndex == arr.length - 1) || (arr(middleIndex + 1) > degree)) {
            return middleIndex + 1
          } else {
            minIndex = middleIndex + 1
          }

        } else {

          if (degree < arr(middleIndex)) {

            if (minIndex == maxIndex) {
              return minIndex
            } else {
              maxIndex = middleIndex - 1
            }
          }
          else {

            if (minIndex == maxIndex) {
              return minIndex + 1
            } else {
              minIndex = middleIndex + 1
            }
          }
        }

      }
      return minIndex // The new element should be inserted in this index
    }
  }

  /** ------------------------------------------- */
  /** "Insert Into Degrees Array" method          */
  /** ------------------------------------------- */
  def insertIntoDegreesArray(degree: Int): Unit = {

      val index = findIndexWithBinarySearch(degree, sortedGraphDegreesArray)

      //println("Inside insertIntoDegreesArray.. index:" + index + ", degree:" + degree + ", sortedGraphDegreesArray:" + sortedGraphDegreesArray)
      if (index == -1) { sortedGraphDegreesArray.insert(0, degree) }
      else {

        if (index >= sortedGraphDegreesArray.length ) {
          sortedGraphDegreesArray += degree
        } else {
          if (index >= 0 && index < sortedGraphDegreesArray.length) {
            sortedGraphDegreesArray.insert(index, degree)
          }
        }
      }
    //println("sortedGraphDegreesArray at the end of insertIntoDegreesArray:" + sortedGraphDegreesArray)

  }

  /** ------------------------------------------- */
  /** "Remove From Degrees Array" method          */
  /** ------------------------------------------- */
  def removeFromDegreesArray(degree: Int): Unit = {

    sortedGraphDegreesArray -= degree

  }


  /** ------------------------------------------- */
  /** "Calculate Median" method                   */
  /** ------------------------------------------- */
  def calcMedian(arr: ArrayBuffer[Int]): Double = {

    var middle = arr.length/2
    //println("Inside calcMedian, arr:" + arr + ", middle:" + middle + ", arr.length % 2:" + arr.length % 2)
    var result: Double = 0
    if (arr.length % 2 != 0) {  /** Array length is odd */

      result = arr(Math.floor(middle).toInt)
      result = BigDecimal(result).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
    }
    else { /**  Array length is even */
      if (middle > 0) {
        result = (arr(middle - 1).toDouble + arr(middle).toDouble) / 2
        //println("Inside median method - arr(middle - 1):" + arr(middle - 1) + ", arr(middle):" + arr(middle) + ", result:" + result)
        result = BigDecimal(result).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
      }
    }
    return result
  }


  /** ------------------------------------------- */
  /** "Find Index With Binary Search" method      */
  /** ------------------------------------------- */
  def evictOldTransactions(): Unit = {

    var newConnectionsNum = 0
    var prevConnectionsNum = 0

    //println("----------------------------")
    //println("Inside evictOldTransactions:")

    var transTempArr = ArrayBuffer[Transaction]()

    for (a <- transactionsArr) {

      if (DateTime.parse(a.created_time) <= maxTimeStamp.get - 60.seconds) { //Transaction is older than max time stamp

        //println("This transaction will be evicted: target:" + a.target + ", actor:" + a.actor + ", created_time:" + a.created_time )

        if (vertexMap.contains(a.actor)) {

          if (vertexMap(a.actor).contains(a.target)) {
            /** Remove target from the actor's connections */

            prevConnectionsNum = vertexMap(a.actor).length
            vertexMap(a.actor) -= a.target
            newConnectionsNum = prevConnectionsNum - 1
            //println("sortedGraphDegreesArray before removeFromDegreesArray:" + sortedGraphDegreesArray)
            removeFromDegreesArray(prevConnectionsNum)
            //println("sortedGraphDegreesArray after removeFromDegreesArray and before insertIntoDegreesArray:" + sortedGraphDegreesArray + ", newConnectionsNum:" + newConnectionsNum)
            if (newConnectionsNum > 0) {
              insertIntoDegreesArray(newConnectionsNum)
            }
          }
        }

        if (vertexMap.contains(a.target)) {

          if (vertexMap(a.target).contains(a.actor)) {

            /** Remove actor from target's connections */
            prevConnectionsNum = vertexMap(a.target).length
            vertexMap(a.target) -= a.actor
            newConnectionsNum = prevConnectionsNum - 1
            //println("sortedGraphDegreesArray before removeFromDegreesArray:" + sortedGraphDegreesArray)
            removeFromDegreesArray(prevConnectionsNum)
            //println("sortedGraphDegreesArray after removeFromDegreesArray and before insertIntoDegreesArray:" + sortedGraphDegreesArray + ", newConnectionsNum:" + newConnectionsNum)

            if (newConnectionsNum > 0) {
              insertIntoDegreesArray(newConnectionsNum)
            }
          }
        }
      } else {
        transTempArr += a
      }
    }
    /** Update transactions array */
    transactionsArr = transTempArr

    //println("sortedGraphDegreesArray after evicting transactions:" + sortedGraphDegreesArray)

  }


  /** ------------------------------------------- */
  /** "processTransaction" method                 */
  /** ------------------------------------------- */
  def processTransaction(line: String, lineNum: Int, outputFile: File): Unit = {

    var transactionObj = new Transaction(line, lineNum)

    if (transactionObj.isValid ) {

      /** Convert transaction created_time (String) to nscala-time DateTime */
      var transTime = DateTime.parse(transactionObj.created_time)

      /** Determine max timestamp */
      if (maxTimeStamp.isEmpty) {
        maxTimeStamp = Some(transTime)
      } else {

        if (transTime > maxTimeStamp.get) {
          /** This transactions timestamp will be the new max time */
          maxTimeStamp = Some(transTime)

          /** Evict old transactions */
          evictOldTransactions()
        }
      }

      //println("----------------------------------------------------------------")
      //println("Process Transaction: target:" + transactionObj.target + ", actor:" + transactionObj.actor + ", created_time:" + transactionObj.created_time + ", transTime:" + transTime + ", maxTimeStamp:" + maxTimeStamp)

      var newConnectionsNum = 0
      var prevConnectionsNum = 0

      if (transTime > maxTimeStamp.get - 60.seconds) {

        /** Add transaction object to array of transactions  */
        transactionsArr += transactionObj

        /** Increment connections for the actor */
        if (vertexMap.contains(transactionObj.actor)) {

            if (!vertexMap(transactionObj.actor).contains(transactionObj.target)) { /** Add connection only if actor-target connection was not added before */
              prevConnectionsNum = vertexMap(transactionObj.actor).length
              vertexMap(transactionObj.actor) += transactionObj.target

              newConnectionsNum = prevConnectionsNum + 1

              //println("sortedGraphDegreesArray before removeFromDegreesArray:" + sortedGraphDegreesArray)
              removeFromDegreesArray(prevConnectionsNum)
              //println("sortedGraphDegreesArray after removeFromDegreesArray and before insertIntoDegreesArray:" + sortedGraphDegreesArray)
              insertIntoDegreesArray(newConnectionsNum)
              //println("sortedGraphDegreesArray after insertIntoDegreesArray:" + sortedGraphDegreesArray)

            }
        }
        else {
            vertexMap += (transactionObj.actor -> ArrayBuffer(transactionObj.target))
            insertIntoDegreesArray(1)
        }

        /** Increment connections for the target */
        if (vertexMap.contains(transactionObj.target)) {

          if (!vertexMap(transactionObj.target).contains(transactionObj.actor)) {
            prevConnectionsNum = vertexMap(transactionObj.target).length
            vertexMap(transactionObj.target) += transactionObj.actor
            newConnectionsNum = prevConnectionsNum + 1

            //println("sortedGraphDegreesArray before removeFromDegreesArray:" + sortedGraphDegreesArray)
            removeFromDegreesArray(prevConnectionsNum)
            //println("sortedGraphDegreesArray after removeFromDegreesArray and before insertIntoDegreesArray:" + sortedGraphDegreesArray)
            insertIntoDegreesArray(newConnectionsNum)
            //println("sortedGraphDegreesArray after insertIntoDegreesArray:" + sortedGraphDegreesArray)
          }

        }
        else {
          vertexMap += (transactionObj.target -> ArrayBuffer(transactionObj.actor))
          insertIntoDegreesArray(1)
        }

      } else {
        /** Ignore Transaction but will print same median degree to the output file */

      }


/*      /** Copy HashMap (containing number of connections for each individual) to array, sort and calculate new median degree */
      //var sortedGraphDegreesArray = ArrayBuffer[Int]()
      for ((k, v) <- vertexMap) {
        sortedGraphDegreesArray += v.length
      }
      sortedGraphDegreesArray = sortedGraphDegreesArray.sorted
*/
      var medianValStr = "%.2f".format(calcMedian(sortedGraphDegreesArray)).toString

      /** Write new media degree to output file */
      if (lineNum > 943 && lineNum < 947) {
        println("sortedGraphDegreesArray:" + sortedGraphDegreesArray + ", map:" + vertexMap)
      }
      println("MEDIAN: " + medianValStr)
      val write = new PrintWriter(new FileOutputStream(outputFile, true))
      write.write(medianValStr + "\n")
      write.close()

    }

    else {

      println("Transaction # " + lineNum + " is not valid, it will be ignored")
    }
  }

}
