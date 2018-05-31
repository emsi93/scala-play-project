package forms

import db.ConnectionHandler

case class RegisterForm( login: String, password: String )

object RegisterForm{

  import play.api.data.Form
  import play.api.data.Forms._



  val form: Form[RegisterForm] = Form(
    mapping(
      "login" -> text,
      "password" -> text
    )(RegisterForm.apply)(RegisterForm.unapply)
  )




  def isValid(formData: RegisterForm): Boolean = {
    val userFormConstraints = Form(
      mapping(
        "login" -> nonEmptyText,
        "password" -> nonEmptyText(minLength = 5)
      )(RegisterForm.apply)(RegisterForm.unapply)
    )

    val boundForm = userFormConstraints.bind(Map("login" -> formData.login, "password" -> formData.password))

    val ordinary=(('a' to 'z') ++ ('A' to 'Z') ++ ('0' to '9')).toSet
    def isOrdinary(s:String)=s.forall(ordinary.contains(_))

    var connectionHandler  = new ConnectionHandler()
    connectionHandler.connect()
    val result = connectionHandler.findUserByLogin(formData.login)
    connectionHandler.close()
    connectionHandler = null

    return !boundForm.hasErrors && formData.password.exists(_.isUpper) && !isOrdinary(formData.password) && !result
  }




}
