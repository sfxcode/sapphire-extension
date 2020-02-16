package com.sfxcode.sapphire.extension.scene

import javafx.scene.Node

import com.sfxcode.sapphire.core.cdi.{ ApplicationEnvironment, BeanResolver }
import com.sfxcode.sapphire.core.scene.NodePropertyResolving
import org.controlsfx.control.Rating

import javafx.beans.property.Property

// #NodePropertyResolving
class ExtensionResolver extends NodePropertyResolving {

  def resolve(node: Node): Option[Property[_]] = {
    node match {
      case rating: Rating => Some(rating.ratingProperty())
      case _ => None
    }
  }
}
// #NodePropertyResolving

object ExtensionResolver extends BeanResolver {

  def apply(): ExtensionResolver = new ExtensionResolver()

  def add(): Unit = {
    val env = getBean[ApplicationEnvironment]()
    env.nodePropertyResolver.addResolver(ExtensionResolver())
  }
}
