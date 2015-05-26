package org.analogweb.hello

import java.net.URI
import java.io.InputStream

import scala.io.Source

import org.analogweb.core.Servers
import org.analogweb.scala._
import org.analogweb.scala.Responses._

object Hello extends Analogweb with Resolvers {

  def main(args: Array[String]) = Servers.run()
  
  val userMapping: Request => User = { implicit r =>
    User(parameter.of("n").getOrElse("Anonymous"))
  }
  
  get("/ping") {
      "PONG!"
  }

  get("/helloworld") { implicit r =>
    s"Hello ${mapping.to[User](userMapping).name} World!"
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

case class User(val name: String)

