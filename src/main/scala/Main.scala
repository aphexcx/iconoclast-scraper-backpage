import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps

/** A Backpage scraper for Iconoclast.
  *
  * "Among those that report being advertised online, Backpage is the most common website used -
  * with almost half reporting they were advertised on Backpage."
  *
  * -- A REPORT ON THE USE OF TECHNOLOGY TO RECRUIT, GROOM AND SELL DOMESTIC MINOR SEX TRAFFICKING VICTIMS
  * https://www.wearethorn.org/wp-content/uploads/2015/02/Survivor_Survey_r5.pdf
  *
  * Created by aphex on 7/1/16.
  */

object Main extends App {
  val HOME = "http://www.backpage.com"
  val cities: List[String] = getCities
  val categories: Seq[String] = Seq("MaleEscorts", "FemaleEscorts")
  val mainBrowser = JsoupBrowser()

  val allAdsInTheWorld: Stream[String] = {
    val urls: List[String] =
      for {city <- cities
           category <- categories
           page <- 1 to 100
      } yield s"$city$category/?layout=gallery&page=$page"

    urls.toStream flatMap {
      mainBrowser.get(_) >> elementList("div.galleryHeader") >> attr("href")("a")
    }
  }

  def getCities: List[String] = {
    val browser = JsoupBrowser()
    val doc = browser.get(HOME)

    doc >> elementList("a") >> attr("href")("a") filterNot (_.contains("/classifieds/")) filterNot (_.contains("my.backpage.com"))
  }

  def extractAd(url: String): Ad = {
    val browser = JsoupBrowser()
    val doc = browser.get(url)

    val imageUrls: List[String] = doc >> elementList("ul#viewAdPhotoLayout") >> elementList("li") flatMap (_ >> elementList("img")) flatMap (_ >> attr("src")("img"))

    val age: Int = (doc >> element("p.metaInfoDisplay") text) filter (_.isDigit) toInt

    val title: String = doc >> element("div#postingTitle") >> element("a.h1link") text

    val text: String = doc >> element("div.postingBody") text

    Ad(imageUrls, age, title, text)
  }

  Api.postAd(extractAd(allAdsInTheWorld.head)) onComplete (r => {
    println(r)
  })

}
