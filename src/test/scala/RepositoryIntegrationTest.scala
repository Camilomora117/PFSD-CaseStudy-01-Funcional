package eci.edu.co

import csv.CsvWorker
import dao.TransactionDAO

import com.dimafeng.testcontainers.{ForAllTestContainer, PostgreSQLContainer}
import com.typesafe.scalalogging.Logger
import eci.edu.co.entities.{Account, Transaction}
import org.scalatest.FunSuite
import org.scalatest.concurrent.ScalaFutures

import java.sql.DriverManager
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

class RepositoryIntegrationTest extends FunSuite with ForAllTestContainer with ScalaFutures {

  val LOGGER = Logger("RepositoryIntegrationTest")

  override val container: PostgreSQLContainer = PostgreSQLContainer(databaseName = "product_db", mountPostgresDataToTmpfs = true)
  container.container.withInitScript("init_script.sql")

  test("Test basic insert/read from Postgres") {
    Class.forName(container.driverClassName)
    val connection = DriverManager.getConnection(container.jdbcUrl, container.username, container.password)
    val transactions = CsvWorker.readFile("csvs/10records.csv")
    val transactionDAO = TransactionDAO(connection)

    val future = transactionDAO
      .saveTransactions("Income", transactions)
      .flatMap(_ => {
        val storedIncomeTransactions = TransactionDAO(connection).getTransactionsOfType("Income")
        Future {
          storedIncomeTransactions.size
        }
      })

    future
      .onComplete {
        case Success(storedIncomeTransactionsSize) =>
          LOGGER.info("The Future completed successfully")
          assert(storedIncomeTransactionsSize == 10)
        case Failure(exception) =>
          fail(s"There was an error: ${exception}")
      }

    whenReady(future) { result => assert(result == 10) }
  }

  test("Get Transaction for Type Income") {
    Class.forName(container.driverClassName)
    val connection = DriverManager.getConnection(container.jdbcUrl, container.username, container.password)
    val transactions = CsvWorker.readFile("csvs/10records.csv")
    val transactionDAO = TransactionDAO(connection)

    transactionDAO.saveTransactions("Income", transactions)

    val resTransaction = Transaction.getTransactionsOfType(transactions, "Income")
    LOGGER.info("Size the list transactions")
    assert(resTransaction.size == 3)
  }

  test("Get Transaction for Type Expense") {
    Class.forName(container.driverClassName)
    val connection = DriverManager.getConnection(container.jdbcUrl, container.username, container.password)
    val transactions = CsvWorker.readFile("csvs/10records.csv")
    val transactionDAO = TransactionDAO(connection)

    transactionDAO.saveTransactions("Expense", transactions)

    val resTransaction = Transaction.getTransactionsOfType(transactions, "Expense")
    LOGGER.info("Size the list transactions")
    assert(resTransaction.size == 7)
  }

  test("Get Transaction of Currency") {
    Class.forName(container.driverClassName)
    val connection = DriverManager.getConnection(container.jdbcUrl, container.username, container.password)
    val transactions = CsvWorker.readFile("csvs/10records.csv")
    val transactionDAO = TransactionDAO(connection)

    transactionDAO.saveTransactions("Currency", transactions)
    //Currency USD
    val resTransactionUSD = Transaction.getTransactionsOfCurrency(transactions, "USD")
    LOGGER.info("Size the list transactions with Currency USD")
    assert(resTransactionUSD.size == 5)
    //Currency COP
    val resTransactionCOP = Transaction.getTransactionsOfCurrency(transactions, "COP")
    LOGGER.info("Size the list transactions with Currency COP")
    assert(resTransactionCOP.size == 3)
    //Currency EUR
    val resTransactionEUR = Transaction.getTransactionsOfCurrency(transactions, "EUR")
    LOGGER.info("Size the list transactions with Currency EUR")
    assert(resTransactionEUR.size == 2)
  }

  test("Sum Transaction of Type") {
    Class.forName(container.driverClassName)
    val connection = DriverManager.getConnection(container.jdbcUrl, container.username, container.password)
    val transactions = CsvWorker.readFile("csvs/10records.csv")
    val transactionDAO = TransactionDAO(connection)

    transactionDAO.saveTransactions("Sum", transactions)
    //Income
    val sumTransactionIncome = Transaction.sumTransactionsOfType(transactions,"Income")
    LOGGER.info("Value the Transactions Income")
    assert(sumTransactionIncome == 45736.07)
    //Expense
    val sumTransactionExpense = Transaction.sumTransactionsOfType(transactions,"Expense")
    LOGGER.info("Value the Transactions Expense")
    assert(sumTransactionExpense == 86717.64)
  }

  test("Account Get Currency Transaction") {
    Class.forName(container.driverClassName)
    val connection = DriverManager.getConnection(container.jdbcUrl, container.username, container.password)
    val transactions = CsvWorker.readFile("csvs/10records.csv")
    val transactionDAO = TransactionDAO(connection)

    transactionDAO.saveTransactions("Currency", transactions)
    val account = Account(transactions)
    //Currency USD
    val resTransactionUSD = account.getCurrencyTransactions("USD")
    LOGGER.info("Size the list transactions with Currency USD")
    assert(resTransactionUSD.size == 5)
    //Currency COP
    val resTransactionCOP = account.getCurrencyTransactions("COP")
    LOGGER.info("Size the list transactions with Currency COP")
    assert(resTransactionCOP.size == 3)
    //Currency EUR
    val resTransactionEUR = account.getCurrencyTransactions("EUR")
    LOGGER.info("Size the list transactions with Currency EUR")
    assert(resTransactionEUR.size == 2)
  }

  test("Account Get USD Transaction Typee") {
    Class.forName(container.driverClassName)
    val connection = DriverManager.getConnection(container.jdbcUrl, container.username, container.password)
    val transactions = CsvWorker.readFile("csvs/10records.csv")
    val transactionDAO = TransactionDAO(connection)

    transactionDAO.saveTransactions("Currency", transactions)
    val account = Account(transactions)
    //Currency USD - Income
    val resTransactionUSDIncome = account.getCurrencyIncomeTransactions("USD")
    LOGGER.info("Size the list transactions with Currency USD and Type Income")
    assert(resTransactionUSDIncome.size == 1)
    //Currency USD - Expense
    val resTransactionUSDExpense = account.getCurrencyExpenseTransactions("USD")
    LOGGER.info("Size the list transactions with Currency USD and Type Expense")
    assert(resTransactionUSDExpense.size == 4)
  }

  test("Account Get COP Transaction Type") {
    Class.forName(container.driverClassName)
    val connection = DriverManager.getConnection(container.jdbcUrl, container.username, container.password)
    val transactions = CsvWorker.readFile("csvs/10records.csv")
    val transactionDAO = TransactionDAO(connection)

    transactionDAO.saveTransactions("Currency", transactions)
    val account = Account(transactions)
    //Currency COP - Income
    val resTransactionCOPIncome = account.getCurrencyIncomeTransactions("COP")
    LOGGER.info("Size the list transactions with Currency COP and Type Income")
    assert(resTransactionCOPIncome.size == 1)
    //Currency COP - Expense
    val resTransactionCOPExpense = account.getCurrencyExpenseTransactions("COP")
    LOGGER.info("Size the list transactions with Currency COP and Type Expense")
    assert(resTransactionCOPExpense.size == 2)
  }

  test("Account Get EUR Transaction Type") {
    Class.forName(container.driverClassName)
    val connection = DriverManager.getConnection(container.jdbcUrl, container.username, container.password)
    val transactions = CsvWorker.readFile("csvs/10records.csv")
    val transactionDAO = TransactionDAO(connection)

    transactionDAO.saveTransactions("Currency", transactions)
    val account = Account(transactions)
    //Currency EUR - Income
    val resTransactionEURIncome = account.getCurrencyIncomeTransactions("EUR")
    LOGGER.info("Size the list transactions with Currency EUR and Type Income")
    assert(resTransactionEURIncome.size == 1)
    //Currency EUR - Expense
    val resTransactionEURExpense = account.getCurrencyExpenseTransactions("EUR")
    LOGGER.info("Size the list transactions with Currency EUR and Type Expense")
    assert(resTransactionEURExpense.size == 1)
  }

  test("Account Get USD Type Transactions Value") {
    Class.forName(container.driverClassName)
    val connection = DriverManager.getConnection(container.jdbcUrl, container.username, container.password)
    val transactions = CsvWorker.readFile("csvs/10records.csv")
    val transactionDAO = TransactionDAO(connection)

    transactionDAO.saveTransactions("Value", transactions)
    val account = Account(transactions)
    //Currency USD - Income
    val resTransactionUSDIncome = account.getCurrencyIncomeTransactionsValue("USD")
    LOGGER.info("Value list Transactions USD Income")
    assert(resTransactionUSDIncome == 14.34)
    //Currency USD - Expense
    val resTransactionUSDExpense = account.getCurrencyExpenseTransactionsValue("USD")
    LOGGER.info("Value list Transactions USD Expense")
    assert(resTransactionUSDExpense == 26.37)
  }

  test("Account Get COP Type Transactions Value") {
    Class.forName(container.driverClassName)
    val connection = DriverManager.getConnection(container.jdbcUrl, container.username, container.password)
    val transactions = CsvWorker.readFile("csvs/10records.csv")
    val transactionDAO = TransactionDAO(connection)

    transactionDAO.saveTransactions("Value", transactions)
    val account = Account(transactions)
    //Currency COP - Income
    val resTransactionCOPIncome = account.getCurrencyIncomeTransactionsValue("COP")
    LOGGER.info("Value list Transactions COP Income")
    assert(resTransactionCOPIncome == 45712.19)
    //Currency COP - Expense
    val resTransactionCOPExpense = account.getCurrencyExpenseTransactionsValue("COP")
    LOGGER.info("Value list Transactions COP Expense")
    assert(resTransactionCOPExpense == 86685.52)
  }

  test("Account Get EUR Type Transactions Value") {
    Class.forName(container.driverClassName)
    val connection = DriverManager.getConnection(container.jdbcUrl, container.username, container.password)
    val transactions = CsvWorker.readFile("csvs/10records.csv")
    val transactionDAO = TransactionDAO(connection)

    transactionDAO.saveTransactions("Value", transactions)
    val account = Account(transactions)
    //Currency EUR - Income
    val resTransactionEURIncome = account.getCurrencyIncomeTransactionsValue("EUR")
    LOGGER.info("Value list Transactions EUR Income")
    assert(resTransactionEURIncome == 9.54)
    //Currency EUR - Expense
    val resTransactionEURExpense = account.getCurrencyExpenseTransactionsValue("EUR")
    LOGGER.info("Value list Transactions EUR Expense")
    assert(resTransactionEURExpense == 5.75)
  }

  test("Account Get Currency Transactions Balance") {
    Class.forName(container.driverClassName)
    val connection = DriverManager.getConnection(container.jdbcUrl, container.username, container.password)
    val transactions = CsvWorker.readFile("csvs/10records.csv")
    val transactionDAO = TransactionDAO(connection)

    transactionDAO.saveTransactions("Value", transactions)
    val account = Account(transactions)
    //Currency USD
    val resTransactionUSD = account.getCurrencyTransactionsBalance("USD")
    LOGGER.info("Value list Transactions Balance USD")
    assert(resTransactionUSD == -12.03)
    //Currency COP
    val resTransactionCOP = account.getCurrencyTransactionsBalance("COP")
    LOGGER.info("Value list Transactions Balance COP")
    assert(resTransactionCOP == -40973.33)
    //Currency USD
    val resTransactionEUR = account.getCurrencyTransactionsBalance("EUR")
    LOGGER.info("Value list Transactions Balance EUR")
    assert(resTransactionEUR == 3.79)
  }
}
