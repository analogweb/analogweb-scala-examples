package org.analogweb.hello

import java.net.URI

import org.analogweb.core.Servers

import com.google.inject._
import org.analogweb.core.DefaultApplicationProperties
import org.analogweb.guice.GuiceApplicationContext

object Runner {

  def main(args: Array[String]) = {
    val injector = Guice.createInjector(new HelloGuiceModule())
    val context = GuiceApplicationContext.context(injector)
    val props = DefaultApplicationProperties.defaultProperties()
    Servers.create(URI.create("http://localhost:8080"),props,context).run()
  }

} 

