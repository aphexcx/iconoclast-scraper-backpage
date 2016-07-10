import akka.actor.Actor
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Document

import scala.language.postfixOps

case class AdUrl(url: String)

class AdExtractor extends Actor {
  val browser = JsoupBrowser()
  implicit val ec = Main.system.dispatcher

  override def receive: Receive = {
    case AdUrl(url) => Api.postAd(extractAd(url)) onComplete (r => {
      println(r)
    })
  }

  def extractAd(url: String): Ad = {
    println("scraping..." + url)

    val doc: Document = browser.get(url)

    val imageUrls: List[String] = doc >> elementList("ul#viewAdPhotoLayout") >> elementList("li") flatMap (_ >> elementList("img")) flatMap (_ >> attr("src")("img"))

    val age: Int = (doc >> element("p.metaInfoDisplay") text) filter (_.isDigit) toInt

    val title: String = doc >> element("div#postingTitle") >> element("a.h1link") text

    val text: String = doc >> element("div.postingBody") text

    Ad(url, imageUrls, age, title, text)
  }
}
