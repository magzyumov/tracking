package ru.magzyumov.coordinates.util

import ru.magzyumov.coordinates.model.Coordinates.Coordinate
import java.text.SimpleDateFormat
import java.lang.Math.*
import java.util.*

class Calculations {

    fun getSpeed(coordinateStart: Coordinate, coordinateEnd: Coordinate): Int{
        return getDistance(coordinateStart, coordinateEnd) / getDuration(coordinateStart, coordinateEnd)
    }

    fun getDistance(coordinateStart: Coordinate, coordinateEnd: Coordinate): Int {
        val earthRadiusKm = 6378.1370
        val d2R = PI / 180.0

        val lat1 = coordinateStart.getPoint().latitude
        val lat2 = coordinateEnd.getPoint().latitude
        val lon1 = coordinateStart.getPoint().longitude
        val lon2 = coordinateEnd.getPoint().longitude

        val dLon = (lon2-lon1) * d2R
        val dLat = (lat2-lat1) * d2R

        val a = pow(sin(dLat / 2.0), 2.0 ) + (cos(lat1 * d2R) * cos(lat2 * d2R) * pow(sin(dLon / 2.0), 2.0))
        val c = 2.0 * atan2(sqrt(a), sqrt(1.0 - a))
        val d: Double = earthRadiusKm * c

        return (1000.0 * d).toInt()
    }

    fun getDuration(coordinateStart: Coordinate, coordinateEnd: Coordinate): Int{
        var result = 0

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

        val startDate = dateFormat.parse(coordinateStart.getTimeStamp())
        val endDate = dateFormat.parse(coordinateEnd.getTimeStamp())

        if ((startDate != null) && (endDate != null)){
            result = ((endDate.time - startDate.time ) / 1000.0).toInt()
        }

        return result
    }


}