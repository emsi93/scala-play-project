package controllers

import javax.inject._

import db.ConnectionHandler
import forms.PhoneNumberForm
import play.api.i18n.I18nSupport
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with I18nSupport {

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
    request.session.get("username").map { username =>
      Ok(views.html.index2(username))
    }.getOrElse {
      Ok(views.html.error("You must be logged"))
    }
  }

  def home = Action { implicit request =>
    Ok(views.html.home())
  }


  def listUser = Action { implicit request =>
    request.session.get("username").map { username =>
      var connectionHandler = new ConnectionHandler()
      connectionHandler.connect()
      var listUser =  connectionHandler.findAllUser()
      connectionHandler.close()
      Ok(views.html.list(listUser))
    }.getOrElse {
      Ok(views.html.error("You must be logged"))
    }
  }

  def userDetails(id: Int) = Action { implicit request =>

    request.session.get("username").map { username =>
      var connectionHandler = new ConnectionHandler()
      connectionHandler.connect()
      var user =  connectionHandler.findUserById(id)
      var numberList = connectionHandler.findPhoneNumberByUserId(id)
      connectionHandler.close()
      Ok(views.html.details(user, numberList))
    }.getOrElse {
      Ok(views.html.error("You must be logged"))
    }
  }

  def newPhoneNumber(id: Int) = Action{ implicit request =>
    request.session.get("username").map { username =>
      Ok(views.html.phone(PhoneNumberForm.form, id))
    }.getOrElse {
      Ok(views.html.error("You must be logged"))
    }

  }

  def newPhoneNumberPost() = Action{ implicit request =>

    request.session.get("username").map { username =>
      val formData: PhoneNumberForm = PhoneNumberForm.form.bindFromRequest.get
      if (PhoneNumberForm.isValid(formData)) {
        var connectionHandler = new ConnectionHandler()
        connectionHandler.connect()
        connectionHandler.savePhoneNumber(formData.phoneNumber, formData.idUser)
        connectionHandler.close()
        connectionHandler = null
        Ok(views.html.success("Adding the phone number was successful"))
      } else {
        Ok(views.html.error("Incorrect phone number"))
      }
    }.getOrElse {
      Ok(views.html.error("You must be logged"))
    }
  }

  def deleteNumber(id:Int) = Action { implicit request =>

    request.session.get("username").map { username =>
      var connectionHandler = new ConnectionHandler()
      connectionHandler.connect()
      connectionHandler.deleteNumber(id)
      connectionHandler.close()
      Ok(views.html.success("Deleted number"))
    }.getOrElse {
      Ok(views.html.error("You must be logged"))
    }
  }
}
