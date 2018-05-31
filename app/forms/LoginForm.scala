package forms

case class LoginForm( login: String, password: String )


object LoginForm {

  import play.api.data.Form
  import play.api.data.Forms._


  val form: Form[LoginForm] = Form(
    mapping(
      "login" -> text,
      "password" -> text
    )(LoginForm.apply)(LoginForm.unapply)
  )
}
