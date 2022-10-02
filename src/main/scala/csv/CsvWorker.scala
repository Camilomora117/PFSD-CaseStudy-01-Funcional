package eci.edu.co
package csv

import entities.Transaction

import com.github.tototoshi.csv._

import java.io.File
import java.sql.Timestamp

object CsvWorker {

  def readFile(path: String): List[Transaction] = {
    val reader = CSVReader.open(new File(path))
    reader.all().tail.map(row => row match {
      case List(value, currency, typee, category, created) => Transaction(value.toDouble, currency, typee, category, Timestamp.valueOf(created))
    })
  }

}
