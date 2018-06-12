package services

import akka.actor.{FSM, Props}
import model.{Destination, SpeechKeywords}
import services.User._

object User {
  case class Request(
    session_uid: String,
    request: String
  )

  case class Response(
    response: String,
    suggestions: Seq[Destination] = Seq.empty[Destination]
  )

  case class UserSpeech(text: String)

  case object Greeting extends DialogueStage
  case object AskingFor4Help extends DialogueStage
  case object AskingForDestination extends DialogueStage
  case object AskingForStayPlace extends DialogueStage
  case object AskingForBudget extends DialogueStage
  sealed abstract class DialogueStage

  object State {
    val noneSpecified = State(
      destination = None,
      stayPlace = None
    )
  }

  case class State(
    destination: Option[String],
    stayPlace: Option[String]
  )

  def props(destinations: Seq[Destination]) = Props(new User(destinations))
}

final class User(destinations: Seq[Destination]) extends FSM[DialogueStage, State] {
  when (Greeting) {
    case Event(UserSpeech(text), state) =>
      if (SpeechKeywords.TripBooking.exists(keyword => text.toLowerCase().contains(keyword))) {
        val matchedDestination = SpeechKeywords.Destinations.find(keyword => text.toLowerCase().contains(keyword))
        if (matchedDestination.isDefined) {
          goto(AskingForStayPlace)
            .replying(Response("And would you like to stay in a hotel, motel or camping?"))
            .using(state.copy(
              destination = matchedDestination
            ))
        } else {
          goto(AskingForDestination) replying Response("Do you know where you would like to go?")
        }
      } else {
        // by default we'll just be kind and greet the user back and ask them how we can help them
        goto(AskingFor4Help) replying Response("Hi there. How can I help you?")
      }
  }

  when (AskingFor4Help) {
    case Event(UserSpeech(text), state) =>
      if (SpeechKeywords.TripBooking.exists(keyword => text.toLowerCase().contains(keyword))) {
        val matchedDestination = SpeechKeywords.Destinations.find(keyword => text.contains(keyword))
        if (matchedDestination.isDefined) {
          goto(AskingForStayPlace)
            .replying(Response("And would you like to stay in a hotel, motel or camping?"))
            .using(state.copy(
              destination = matchedDestination
            ))
        } else {
          goto(AskingForDestination) replying Response("Do you know where you would like to go?")
        }
      } else {
        stay() replying Response("Could you please try again?")
      }
  }

  when (AskingForDestination) {
    case Event(UserSpeech(text), state) =>
      val matchedDestination = SpeechKeywords.Destinations.find(keyword => text.toLowerCase().contains(keyword))
      if (matchedDestination.isDefined) {
        goto(AskingForStayPlace)
          .replying(Response("And would you like to go to a hotel, motel or camping?"))
          .using(state.copy(destination = matchedDestination))
      } else {
        stay() replying Response("Could you please try again?")
      }
  }

  when (AskingForStayPlace) {
    case Event(UserSpeech(text), state) =>
      goto(AskingForBudget) replying Response(s"How much would you like to spend on your stay place per night?")
  }

  when (AskingForBudget) {
    case Event(UserSpeech(text), state) =>
      val budget = SpeechKeywords.Prices.find(keyword => text.toLowerCase().contains(keyword))
      if (budget.isDefined) {
        val suggestion = destinations.find(_.name.toLowerCase().contains(state.destination.get))
        if (suggestion.isDefined) {
          stay() replying Response("", suggestion.toSeq)
        } else {
          stay() replying Response("Could not find suggestions based on criteria")
        }
      } else {
        stay() replying Response("Could you please try again?")
      }
  }

  startWith(Greeting, State.noneSpecified)
}
