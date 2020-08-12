package ru.magzyumov.coordinates.model


import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface ICoordinatesRequest {
    @GET("coordinates.json")
    fun getCoordinatesFromServer(): Single<String>
}