package analogweb.example

import analogweb._

object Runner {

  def main(args: Array[String]) = http("0.0.0.0",8000){
    HelloAnalogweb.routes
  }.run

} 

