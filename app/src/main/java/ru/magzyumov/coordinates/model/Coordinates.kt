package ru.magzyumov.coordinates.model

import com.google.android.gms.maps.model.LatLng

class Coordinates {
    private var coordinates: ArrayList<Coordinate> = ArrayList()

    fun getCoordinates(): ArrayList<Coordinate> {return coordinates }
    fun setCoordinates(_coordinates: ArrayList<Coordinate>) {coordinates = _coordinates}

    class Coordinate(_timeStamp: String, _point: LatLng, _speed: Int = 0) {
        private var timeStamp: String =_timeStamp
        private var point: LatLng = _point
        private var speed: Int = _speed


        fun getTimeStamp(): String {return timeStamp }
        fun setTimeStamp(_timeStamp: String) {timeStamp = _timeStamp}

        fun getPoint(): LatLng {return point }
        fun setPoint(_point: LatLng) {point = _point}

        fun getSpeed(): Int {return speed }
        fun setSpeed(_speed: Int) {speed = _speed}
    }
}