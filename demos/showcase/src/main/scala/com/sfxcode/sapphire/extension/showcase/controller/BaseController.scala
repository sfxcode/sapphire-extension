package com.sfxcode.sapphire.extension.showcase.controller

import com.sfxcode.sapphire.core.application.ApplicationEnvironment
import com.sfxcode.sapphire.core.controller.ViewController
import com.sfxcode.sapphire.extension.showcase.ApplicationController

trait BaseController extends ViewController {

  def applicationController: ApplicationController = ApplicationEnvironment.applicationController[ApplicationController]

  def showcaseController: ShowcaseViewController = applicationController.showcaseController

  def updateShowcaseContent(controller: ViewController): Unit =
    showcaseController.updateShowcaseContent(controller)
}
