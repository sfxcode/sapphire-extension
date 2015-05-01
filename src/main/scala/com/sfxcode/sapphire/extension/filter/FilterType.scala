package com.sfxcode.sapphire.extension.filter

object FilterType extends Enumeration {

  type FilterValue = Value
  val FilterEquals, FilterEqualsIgnoreCase, FilterContains, FilterContainsIgnoreCase = Value
}
