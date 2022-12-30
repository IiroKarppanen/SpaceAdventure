package o1.SpaceAdventure

import scala.collection.mutable.{Buffer, Map}
import scala.math.*
import scala.util.Random

/** A `Player` object represents a player character controlled by the real-life user
  * of the program.
  *
  * A player object’s state is mutable: the player’s location and possessions can change,
  * for instance.
  *
  * @param startingArea  the player’s initial location */
class Player(startingArea: Star):

  private var currentLocation = startingArea        // gatherer: changes in relation to the previous location
  private var quitCommandGiven = false              // one-way flag
  private var planetFound = false
  private val cargo = Map[String, Item]()     // container of all the items that the player has
  private var shipSystems = Buffer[Int](100, 100, 100) // Condition for Shields, Sensors, Engines

  def shipCondition = shipSystems

  /** Determines if the player has indicated a desire to quit the game. */
  def hasQuit = this.quitCommandGiven

  /** Returns the player’s current location. */
  def location = this.currentLocation

  def hasWon = this.planetFound

  /** Attempts to move the player in the given direction. This is successful if there
    * is an exit from the player’s current location towards the direction name. Returns
    * a description of the result: "You go DIRECTION." or "You can't go DIRECTION." */
  def go(direction: String) =
    val destination = this.location.neighbor(direction)


    if destination.isDefined && direction != "trappist-1" then
      this.currentLocation = destination.getOrElse(this.currentLocation)
      "You go " + direction + "."
    else if destination.isDefined && direction == "trappist-1" then
      if inventory.contains("warp drive upgrade") then
        this.currentLocation = destination.getOrElse(this.currentLocation)
        "You go " + direction + "."

      else
        "You need warp drive upgrade to go to trappist-1"
    else
      "You can't go " + direction + "."

  /** Causes the player to rest for a short while (this has no substantial effect in game terms).
    * Returns a description of what happened. */
  def scan(): String =
    if this.location.name == "kepler 90" && !cargo.contains("long range sensors") then
      "No habitable planets found."
    else
      this.location.description

  def examine(itemName: String): String =
    cargo.get(itemName) match
      case None => "You don't have that item."
      case Some(item) => item.description

  def search() =
    var outcomeWithoutItem = ""
    var outcome = ""
    var objectToSearch = this.location.returnObject()

    objectToSearch match
      case None => outcome = "Nothing to search here"
      case Some(muuta) =>
        outcomeWithoutItem = muuta._2
        if this.location.listItems().isEmpty then
        outcome = outcomeWithoutItem
        else
          cargo += this.location.listItems().head._1 -> this.location.listItems().head._2
          outcome = s"You search the ${this.location.returnObject().head._1} and find ${this.location.listItems().head._1}."
          this.location.removeItem(this.location.listItems().head._1)

    this.location.removeObject()
    outcome


  /** Signals that the player wants to quit the game. Returns a description of what happened
    * within the game as a result (which is the empty string, in this case). */
  def quit() =
    this.quitCommandGiven = true
    ""


  /** Returns a brief description of the player’s state, for debugging purposes. */
  override def toString = "Now at: " + this.location.name


  /** Tries to pick up an item of the given name. This is successful if such an item is
    * located in the player’s current location. If so, the item is added to the player’s
    * inventory. Returns a description of the result: "You pick up the ITEM." or
    * "There is no ITEM here to pick up." */
  def get(itemName: String) =
    val received = this.location.removeItem(itemName)

    for newItem <- received do
      this.cargo.put(newItem.name, newItem)
    if received.isDefined then
      "You pick up the " + itemName + "."
    else
      "There is no " + itemName + " here to pick up."


  def shipIntact() = shipSystems.drop(1).forall(_ > 0)


  /** Determines whether the player is carrying an item of the given name. */
  def has(itemName: String) = this.cargo.contains(itemName)


  def use(itemName: String) =
    if itemName == "shield booster" then
      shipSystems(0) += 30
      "Your shields are stronger now."

    else if itemName == "repair kit" then
      //var toFix = shipSystems.find(_ == shipSystems.min).getOrElse(shipSystems.head)
      //toFix = 100
      println(shipSystems.find(_ == shipSystems.min))
      "You fixed the most damaged part of your ship."
    else
      "You can't use that."


  /** Tries to drop an item of the given name. This is successful if such an item is
    * currently in the player’s possession. If so, the item is removed from the
    * player’s inventory and placed in the area. Returns a description of the result
    * of the attempt: "You drop the ITEM." or "You don't have that!". */
  def drop(itemName: String) =
    val removed = this.cargo.remove(itemName)
    for oldItem <- removed do
      this.location.addItem(oldItem)
    if removed.isDefined then "You throw " + itemName + " out of the airlock." else "You don't have that!"


  /** Causes the player to list what they are carrying. Returns a listing of the player’s
    * possessions or a statement indicating that the player is carrying nothing. The return
    * value has the form "You are carrying:\nITEMS ON SEPARATE LINES" or "You are empty-handed."
    * The items are listed in an arbitrary order. */
  def inventory =
    if this.cargo.isEmpty then
      "You haven't found anything yet"
    else
      "Item's in ship cargo haul:\n" + this.cargo.keys.mkString("\n")

  def help = "Commands:\ngo [Star System Name] - travel to that system\nscan - scans the star system for planets\nsearch - searches nearby object\n" +
              "examine [Item Name]- use this to get description of item you have.\nuse - apply item\nget - pick up item\n" +
              "drop [Item Name] - drops the item\ncargo - lists your items if you have any\nquit - closes the game"

  def respondToEvent(command: String, event: Event): String =

    var outcomeText = ""
    var outcome:((String, Vector[Item]), Vector[Int]) = null

    command match
      case "1" => outcome = Random.shuffle(event.firstOutcomes).head
      case "2" => outcome = Random.shuffle(event.secondOutcomes).head
      case _ =>
        outcomeText = event.description + "\n\nYou can't use other commands during special event."

    if outcome != null then
      println(outcome._2(0))
      println(max(outcome._2(0), 0))
      shipSystems(0) = max(shipSystems(0) - outcome._2(0), 0)
      shipSystems(1) = max(shipSystems(1) - outcome._2(1), 0)
      shipSystems(2) = max(shipSystems(2) - outcome._2(2), 0)

      outcomeText = outcome._1._1

      if outcome._1._2.nonEmpty then
        cargo += Random.shuffle(outcome._1._2).head.name -> Random.shuffle(outcome._1._2).head

    // This is hard coded event that doesn't depend on player response
    if event.secondOutcomes.isEmpty then
      if event.description.contains("huge asteroid") then
        if shipSystems.head == 100 then
          outcomeText = event.description + "\n\n" + event.firstOutcomes.head._1._1
          shipSystems(0) = 0
        else if shipSystems.head != 0 then
          outcomeText = event.description + "\n\n" + event.firstOutcomes(1)._1._1
          shipSystems(0) = 0
          shipSystems(1) = max(shipSystems(1) - Random.nextInt(40), 0)
          shipSystems(2) = max(shipSystems(2) - Random.nextInt(40), 0)
        else if shipSystems.head == 0 then
          outcomeText = event.description + "\n\n" + event.firstOutcomes.last._1._1
          shipSystems(1) = 0
          shipSystems(2) = 0

    outcomeText

end Player

