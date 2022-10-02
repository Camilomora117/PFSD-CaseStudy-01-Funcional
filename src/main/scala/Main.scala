package eci.edu.co

import csv.CsvWorker

import com.typesafe.scalalogging.Logger

object Main extends App {
  val LOGGER = Logger("MainLogger")
  val transactions = CsvWorker.readFile("csvs/10records.csv")
  transactions.foreach(t => LOGGER.info(t.toString))
}
