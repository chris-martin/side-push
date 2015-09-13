package sidepush.atmosphere

import org.atmosphere.cpr.{AtmosphereHandler, AtmosphereResource, AtmosphereResourceEvent}

import scala.concurrent.ExecutionContext

/**
 * Atmosphere handler that a client uses to subscribe to its messages.
 */
case class SidepushAtmosphereHandler(channelSelector: ChannelSelector)
    (implicit executionContext: ExecutionContext) extends AtmosphereHandler {

  override def onRequest(resource: AtmosphereResource): Unit =
    initialize(resource, channelSelector)

  override def onStateChange(event: AtmosphereResourceEvent): Unit =
    handleEvent(event)

  override def destroy() {}
}
