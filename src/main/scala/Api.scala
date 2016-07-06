import akka.actor.ActorSystem
import spray.client.pipelining._
import spray.http._
import spray.httpx.SprayJsonSupport._
import spray.json.DefaultJsonProtocol

import scala.concurrent.Future
import scala.concurrent.duration._

case class Ad(imageUrls: List[String], age: Int, title: String, text: String)

object AdJsonProtocol extends DefaultJsonProtocol {
  implicit val adFormat = jsonFormat4(Ad)
}

import AdJsonProtocol._

object Api {
  val apiLocation = "http://localhost:9000/api"
  val timeout = 5.seconds

  //Spray needs an implicit ActorSystem and ExecutionContext
  implicit val system = ActorSystem("apiClient")

  import system.dispatcher

  val pipeline: HttpRequest => Future[HttpResponse] = sendReceive
  //    (
  //    addHeader("X-My-Special-Header", "fancy-value")
  //    ~> addCredentials(BasicHttpCredentials("bob", "secret"))
  //      ~> encode(Gzip)
  //      ~> sendReceive
  //      ~> decode(Deflate)
  //    ~> unmarshal[OrderConfirmation]
  //    )

  def postAd(ad: Ad): Future[HttpResponse] = pipeline(Post(s"$apiLocation/ad", ad))

}
