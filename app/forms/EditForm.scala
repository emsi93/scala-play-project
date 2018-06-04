package forms

import db.ConnectionHandler

case class EditForm(name:String, surname:String, email:String) {

}

object EditForm {

  import play.api.data.Form
  import play.api.data.Forms._

  val form: Form[EditForm] = Form(
    mapping(
      "name" -> text,
      "surname" -> text,
      "email" -> text
    )(EditForm.apply)(EditForm.unapply)
  )


  def isValid(formData: EditForm): Boolean = {
    val userFormConstraints = Form(
      mapping(
        "name" -> nonEmptyText,
        "surname" -> nonEmptyText,
        "email" -> nonEmptyText
      )(EditForm.apply)(EditForm.unapply)
    )

    val boundForm = userFormConstraints.bind(Map("name" -> formData.name, "surname" -> formData.surname, "email" -> formData.email))

    def isValid(email: String): Boolean =
      """(\w+)@([\w\.]+)""".r.unapplySeq(email).isDefined
    val emailIsValid = isValid(formData.email)

    var connectionHandler  = new ConnectionHandler()
    connectionHandler.connect()
    val result = connectionHandler.findUserByEmail(formData.email)
    connectionHandler.close()
    connectionHandler = null

    return !boundForm.hasErrors && !result && emailIsValid
  }
}
