package db

import java.sql.{Connection, DriverManager}

class ConnectionHandler {


  val driver = "com.mysql.jdbc.Driver"
  val url = "jdbc:mysql://localhost:3306/play?verifyServerCertificate=true&useSSL=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC"
  val username = "root"
  val password = "praktyka"

  var connection:Connection = null

  def connect():Unit = {
    Class.forName(driver)
    connection = DriverManager.getConnection(url, username, password)
  }

  def findUserByLogin(login:String): Boolean = {
    var result : Boolean = false
    val statement = connection.prepareStatement("SELECT username FROM users WHERE username = ?")
    statement.setString(1, login)
    val resultSet = statement.executeQuery()
    while ( resultSet.next() ) {
      result = true

    }
    result
  }

  def saveUser(login:String, password:String): Unit = {
    val statement = connection.prepareStatement("INSERT INTO play.users(created, password, role, username) VALUES (NOW(), ?, ?, ?)")
    var passwordHash = String.format("%064x", new java.math.BigInteger(1, java.security.MessageDigest.getInstance("SHA-256").digest(password.getBytes("UTF-8"))))
    statement.setString(1, passwordHash)
    statement.setString(2, "USER")
    statement.setString(3, login)
    statement.executeUpdate()

  }

  def close():Unit = {
    connection.close()
  }
}
