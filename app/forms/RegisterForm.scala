package forms

case class RegisterForm( login: String, password: String )

object RegisterForm{

  import play.api.data.Forms._
  import play.api.data.Form

  val form: Form[RegisterForm] = Form(
    mapping(
      "login" -> text,
      "password" -> text
    )(RegisterForm.apply)(RegisterForm.unapply)
  )
}
