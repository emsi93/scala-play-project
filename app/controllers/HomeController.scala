package controllers

import javax.inject._

import models.{DAO, HibernateHolder, User, UserDAO}
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def index2 = Action { implicit request =>
    test
    Ok(views.html.index2("Michał"))
  }

  def test : Unit = {
    HibernateHolder.init()
    val userDAO: DAO[User] = new UserDAO()
    val user = new User()
    user.email = "test"
    userDAO.create(user)
  }

}
