package ru.magzyumov.coordinates.ui.base

open class BasePresenter<V: IBaseContract.View>: IBaseContract.Presenter<V> {
    protected var view: V? = null

    override fun attachView(view: V) {
        this.view = view
    }

    override fun detachView() {
        view = null
    }

}