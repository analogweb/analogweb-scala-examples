package org.analogweb.hello

import java.net.URI
import javax.inject.Inject

import com.google.inject._
import net.codingwell.scalaguice._
import org.analogweb.scala.Analogweb

class HelloAnalogwebWithGuice @Inject() (s: SayHello) extends Analogweb {
  get("/guice") {
    s.hello
  }
}

class SayHello {
  def hello = "hello!"
}

class HelloGuiceModule extends AbstractModule with ScalaModule {
  def configure {
    bind[SayHello]
    bind[HelloAnalogwebWithGuice]
  }
}
