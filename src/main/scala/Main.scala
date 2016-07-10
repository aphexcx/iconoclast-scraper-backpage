import akka.actor.{ActorSystem, Props}
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Document

import scala.language.postfixOps
import scala.util.Try

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

    val validAds: Stream[Try[Document]] = urls.toStream map (s => Try(mainBrowser.get(s))) filter (_.isSuccess)

    validAds flatMap (_.get >> elementList("div.galleryHeader") >> attr("href")("a"))
  }
  val system = ActorSystem("AdExtractors")

  def getCities: List[String] = {
    val browser = JsoupBrowser()
    val doc = browser.get(HOME)

    doc >> elementList("a") >> attr("href")("a") filterNot (_.contains("/classifieds/")) filterNot (_.contains("my.backpage.com"))
  }

  allAdsInTheWorld.foreach(url =>
    system.actorOf(Props[AdExtractor], name = url.filter(_ != '/')) ! AdUrl(url)
  )
}
