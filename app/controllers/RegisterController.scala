package controllers

import java.util.Date
import javax.inject.{Inject, Singleton}

import forms.RegisterForm
import models.{DAO, HibernateHolder, User, UserDAO}
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}


@Singleton
class RegisterController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with I18nSupport {

  def register() = Action {  implicit request: Request[AnyContent] =>
    Ok(views.html.registerForm(RegisterForm.form))
  }

  def registerPost() = Action { implicit request =>
    val formData: RegisterForm = RegisterForm.form.bindFromRequest.get
    registerUser(formData)
    Ok(formData.toString) // just returning the data because it's an example :)
  }

  def registerUser(formData: RegisterForm) : Unit = {
    HibernateHolder.init()
    val userDAO: DAO[User] = new UserDAO()
    val user = new User()
    user.username = formData.login
    user.password = formData.password
    user.role = "USER"
    user.created = new Date()
    userDAO.create(user)
  }
}
