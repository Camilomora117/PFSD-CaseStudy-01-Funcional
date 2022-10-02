package eci.edu.co
package entities

import java.sql.Timestamp

trait FinanceOperation {

  def Income(`type`: String): Unit = {
    if (`type` == Transaction.Income)
      println("Expense operation")
  }

  def expense(`type`: String): Unit = {
    if (`type` == Transaction.Expense)
      println("Expense operation")
  }
}

case class Transaction(value: Double, currency: String, `type`: String = "", category: String, created: Timestamp) extends FinanceOperation {

  def this(currency: String, `type`: String, category: String) {
    this(0, currency, `type`, category, new Timestamp(0))
  }

}

object Transaction {

  import scala.util.Random

  //Concurrency
  val COP = "COP"
  val USD = "USD"
  val EUR = "EUR"
  val GBP = "GBP"
  val JPY = "JPY"
  val Currencies = List(COP, USD, EUR, GBP, JPY)

  //Value Concurrency
  val USD_COP = 4167
  val EUR_COP = 4276
  val GBP_COP = 5275
  val JPY_COP = 35.26

  //Types
  val Income = "Income"
  val Expense = "Expense"
  val Types = List(Income, Expense)

  val random = new Random()

  def getTransactionsOfType(transactions: List[Transaction], `type`: String): List[Transaction] = {
    transactions.filter(t => t.`type`.equals(`type`))
  }

  def getTransactionsOfCurrency(transactions: List[Transaction], currency: String): List[Transaction] = {
    transactions.filter(t => t.currency.equals(currency))
  }

  def sumTransactionsOfType(transaction: List[Transaction], `type`: String): Double = {
    getTransactionsOfType(transaction,`type`).map(_.value).sum
  }

}
