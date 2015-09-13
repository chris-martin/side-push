package sidepush.core

trait CaseInsensitiveString {

  def lowerCase: String
  def upperCase: String

  override def equals(obj: scala.Any): Boolean = obj match {
    case that: CaseInsensitiveString => this.lowerCase equals that.lowerCase
    case _ => false
  }

  override def hashCode(): Int = lowerCase.hashCode
}

object CaseInsensitiveString {

  def apply(s: String): CaseInsensitiveString = new LowerCaseString(s.toLowerCase)

  class LowerCaseString private[CaseInsensitiveString] (val lowerCase: String)
      extends CaseInsensitiveString {

    override def upperCase: String = lowerCase.toUpperCase
  }
}
