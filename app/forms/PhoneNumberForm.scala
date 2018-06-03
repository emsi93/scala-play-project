package forms

case class PhoneNumberForm(phoneNumber: String, idUser: String) {

}


object PhoneNumberForm{
  import play.api.data.Form
  import play.api.data.Forms._

  val form: Form[PhoneNumberForm] = Form(
    mapping(
      "phoneNumber" -> text,
      "idUser" -> text
    )(PhoneNumberForm.apply)(PhoneNumberForm.unapply)
  )


  def isValid(formData: PhoneNumberForm): Boolean = {
    val userFormConstraints = Form(
      mapping(
        "phoneNumber" -> nonEmptyText(minLength = 9),
        "idUser" -> nonEmptyText
      )(PhoneNumberForm.apply)(PhoneNumberForm.unapply)
    )

    val boundForm = userFormConstraints.bind(Map("phoneNumber" -> formData.phoneNumber, "idUser" -> formData.idUser))

    val ordinary=(('0' to '9')).toSet
    def isOrdinary(s:String)=s.forall(ordinary.contains(_))


    return !boundForm.hasErrors && isOrdinary(formData.phoneNumber)
  }

}
