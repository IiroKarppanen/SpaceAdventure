package o1.SpaceAdventure

class Event(val description: String,
            val firstOutcomes: Vector[((String, Vector[Item]), Vector[Int])],
            val secondOutcomes: Vector[((String, Vector[Item]), Vector[Int])]):

  override def toString = description + firstOutcomes + secondOutcomes