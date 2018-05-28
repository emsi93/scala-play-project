package models
import org.hibernate.cfg.Configuration
import org.hibernate.SessionFactory

object HibernateHolder {

  private var sessionFactory: Option[SessionFactory] = None

  def init() = sessionFactory match {
    case None => sessionFactory = Some(new Configuration().configure().buildSessionFactory())
    case _ => sessionFactory = None
  }

  def getSessionFactory: Option[SessionFactory] = sessionFactory

}