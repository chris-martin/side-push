package sidepush

import org.atmosphere.cpr.{AtmosphereResource, AtmosphereResourceEvent, Broadcaster}
import org.slf4j.LoggerFactory
import sidepush.core.{BadRequest, Channel, ChannelFutures}

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

package object atmosphere {

  type Message = String

  val logger = LoggerFactory.getLogger("sidepush.atmosphere")

  def getBroadcaster(resource: AtmosphereResource, channel: Channel): Broadcaster =
    resource.getAtmosphereConfig.getBroadcasterFactory.lookup(channel, true)

  def subscribe(resource: AtmosphereResource, channel: Channel): Unit =
    getBroadcaster(resource, channel).addAtmosphereResource(resource)

  def broadcast(resource: AtmosphereResource, channel: Channel, message: Message): Unit = {
    logger.info(s"Broadcasting to $channel: $message")
    getBroadcaster(resource, channel).broadcast(message)
  }

  def initialize(resource: AtmosphereResource, channelSelector: ChannelSelector)
      (implicit executionContext: ExecutionContext) {

    val request = resource.getRequest

    def m(s: String): String = s"Atmosphere resource ${resource.uuid} - $s"

    logger.info(m("Initializing."))

    channelSelector.selectChannels(request) match {

      case BadRequest(message) =>

        logger.debug(m(s"Initialization failed.\n$message"))

        val response = resource.getResponse
        response.setStatus(400)
        response.write(message).flushBuffer()

      case ChannelFutures(channelFutures) =>

        resource.suspend()

        for (channelFuture <- channelFutures) {
          channelFuture.onComplete({

            case Success(channels) =>
              for (channel <- channels) {
                logger.debug(m(s"Subscribed to channel: $channel."))
                subscribe(resource, channel)
              }

            case Failure(e) =>
              logger.warn(m(s"Failed to determine channel.\n${e.getMessage}"))
          })
        }
    }
  }

  def handleEvent(event: AtmosphereResourceEvent) {
    event.getMessage match {

      case null =>

      case message: Message =>
        pushMessage(message, event.getResource)

      case x =>
        logger.warn(s"Unexpected broadcast: $x")
        event.getResource.suspend()
    }
  }

  def pushMessage(message: Message, resource: AtmosphereResource) {
    resource.getResponse.write(message).flushBuffer()
    resource.suspend()
  }
}
