package com.sfxcode.sapphire.extension.report

import better.files.{File, Resource}
import com.sfxcode.sapphire.extension.test.{Person, PersonDatabase}
import com.typesafe.scalalogging.LazyLogging
import org.specs2.mutable.Specification

class PdfExporterSpec extends Specification with LazyLogging {
  sequential

  "PDFReport" should {

    "export simple Report" in {
      val exporter     = PdfExporter(Resource.getUrl("report/TestReport.jasper"))
      val exportResult = exporter.exportReport(File.newTemporaryFile())
      println(exportResult.exportFile)
      exportResult.completed must beTrue
    }

    "export Report with Parameter" in {
      val exporter     = PdfExporter(Resource.getUrl("report/test.jrxml"))
      val exportResult = exporter.exportReport(File.newTemporaryFile(), Map("test" -> "My Test"))
      println(exportResult.exportFile)
      exportResult.completed must beTrue
    }

    "export Report with DataSource" in {
      val exporter = PdfExporter(Resource.getUrl("report/Simple_Blue.jasper"))
      val exportResult = exporter.exportReport(
        File.newTemporaryFile(),
        Map("test" -> "My Test"),
        FXBeanDataSource(PersonDatabase.testFriends)
      )
      println(exportResult.exportFile)
      exportResult.completed must beTrue
    }

  }

}
