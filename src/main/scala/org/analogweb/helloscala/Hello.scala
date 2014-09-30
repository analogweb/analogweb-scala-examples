package org.analogweb.helloscala

import java.net.URI
import org.analogweb.core.httpserver._
import org.analogweb.scala.Analogweb
import org.analogweb.scala.Resolvers
import org.analogweb.scala.Responses._
import org.analogweb.scala.Request
import org.analogweb.netty.HttpServers

class Hello extends Analogweb with Resolvers {

  val user: Request => User = { implicit r =>
    User(parameter.of("p").get)
  }
  
  def helloworld = get("/helloworld") { r =>
    "Hello World"
  }

  def hello = get("/hello") { implicit r =>
    s"Hello ${mapping.to[User](user)} Scala!"
  }

  def upload = post("/upload") { implicit r =>
    multipart.as[java.io.InputStream]("filedata").map { is =>
      scala.io.Source.fromInputStream(is).getLines().mkString("\n")
    }.getOrElse(BadRequest)
  }
  
  def helloJson = get("/helloJson") { implicit r =>
    Ok(asJson(User("snowgoose")))
  }

  def helloViaJson = post("/helloViaJson") { implicit r =>
    json.as[User].map(o => Ok(asJson(o))).getOrElse(BadRequest)
  }
}

case class User(val name: String)

object Run {
  def main(args: Array[String]): Unit = {
    HttpServers.create("http://localhost:8080").run
  }
}
