package o1.SpaceAdventure
import scala.util.Random
import scala.collection.mutable.Buffer

/** Possible events are stored here **/

var eventInProgress: Option[Event] = None
var events = Buffer[Event]()

// Create Event classes from the list

// Contains description, tuple for outcomes with command 1, tuple for outcomes with command 2. Inside tuple is outcome, possibbleItems got, Damage to ship
var eventList = Vector[(String, (Vector[((String, Vector[Item]), Vector[Int])], Vector[((String, Vector[Item]), Vector[Int])]))](

// 2 options, 4 different outcomes.
  "Your recent actions have not been pleasing everyone on the ship. " +
  "A few crew members have been talking behing your back\nand have now declared mutiny. " +
  "How will you respond?\n" +
  "1) - Throw them to space from the airlock before we all die!!! \n"+
  "2) - Try to talk it out."
  -> (Vector[((String, Vector[Item]), Vector[Int])](
     ("Your attempt to eject them out of the ship ends up in a big fight that damages the ship.", Vector[Item]()) -> Vector[Int](Random.nextInt(50),Random.nextInt(50),Random.nextInt(50))
    ,("You got rid of them but your conscience took a beating.", Vector[Item]()) -> Vector[Int](0,0,0)
  ), Vector[((String, Vector[Item]), Vector[Int])](
     ("Talking was not enough and they got rid of you instead. And of course those fools managed to destroy the ship.", Vector[Item]()) -> Vector[Int](100,100,100)
    ,("You resolve the conflict peacefully.", Vector[Item]()) -> Vector[Int](0,0,0))
  )

,
// 2 options, 4 different outcomes
  "Your ship's sensors have detected a massive amount of radiotion coming from an exploded star, " +
  "the ship can protect people inside it but the radiation might damage some ship systems if powered, " +
  "how to proceed?\n" +
  "1) - Shut down ship systems for a while.\n" +
  "2) - Ignore the warning."
  -> (Vector[((String, Vector[Item]), Vector[Int])](
    ("You shut down everything and let the radiotion pass, you ship survives without any damage", Vector[Item]()) -> Vector[Int](0,0,0),
    ("You shut down everything and let the radioation pass, meanwhile a tiny asteroid " +
     " hits your ship causing some damage.", Vector[Item]()) -> Vector[Int](Random.nextInt(20),Random.nextInt(20),Random.nextInt(20))
  ), Vector[((String, Vector[Item]), Vector[Int])](
    ("You wait for the radiation to pass, it doesn't do anything to your ship.", Vector[Item]()) -> Vector[Int](0,0,0),
    ("The radiation causes massive damage to your powered ship systems.", Vector[Item]()) -> Vector[Int](Random.nextInt(60),Random.nextInt(60),Random.nextInt(60)))
  ),

  // 2 options, 3 different outcomes.
  "Your ship's sensors have detected an unknown object approaching, " +
  "it doesn't seem like an asteroid. It's a space ship, It's Aliens!!!\n" +
  "1) - Wait for the aliens to arrive.\n" +
  "2) - Push your thrusters over the limits and try to escape!"
  -> (Vector[((String, Vector[Item]), Vector[Int])](
    ("You meet the aliens and they are friendly. You struggle to communicate but they give you " +
    "an item that looks useful", Vector[Item](Item("shield booster", "this device appears to have the capability to make your shield stronger"), Item("repair kit", "with this you can repair your ship, use it wisely."))) -> Vector[Int](0,0,0),
    ("The aliens are not as friendly as you hoped, they board the ship and incapacite everyone " +
    "with their superior technology, they take everyone to their ship and begin running all " +
    "kinds of experiments with the people. It's over now. ", Vector[Item]()) -> Vector[Int](100,100,100)
  ),Vector[((String, Vector[Item]), Vector[Int])](
     ("You manage to espace but damage your ship's engines", Vector[Item]()) -> Vector[Int](0,0, Random.nextInt(40)))
  )

  ,
  // Outcome depends on shield condition
  "A huge asteroid appears out of the blue and impacts your ship."
    -> (Vector[((String, Vector[Item]), Vector[Int])](
    ("Fortunately your ship's shields were still at 100% and managed to protect the ship.", Vector[Item]()) -> Vector[Int](100,0,0),
    ("Your Damaged shields couldn't cancel out all of the impact, your ship took some damage.", Vector[Item]()) -> Vector[Int](0,Random.nextInt(20),Random.nextInt(20)),
    ("Your ship's shields were already gone before the impact and so is the ship now", Vector[Item]()) -> Vector[Int](100,100,100)),
    Vector[((String, Vector[Item]), Vector[Int])]())

)