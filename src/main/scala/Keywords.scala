import scala.collection.immutable.HashMap

/**
  * Created by aphex on 7/7/16.
  */

object Suspicion {
  val HIGH = 10
  val MED = 5
  val LOW = 1
}

/** "Younger respondents reported that advertisements sometimes used words and phrases that
  * signaled their age, including: fresh meat, young, virgin, prime, coochie (shaved), non-pro,
  * new, barely legal/18, college student/girl, lovely, daddy’s little girl, sweet, 1986 Firebird,
  * new in the life, liked girls, youthful, and fantasy."
  */

object Keywords {

  import Suspicion._

  val keywords: HashMap[String, Int] = HashMap(
    "fresh meat" -> HIGH,
    "young" -> LOW,
    "virgin" -> MED,
    "prime" -> HIGH,
    "coochie (shaved)" -> HIGH,
    "non-pro" -> HIGH,
    "new" -> MED,
    "barely legal" -> MED,
    "college student" -> LOW,
    "college girl" -> LOW,
    "college boy" -> LOW,
    "lovely" -> LOW,
    "daddy's little girl" -> HIGH,
    "sweet" -> LOW,
    "new in the life" -> HIGH,
    "youthful" -> MED,
    "fantasy" -> MED)
}
