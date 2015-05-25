package com.sfxcode.sapphire.extension.control

import javafx.scene.control.{Control, Skin}

import com.sfxcode.sapphire.extension.skin.DualDataListViewSkin

import scalafx.beans.property.StringProperty


class DualDataListView [S<:AnyRef] extends Control {

  val leftDataListView = new DataListView[S]()

  val rightDataListView = new DataListView[S]()

  val cellProperty = StringProperty("${_self.toString()}")

  protected override def createDefaultSkin: Skin[DualDataListView[S]] = {
    new DualDataListViewSkin[S](this)
  }

}
