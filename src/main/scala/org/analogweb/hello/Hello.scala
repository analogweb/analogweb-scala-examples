package org.analogweb.hello

import java.net.URI
import java.io.InputStream

import scala.io.Source
import scala.concurrent.Future

import org.analogweb.RequestPath
import org.analogweb.core.Servers
import org.analogweb.scala.{Analogweb,Request}
import org.analogweb.scala.Execution.Implicits._

import java.net.URI
import javax.inject.Inject
import com.google.inject._
import net.codingwell.scalaguice._
import org.analogweb.core.DefaultApplicationProperties
import org.analogweb.guice.GuiceApplicationContext

object Hello extends Analogweb {

  def main(args: Array[String]) = {
    val injector = Guice.createInjector(new MyModule())
    val context = GuiceApplicationContext.context(injector)
    val props = DefaultApplicationProperties.defaultProperties()
    Servers.create(URI.create("http://localhost:8080"),props,context).run()
  }
  
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
    context.as[RequestPath].get.getActualPath 
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

case class User(val name: String)

class SayHello {
  def hello = "hello!"
}

class UsingGuice @Inject() (s: SayHello) extends Analogweb {
  get("/guice") {
    s.hello
  }
}

class MyModule extends AbstractModule with ScalaModule {
  def configure {
    bind[SayHello]
    bind[UsingGuice]
  }
}
