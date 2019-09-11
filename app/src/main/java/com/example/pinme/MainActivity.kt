package com.example.pinme

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton
import android.view.View
import com.example.pinme.model.LocalDatabase
import com.example.pinme.model.entity.PinDetails
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style
import android.widget.Toast
import android.annotation.SuppressLint
import android.widget.ImageView
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng

class MainActivity : AppCompatActivity(), OnMapReadyCallback, PermissionsListener {


    companion object {

        lateinit var pinDetails: PinDetails
        lateinit var mapBoxMap: MapboxMap

    }

    private val PIN_LIST = "pin_list"
    private val BACKSTACK_PIN = "pin_backstack"


    private lateinit var mapView: MapView
    private lateinit var floatingButton: FloatingActionButton
    private var localDB: LocalDatabase? = null
    private lateinit var permissionsManager: PermissionsManager

    private lateinit var imageView: ImageView
    private lateinit var locationComponent: LocationComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //initial declarations
        init()

        setContentView(R.layout.activity_main)

        //mapview declarations
        mapView = findViewById<View>(R.id.mapView) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        //FAB declarations
        floatingButton = findViewById(R.id.button)

        //on click of floating button , the list is generated
        floatingButton.setOnClickListener(View.OnClickListener { _ ->

            var manager = supportFragmentManager

            if (manager.fragments.size == 0) {

                floatingButton.setImageResource(R.drawable.ic_keyboard_arrow_left_black_24dp)

                //transaction the pin details list fragment
                var transacion = manager.beginTransaction()
                var pinList = PinListFragment()
                transacion.setCustomAnimations(R.anim.load_up, 0)
                transacion.add(R.id.fragment, pinList, PIN_LIST)
                transacion.addToBackStack(BACKSTACK_PIN)
                transacion.commit()

            } else {

                floatingButton.setImageResource(R.drawable.ic_format_list_bulleted_black_24dp)
                //getting back to the acitvity
                manager.popBackStack()
            }
        })

        //User location
        imageView = findViewById(R.id.userLocation)
        userLocationListner()

    }

    //initial declarations
    fun init() {

        Mapbox.getInstance(this, getString(R.string.access_token))

        localDB = LocalDatabase.getDB(this)
    }

    override fun onMapReady(mapboxMap: MapboxMap) {

        mapboxMap.setStyle(Style.MAPBOX_STREETS, Style.OnStyleLoaded { style ->
            mapBoxMap = mapboxMap

            //user location permission check and getting the location
            enableUserLocation(style)

            //call for fetching the datas
            FetchDetails(this, mapboxMap, mapView, style).execute()
        })
    }

    @SuppressLint("MissingPermission")
    fun enableUserLocation(style: Style) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Get an instance of the component
            locationComponent = mapBoxMap.getLocationComponent()

            locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(this, style).build())

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true)
            locationComponent.setRenderMode(RenderMode.COMPASS)

        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(this)
        }
    }

    // reuqest permission
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    //permission response
    override fun onPermissionResult(granted: Boolean) {
        if (granted) {

            enableUserLocation(mapBoxMap.style!!)

        } else {
            Toast.makeText(this, getString(R.string.location_not_permitted), Toast.LENGTH_LONG).show()
        }
    }

    //  explanation to permit
    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        Toast.makeText(this, getString(R.string.permission_explanation), Toast.LENGTH_LONG).show()
    }

    // on image click zooming into the user location
    fun userLocationListner() {
        imageView.setOnClickListener(View.OnClickListener { _ ->
            if (PermissionsManager.areLocationPermissionsGranted(this)) {
                val location = locationComponent.lastKnownLocation

                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    val cameraPosition =
                        CameraPosition.Builder().target(LatLng(latLng)).zoom(12.0).bearing(180.0).tilt(10.0).build()
                    mapBoxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000);
                }
            }else {
                Toast.makeText(this, getString(R.string.permission_explanation), Toast.LENGTH_LONG).show()
            }
        })

    }

    public override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    public override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    public override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    public override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        if (outState != null) {
            mapView.onSaveInstanceState(outState)
        }
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount

        if (count == 0) {
            super.onBackPressed()
        } else {
            // from fragment to activity
            supportFragmentManager.popBackStack()
        }
    }
}