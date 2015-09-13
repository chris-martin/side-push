package sidepush.nettosphere
package config

import sidepush.core.ChannelSelector

case class Config(
  host: String,
  port: Int,
  endpoints: Seq[Endpoint],
  channelSelectors: Seq[ChannelSelector]
)

sealed trait EndpointType

object EndpointType {
  case object Atmosphere extends EndpointType
  case object SocketIO extends EndpointType
  case object SockJS extends EndpointType
  case object Push extends EndpointType
}

case class Endpoint(
  `type`: EndpointType,
  path: String
)
