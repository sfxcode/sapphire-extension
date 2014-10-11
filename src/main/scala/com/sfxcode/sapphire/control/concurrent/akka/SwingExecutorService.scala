package com.sfxcode.sapphire.control.concurrent.akka

import javax.swing.SwingUtilities

/**
 * Created by tom on 11.10.14.
 */
object SwingExecutorService extends GUIExecutorService {
  override def execute(command: Runnable) = SwingUtilities.invokeLater(command)
}
