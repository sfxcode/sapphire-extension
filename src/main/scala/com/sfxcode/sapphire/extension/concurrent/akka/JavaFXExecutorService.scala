package com.sfxcode.sapphire.extension.concurrent.akka

import javafx.application.Platform

/**
 * Created by tom on 11.10.14.
 */
object JavaFXExecutorService extends GUIExecutorService {
  override def execute(command: Runnable) = Platform.runLater(command)
}
