package o1.SpaceAdventure.ui
import scala.util.Random
import scala.swing.{Color, *}
import scala.swing.event.*
import scala.swing.*
import javax.swing.{ImageIcon, SwingConstants, UIManager}
import o1.SpaceAdventure.SpaceAdventure

import scala.swing.GridBagPanel.Anchor.*
import scala.swing.GridBagPanel.Fill
import java.awt.{Color, Dimension, Insets, Point}
import java.io.File
import javax.imageio.ImageIO
import javax.swing.border.LineBorder
import scala.language.adhocExtensions // enable extension of Swing classes

/** The singleton object `AdventureGUI` represents a GUI-based version of the Adventure
  * game application. The object serves as a possible entry point for the game app, and can
  * be run to start up a user interface that operates in a separate window. The GUI reads
  * its input from a text field and displays information about the game world in uneditable
  * text areas.
  *
  * **NOTE TO STUDENTS: In this course, you don’t need to understand how this object works
  * or can be used, apart from the fact that you can use this file to start the program.**
  *
  * @see [[AdventureTextUI]] */
object SpaceAdventureGUI extends SimpleSwingApplication:
  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)

  def top = new MainFrame:

    // Access to the application’s internal logic:

    val game = SpaceAdventure()
    val player = game.player

    // Components:

    val locationInfo = new TextArea(7, 80):
      foreground = Color.green
      background = Color(30, 30, 30)
      editable = false
      wordWrap = true
      lineWrap = true
    val turnOutput = new TextArea(7, 80):
      foreground = Color.green
      background = Color(30, 30, 30)
      editable = false
      wordWrap = true
      lineWrap = true

    // Ship condition panel

    val shields = new TextArea(10, 10):
      editable = false
      wordWrap = true
      lineWrap = true

    val hull = new TextArea(10, 10):
      editable = false
      wordWrap = true
      lineWrap = true

    val engines = new TextArea(10, 10):
      editable = false
      wordWrap = true
      lineWrap = true

    val locationLabel = Label("Location: ")
    locationLabel.foreground = Color.green

    val commandLabel = Label("Command: ")
    commandLabel.foreground = Color.green

    val eventLabel = Label("Events: ")
    eventLabel.foreground = Color.green

    val shipLabel = Label()
    shipLabel.text = "Ship Condition"
    shipLabel.font = Font("Calibri", Font.Plain, 16)

    val shieldLabel = Label()
    shieldLabel.text = s"            Shield | ${player.shipCondition.head}%"
    shieldLabel.foreground = Color.green
    shieldLabel.font = Font("Calibri", Font.Plain, 16)

    val sensorsLabel = Label()
    sensorsLabel.text = s"           Sensors | ${player.shipCondition(1)}%"
    sensorsLabel.foreground = Color.green
    sensorsLabel.font = Font("Calibri", Font.Plain, 16)

    val engineLabel = Label()
    engineLabel.text = s"            Engines | ${player.shipCondition(2)}%"
    engineLabel.foreground = Color.green
    engineLabel.font = Font("Calibri", Font.Plain, 16)

    val emptySpace = Label()
    emptySpace.text = "       "
    emptySpace.size.height = 500
    emptySpace.minimumSize.height = 500


    val shieldPic = Label()
    var shieldIconRaw = ImageIcon(this.getClass.getResource("shields.jpg")).getImage()
    var shieldIconResized = shieldIconRaw.getScaledInstance(180, 180, java.awt.Image.SCALE_SMOOTH)
    var shieldIcon = new ImageIcon(shieldIconResized)
    shieldPic.icon = shieldIcon

    val sensorsPic = Label()
    var sensorsIconRaw = ImageIcon(this.getClass.getResource("sensors.jpg")).getImage()
    var sensorsIconResized = sensorsIconRaw.getScaledInstance(180, 180, java.awt.Image.SCALE_SMOOTH)
    var sensorsIcon = new ImageIcon(sensorsIconResized)
    sensorsPic.icon = sensorsIcon

    val enginePic = Label()
    var engineIconRaw = ImageIcon(this.getClass.getResource("engines.jpg")).getImage()
    var engineIconResized = engineIconRaw.getScaledInstance(180, 100, java.awt.Image.SCALE_SMOOTH)
    var engineIcon = new ImageIcon(engineIconResized)
    enginePic.icon = engineIcon


    val ship = new BoxPanel(Orientation.Vertical):
      background = Color.black
      this.contents ++= Vector(shipLabel, shieldPic, shieldLabel, sensorsPic, sensorsLabel, emptySpace, enginePic, engineLabel)

    val input = new TextField(80):
      background = Color(30, 30, 30)
      border = new LineBorder(Color(45,45,45),1)
      foreground = Color.green
      minimumSize = preferredSize
    this.listenTo(input.keys)
    val turnCounter = Label()

    // Events:

    this.reactions += {
      case keyEvent: KeyPressed =>
        if keyEvent.source == this.input && keyEvent.key == Key.Enter && !this.game.isOver then
          val command = this.input.text.trim
          if command.nonEmpty then
            this.input.text = ""
            this.playTurn(command)
    }

    // Layout:

    this.contents = new GridBagPanel:
      background = Color(15,15,15)

      layout += locationLabel      -> Constraints(0, 2, 1, 1, 0, 1, NorthWest.id, Fill.None.id, Insets(8, 5, 5, 5), 0, 0)
      layout += commandLabel       -> Constraints(0, 1, 1, 1, 0, 0, NorthWest.id, Fill.None.id, Insets(8, 5, 5, 5), 0, 0)
      layout += eventLabel         -> Constraints(0, 0, 1, 1, 0, 0, NorthWest.id, Fill.None.id, Insets(8, 5, 5, 5), 0, 0)
      layout += turnCounter        -> Constraints(0, 4, 2, 1, 0, 0, NorthWest.id, Fill.None.id, Insets(8, 5, 5, 5), 0, 0)
      layout += locationInfo       -> Constraints(1, 2, 1, 1, 1, 1, NorthWest.id, Fill.Both.id, Insets(5, 5, 5, 5), 0, 0)
      layout += input              -> Constraints(1, 1, 1, 1, 1, 0, NorthWest.id, Fill.None.id, Insets(5, 5, 5, 5), 0, 0)
      layout += turnOutput         -> Constraints(1, 0, 1, 1, 1, 1, SouthWest.id, Fill.Both.id, Insets(5, 5, 5, 5), 0, 0)
      layout += ship               -> Constraints(8, 0, 0, 0, 0, 0, NorthEast.id, Fill.None.id, Insets(5, 5, 0, 5), 0, 0)

    // Menu:
    this.menuBar = new MenuBar:
      contents += new Menu("Program"):
        val quitAction = Action("Quit")( dispose() )
        contents += MenuItem(quitAction)

    // Set up the GUI’s initial state:

    this.title = game.title
    this.updateInfo(this.game.welcomeMessage)
    this.location = Point(50, 50)
    this.minimumSize = Dimension(400, 200)
    this.pack()
    this.input.requestFocusInWindow()


    def playTurn(command: String) =
      val turnReport = this.game.playTurn(command)
      if this.player.hasQuit then
        this.dispose()
      else
        this.updateInfo(turnReport)
        this.input.enabled = !this.game.isOver


    def updateInfo(info: String) =

      shieldLabel.text = s"            Shield | ${player.shipCondition.head}%"
      if player.shipCondition.head < 60 then shieldLabel.foreground = Color.yellow
      if player.shipCondition.head < 30 then shieldLabel.foreground = Color.red
      sensorsLabel.text = s"            Sensors | ${player.shipCondition(1)}%"
      if player.shipCondition(1) < 60 then sensorsLabel.foreground = Color.yellow
      if player.shipCondition(1) < 30 then sensorsLabel.foreground = Color.red
      engineLabel.text = s"            Engines | ${player.shipCondition(2)}%"
      if player.shipCondition(2) < 60 then engineLabel.foreground = Color.yellow
      if player.shipCondition(2) < 30 then engineLabel.foreground = Color.red

      if !this.game.isOver then
        this.turnOutput.text = info
      else
        this.turnOutput.text = info + "\n\n" + this.game.goodbyeMessage

      if this.game.eventActive then
        this.locationInfo.text = ""
      else
        this.locationInfo.text = this.player.location.fullDescription

      this.turnCounter.text = "Turns played: " + this.game.turnCount


  end top

  // Enable this code to work even under the -language:strictEquality compiler option:
  private given CanEqual[Component, Component] = CanEqual.derived
  private given CanEqual[Key.Value, Key.Value] = CanEqual.derived

end SpaceAdventureGUI

