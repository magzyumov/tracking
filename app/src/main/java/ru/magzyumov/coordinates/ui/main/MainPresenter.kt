package ru.magzyumov.coordinates.ui.main

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import ru.magzyumov.coordinates.App
import ru.magzyumov.coordinates.model.Coordinates
import ru.magzyumov.coordinates.ui.base.BasePresenter
import ru.magzyumov.coordinates.util.Calculations
import ru.magzyumov.coordinates.util.ResourceManager
import ru.magzyumov.coordinates.util.retrofit.IRetrofitWorker
import ru.magzyumov.coordinates.util.retrofit.RetrofitWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class MainPresenter: BasePresenter<IMainContract.View>(), IMainContract.Presenter,
    IRetrofitWorker {

    @Inject
    lateinit var resourceManager: ResourceManager

    private var coordinatesModel: Coordinates
    private var retrofitWorker: RetrofitWorker
    private var trackingEnable: Boolean = false
    private var calculations: Calculations
    private var position: Long = 0
    private lateinit var disposable: Disposable

    init {
        coordinatesModel = Coordinates()
        retrofitWorker = RetrofitWorker()
        calculations = Calculations()
        App.getComponent().inject(this)
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

    override fun switchTracking(){

        trackingEnable = !trackingEnable

        when(trackingEnable){
            true -> {startTrack()}
            false -> {disposable.dispose()}
        }
    }

    private fun startTrack(){
        val count: Long = coordinatesModel.getCoordinates().size.toLong() - 1
        disposable = Observable.intervalRange(position, count - position, 0, 500, TimeUnit.MILLISECONDS)
            .doOnDispose{view?.showMessage(resourceManager.getPauseString())}
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableObserver<Long>() {
                override fun onStart() {
                    view?.showMessage(resourceManager.getStartString())
                }

                override fun onComplete() {
                    view?.showMessage(resourceManager.getFinishString())
                }

                override fun onNext(positionNumber: Long) {
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

                override fun onError(error: Throwable) {
                    view?.showMessage(Thread.currentThread().name + " " + error.message)
                }
            }
            )
    }
}



