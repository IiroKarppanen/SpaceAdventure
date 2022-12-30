package o1.SpaceAdventure
import scala.collection.mutable
import scala.util.Random


class SpaceAdventure:

  val title = "Space Adventure"

  // Star Systems
  private val startingStar1 = Star("kepler 142", "No planets here, keep going.")
  private val startingStar2 = Star("kepler 150", "This star doesn't have any planets")
  private val startingStar3 = Star("gliese 141", "This star doesn't have any planets")
  private val star1 = Star("kepler 98", "No Habitable planets here.")
  private val star2 = Star("kepler 103", "Your sensors detect 2 rocky planets, but both of them are too hot for humans.")
  private val star3 = Star("toi 700", "You discover one promising planet, it has the right temperature and atmosphere with oxygen, but the planet is full of radiation that would make life impossible there.")
  private val star4 = Star("toi 800", "You find one gas giant planet, but nothing suitable for humans.")
  private val star5 = Star("gliese 163", "You discover only one barren planet here.")
  private val target1 = Star("kepler 90", "Your sensors find one promising planet, it has liquid water, atmosphere with perfect amount of oxygen, comfortable gravity and just the right temperature. Everything looks good about this planet, you have finally done it! Humanity is saved.")
  private val target2 = Star("trappist 1", "Your sensors immediately pick up multiple interesting planets. All of them seem habitable for humans. You have successfully completed the mission.")

  private val startingSystems = Vector[Star](startingStar1, startingStar2, startingStar3)
  private val emptySystems = Vector[Star](star1, star2, star3, star4, star5)
  private val items = Vector[Item](Item("warp drive upgrade", "This upgrades your warp drive and enables travel to further away.")
                                  ,Item("long range sensors", "Improves your sensors range, you can now scan planets from further away."))


  // Construct game map
  startingStar1.setNeighbors(Vector("kepler 150" -> startingStar2, "kepler 98" -> star1))
  startingStar2.setNeighbors(Vector("kepler 142" -> startingStar1, "kepler 103" -> star2, "toi 700" -> star3))
  startingStar3.setNeighbors(Vector("kepler 103" -> star2))
  star1.setNeighbors(Vector("kepler 142" -> startingStar1, "gliese 163" -> star5))
  star2.setNeighbors(Vector("kepler 150" -> startingStar2, "gliese 141" -> startingStar3, "toi 700" -> star3, "toi 800" -> star4))
  star3.setNeighbors(Vector("kepler 150" -> startingStar2, "toi 800" -> star4, "kepler 103" -> star2, "gliese 163" -> star5, "kepler 90" -> target1))
  star4.setNeighbors(Vector("kepler 103" -> star3, "toi 700" -> star3, "kepler 90" -> target1))
  star5.setNeighbors(Vector("toi 700" -> star3, "kepler 90" -> target1, "kepler 98" -> star1, "trappist 1" -> target2))
  target1.setNeighbors(Vector("gliese 163" -> star5, "toi 700" -> star3, "toi 800" -> star4))
  target1.setNeighbors(Vector("gliese 163" -> star5))


  // Add 3 searchable objects
  var randomSystems = Random.shuffle(emptySystems).take(3)
  randomSystems.head.addSearchableObject("Remains of old space ship" -> "You use the ship's sensors to analyze the remains and can't find anything useful.")
  randomSystems(1).addSearchableObject("Unkown metal object" -> "You use the ship's sensors to analyze this object and can't make any sense of it, nothing found.")
  randomSystems.tail.head.addSearchableObject("Alien artifact" -> "The artifact starts glowing and emitting some kind of pulse that damages your sensors.")

  // Add 2 items
  var systemsAndItems = randomSystems.zip(items)
  systemsAndItems.foreach(n => n._1.addItem(n._2))

  // Create events
  eventList.foreach(n => events += Event(n._1, n._2._1, n._2._2))

  /** The character who is the protagonist of the space adventure and whom the real-life player controls. */
  val player = Player(Random.shuffle(startingSystems).head)

  /** The number of turns that have passed since the start of the game. */
  var turnCount = 0

  /** The maximum number of turns that this adventure game allows before time runs out. */
  val timeLimit = 50

  def eventActive = eventInProgress.isDefined

  /** Determines if the adventure is complete, that is, if the player has won. */
  def isComplete = player.hasWon

  /** Determines whether the player has won, lost, or quit, thereby ending the game. */
  def isOver = this.isComplete || this.player.hasQuit || this.turnCount == this.timeLimit || !player.shipIntact()

  /** Returns a message that is to be displayed to the player at the beginning of the game. */
  def welcomeMessage = "You are the commander of the Genenis vessel, your ship has already travelled around " +
    "100 light years away from Earth. Most of humanity has died from war and shortage of all major resources " +
    "on Earth. You must find a habitable planet to save humanity. Otherwise humanity might go extinct! "

  /** Returns a message that is to be displayed to the player at the end of the game. The message
    * will be different depending on whether or not the player has completed their quest. */
  def goodbyeMessage =
    if this.isComplete then
      "\n\nMission Complete"
    else if this.turnCount == this.timeLimit then
      "Your voyage has taken so long that all of the people onboard have already died, mission failed!"
    else
      "Mission Failure"

  def playTurn(command: String) =

    val action = Action(command)
    var outcomeReport = action.execute(this.player)
    if outcomeReport.isDefined then
      this.turnCount += 1


    // If player travels succesfully, an event might occur
    if outcomeReport.getOrElse("").contains("You go") then

      if Random.nextInt(3) == 1 && turnCount > 1 then
        var event = Random.shuffle(events).head

        // If event outcome doesn't depened on player response, run it immediately without setting it as in progress.
        if event.secondOutcomes.isEmpty then
          outcomeReport = Some(player.respondToEvent(command, event))
        // Else wait for player to respond with some command
        else
          eventInProgress = Some(event)
          outcomeReport = Some(event.description)

    // If special event is in progress and input is 1 or 2, finish event
    if eventInProgress.isDefined && (command == "1" || command == "2") then
      eventInProgress = None

    outcomeReport.getOrElse(s"""Unknown command: "$command".""")


end SpaceAdventure
