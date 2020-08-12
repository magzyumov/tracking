package ru.magzyumov.coordinates.util.retrofit

import com.google.android.gms.maps.model.LatLng
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import org.json.JSONArray
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

import ru.magzyumov.coordinates.model.Coordinates
import ru.magzyumov.coordinates.model.Coordinates.Coordinate
import ru.magzyumov.coordinates.model.ICoordinatesRequest

class RetrofitWorker {
    private var retrofit: Retrofit
    private var request: ICoordinatesRequest
    private var listeners: MutableList<IRetrofitWorker> = ArrayList()

    init{
        retrofit = Retrofit.Builder()
            .baseUrl("https://kz.skif.me/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build();

        request = retrofit.create(ICoordinatesRequest::class.java)
    }

    fun addListeners(listener: IRetrofitWorker){ this.listeners.add(listener)}
    fun removeListeners(listener: IRetrofitWorker){ this.listeners.remove(listener)}

    fun getCoordinates() {
        request.getCoordinatesFromServer()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map(parseResponseFromServer)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: DisposableSingleObserver<Coordinates>() {
                override fun onSuccess(coordinates: Coordinates) {
                    dataReady(coordinates)
                }
                override fun onError(e: Throwable) {
                    sendMessage(Thread.currentThread().name + " " + e.message)
                }
            })
    }

    private fun dataReady(coordinates: Coordinates){
        for (listener in listeners) {
            listener.takeDataFromServer(coordinates)
        }
    }

    private fun sendMessage(message: String){
        for (listener in listeners) {
            listener.sendInfo(message)
        }
    }

    private val parseResponseFromServer: Function<String, Coordinates> =
        Function { jsonCoordinates: String ->

            var coordinates = Coordinates()
            val jsonArray = JSONArray(jsonCoordinates)

            for (i in 0 until jsonArray.length()) {
                val innerJsonArray = jsonArray.getJSONArray(i)

                var coordinate = Coordinate(innerJsonArray.getString(0),
                    LatLng(innerJsonArray.getString(1).toDouble(), innerJsonArray.getString(2).toDouble()))

                coordinates.getCoordinates().add(coordinate)
            }
            coordinates
        }
}