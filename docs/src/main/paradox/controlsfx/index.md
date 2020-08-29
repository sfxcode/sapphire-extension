# ControlsFX

ControlsFX is used in several parts of sapphire-extensions.

[Github Source](https://github.com/controlsfx/controlsfx)

[Features](https://github.com/controlsfx/controlsfx/wiki/ControlsFX-Features)

## Property resolver for ControlsFX controls

Only Rating is supported at this time.

ExtensionResolver should be added in the Application:

@@snip [Application.scala](../../../../../demos/showcase/src/main/scala/com/sfxcode/sapphire/extension/showcase/Application.scala) {#ExtensionResolver}

Including of custom controls in sapphire-core can be done like this:

@@snip [ExtensionResolver.scala](../../../../../src/main/scala/com/sfxcode/sapphire/extension/scene/ExtensionResolver.scala) {#NodePropertyResolving}

## PropertySheet

@@include[property_sheet_basic](../includes/property_sheet_basic.md)

@@@ index

- [property_sheet](property_sheet.md)

@@@
