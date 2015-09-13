package sidepush.core

/**
 * Determines what channels to subscribe to when a connection is first opened.
 */
sealed trait ChannelSelector

case class HttpChannelSelector(url: String, headerForwarding: HttpHeaderForwarding)
  extends ChannelSelector

case class HttpHeaderForwarding(headers: Set[CaseInsensitiveString], cookies: Set[String]) {

  def header(name: String): Boolean = headers.contains(CaseInsensitiveString(name))

  def allCookies: Boolean = headers.contains(CaseInsensitiveString("cookie"))
  
  def cookie(name: String): Boolean = cookies.contains(name)
}
