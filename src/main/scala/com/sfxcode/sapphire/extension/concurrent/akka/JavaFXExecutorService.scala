package com.sfxcode.sapphire.extension.concurrent.akka

import javafx.application.Platform

object JavaFXExecutorService extends GUIExecutorService {
  override def execute(command: Runnable) = Platform.runLater(command)
}
