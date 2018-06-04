package controllers

import javax.inject._

import db.ConnectionHandler
import forms.EditForm
import play.api.i18n.I18nSupport
import play.api.mvc._


@Singleton
class EditController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with I18nSupport{

  def edit() = Action { implicit request: Request[AnyContent] =>
    request.session.get("username").map { username =>
      Ok(views.html.edit(EditForm.form))
    }.getOrElse {
      Ok(views.html.error("You must be logged"))
    }
  }

  def editPost() = Action { implicit request: Request[AnyContent] =>

    request.session.get("username").map { username =>
      val formData: EditForm = EditForm.form.bindFromRequest.get
      if (EditForm.isValid(formData)) {
        var connectionHandler = new ConnectionHandler()
        connectionHandler.connect()
        var id = connectionHandler.findIdByLogin(username)
        connectionHandler.updateUser(id, formData.name, formData.surname, formData.email)
        connectionHandler.close()
        connectionHandler = null
        Ok(views.html.success("Edition was successful"))
      } else {
        Ok(views.html.error("Incorrect fields"))
      }
    }.getOrElse {
      Ok(views.html.error("You must be logged"))
    }

    // just returning the data because it's an example :)
  }
}
