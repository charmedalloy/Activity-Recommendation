package Application

import RecommendationSystem.{InterestPoint, Recommendation, Weather, WeatherCollection}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

/**
  * Created by avalj on 12/09/16.
  */
object MyApp {

  def main(args: Array[String]): Unit = {
    val Point_Of_Interest = "Richardson"
    val maxDistance = 1.0
    val todayDate = new DateTime()
    val nextDate = todayDate.plusDays(1)
    println("For: " + nextDate)
    val Weather_data = WeatherCollection.weather(Point_Of_Interest, maxDistance, nextDate)
    val weather = Weather_data.map(w => (w.Place, w.Temp_max, w.Temp_min, w.weather_type))
    weather.sortBy(w => w._1).foreach(println)
    val place_of_weather = weather(0)._1
    println(place_of_weather)
    val placeCoord = WeatherCollection.getGeoCoordinate(place_of_weather)
    val nearInterest = InterestPoint.getNearestInterestPoints(placeCoord, maxDistance)
    //nearInterest.foreach(println)
    val recommendation = Recommendation.Recommend(nearInterest, Weather("", 20.0, 20.0, "fair"))
    val array_of_points = recommendation.flatMap(r => r.places)
    val rPoints = array_of_points.map(p =>
      (p.latitude, p.longtitude, p.name, p.tourism, p.cultural, p.parks, p.leisure, p.sport, p.nightlife)
    )
    rPoints.foreach { line =>
      println("Name<- " + line._3)
      println("Coordinates<- " + line._1 + "," + line._2)
      if (!line._4.equals("None")) println("Tourism<- " + line._4)
      if (!line._5.equals("None")) println("Cultural<- " + line._5)
      if (!line._6.equals("None")) println("Parks<- " + line._6)
      if (!line._7.equals("None")) println("Leisure<- " + line._7)
      if (!line._8.equals("None")) println("Sport<- " + line._8)
      if (!line._9.equals("None")) println("Nightlife<- " + line._9)
      println("**********************************************************************")

    }
  }


}
