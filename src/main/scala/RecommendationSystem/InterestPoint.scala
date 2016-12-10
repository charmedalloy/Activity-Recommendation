package RecommendationSystem

import com.mongodb.DBObject
import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.query.Imports._
import org.json4s._
import org.json4s.native.JsonMethods._


/**
  * Created by avalj on 12/09/16.
  */

case class InterestPoint_out(id: String, latitude: Double, longtitude: Double, name: String, tags: List[String])

case class InterestPoint_reformed(id: String, latitude: Double, longtitude: Double, name: String, tourism: String, cultural: String, parks: String, leisure: String, nightlife: String, sport: String)

object InterestPoint {

  def getNearestInterestPoints(location: Location, radius: Double): Array[InterestPoint_reformed] = {
    //Connect to db and get interesting places
    val mongoClient = MongoClient("localhost", 27017)
    val db = mongoClient("bigdataproject")
    val places_collection = db("interestPoint")

    //Location coordinates
    val lat = location.latitude
    val long = location.longtitude

    val points = "geometry.coordinates" $near(lat, long) $maxDistance radius
    val result = places_collection.find(points).toArray
    result.map { x => fromBson(x) }.filter { x => filterNoneTags(x) }
  }

  def fromBson(o: DBObject): InterestPoint_reformed = {
    val point = InterestPoint_out(
      o.as[String]("osm_id"),
      readCoordinates(o.as[DBObject]("geometry").toString)._1,
      readCoordinates(o.as[DBObject]("geometry").toString)._2,
      o.as[String]("name"),
      List(o.as[String]("tourism"), o.as[String]("amenity"), o.as[String]("sport"))
    )
    reformatTags(point)
  }

  def filterNoneTags(point: InterestPoint_reformed): Boolean = {
    !(point.tourism == "None" && point.cultural == "None" && point.parks == "None"
      && point.leisure == "None" && point.nightlife == "None" && point.sport == "None")
  }

  //Convert coordinates from Bson database output
  def readCoordinates(ob: String): (Double, Double) = {
    val data = parse(ob)
    val coordinates_string = compact(render(data \ "coordinates"))
    val latitude = coordinates_string.split(",")(0).drop(1).toDouble
    val longtitude = coordinates_string.split(",")(1).dropRight(1).toDouble
    (latitude, longtitude)
  }

  // Reformat tags by 6 categories and return new case class InterestPoit_reform
  def reformatTags(point: InterestPoint_out): InterestPoint_reformed = {
    val tags = checkTag(point.tags)
    InterestPoint_reformed(point.id, point.latitude, point.longtitude, point.name, tags(0), tags(1), tags(2), tags(3), tags(4), tags(5))
  }

  def checkTag(tags: List[String]): Array[String] = {
    var reformated_tags = Array("None", "None", "None", "None", "None", "None")
    tags.foreach { x =>
      if (List("attraction", "information", "viewpoint").contains(x)) reformated_tags(0) = x // tourism tags
      if (List("arts_centre", "planetarium", "theatre").contains(x)) reformated_tags(1) = x // cultural tags
      if (List("picnic_site").contains(x)) reformated_tags(2) = x // parks tags
      if (List("theme_park", "zoo", "cinema", "gym", "marketplace", "restaurant").contains(x)) reformated_tags(3) = x // leisure tags
      if (List("nightclub", "casino", "striplub", "cinema", "bar", "pub").contains(x)) reformated_tags(4) = x // nightlife tags
      if (List("9pin", "10pin", "american_football", "base", "baseball", "basketball", "bmx", "boxing", "canoe", "cliff_diving",
        "climbing_adventure", "roller_skating", "scuba_diving", "sailing", "safety_training", "skateboard", "skiing", "surfing",
        "swimming", "water_ski").contains(x))
        reformated_tags(5) = x // sport tags
    }
    reformated_tags
  }

  //Input:Array(InterestPoint_out) with all interest places. Output: array with tourism points only
  def getTourismPoints(interest_places: Array[InterestPoint_reformed]): Array[InterestPoint_reformed] = {
    val tourism_filtered = interest_places.filter { x => x.tourism != "None" }
    tourism_filtered

  }

  //Input:Array(InterestPoint_out) with all interest places. Output: array with cultural points only
  def getCulturalPoints(interest_places: Array[InterestPoint_reformed]): Array[InterestPoint_reformed] = {
    val cultural_filtered = interest_places.filter { x => x.cultural != "None" }
    cultural_filtered

  }

  //Input:Array(InterestPoint_out) with all interest places. Output: array with parks points only
  def getParksPoints(interest_places: Array[InterestPoint_reformed]): Array[InterestPoint_reformed] = {
    val parks_filtered = interest_places.filter { x => x.parks != "None" }
    parks_filtered

  }

  //Input:Array(InterestPoint_out) with all interest places. Output: array with leisure points only
  def getLeisurePoints(interest_places: Array[InterestPoint_reformed]): Array[InterestPoint_reformed] = {
    val leisure_filtered = interest_places.filter { x => x.leisure != "None" }
    leisure_filtered

  }


  //Input:Array(InterestPoint_out) with all interest places. Output: array with nightlife points only
  def getNightLifePoints(interest_places: Array[InterestPoint_reformed]): Array[InterestPoint_reformed] = {
    val nightlife_filtered = interest_places.filter { x => x.nightlife != "None" }
    nightlife_filtered
  }


  //Input:Array(InterestPoint_out) with all interest places. Output: array with sport points only
  def getSportPoints(interest_places: Array[InterestPoint_reformed]): Array[InterestPoint_reformed] = {
    val sport_filtered = interest_places.filter { x => !x.sport.equals("None") }
    sport_filtered
  }
}
