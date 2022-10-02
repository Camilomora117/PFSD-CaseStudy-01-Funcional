package eci.edu.co
package entities

case class Account(transactions: List[Transaction]) {

  import Transaction._

  import MathUtilies._

  def getCopTransactions(): List[Transaction] = Transaction.getTransactionsOfCurrency(transactions, "COP")

  def getCopIncomeTransactions(): List[Transaction] = Transaction.getTransactionsOfType(getCopTransactions(), "Income")

  def getCopIncomeTransactionsValue(): Double = Transaction.sumTransactionsOfType(getCopIncomeTransactions(), "Income")

  def getCopExpenseTransactions(): List[Transaction] = Transaction.getTransactionsOfType(getCopTransactions(), "Expense")

  def getCopExpenseTransactionsValue(): Double = Transaction.sumTransactionsOfType(getCopIncomeTransactions(), "Expense")

  def getCopTransactionsBalance(): Double = MathUtilies.roundDouble(getCopIncomeTransactionsValue() - getCopExpenseTransactionsValue())

  //Forma mas general

  def getCurrencyTransactions(currency: String): List[Transaction] = Transaction.getTransactionsOfCurrency(transactions, currency)

  def getCurrencyIncomeTransactions(currency: String): List[Transaction] = Transaction.getTransactionsOfType(getCurrencyTransactions(currency), "Income")

  def getCurrencyExpenseTransactions(currency: String): List[Transaction] = Transaction.getTransactionsOfType(getCurrencyTransactions(currency), "Expense")

  def getCurrencyIncomeTransactionsValue(currency: String): Double = Transaction.sumTransactionsOfType(getCurrencyIncomeTransactions(currency), "Income")

  def getCurrencyExpenseTransactionsValue(currency: String): Double = Transaction.sumTransactionsOfType(getCurrencyExpenseTransactions(currency), "Expense")

  def getCurrencyTransactionsBalance(currency: String): Double = MathUtilies.roundDouble(getCurrencyIncomeTransactionsValue(currency) - getCurrencyExpenseTransactionsValue(currency))

}


object MathUtilies {

  def roundDouble(d: Double): Double = {
    Math.round(d * 100.0) / 100.0
  }

}
