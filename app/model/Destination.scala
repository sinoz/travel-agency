package model

import java.io.InputStream

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Json, Reads}

object Destination {
  implicit val destinationReads: Reads[Destination] = (
    (JsPath \ "name").read[String] and
      (JsPath \ "images").read[Seq[String]] and
      (JsPath \ "activities").read[String]
    )(Destination.apply _)

  def parse(inputStream: InputStream) =
    Json.fromJson(Json.parse(inputStream))(Reads.seq(destinationReads)).get
}

case class Destination(
  name: String,
  images: Seq[String],
  activities: String
)
