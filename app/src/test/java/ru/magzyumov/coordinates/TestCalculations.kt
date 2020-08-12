package ru.magzyumov.coordinates

import com.google.android.gms.maps.model.LatLng
import org.junit.Before
import org.junit.Test
import ru.magzyumov.coordinates.model.Coordinates.Coordinate
import ru.magzyumov.coordinates.util.Calculations

class TestCalculations {
    private var calculations: Calculations? = null
    private var coordinateStart: Coordinate? = null
    private var coordinateEnd: Coordinate? = null

    @Before
    fun startUp() {
        calculations = Calculations()
        coordinateStart = Coordinate("2019-06-28T07:33:03", LatLng(37.610225, 55.651365))
        coordinateEnd = Coordinate("2019-06-28T07:33:38", LatLng(37.610785, 55.6510233333333))
    }

    @Test
    fun testGetDuration() {
        if (coordinateStart != null && coordinateEnd != null ){
            calculations?.getDuration(coordinateStart!!, coordinateEnd!!)
        }
    }

    @Test
    fun testGetDistance() {
        if (coordinateStart != null && coordinateEnd != null ){
            calculations?.getDistance(coordinateStart!!, coordinateEnd!!)
        }
    }

    @Test
    fun testGetSpeed() {
        if (coordinateStart != null && coordinateEnd != null ){
            calculations?.getSpeed(coordinateStart!!, coordinateEnd!!)
        }
    }
}