package controllers

import javax.inject.{Inject, Singleton}

import db.ConnectionHandler
import forms.{LoginForm, RegisterForm}
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}


@Singleton
class AuthenticateController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with I18nSupport{



  def login = Action { implicit request =>
    Ok(views.html.login(LoginForm.form))
  }

  def authenticate = Action { implicit request =>
    var formData: RegisterForm = RegisterForm.form.bindFromRequest.get
    var connectionHandler = new ConnectionHandler()
    connectionHandler.connect()
    var username = connectionHandler.findUsernameByLoginAndPassword(formData.login, formData.password)
    connectionHandler.close()
    if(username.equals("") || username == null){
      Ok(views.html.error("Wrong credentials"))
    }else{
      Ok(views.html.index2(username)).withSession("username" -> username)
    }
  }

  def logout = Action {
    Redirect(routes.AuthenticateController.login).withNewSession.flashing(
      "success" -> "You've been logged out"
    )
  }

}
