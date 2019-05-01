package com.sfxcode.sapphire.extension.showcase.controller

import com.sfxcode.sapphire.core.controller.ViewController

class AbstractBaseController extends ViewController {

  def mainWindowController: MainWindowController = {
    parent.asInstanceOf[MainWindowController]
  }

  def workspaceManager = mainWindowController.workspaceManager

}
