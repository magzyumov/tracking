package ru.magzyumov.coordinates.ui.base

interface IBaseContract {
    interface View

    interface Presenter<V : View?> {
        fun attachView(view: V)
        fun detachView()
    }
}