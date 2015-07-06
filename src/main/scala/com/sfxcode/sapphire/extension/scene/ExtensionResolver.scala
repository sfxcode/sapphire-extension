package com.sfxcode.sapphire.extension.scene

import javafx.scene.Node

import com.sfxcode.sapphire.core.controller.ApplicationEnvironment
import com.sfxcode.sapphire.core.scene.NodePropertyResolving
import org.controlsfx.control.Rating

import scalafx.Includes._
import scalafx.beans.property.Property


class ExtensionResolver extends NodePropertyResolving{

  def resolve(node: Node): Option[Property[_, _ <: Any]] = {
    node match {
      case rating: Rating => Some(rating.ratingProperty())
      case _ => None
    }
  }
}

object ExtensionResolver {

  def apply():ExtensionResolver = new ExtensionResolver()

  def add() = ApplicationEnvironment.nodePropertyResolver.addResolver(ExtensionResolver())
}
