package com.example.pinme

import android.content.Context
import android.os.AsyncTask
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.example.pinme.model.LocalDatabase
import com.example.pinme.model.entity.PinDetails
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolClickListener
import com.mapbox.mapboxsdk.plugins.annotation.Symbol
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.mapboxsdk.style.layers.Property.ICON_ANCHOR_BOTTOM
import com.mapbox.mapboxsdk.utils.BitmapUtils
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class FetchDetails(var context: Context, var mapbox: MapboxMap, var mapView: MapView, var style: Style) :
    AsyncTask<Void, MapboxMap, ArrayList<PinDetails>>() {

    //Database
    var localDB: LocalDatabase? = null

    // intitalizers reagrding map details
    private lateinit var pinDetails: PinDetails
    private lateinit var pinDetails_list: ArrayList<PinDetails>
    private lateinit var latLong: LatLng
    private var symbolManager: SymbolManager? = null
    private lateinit var symbols: ArrayList<Symbol>
    private var latLngs: ArrayList<LatLng> = ArrayList()
    private var options: ArrayList<SymbolOptions> = ArrayList<SymbolOptions>()
    private val IMAGE = "image"

    // data url
    private val url_string = "https://annetog.gotenna.com/development/scripts/get_map_pins.php"

    override fun onPreExecute() {
        localDB = LocalDatabase.getDB(context)

    }

    override fun doInBackground(vararg params: Void?): ArrayList<PinDetails> {

        pinDetails_list = ArrayList<PinDetails>()
        var httpURLConnection: HttpURLConnection?

        val url = URL(url_string)
        try {
            httpURLConnection = url.openConnection() as HttpURLConnection

            val response_code = httpURLConnection.responseCode
            if (response_code == HttpURLConnection.HTTP_OK) {

                val inputStream = httpURLConnection.inputStream
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                val builder = StringBuilder()

                //data from url
                for (inputString in bufferedReader.readLine()) {
                    builder.append(inputString)
                }

                val jsonArray = JSONArray(builder.toString())

                //delete all data
                localDB!!.dao().deleteAllPinDetails()

                for (i in 0 until jsonArray.length()) {

                    val jsonObject = jsonArray.getJSONObject(i)
                    val id = jsonObject.getInt("id")
                    val name = jsonObject.getString("name")
                    val latitude = jsonObject.getDouble("latitude")
                    val longitude = jsonObject.getDouble("longitude")
                    val description = jsonObject.getString("description")
                    pinDetails = PinDetails(id, name, latitude, longitude, description)

                    //insert to DB
                    localDB!!.dao().insertPinData(pinDetails)

                    //adding each pin datas to the list
                    pinDetails_list.add(pinDetails)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return pinDetails_list
    }

    override fun onPostExecute(result: ArrayList<PinDetails>?) {

        //intializing the symbol manager
        symbolManager = SymbolManager(mapView, mapbox, style)
        symbolManager!!.setIconAllowOverlap(false)
        symbolManager!!.setTextAllowOverlap(false)

        //adding the marker image
        BitmapUtils.getBitmapFromDrawable(ContextCompat.getDrawable(context, R.drawable.map_marker_icons_1077349))?.let {
            style.addImage(IMAGE,
                it
            )
        }

        if (result != null && result.size > 0) {

            val latLngBounds = LatLngBounds.Builder()

            for (pin in result) {

                //getting the values : latitude,longitude,name,description
                val latitude = pin.latitude
                val longitude = pin.longitude
                val textField = pin.name + "\n" + pin.description
                latLong = LatLng(latitude, longitude)

                //adding a bounding area
                latLngBounds.include(latLong)

                //adding each latLng to list
                latLngs.add(latLong)

                //adding the marker
                val symbolOptions = SymbolOptions()
                    .withLatLng(latLong).withIconImage(IMAGE).withIconSize(0.09f).withIconAnchor(ICON_ANCHOR_BOTTOM)
                    .withTextField(textField).withTextSize(12f).withTextAnchor("top")

                // for multiple markers
                options.add(symbolOptions)

                symbolManager!!.addClickListener(OnSymbolClickListener { symbol: Symbol? ->
                    if (symbol != null) {
                        Toast.makeText(context, symbol.textField, Toast.LENGTH_SHORT).show()
                    }
                })
            }

            // bounding the area according to the latlng values
            mapbox.easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds.build(), 50), 1000)

            symbols = symbolManager!!.create(options) as ArrayList<Symbol>

        }

    }

}