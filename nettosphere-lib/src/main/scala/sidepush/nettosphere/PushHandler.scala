package sidepush.nettosphere

import javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST

import org.atmosphere.cpr.AtmosphereResource
import org.atmosphere.nettosphere.Handler
import sidepush.atmosphere.broadcast
import sidepush.core.Channel

case class PushHandler() extends Handler {

  override def handle(resource: AtmosphereResource): Unit =

    Option(resource.getRequest.getParameter("channel")) match {

      case Some(channel) =>
        broadcast(resource, Channel(channel), readAll(resource.getRequest.getReader))
        resource.getResponse.write("OK").flushBuffer()
        resource.close()

      case None =>
        val response = resource.getResponse
        response.setStatus(SC_BAD_REQUEST)
        response.write("""Missing parameter "channel".""").flushBuffer()
        resource.close()
    }
}
