package models

import java.util.Date
import javax.persistence._

import org.hibernate.annotations.GenericGenerator



@Entity
@Table(schema = "play-db", name = "users")
class User {

  def this(id: Int) = {
    this()
    this.id = id
  }

  @Id
  @GenericGenerator(name = "gen", strategy = "increment")
  @GeneratedValue(generator = "gen")
  @Column(name = "id", unique = true)
  var id: Int = _

  @Column(name = "email")
  var email: String = _

  @Column(name = "username")
  var username: String = _

  @Column(name = "password")
  var password: String = _

  @Column(name = "created")
  var created: Date = _

  @Column(name = "role")
  var role: String = _
}