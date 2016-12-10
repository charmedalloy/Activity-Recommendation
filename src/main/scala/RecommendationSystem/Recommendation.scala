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
        //Snow means Skiing is recommended
        recommend += RecommededPlaces(places.filter(point => point.sport.contains("skiing")), weather)
      }
      if (avg_temp >= 10 && avg_temp <= 30) {
        recommend += RecommededPlaces(places.filter(interestPoint => hotActivityTags.contains(interestPoint.cultural)
          || hotActivityTags.contains(interestPoint.leisure) || hotActivityTags.contains(interestPoint.sport)
          || hotActivityTags.contains(interestPoint.parks) || hotActivityTags.contains(interestPoint.tourism)), weather)

      }
    }
    else {
      recommend += RecommededPlaces(places.filter(interestPoint => interestPoint.cultural.contains("arts_centre")
        || interestPoint.cultural.contains("planetarium") || interestPoint.leisure.contains("cinema")
        || interestPoint.leisure.contains("gym") || interestPoint.sport.contains("9pin")
        || interestPoint.sport.contains("10pin") || interestPoint.sport.contains("boxing")
        || interestPoint.nightlife.contains("nightclub")), weather)

    }
    recommend.toArray
  }
}
