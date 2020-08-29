# PropertySheet

@@include[property_sheet_basic](../includes/property_sheet_basic.md)

## BeanItems

BeanItems is the sapphire implementation of Items in ControlsFX PropertySheet.

### Usage

* Use [FXBeanAdapter](https://sfxcode.github.io/sapphire-core//detail/fxbean_adapter.html) for Bean Bindings
* Init [PropertySheet](https://github.com/controlsfx/controlsfx/wiki/ControlsFX-Features#propertysheet)
* Init BeanItems

@@snip [PropertiesFormController.scala](../../../../../demos/showcase/src/main/scala/com/sfxcode/sapphire/extension/showcase/controller/form/PropertiesFormController.scala) { #BeanItemsInit }

* Init BeanItems Bindings in the didGainVisibilityFirstTime LifeCycle of the [ViewController](https://sfxcode.github.io/sapphire-core//detail/view_controller.html)

@@snip [PropertiesFormController.scala](../../../../../demos/showcase/src/main/scala/com/sfxcode/sapphire/extension/showcase/controller/form/PropertiesFormController.scala) { #BeanItems }

* Update BeanItems Data

@@snip [PropertiesFormController.scala](../../../../../demos/showcase/src/main/scala/com/sfxcode/sapphire/extension/showcase/controller/form/PropertiesFormController.scala) { #BeanItemsUpdate }


