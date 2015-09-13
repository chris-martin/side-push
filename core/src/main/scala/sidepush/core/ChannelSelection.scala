package sidepush.core

import scala.concurrent.Future

sealed trait ChannelSelection

case class ChannelFutures(futures: Iterable[Future[Iterable[Channel]]])
  extends ChannelSelection

/**
 * Channel selection was not attempted because the request was invalid.
 *
 * @param message A client-visible error message served with a 400 response.
 */
case class BadRequest(message: String)
  extends ChannelSelection
