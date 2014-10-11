package com.sfxcode.sapphire.control.concurrent.akka

import java.util.Collections
import java.util.concurrent.{AbstractExecutorService, TimeUnit}

/**
 * Created by tom on 11.10.14.
 */
// First we wrap invokeLater/runLater as an ExecutorService
abstract class GUIExecutorService extends AbstractExecutorService {
  def execute(command: Runnable): Unit

  def shutdown(): Unit = ()

  def shutdownNow() = Collections.emptyList[Runnable]

  def isShutdown = false

  def isTerminated = false

  def awaitTermination(l: Long, timeUnit: TimeUnit) = true
}
