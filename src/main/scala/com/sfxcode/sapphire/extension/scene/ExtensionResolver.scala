package com.sfxcode.sapphire.extension.scene

import com.sfxcode.sapphire.core.application.ApplicationEnvironment
import javafx.scene.Node
import com.sfxcode.sapphire.core.scene.NodePropertyResolving
import org.controlsfx.control.Rating
import javafx.beans.property.Property

// #NodePropertyResolving
class ExtensionResolver extends NodePropertyResolving {

  def resolve(node: Node): Option[Property[_]] =
    node match {
      case rating: Rating => Some(rating.ratingProperty())
      case _ => None
    }
}
// #NodePropertyResolving

object ExtensionResolver {

  def apply(): ExtensionResolver = new ExtensionResolver()

  def add(): Unit =
    ApplicationEnvironment.nodePropertyResolver.addResolver(ExtensionResolver())
}
