package com.sfxcode.sapphire.extension.report

import com.sfxcode.sapphire.core.value.{ BeanConversions, FXBean }
import javafx.collections.ObservableList
import net.sf.jasperreports.engine.{ JRField, JRRewindableDataSource }

case class FXBeanDataSource[T <: AnyRef](dataList: List[FXBean[T]]) extends JRRewindableDataSource with Serializable {
  private var index = -1

  override def moveFirst(): Unit = index = -1

  override def next(): Boolean = {
    index = index + 1
    dataList.nonEmpty && index < dataList.size
  }

  override def getFieldValue(jrField: JRField): AnyRef = {
    val fieldName: String = jrField.getName
    val bean: FXBean[_] = dataList(index)

    bean.getValue(fieldName).asInstanceOf[AnyRef]
  }
}

object FXBeanDataSource extends BeanConversions {

  def fromList[T <: AnyRef](dataList: List[T]): FXBeanDataSource[T] =
    new FXBeanDataSource[T](dataList.map(item => FXBean[T](item)))

  def fromObservableList[T <: AnyRef](dataList: ObservableList[FXBean[T]]): FXBeanDataSource[T] =
    fromList(dataList.toList)
}
