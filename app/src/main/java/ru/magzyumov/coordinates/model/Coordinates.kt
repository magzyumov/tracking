package ru.magzyumov.coordinates.model

import com.google.android.gms.maps.model.LatLng

class Coordinates {
    private var coordinates: ArrayList<Coordinate> = ArrayList()

    fun getCoordinates(): ArrayList<Coordinate> {return coordinates }
    fun setCoordinates(_coordinates: ArrayList<Coordinate>) {coordinates = _coordinates}

    class Coordinate(_timeStamp: String, _point: LatLng) {
        private var timeStamp =_timeStamp
        private var point = _point

        fun getTimeStamp(): String? {return timeStamp }
        fun setTimeStamp(_timeStamp: String) {timeStamp = _timeStamp}

        fun getPoint(): LatLng {return point }
        fun setPoint(_point: LatLng) {point = _point}
    }
}