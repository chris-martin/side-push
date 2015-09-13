package sidepush

import java.io.BufferedReader

import akka.actor.ActorSystem
import com.typesafe.config.{Config => Hocon}
import org.atmosphere.cpr.AtmosphereRequest
import org.atmosphere.nettosphere.{Config => NettosphereConfig, Nettosphere}
import sidepush.atmosphere.SidepushAtmosphereHandler
import sidepush.atmosphere.socketio.SidepushSocketIOAtmosphereHandler
import sidepush.core._
import sidepush.nettosphere.config.{Config, EndpointType}
import spray.client.pipelining._
import spray.http.HttpHeaders.RawHeader
import spray.http.{HttpCookie, HttpHeader, HttpHeaders}

import scala.collection.JavaConversions._

package object nettosphere {

  def loadFromHocon(hocon: Hocon): Config = nettosphere.hocon.loadRoot(hocon)

  def getNettosphere(config: Config)
      (implicit actorSystem: ActorSystem): Nettosphere =
    new Nettosphere.Builder().config(getNettosphereConfig(config)).build()


  def getNettosphereConfig(config: Config)
      (implicit actorSystem: ActorSystem): NettosphereConfig = {

    import actorSystem.dispatcher

    val builder =
      new NettosphereConfig.Builder()
        .host(config.host)
        .port(config.port)

    val channelSelector = getChannelSelector(config.channelSelectors)

    for (endpoint <- config.endpoints) {
      endpoint.`type` match {

        case EndpointType.Push =>
          builder.resource(endpoint.path, PushHandler())

        case EndpointType.Atmosphere =>
          builder.resource(endpoint.path,
            SidepushAtmosphereHandler(channelSelector))

        case EndpointType.SocketIO =>
          builder.resource(endpoint.path,
            SidepushSocketIOAtmosphereHandler(channelSelector))
      }
    }

    builder.build()
  }

  def getChannelSelector(xs: Seq[core.ChannelSelector])
      (implicit actorSystem: ActorSystem) = {

    import actorSystem.dispatcher

    new atmosphere.ChannelSelector {

      override def selectChannels(request: AtmosphereRequest): ChannelSelection =

        ChannelFutures(xs.map({

          case x: HttpChannelSelector =>
            val headers = filterHeaders(request, x.headerForwarding)
            val pipeline = addHeaders(headers) ~> sendReceive ~> unmarshal[String]
            pipeline.apply(Get(x.url)).map(Channel).map(Seq(_))
        }))
    }
  }

  def filterHeaders(request: AtmosphereRequest, include: HttpHeaderForwarding):
      List[HttpHeader] = {

    val headers = request.headersMap.toList.map({
      case (k, v) => RawHeader(k, Option(v).getOrElse(""))
    })

    sidepush.atmosphere.logger.info("#" + request.headersMap)

    val cookies = request.getCookies.map(c => HttpCookie(c.getName, c.getValue))

    val cookieHeaderOption =
      if (include.allCookies) None
      else Some(HttpHeaders.Cookie(cookies.filter(c => include.cookie(c.name))))

    headers.filter(h => include.header(h.name)) ++ cookieHeaderOption
  }

  def readAll(reader: BufferedReader): String =
    Stream.continually(reader.readLine()).takeWhile(_ != null).mkString("\n")
}
