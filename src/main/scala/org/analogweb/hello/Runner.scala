package org.analogweb.hello

import java.net.URI
import com.google.inject._
import org.analogweb.core.Servers
import org.analogweb.core.DefaultApplicationProperties._
import org.analogweb.guice.GuiceApplicationContext._

object Runner {

  def main(args: Array[String]) = {
    val injector = Guice.createInjector(new HelloGuiceModule())
    val port = System.getProperty("http.port")//.getOrElse("9999")
    Servers.create(URI.create("http://localhost:"+port),defaultProperties(),context(injector)).run()
  }

} 

