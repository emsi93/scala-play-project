package models

import org.hibernate.{Session, SessionFactory, Transaction}

class DAOExecutor[E] {

  private var session: Session = _
  private var transaction: Transaction = _

  /** Provide valid session factory to func */
  def executeWithSessionFactory[T](func: (SessionFactory) => Option[T]): Option[T] = HibernateHolder.getSessionFactory match {
    case Some(sessionFactory) =>
      try {
        func(sessionFactory)
      } catch {
        case e: Exception =>
          e.printStackTrace()
          None
      }
    case None =>
      println("Cannot get session factory")
      None
  }

  /** Provide valid session for func and automatically close it after execution */
  def executeWithSession[T](func: (Session) => Option[T]): Option[T] = HibernateHolder.getSessionFactory match {
    case Some(sessionFactory) =>
      try {
        session = sessionFactory.openSession()
        func(session)
      } catch {
        case e: Exception =>
          e.printStackTrace()
          None
      } finally {
        session.close()
      }
    case None =>
      println("Cannot get session factory")
      None
  }

  def executeWithTransaction[T](func: (Session) => Option[T]): Option[T] = HibernateHolder.getSessionFactory match {
    case Some(sessionFactory) =>
      try {
        session = sessionFactory.openSession()
        transaction = session.beginTransaction()
        val result = func(session)
        transaction.commit()
        result
      } catch {
        case e: Exception =>
          if (transaction != null) transaction.rollback()
          e.printStackTrace()
          None
      } finally {
        session.close()
      }
    case None =>
      println("Cannot get session factory")
      None
  }

  def toScalaSeq(list: java.util.List[_]): Seq[E] = {
    list.asInstanceOf[Seq[E]]
  }

  def toScalaList(list: java.util.List[_]): List[E] = {
    list.asInstanceOf[List[E]]
  }

}
