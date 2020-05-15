package com.sfxcode.sapphire.extension.report

import com.sfxcode.sapphire.core.value.FXBean
import net.sf.jasperreports.engine.{JRField, JRRewindableDataSource}

case class FXBeanDataSource(dataList: List[FXBean[_]]) extends JRRewindableDataSource with Serializable {
  private var index = -1

  override def moveFirst(): Unit = index = -1

  override def next(): Boolean = {
    index = index + 1
    dataList.nonEmpty && index < dataList.size
  }

  override def getFieldValue(jrField: JRField): AnyRef = {
    val fieldName: String = jrField.getName
    val bean: FXBean[_]   = dataList(index)

    bean.getValue(fieldName).asInstanceOf[AnyRef]
  }
}
