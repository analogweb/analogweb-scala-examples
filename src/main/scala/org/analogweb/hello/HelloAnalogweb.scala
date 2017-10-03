package analogweb.example

import java.io.InputStream
import scala.io.Source
import scala.concurrent.Future
import analogweb._, circe._, io.circe._, generic.semiauto._
import org.analogweb._
import org.analogweb.core._
import org.analogweb.scala._
import org.analogweb.util.logging.Logs

case class Member(name: String)

case class Message(message: String)

object HelloAnalogweb {

  val log = Logs.getLog("HelloAnalogweb")

  def main(args: Array[String]) = http("0.0.0.0", 8000)(routes).run

  implicit val around = before { r =>
    log.debug("Before")
    pass()
  } :+ after {
    case r: Renderable => log.debug("After"); r
  }

  implicit val memberDecoder: Decoder[Member]   = deriveDecoder[Member]
  implicit val memberEncoder: Encoder[Member]   = deriveEncoder[Member]
  implicit val messageEncoder: Encoder[Message] = deriveEncoder[Message]

  val validateParameter = before { implicit r =>
    parameter.asOption[String]("n").map(x => pass()).getOrElse(reject(BadRequest))
  }

  val routes =
    get("/ping") { _ =>
      "PONG"
    } ++
      head("/healthcheck") { _ =>
        Ok
      } ++
      get("/future") { _ =>
        for {
          one   <- Future(1)
          two   <- Future(2)
          three <- Future(3)
          four  <- Future(4)
        } yield Created(asText(s"${one + two + three + four}"))
      } ++
      get("/path/*") { implicit r =>
        context
          .as[RequestPath]
          .map { p =>
            p.getActualPath
          }
          .getOrElse(NotFound)
      } ++
      post("/form") { implicit r =>
        s"Hello ${user.name} World!"
      }(around ++ validateParameter) ++
      get("/hello/{who}/world") { implicit r =>
        s"Hello ${param("who")} World!"
      } ++
      get("/user-agent") { implicit r =>
        s"Hello World ${r.headerOption("User-Agent").getOrElse("Unknown")}"
      } ++
      post("/upload") { implicit r =>
        multipart
          .as[InputStream]("filedata")
          .map { is =>
            Source.fromInputStream(is).getLines().mkString("\n")
          }
          .getOrElse(BadRequest)
      } ++
      get("/json") { implicit r =>
        Ok(asJson(Message("Hello, World!")))
      } ++
      post("/json") { implicit r =>
        json.as[Member].map(x => Ok(asJson(x))).getOrElse(BadRequest)
      }

  def user: Request => Member = { implicit r =>
    Member(param("n"))
  }

}
