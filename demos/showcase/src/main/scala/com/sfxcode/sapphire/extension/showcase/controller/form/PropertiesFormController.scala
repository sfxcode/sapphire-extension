package com.sfxcode.sapphire.extension.showcase.controller.form

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.layout.AnchorPane

import com.sfxcode.sapphire.core.value.{ FXBean, FXBeanAdapter, KeyBindings }
import com.sfxcode.sapphire.extension.showcase.controller.AbstractBaseController
import com.sfxcode.sapphire.extension.showcase.model.{ Person, PersonDatabase }
import com.sfxcode.sapphire.extension.property.BeanItems
import org.controlsfx.control.PropertySheet

import scala.util.Random

class PropertiesFormController extends AbstractBaseController {

  @FXML
  var propPane: AnchorPane = _

  val random = new Random()
  val propertySheet = new PropertySheet()

  lazy val adapter = FXBeanAdapter[Person](this)

  val beanItems = BeanItems()

  override def didGainVisibilityFirstTime() {
    super.didGainVisibilityFirstTime()

    beanItems.addItem("name", "Name", "Basic", "Name")
    beanItems.addItem("age", "Age", "Basic", "Age", editable = false)
    beanItems.addItem("isActive", "Active", "Extended", "Active")
    beanItems.addItem("registered", "Registered", "Extended", "Registered")

    propPane.getChildren.add(propertySheet)

    val bindings = KeyBindings()
    bindings.add("person", "Person ${_self.name()} with age of ${_self.age()} is active: ${_self.isActive()} ${sf:dateString(_self.registered())}")
    adapter.addBindings(bindings)

    setRandomPerson()

  }

  override def didGainVisibility() {
    super.didGainVisibility()
    setRandomPerson()
  }

  def actionChangePerson(event: ActionEvent): Unit = {
    setRandomPerson()
  }

  def setRandomPerson(): Unit = {
    val person: FXBean[Person] = PersonDatabase.testPerson(random.nextInt(100))
    adapter.set(person)

    beanItems.updateBean(person)
    propertySheet.getItems.setAll(beanItems.getItems)

  }

}
