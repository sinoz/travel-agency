package model

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Json, Reads}

object Destination {
  implicit val destinationReads: Reads[Destination] = (
    (JsPath \ "name").read[String] and
      (JsPath \ "images").read[Seq[String]] and
      (JsPath \ "activities").read[String]
    )(Destination.apply _)

  def parse(jsonString: String) =
    Json.fromJson(Json.parse(jsonString))(Reads.seq(destinationReads)).get
}

case class Destination(
  name: String,
  images: Seq[String],
  activities: String
)
