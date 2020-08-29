# ViewController Extensions

All ViewController Extensions are subclasses of the sapphire-core [ViewController](https://sfxcode.github.io/sapphire-core/detail/view_controller.html).

All Controllers are abstract by default. Most of this VievController are used for viewing/editing data mapped in [FXBeans]().
Dealing with Generics is done by defining a class tag.

Example for implementing the definition for a Person class:

```scala

 type R = Person

 def ct: ClassTag[Person] = classTag[R]
 
```

## Types

* BaseDataTableController (adds easy table implementation with sorting, filtering, ...)
* BaseEditorController (updateBean, save, revert, ...)
* BaseTabController (Tab Selection, ...)
* BaseMasterController, BaseDetailController (simple master detail handling with navigation)

## Example

Shows usage of Filters in action.

@@snip [FriendTableController.scala](../../../../../demos/showcase/src/main/scala/com/sfxcode/sapphire/extension/showcase/controller/table/FriendTableController.scala)

![friends_table.png](../images/friends_table.png)