package sidepush.nettosphere

import com.typesafe.config.ConfigFactory

class ConfigTest extends org.scalatest.FreeSpec {

  "Nettosphere-lib Config" - {
    "Default config should be okay" in {
      loadFromHocon(ConfigFactory.parseResourcesAnySyntax("reference"))
    }
  }
}
