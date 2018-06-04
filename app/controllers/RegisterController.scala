package controllers

import javax.inject.{Inject, Singleton}

import db.ConnectionHandler
import forms.{LoginForm, RegisterForm}
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}


@Singleton
class RegisterController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with I18nSupport {

  def register() = Action { implicit request: Request[AnyContent] =>
    request.session.get("username").map { username =>
      Ok(views.html.index2(username))
    }.getOrElse {
      Ok(views.html.registerForm(RegisterForm.form))
    }
  }

  def registerPost() = Action { implicit request: Request[AnyContent] =>
    val formData: RegisterForm = RegisterForm.form.bindFromRequest.get
    if (RegisterForm.isValid(formData)) {
      var connectionHandler = new ConnectionHandler()
      connectionHandler.connect()
      connectionHandler.saveUser(formData.login, formData.password)
      connectionHandler.close()
      connectionHandler = null
      Ok(views.html.success("Registration was successful"))
    } else {
      Ok(views.html.error("Incorrect fields"))
    }
    // just returning the data because it's an example :)
  }

}
