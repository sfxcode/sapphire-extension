package com.sfxcode.sapphire.extension.concurrent.akka

import java.util.concurrent.{ExecutorService, ThreadFactory}

import akka.dispatch.{ExecutorServiceConfigurator, DispatcherPrerequisites, ExecutorServiceFactory}
import com.typesafe.config.Config

/**
 * Created by tom on 11.10.14.
 */
// Then we create an ExecutorServiceConfigurator so that Akka can use our SwingExecutorService for the dispatchers
class SwingEventThreadExecutorServiceConfigurator(config: Config, prerequisites: DispatcherPrerequisites) extends ExecutorServiceConfigurator(config, prerequisites) {
  private val f = new ExecutorServiceFactory {
    def createExecutorService: ExecutorService = SwingExecutorService
  }

  def createExecutorServiceFactory(id: String, threadFactory: ThreadFactory): ExecutorServiceFactory = f
}
