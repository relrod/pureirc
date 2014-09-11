package pureirc

import scalaz._, Scalaz._
import scalaz.effect.SafeApp
import scalaz.effect.IO._

object Foo extends SafeApp {
  override def runc = for {
    _ <- putStrLn("Hello world!")
  } yield ()
}
