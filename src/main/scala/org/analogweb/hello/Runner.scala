package org.analogweb.hello

import java.net.URI
import com.google.inject._
import org.analogweb.core.Servers
import org.analogweb.core.DefaultApplicationProperties._
import org.analogweb.guice.GuiceApplicationContext._

object Runner {

  def main(args: Array[String]) = {
    val injector = Guice.createInjector(new HelloGuiceModule())
    val port = sys.props.get("http.port").getOrElse(sys.env.get("PORT").getOrElse("9999"))
    val uri = URI.create("http://localhost:"+port)
    println(uri)
    Servers.create(uri,defaultProperties(),context(injector)).run()
  }

} 

