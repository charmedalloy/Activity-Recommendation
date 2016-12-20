package api

import recommendationSystem._
import org.joda.time.DateTime
import org.json4s._
import org.json4s.JsonDSL._

/**
  * Created by avalj on 12/09/16.
  */
object Location {

  def app(lat: String, long: String): JObject = {
    val LocObject = new Location(lat.toDouble, long.toDouble)
    //User location ( we can get this from browser when implementing web-app)
    val maxDistance = 1.0
    val todayDate = new DateTime()
    val nextDate = todayDate.plusDays(1)

    val Weather_data = WeatherCollection.weather(LocObject, maxDistance, nextDate)

    val weather = Weather_data.map(w => (w.Place, w.Temp_max, w.Temp_min, w.weather_type))
    weather.sortBy(w => w._1).foreach(println)
    val place_of_weather = weather(0)._1
    println(Weather_data(0))
    val placeCoord = WeatherCollection.getGeoCoordinate(place_of_weather)
    val nearInterest = InterestPoint.getNearestInterestPoints(placeCoord, maxDistance)
    val recommendation = Recommendation.Recommend(nearInterest, Weather_data(0))
    val array_of_points = recommendation.flatMap(r => r.places)
    val jsonObj = convertAllPointsToGeoJson(array_of_points, Weather_data(0))
    val finalJson =
      ("type" -> "FeatureCollection") ~
        ("features" -> jsonObj.toList)
    finalJson
  }

  //Convert InterestPoint_out array to Array[String] GeoJSON format
  def convertAllPointsToGeoJson(arr: Array[InterestPoint_reformed], weather: Weather): Array[JObject] = {
    arr.map(x => convertToGeoJson(x, weather))
  }

  //Convert InterestPoint_out data structure to GeoJson
  def convertToGeoJson(point: InterestPoint_reformed, weather: Weather): JObject = {
    val coordinates = List(point.longtitude, point.latitude)
    val temperature = (weather.Temp_max + weather.Temp_min) / 2.0
    val bar = weather.weather_type
    val json =
      ("type" -> "Feature") ~
        ("geometry" ->
          ("type" -> "Point") ~
            ("coordinates" -> coordinates)
          ) ~
        ("properties" ->
          ("name" -> point.name) ~
            ("tourism" -> point.tourism) ~
            ("cultural" -> point.cultural) ~
            ("parks" -> point.parks) ~
            ("leisure" -> point.leisure) ~
            ("nightlife" -> point.nightlife) ~
            ("sport" -> point.sport) ~
            ("temperature" -> temperature) ~
            ("bar" -> bar)
          )
    json
  }
}
