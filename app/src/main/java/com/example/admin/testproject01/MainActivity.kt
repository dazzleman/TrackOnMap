package com.example.admin.testproject01

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.admin.testproject01.model.ResponseData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    enum class SpeedState { NONE, STATE_ONE, STATE_TWO, STATE_THREE, STATE_FOUR }

    private lateinit var mMap: GoogleMap

    private var prevSpeedState: SpeedState = SpeedState.NONE
    private var currentSpeedState: SpeedState = SpeedState.STATE_FOUR
    private var colorString: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        loadTrack()
                .flatMap { parseJson(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    lateinit var polyLine: PolylineOptions

                    val middleArray = (it.aPoints.size / 2)
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(it.aPoints[middleArray][0], it.aPoints[middleArray][1])))

                    for (i in 0 until it.aPoints.size) {
                        val currentSpeed = it.aPoints[i][2]
                        val lat = it.aPoints[i][0]
                        val lon = it.aPoints[i][1]

                        setCurrentSpeedAndColor(currentSpeed)

                        if (currentSpeedState != prevSpeedState) {
                            if (i != 0) mMap.addPolyline(polyLine)
                            polyLine = createPolylineOptions(colorString)
                            if (i != 0) addCoordinateToPolyLine(polyLine, LatLng(it.aPoints[i - 1][0], it.aPoints[i - 1][1]))
                            addCoordinateToPolyLine(polyLine, LatLng(lat, lon))
                            prevSpeedState = currentSpeedState
                        } else {
                            addCoordinateToPolyLine(polyLine, LatLng(lat, lon))
                        }
                    }
                })
    }

    private fun createPolylineOptions(colorString: String): PolylineOptions {
        return PolylineOptions()
                .color(Color.parseColor(colorString))
                .width(8.toFloat())
                .geodesic(true)
    }

    private fun loadTrack(): Observable<String> {
        return Observable.fromCallable({
            val url = URL("http://avionicus.com/android/track_v0649.php?avkey=1M1TE9oeWTDK6gFME9JYWXqpAGc%3D&hash=58ecdea2a91f32aa4c9a1d2ea010adcf2348166a04&track_id=36131&user_id=22")
            var bufferedReader: BufferedReader? = null
            val sb = StringBuilder()
            try {
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"
                conn.readTimeout = 10000
                conn.connect()

                val responseCode = conn.responseCode
                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    bufferedReader = BufferedReader(InputStreamReader(conn.inputStream))
                    var line: String? = ""

                    while (line != null) {
                        line = bufferedReader.readLine()
                        if (line != null) sb.append(line)
                    }
                    bufferedReader.close()
                }
            } finally {
                if (bufferedReader != null) {
                    bufferedReader.close()
                }
            }
            sb.toString()
        })
    }

    private fun parseJson(jsonString: String): Observable<ResponseData> {
        return Observable.fromCallable({
            val builder = GsonBuilder()
            val gson = builder.create()
            gson.fromJson(jsonString, ResponseData::class.java)
        })
    }

    private fun setCurrentSpeedAndColor(currentSpeed: Double) {
        if (currentSpeed <= 10) {
            currentSpeedState = SpeedState.STATE_ONE
            colorString = "#40E0D0"
        } else if (currentSpeed > 10 && currentSpeed < 30) {
            currentSpeedState = SpeedState.STATE_TWO
            colorString = "#008000"
        } else if (currentSpeed >= 30 && currentSpeed < 50) {
            currentSpeedState = SpeedState.STATE_THREE
            colorString = "#FFFF00"
        } else {
            currentSpeedState = SpeedState.STATE_FOUR
            colorString = "#FFFF00"
        }
    }

    private fun addCoordinateToPolyLine(polyLine: PolylineOptions, latLan: LatLng) {
        polyLine.add(latLan)
    }
}

