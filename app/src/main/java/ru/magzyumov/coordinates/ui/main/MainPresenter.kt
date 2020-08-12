package ru.magzyumov.coordinates.ui.main

import com.google.android.gms.maps.model.LatLng
import hu.akarnokd.rxjava3.operators.FlowableTransformers
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.processors.PublishProcessor
import io.reactivex.rxjava3.schedulers.Schedulers
import org.json.JSONArray
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

import ru.magzyumov.coordinates.model.Coordinates
import ru.magzyumov.coordinates.model.Coordinates.Coordinate
import ru.magzyumov.coordinates.model.ICoordinatesRequest
import ru.magzyumov.coordinates.ui.base.BasePresenter
import java.util.concurrent.TimeUnit


class MainPresenter: BasePresenter<IMainContract.View>(), IMainContract.Presenter {
    private lateinit var coordinatesModel: Coordinates
    private lateinit var retrofit: Retrofit
    private lateinit var request: ICoordinatesRequest
    private var trackingEnable: Boolean = false
    private var trackingStarted: Boolean = false
    private val valve = PublishProcessor.create<Boolean>()


    override fun init(){
        retrofit = Retrofit.Builder()
            .baseUrl("https://kz.skif.me/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build();

        request = retrofit.create(ICoordinatesRequest::class.java)
        coordinatesModel = Coordinates()
    }

    override fun getCoordinates() {
        request.getCoordinatesFromServer()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map(parseResponseFromServer)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: DisposableSingleObserver<Coordinates>() {
                override fun onSuccess(coordinates: Coordinates) {
                    coordinatesModel.setCoordinates(coordinates.getCoordinates()!!)
                    view?.showMessage("Данные готовы!")
                    view?.dataReady(coordinatesModel)
                }
                override fun onError(e: Throwable) {
                    view?.showMessage(Thread.currentThread().name + " " + e.message)
                }
            })
    }

    override fun startTracking() {
        val count: Long = coordinatesModel.getCoordinates().size.toLong()
        Flowable.interval(500, TimeUnit.MILLISECONDS)
            .compose(FlowableTransformers.valve(valve, true))
            .take(count)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { positionNumber ->
                view?.genNewPoint(coordinatesModel.getCoordinates()[positionNumber.toInt()])
                }
        view?.showMessage("Поехали!")
        trackingStarted = true
    }

    private val parseResponseFromServer: Function<String, Coordinates> =
        Function { jsonCoordinates: String ->

            var coordinates = Coordinates()
            val jsonArray = JSONArray(jsonCoordinates)

            for (i in 0 until jsonArray.length()) {
                val innerJsonArray = jsonArray.getJSONArray(i)
                coordinates.getCoordinates().add(Coordinate(innerJsonArray.getString(0),
                    LatLng(innerJsonArray.getString(1).toDouble(), innerJsonArray.getString(2).toDouble())))
            }
            coordinates
        }

    override fun switchTracking() {
        trackingEnable = !trackingEnable
        if (!trackingEnable) {
            view?.showMessage("Перекур!")
        } else {
            view?.showMessage("Поехали!")
        }
        valve.onNext(trackingEnable)
    }

    override fun getTracking(): Boolean {return trackingStarted}
}



