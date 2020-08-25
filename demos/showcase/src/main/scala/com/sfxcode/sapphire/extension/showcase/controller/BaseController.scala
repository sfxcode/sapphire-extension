package com.sfxcode.sapphire.extension.showcase.controller

import com.sfxcode.sapphire.core.controller.ViewController
import com.sfxcode.sapphire.extension.showcase.ApplicationController

trait BaseController extends ViewController {

  // load applicationController by CDI
  def applicationController: ApplicationController = {
    val result = getBean[ApplicationController]()
    result
  }

  def showcaseController: ShowcaseViewController = applicationController.showcaseController

  def updateShowcaseContent(controller: ViewController): Unit =
    showcaseController.updateShowcaseContent(controller)
}
