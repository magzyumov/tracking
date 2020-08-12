package ru.magzyumov.coordinates.util

import ru.magzyumov.coordinates.model.Coordinates
import java.text.SimpleDateFormat
import java.lang.Math.*
import java.util.*

class Calculations {

    fun getSpeed(coordinateStart: Coordinates.Coordinate, coordinateEnd: Coordinates.Coordinate): Int{
        return getDistance(coordinateStart, coordinateEnd) / getDuration(coordinateStart, coordinateEnd)
    }

    fun getDistance(coordinateStart: Coordinates.Coordinate, coordinateEnd: Coordinates.Coordinate): Int {
        val earthRadiusKm = 6378.1370;
        val d2R = PI / 180.0

        var lat1 = coordinateStart.getPoint().latitude
        var lat2 = coordinateEnd.getPoint().latitude
        var lon1 = coordinateStart.getPoint().longitude
        var lon2 = coordinateEnd.getPoint().longitude

        var dLon = (lon2-lon1) * d2R
        var dLat = (lat2-lat1) * d2R

        val a = Math.pow(sin(dLat / 2.0), 2.0 ) + (Math.cos(lat1 * d2R) * cos(lat2 * d2R) * pow(sin(dLon / 2.0), 2.0))
        val c = 2.0 * atan2(Math.sqrt(a), sqrt(1.0 - a))
        val d: Double = earthRadiusKm * c

        return (1000.0 * d).toInt()
    }

    fun getDuration(coordinateStart: Coordinates.Coordinate, coordinateEnd: Coordinates.Coordinate): Int{
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

        val startDate: Date = dateFormat.parse(coordinateStart.getTimeStamp())
        val endDate: Date = dateFormat.parse(coordinateEnd.getTimeStamp())

        return ((endDate.time - startDate.time ) / 1000.0).toInt()
    }


}