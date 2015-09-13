package sidepush.nettosphere

import com.typesafe.config.{Config => Hocon}
import sidepush.core._
import sidepush.nettosphere.config.{Config, Endpoint, EndpointType}

import scala.collection.convert.wrapAsScala._

package object hocon {

  def loadRoot(hocon: Hocon): Config = loadConfig(hocon.getConfig("side-push"))

  def loadConfig(hocon: Hocon): Config =
    Config(
      host = hocon.getString("host"),
      port = hocon.getInt("port"),
      endpoints = hocon.getConfigList("endpoints").toSeq.map(loadEndpoint),
      channelSelectors = loadChannelSelectors(hocon.getConfig("channel-selectors"))
    )

  def loadEndpoint(hocon: Hocon): Endpoint =
    Endpoint(
      `type` = hocon.getString("type") match {
        case "atmosphere" => EndpointType.Atmosphere
        case "socket-io" => EndpointType.SocketIO
        case "sock-js" => EndpointType.SockJS
        case "push" => EndpointType.Push
        case x => throw new RuntimeException(s"Unrecognized Endpoint type: $x")
      },
      path = hocon.getString("path")
    )

  def loadChannelSelectors(hocon: Hocon): Seq[ChannelSelector] =
    hocon.getConfigList("http").map(loadHttpChannelSelector)

  def loadHttpChannelSelector(hocon: Hocon): HttpChannelSelector =
    HttpChannelSelector(
      url = hocon.getString("url"),
      headerForwarding = loadHttpHeaderForwarding(hocon.getConfig("forward"))
    )

  def loadHttpHeaderForwarding(hocon: Hocon) =
    HttpHeaderForwarding(
      headers = hocon.getStringList("headers").map(CaseInsensitiveString(_)).toSet,
      cookies = hocon.getStringList("cookies").toSet
    )
}
