package analogweb.example

import java.io.InputStream
import scala.io.Source
import scala.concurrent.Future
import analogweb._,json4s._
import org.analogweb.util.logging.Logs
import org.analogweb._,scala._

object HelloAnalogweb {

  val log = Logs.getLog("HelloAnalogweb")

  implicit val around = before { r => 
    log.debug("Before")
    pass()
  } :+ after {
    case r:Renderable => log.debug("After");r
  }

  val routes = get("/ping") {
    "PONG"
  }

  get("/file") { 
    Future {
      Source.fromFile(new java.io.File("/Users/yukio/l.txt")).getLines().mkString("\n")
    }
  }

  get("/calc") {
    for {
      one   <- Future(1) 
      two   <- Future(2) 
      three <- Future(3) 
      four  <- Future(4) 
    } yield "${one + two + three + four}"
  }

  get("/path/*") { implicit r =>
    context.as[RequestPath].map {p =>
      p.getActualPath
    }.getOrElse(NotFound)
  }

  def user: Request => User = { implicit r =>
    User(param("n"))
  }
  
  val validateParameter = before { implicit r =>
    parameter.asOption[String]("n").map(x => pass()).getOrElse(reject(BadRequest))
  }

  get("/helloworld") { implicit r =>
    s"Hello ${user.name} World!"
  }(around ++ validateParameter)

  get("/hello/{who}/world") { implicit r =>
    s"Hello ${param("who")} World!"
  }
  
  get("/agent") { implicit r =>
    s"Hello World ${r.headerOption("User-Agent").getOrElse("Unknown")}"
  }
  
  post("/upload") { implicit r =>
    multipart.as[InputStream]("filedata").map { is =>
      Source.fromInputStream(is).getLines().mkString("\n")
    }.getOrElse(BadRequest)
  }
  
  get("/json") { implicit r =>
    Ok(asJson(User("snowgooseyk")))
  }

  post("/json") { implicit r =>
    json.as[User].map(x => Ok(asJson(x))).getOrElse(BadRequest)
  }
}

case class User(name:String)
