package com.sfxcode.sapphire.extension.control

import javafx.scene.control.{Control, Skin}

import com.sfxcode.sapphire.core.value.FXBean
import com.sfxcode.sapphire.extension.skin.DualDataListViewSkin

import scalafx.beans.property.ObjectProperty
import scalafx.collections.ObservableBuffer


class DualDataListView [S<:AnyRef] extends Control {

  val items =  ObjectProperty[ObservableBuffer[FXBean[S]]](this, "dualListViewItems", ObservableBuffer[FXBean[S]]())

  val leftDataListView = new DataListView[S]()

  val rightDataListView = new DataListView[S]()


  protected override def createDefaultSkin: Skin[DualDataListView[S]] = {
    new DualDataListViewSkin[S](this)
  }

}
