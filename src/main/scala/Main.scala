import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._

import scala.language.postfixOps

/**
  * Created by aphex on 7/1/16.
  */
object Main extends App {
  val browser = JsoupBrowser()
  val HOME = "http://sfbay.backpage.com/FemaleEscorts/two-girlsdouble-the-actioncall-if-your-alone-or-with-a-friend/33710143"
  val doc = browser.get(HOME)

  // Extract the image Urls
  val imageUrls: List[String] = doc >> elementList("ul#viewAdPhotoLayout") >> elementList("li") flatMap (_ >> elementList("img")) flatMap (_ >> attr("src")("img"))

  val age: Int = (doc >> element("p.metaInfoDisplay") text) filter (_.isDigit) toInt

  val title: String = doc >> element("div#postingTitle") >> element("a.h1link") text

  val text: String = doc >> element("div.postingBody") text

  println()

}
