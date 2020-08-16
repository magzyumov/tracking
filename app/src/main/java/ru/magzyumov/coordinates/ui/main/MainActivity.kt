package ru.magzyumov.coordinates.ui.main


import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_main.*
import ru.magzyumov.coordinates.R
import ru.magzyumov.coordinates.model.Coordinates
import ru.magzyumov.coordinates.model.Coordinates.Coordinate
import ru.magzyumov.coordinates.util.LatLngInterpolator


class MainActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMapClickListener, IMainContract.View {

    private lateinit var map: GoogleMap
    private var presenter: IMainContract.Presenter? = null
    private var coordinates: Coordinates = Coordinates()
    private lateinit var startMarker: Marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mapFrame.visibility = GONE

        presenter = MainPresenter()
        (presenter as MainPresenter).attachView(this)

        if (presenter != null) {
            presenter!!.getCoordinates()
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        textViewSpeed.text = getString(R.string.dataPreparation)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnMapClickListener(this)
    }

    private fun drawRoute() {
        val line = PolylineOptions()
        line.width(8f).color(R.color.colorAccent)
        val latLngBuilder = LatLngBounds.Builder()
        for (i in coordinates.getCoordinates().indices) {
            if (i == 0) {
                val startMarkerOptions = MarkerOptions()
                    .position(coordinates.getCoordinates()[i].getPoint())
                    .title(coordinates.getCoordinates()[i].getTimeStamp())
                startMarker = map.addMarker(startMarkerOptions)
            }
            line.add(coordinates.getCoordinates()[i].getPoint())
            latLngBuilder.include(coordinates.getCoordinates()[i].getPoint())
        }
        map.addPolyline(line)
        val size = resources.displayMetrics.widthPixels
        val latLngBounds = latLngBuilder.build()
        val track = CameraUpdateFactory.newLatLngBounds(latLngBounds, size, size, 25)
        map.moveCamera(track);
    }

    private fun moveMarker(marker: Marker, finalPosition: LatLng) {
        val latLngInterpolator: LatLngInterpolator = LatLngInterpolator.LinearFixed()
        val startPosition = marker.position
        val handler = Handler()
        val start = SystemClock.uptimeMillis()
        val interpolator: Interpolator = AccelerateDecelerateInterpolator()
        val durationInMs = 300f
        handler.post(object: Runnable {
            override fun run() {
                val elapsed = SystemClock.uptimeMillis() - start
                val t = elapsed / durationInMs
                val v = interpolator.getInterpolation(t)
                marker.position = latLngInterpolator.interpolate(v, startPosition, finalPosition)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 16f))

                if (t < 1) {
                    handler.postDelayed(this, 16)
                }
            }
        })
    }

    override fun dataReady(_coordinates: Coordinates) {
        coordinates = _coordinates
        progressBar.visibility = GONE
        mapFrame.visibility = VISIBLE
        drawRoute()
        textViewSpeed.text = getString(R.string.trackReady)
    }

    override fun showMessage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }


    override fun genNewPoint(coordinate: Coordinate) {
        moveMarker(startMarker, coordinate.getPoint())
        var speed = String.format(getString(R.string.speed), coordinate.getSpeed())
        textViewSpeed.text = speed
    }
    
    override fun onMapClick(p0: LatLng?) {
        presenter?.switchTracking()
    }

}

