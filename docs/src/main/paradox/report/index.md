# Reports

For Report Generation [JasperReports](https://de.wikipedia.org/wiki/JasperReports) can be used.
It is based on the [itext library](https://de.wikipedia.org/wiki/IText).

For PDF reports the PdfExporter can be used.
As DataSource implementation is a FXBeanDataSource available.

As visual report editor you can use [Jaspersoft Studio (Community Edition)](https://community.jaspersoft.com/project/jaspersoft-studio/releases).

## DataSource

FXBeanDataSource wraps Beans in [FXBeans](https://sfxcode.github.io/sapphire-core/detail/fxbean.html) for building a rewindable JasperReports DataSource.

In the Templates (.jrxml) items can be accessed like maps.

## PDFExporter

PDFExporter simplifies the export to PDF task. You need only a JasperReports File (compiled or uncompiled).

* Report Parameter are optional by creation a Map of key/values.
* DataSource is optional (use a FXBeanDataSource if needed)
* optional exporter / report configuration

### Example

@@snip [PersonTableController.scala](../../../../../demos/showcase/src/main/scala/com/sfxcode/sapphire/extension/showcase/controller/table/PersonTableController.scala) {#Export}




