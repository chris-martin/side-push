package sidepush.atmosphere

import org.atmosphere.cpr.AtmosphereRequest
import sidepush.core.ChannelSelection

/**
 * Determines what channels to subscribe to when a connection is first opened.
 */
trait ChannelSelector {

  def selectChannels(request: AtmosphereRequest): ChannelSelection
}
