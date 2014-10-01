package org.analogweb.util.logging;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

class JulLogConfigImpl extends JulLogConfig {

  override def createLoggerInternal(name: String, manager: LogManager) = {
    val logger = Logger.getLogger(name)
    logger.setLevel(Level.INFO)
    logger.addHandler(createConsoleHandler)
    manager.addLogger(logger)
    logger
  }

  def createConsoleHandler = {
    val console = new ConsoleHandler()
    console.setFormatter(new JulLogFormatter())
    console.setLevel(Level.INFO)
    console
  }
}
