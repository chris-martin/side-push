package sidepush.atmosphere
package socketio

import org.atmosphere.cpr.AtmosphereResource
import org.atmosphere.socketio.SocketIOSessionOutbound
import org.atmosphere.socketio.cpr.SocketIOAtmosphereHandler
import org.atmosphere.socketio.transport.DisconnectReason

import scala.concurrent.ExecutionContext

case class SidepushSocketIOAtmosphereHandler(channelSelector: ChannelSelector)
    (implicit executionContext: ExecutionContext) extends SocketIOAtmosphereHandler {

  override def onConnect(resource: AtmosphereResource, handler: SocketIOSessionOutbound): Unit =
    initialize(resource, channelSelector)

  override def onMessage(resource: AtmosphereResource, handler: SocketIOSessionOutbound,
    message: Message): Unit = pushMessage(message, resource)

  override def onDisconnect(resource: AtmosphereResource, handler: SocketIOSessionOutbound,
    reason: DisconnectReason) {}
}
