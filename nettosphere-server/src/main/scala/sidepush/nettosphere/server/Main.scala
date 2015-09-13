package sidepush.nettosphere
package server

import java.nio.file.Paths

import akka.actor.ActorSystem
import com.typesafe.config.ConfigRenderOptions
import edu.gatech.gtri.typesafeconfigextensions.factory.ConfigFactory._

object Main {

  def main(args: Array[String]) {
    val config = loadConfig
    implicit val actorSystem = ActorSystem()
    getNettosphere(config).start()
  }

  def loadConfig = {
    val hocon = loadHocon
    logger.info(s"\n\n${hocon.root.render(ConfigRenderOptions.defaults.setJson(false))}\n")
    val config = loadFromHocon(hocon)
    logger.info(s"\n\n${config.toString}\n")
    config
  }

  def loadHocon = emptyConfigFactory
    .bindDefaults()
    .withSources(
      classpathResource("reference"),
      classpathResource("application"),
      configFile().byPath(Paths.get("local.conf")),
      configFile().byKey("side-push.config.file"),
      systemProperties()
    ).fromLowestToHighestPrecedence()
    .load()
}
