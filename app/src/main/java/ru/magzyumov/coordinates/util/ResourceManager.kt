package ru.magzyumov.coordinates.util

import android.content.Context
import ru.magzyumov.coordinates.App
import ru.magzyumov.coordinates.R

import javax.inject.Inject

class ResourceManager  {
    @Inject
    lateinit var context: Context

    init {
        App.getComponent().inject(this)
    }

    fun getStartString(): String{ return context.getString(R.string.start) }
    fun getPauseString(): String{ return context.getString(R.string.pause) }
    fun getFinishString(): String{ return context.getString(R.string.finish) }
}