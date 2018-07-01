package com.sfxcode.sapphire.extension.demo

import javax.enterprise.context.ApplicationScoped
import javax.inject.Named

import com.sfxcode.sapphire.core.cdi.FXApp
import com.sfxcode.sapphire.core.controller.AppController
import com.sfxcode.sapphire.extension.demo.controller.MainWindowController
import com.sfxcode.sapphire.extension.scene.ExtensionResolver
import com.typesafe.scalalogging.LazyLogging

import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.stage.Stage

object Application extends FXApp {
  ExtensionResolver.add()

  override def applicationStage: Stage = new PrimaryStage {
    title = "Sapphire Demo App"
    scene = new Scene {
      minWidth = 800
      minHeight = 600
    }
  }
}

@Named
@ApplicationScoped
class ApplicationController extends AppController with LazyLogging {

  lazy val controller = getController[MainWindowController]()

  def applicationDidLaunch() {
    logger.debug("start " + this)
    replaceSceneContent(controller)
  }

  def replacePrimarySceneContent(): Unit = {
    val newMainWindowController = getController[MainWindowController]()
    replaceSceneContent(newMainWindowController)

  }


}

