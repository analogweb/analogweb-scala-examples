package analogweb.example

import scala.concurrent.Future
import analogweb._, circe._, io.circe._, generic.semiauto._
import org.analogweb._
import org.analogweb.core._
import org.analogweb.scala._

case class Member(name: String)

case class Message(message: String)

object HelloAnalogweb {

  def main(args: Array[String]) = http(
    "0.0.0.0",
    sys.props.get("http.port").getOrElse("8000")
  )(routes).run

  // JSON Encoder and Decoders.(circe)
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
          one   <- Future.successful(1)
          two   <- Future.successful(2)
          three <- Future.successful(3)
          four  <- Future.successful(4)
        } yield Ok(asText(s"${one + two + three + four}"))
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
      }(validateParameter) ++
      get("/hello/{who}/world") { implicit r =>
        s"Hello ${param("who")} World!"
      } ++
      get("/user-agent") { implicit r =>
        s"Hello World ${r.headerOption("User-Agent").getOrElse("Unknown")}"
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
