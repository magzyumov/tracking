package ru.magzyumov.coordinates.ui.main

import ru.magzyumov.coordinates.model.Coordinates
import ru.magzyumov.coordinates.model.Coordinates.Coordinate
import ru.magzyumov.coordinates.ui.base.IBaseContract

interface IMainContract: IBaseContract.View {

    interface View: IBaseContract.View {
        fun dataReady(coordinates: Coordinates)
        fun showMessage(message: String)
        fun genNewPoint(coordinate: Coordinate)
    }

    interface Presenter: IBaseContract.Presenter<View> {
        fun getCoordinates()
        fun switchTracking()
    }

}