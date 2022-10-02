package eci.edu.co
package dao

import entities.Transaction

import com.typesafe.scalalogging.Logger

import java.sql.{Connection, PreparedStatement, ResultSet, Timestamp}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class TransactionDAO(connection: Connection) {

  val LOGGER = Logger("TransactionDAO")

  def saveTransactions(table: String, transactions: List[Transaction]): Future[Unit] = {
    val sql = s"INSERT INTO ${table.toLowerCase} (value,currency,category,created) VALUES(?, ?, ?, ?)"

    val preparedStatement = connection.prepareStatement(sql)
    connection.setAutoCommit(false)

    Future {
      transactions
        .grouped(10000)
        .foreach(
          batch => {
            batch.foreach(t => {
              preparedStatement.setDouble(1, t.value)
              preparedStatement.setString(2, t.currency)
              preparedStatement.setString(3, t.category)
              preparedStatement.setTimestamp(4, t.created)
              preparedStatement.addBatch()
            })
            preparedStatement.executeBatch()
            connection.commit()
          }
        )
    }
  }

  def getTransactionsOfType(`type`: String): List[Transaction] = {
    // TODO How would you improve this to not use a var?
    var transactions: List[Transaction] = List.empty
    val preparedStatement: PreparedStatement = connection.prepareStatement("Select * from " + `type`.toLowerCase)
    val resultSet: ResultSet = preparedStatement.executeQuery
    while (resultSet.next) {
      val value: Double = resultSet.getDouble("value")
      val currency: String = resultSet.getString("currency")
      val category: String = resultSet.getString("category")
      val timestamp: Timestamp = resultSet.getTimestamp("created")
      transactions = transactions :+ Transaction(value, currency, `type`, category, timestamp)
    }
    transactions
  }
}
