package ru.magzyumov.coordinates.ui.main

import android.annotation.SuppressLint
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
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
    private var calculations: Calculations
    private var position: Long = 0

    init {
        coordinatesModel = Coordinates()
        retrofitWorker = RetrofitWorker()
        calculations = Calculations()
    }

    override fun getCoordinates() {
        retrofitWorker.addListeners(this)
        retrofitWorker.getCoordinates()
    }

    override fun takeDataFromServer(coordinates: Coordinates) {
        coordinatesModel = coordinates
        view?.dataReady(coordinatesModel)
        retrofitWorker.removeListeners(this)
    }

    override fun sendInfo(message: String) {
        view?.showMessage(message)
    }

    @SuppressLint("CheckResult")
    override fun switchTracking(){
        val count: Long = coordinatesModel.getCoordinates().size.toLong() - 1

        trackingEnable = !trackingEnable
        when (trackingEnable) {
            true -> view?.showMessage("Поехали!")
            false -> view?.showMessage("Перекур!")
        }

        Observable.intervalRange(position, count - position, 0, 1, TimeUnit.SECONDS)
            .takeWhile {trackingEnable}
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { positionNumber ->
                position = positionNumber
                if (positionNumber > 1) {
                    var speed = calculations.getSpeed(
                        coordinatesModel.getCoordinates()[positionNumber.toInt() - 1],
                        coordinatesModel.getCoordinates()[positionNumber.toInt()]
                    )
                    coordinatesModel.getCoordinates()[positionNumber.toInt()].setSpeed(speed)
                }
                view?.genNewPoint(coordinatesModel.getCoordinates()[positionNumber.toInt()])
            }
    }
}



