package ru.magzyumov.coordinates.util.retrofit

import ru.magzyumov.coordinates.model.Coordinates

interface IRetrofitWorker {
    fun takeDataFromServer(coordinates: Coordinates)
    fun sendInfo(message: String)
}