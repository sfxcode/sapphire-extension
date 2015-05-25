package com.sfxcode.sapphire.extension.concurrent.akka

import javax.swing.SwingUtilities

object SwingExecutorService extends GUIExecutorService {
  override def execute(command: Runnable) = SwingUtilities.invokeLater(command)
}
