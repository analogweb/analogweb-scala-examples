package org.analogweb.hello

import java.io.InputStream

import scala.io.Source
import scala.concurrent.Future

import org.analogweb.RequestPath
import org.analogweb.scala.{Analogweb,Request}
import org.analogweb.scala.Execution.Implicits._

object HelloAnalogweb extends Analogweb {

  get("/ping") {
    "PONG"
  }

  get("/calc") {
    for {
      one   <- Future(1) 
      two   <- Future(2) 
      three <- Future(3) 
      four  <- Future(4) 
    } yield s"${one + two + three + four}"
  }

  get("/path/*") { implicit r =>
    context.as[RequestPath].map {p =>
      p.getActualPath
    }.getOrElse(NotFound)
  }

  def user: Request => User = { implicit r =>
    User(parameter.of("n").getOrElse("Analogweb"))
  }
  
  get("/helloworld") { implicit r =>
    s"Hello ${user.name} World!"
  }

  get("/hello/{who}/world") { implicit r =>
    s"Hello ${path.of("who").getOrElse("Anonymous")} World!"
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
    json.as[User].map(o => Ok(asJson(o))).getOrElse(BadRequest)
  }
}

case class User(name:String)
