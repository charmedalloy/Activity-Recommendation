package RecommendationSystem

import scala.collection.mutable.ArrayBuffer

/**
  * Created by avalj on 12/09/16.
  */

case class RecommededPlaces(places: Array[InterestPoint_reformed], weather_data: Weather)

object Recommendation {

  def Recommend(places: Array[InterestPoint_reformed], weather: Weather): Array[RecommededPlaces] = {
    var recommend = new ArrayBuffer[RecommededPlaces]()
    var hotActivityTags: List[String] = List("viewpoint", "theme_park", "picnic_park", "zoo", "marketplace",
      "canoe", "cliff_diving", "scuba_diving", "sailing", "safety_training", "surfing", "water_ski", "swimming")
    val avg_temp = (weather.Temp_max + weather.Temp_min) / 2.0
    if (weather.weather_type.equals("fair") || weather.weather_type.equals("dry")) {
      if (avg_temp <= 1) {
        recommend += RecommededPlaces(places.filter(point => point.sport.contains("skiing")), weather)
      }
    }
    recommend.toArray
  }
}
