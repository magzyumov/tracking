package ru.magzyumov.coordinates.ui.main

import hu.akarnokd.rxjava3.operators.FlowableTransformers
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.processors.PublishProcessor
import ru.magzyumov.coordinates.model.Coordinates
import ru.magzyumov.coordinates.ui.base.BasePresenter
import ru.magzyumov.coordinates.util.Calculations
import ru.magzyumov.coordinates.util.retrofit.IRetrofitWorker
import ru.magzyumov.coordinates.util.retrofit.RetrofitWorker
import java.util.concurrent.TimeUnit


class MainPresenter: BasePresenter<IMainContract.View>(), IMainContract.Presenter,
    IRetrofitWorker {

    private var coordinatesModel: Coordinates
    private var retrofitWorker: RetrofitWorker
    private var trackingEnable: Boolean = false
    private var trackingStarted: Boolean = false
    private val valve = PublishProcessor.create<Boolean>()
    private var calculations: Calculations

    init {
        coordinatesModel = Coordinates()
        retrofitWorker = RetrofitWorker()
        calculations = Calculations()
    }

    override fun getCoordinates() {
        retrofitWorker.addListeners(this)
        retrofitWorker.getCoordinates()
    }

    override fun startTracking() {
        val count: Long = coordinatesModel.getCoordinates().size.toLong()
        Flowable.interval(1, TimeUnit.SECONDS)
            .compose(FlowableTransformers.valve(valve, true ))
            .take(count)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { positionNumber ->
                if (positionNumber > 1) {
                    var speed = calculations.getSpeed(
                        coordinatesModel.getCoordinates()[positionNumber.toInt() - 1],
                        coordinatesModel.getCoordinates()[positionNumber.toInt()]
                    )
                    coordinatesModel.getCoordinates()[positionNumber.toInt()].setSpeed(speed)
                }
                view?.genNewPoint(coordinatesModel.getCoordinates()[positionNumber.toInt()])
            }
        view?.showMessage("Поехали!")
        trackingStarted = true
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

    override fun getTrackingStatus(): Boolean {return trackingStarted}

    override fun takeDataFromServer(coordinates: Coordinates) {
        coordinatesModel = coordinates
        view?.dataReady(coordinatesModel)
        retrofitWorker.removeListeners(this)
    }

    override fun sendInfo(message: String) {
        view?.showMessage(message)
    }
}



