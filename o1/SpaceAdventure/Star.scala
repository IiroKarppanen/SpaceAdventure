package o1.SpaceAdventure

import scala.collection.mutable.Map

/** The class `Area` represents locations in a text adventure game world. A game world
  * consists of areas. In general, an “area” can be pretty much anything: a room, a building,
  * an acre of forest, or something completely different. What different areas have in
  * common is that players can be located in them and that they can have exits leading to
  * other, neighboring areas. An area also has a name and a description.
  * @param name         the name of the area
  * @param description  a basic description of the area (typically not including information about items) */
class Star(var name: String, var description: String):

  private val neighbors = Map[String, Star]()
  private val items = Map[String, Item]()
  private var searchableObject: Option[(String, String)] = None


  /** Returns the area that can be reached from this area by moving in the given direction. The result
    * is returned in an `Option`; `None` is returned if there is no exit in the given direction. */
  def neighbor(direction: String) = this.neighbors.get(direction)

  /** Adds an exit from this area to the given area. The neighboring area is reached by moving in
    * the specified direction from this area. */
  def setNeighbor(direction: String, neighbor: Star): Unit =
    this.neighbors += direction -> neighbor

  /** Adds exits from this area to the given areas. Calling this method is equivalent to calling
    * the `setNeighbor` method on each of the given direction–area pairs.
    * @param exits  contains pairs consisting of a direction and the neighboring area in that direction
    * @see [[setNeighbor]] */
  def setNeighbors(exits: Vector[(String, Star)]): Unit =
    this.neighbors ++= exits


  def fullDescription =
    val contentsList =
      if this.searchableObject.isEmpty then
        if this.listItems().isEmpty then ""
        else
          "\n\nYou see here: " + this.listItems().keys.mkString("\n")

      else
        "\nYou see here: " + this.searchableObject.head._1

    val exitList = "\n\nStars withing warp drive range: \n" + this.neighbors.keys.mkString("\n")
    s"You are in ${this.name}. Scan the star system for further information." + contentsList + exitList


  /** Returns a single-line description of the area for debugging purposes. */
  override def toString = this.name + ": " + this.description.replaceAll("\n", " ").take(150)


  /** Places an item in the area so that it can be, for instance, picked up. */
  def addItem(item: Item): Unit =
    this.items.put(item.name, item)

  def addSearchableObject(objectToAdd: (String, String)) = this.searchableObject = Some(objectToAdd)

  def returnObject() = searchableObject

  def listItems() = items

  /** Determines if the area contains an item of the given name. */
  def contains(itemName: String) = this.items.contains(itemName)

  /** Removes the item of the given name from the area, assuming an item with that name
    * was there to begin with. Returns the removed item wrapped in an `Option` or `None`
    * in the case there was no such item present. */
  def removeItem(itemName: String) =
    this.items.remove(itemName)

  def removeObject() = this.searchableObject = None

end Star

