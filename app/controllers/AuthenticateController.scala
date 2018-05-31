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

  def authenticate() = Action { implicit request: Request[AnyContent] =>
    val formData: LoginForm = LoginForm.form.bindFromRequest.get
    Ok(formData.toString)
  }

}
