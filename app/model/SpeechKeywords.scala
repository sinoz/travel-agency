package model

object SpeechKeywords {
  val TripBooking = Set(
    "searching for a vacation",
    "go on a vacation",
    "to go to",
    "book a vacation",
    "book trip",
    "boek trip",
    "trip boeken",
    "boek een trip",
    "book a trip"
  )

  val BookForAmountOfPeople = Set(
    "for one person",
    "for two people",
    "for three people",
    "for four people",
    "for five people",
    "for six people"
  )

  val Greetings = Set(
    "hi",
    "hello",
    "hallo",
    "goedendag",
    "good day",
    "goedemorgen",
    "goodmorning",
    "good afternoon",
    "noon",
    "goedemiddag",
    "goedeavond",
    "goedenavond",
    "good evening"
  )

  val NumericPrices = (50 to 5000).map(price => s"$price euro").toSet
  val TextPrices = Set(
    "fifty",
    "hundred",
    "hundred and fifty",
    "hundred fifty",
    "two hundred",
    "two hundred fifty",
    "two hundred and fifty",
    "three hundred",
    "three hundred fifty",
    "three hundred and fifty",
    "four hundred"
  )

  val Prices = NumericPrices ++ TextPrices

  val StayPlaces = Set(
    "hotel",
    "motel",
    "camping"
  )

  val Destinations = Set(
    "amsterdam",
    "parijs",
    "paris",
    "milan",
    "milaan",
    "new york",
    "london",
    "los angeles",
    "seattle",
    "athene",
    "berlijn",
    "beijing",
    "tokyo",
    "tenerife",
    "dubai",
    "belgie",
    "limburg",
    "schotland",
    "scotland",
    "egypt",
    "cairo",
    "india",
    "rome",
    "moscow",
    "rio de janeiro",
    "oslo",
    "rotterdam"
  )
}
