package db

import java.sql.{Connection, DriverManager}
import java.util

import forms.PhoneNumberForm
import models.{PhoneNumber, User}

import scala.collection.mutable.ListBuffer

class ConnectionHandler {

  val driver = "com.mysql.cj.jdbc.Driver"
  val url = "jdbc:mysql://localhost:3306/play?verifyServerCertificate=true&useSSL=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC"
  val username = "root"
  val password = "praktyka"

  var connection: Connection = null

  def connect(): Unit = {
    Class.forName(driver)
    connection = DriverManager.getConnection(url, username, password)
  }

  def findUserByLogin(login: String): Boolean = {
    var result: Boolean = false
    val statement = connection.prepareStatement("SELECT username FROM play.users WHERE username = ?")
    statement.setString(1, login)
    val resultSet = statement.executeQuery()
    while (resultSet.next()) {
      result = true

    }
    result
  }

  def findAllUser(): List[User] = {
    var listUser = new ListBuffer[User]
    val statement = connection.createStatement()
    val resultSet = statement.executeQuery("SELECT * FROM play.users")
    while (resultSet.next()) {
      var user = new User()
      user.id = resultSet.getInt("id")
      user.username = resultSet.getString("username")
      user.password = resultSet.getString("password")
      user.role = resultSet.getString("role")
      user.email = resultSet.getString("email")
      user.created = resultSet.getDate("created")
      listUser += user
    }
    listUser.toList
  }

  def findPhoneNumberByUserId(id: Int): List[PhoneNumber] = {
    var phoneNumberList = new ListBuffer[PhoneNumber]
    val statement = connection.prepareStatement("SELECT * FROM play.phone_number WHERE id_user = ?")
    statement.setString(1, String.valueOf(id))
    val resultSet = statement.executeQuery()
    while (resultSet.next()) {
      var phoneNumber = new PhoneNumber()
      phoneNumber.id = resultSet.getInt("id")
      phoneNumber.number = resultSet.getString("number")
      phoneNumber.idUser = resultSet.getInt("id_user")
      phoneNumberList += phoneNumber
    }
    phoneNumberList.toList
  }

  def findUserById(id:Int): User = {
    val statement = connection.prepareStatement("SELECT * FROM play.users WHERE id = ?")
    statement.setString(1, String.valueOf(id))
    val resultSet = statement.executeQuery()
    var user = new User()
    while (resultSet.next()) {
      user.id = resultSet.getInt("id")
      user.username = resultSet.getString("username")
      user.password = resultSet.getString("password")
      user.role = resultSet.getString("role")
      user.email = resultSet.getString("email")
      user.created = resultSet.getDate("created")
    }
    user
  }

  def saveUser(login: String, password: String): Unit = {
    val statement = connection.prepareStatement("INSERT INTO play.users(created, password, role, username) VALUES (NOW(), ?, ?, ?)")
    var passwordHash = String.format("%064x", new java.math.BigInteger(1, java.security.MessageDigest.getInstance("SHA-256").digest(password.getBytes("UTF-8"))))
    statement.setString(1, passwordHash)
    statement.setString(2, "USER")
    statement.setString(3, login)
    statement.executeUpdate()
  }

  def savePhoneNumber(phoneNumber: String, idUser: String) : Unit = {
    val statement = connection.prepareStatement("INSERT INTO play.phone_number(number, id_user) VALUES (?, ?)")
    statement.setString(1, phoneNumber)
    statement.setString(2, idUser)
    statement.executeUpdate()
  }

  def deleteNumber(id:Int): Unit = {
    val statement = connection.prepareStatement("DELETE FROM play.phone_number WHERE id = ? ")
    statement.setString(1, String.valueOf(id))
    statement.executeUpdate()
  }

  def close(): Unit = {
    connection.close()
  }
}
