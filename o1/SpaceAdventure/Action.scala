package o1.SpaceAdventure

class Action(input: String):

  private val commandText = input.trim.toLowerCase
  private val verb        = commandText.takeWhile( _ != ' ' )
  private val modifiers   = commandText.drop(verb.length).trim


  def execute(actor: Player) =

    eventInProgress match
      case None =>
        this.verb match
        case "go"        => Some(actor.go(this.modifiers))
        case "scan"      => Some(actor.scan())
        case "search"    => Some(actor.search())
        case "examine"   => Some(actor.examine(this.modifiers))
        case "use"       => Some(actor.use(this.modifiers))
        case "get"       => Some(actor.get(this.modifiers))
        case "drop"  => Some(actor.drop(this.modifiers))
        case "cargo"     => Some(actor.inventory)
        case "quit"      => Some(actor.quit())
        case "help"      => Some(actor.help)
        case other       => None

      // if event is in progress let respondToEvent function handle commands
      case Some(event) => Some(actor.respondToEvent(commandText, event))


  /** Returns a textual description of the action object, for debugging purposes. */

  override def toString = s"$verb (modifiers: $modifiers)"

end Action

