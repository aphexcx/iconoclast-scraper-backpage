import AdJsonProtocol._
import akka.actor.ActorSystem
import spray.client.pipelining._
import spray.http._
import spray.httpx.SprayJsonSupport._

import scala.concurrent.Future
import scala.concurrent.duration._

object Api {
  lazy val IN_DOCKER: Boolean = !System.getProperty("os.name").contains("Mac OS X")

  val apiLocation = if (IN_DOCKER) {
    "http://api:9000/api"
  } else {
    "http://localhost:9000/api"
  }
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

  //  def postImage(image: Image): Future[HttpResponse] = pipeline(Post(s"$apiLocation/image", image))

}
