package com.sfxcode.sapphire.extension.showcase

import com.sfxcode.sapphire.core.application.FXApp
import com.sfxcode.sapphire.core.controller.DefaultWindowController
import com.sfxcode.sapphire.extension.scene.ExtensionResolver
import com.sfxcode.sapphire.extension.showcase.controller.ShowcaseViewController
import com.typesafe.scalalogging.LazyLogging
import javax.enterprise.context.ApplicationScoped
import javax.inject.Named

object Application extends FXApp {
  override def title: String = "Sapphire Extension Showcase"
}

@Named
@ApplicationScoped
class ApplicationController extends DefaultWindowController with LazyLogging {

  lazy val showcaseController: ShowcaseViewController = getController[ShowcaseViewController]()

  // #ExtensionResolver
  def applicationDidLaunch() {
    logger.debug("start " + this)
    ExtensionResolver.add()
    replaceSceneContent(showcaseController)
  }
  // #ExtensionResolver

}

