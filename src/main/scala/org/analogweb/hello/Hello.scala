package org.analogweb.hello

import java.net.URI
import org.analogweb.core.Servers
import org.analogweb.scala.Analogweb
import org.analogweb.scala.Resolvers
import org.analogweb.scala.Responses._
import org.analogweb.scala.Request

class Hello extends Analogweb with Resolvers {

  val user: Request => User = { implicit r =>
    User(parameter.of("n").getOrElse("Anonymous"))
  }
  
  get("/ping") { r =>
    "PONG!"
  }

  get("/helloworld") { implicit r =>
    s"Hello ${mapping.to[User](user).name} World!"
  }

  post("/upload") { implicit r =>
    multipart.as[java.io.InputStream]("filedata").map { is =>
      scala.io.Source.fromInputStream(is).getLines().mkString("\n")
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

object Runner {
  def main(args: Array[String]): Unit = {
    Servers.create("http://localhost:8080").run
  }
}
