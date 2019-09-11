package com.example.pinme.adapter

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.pinme.MainActivity
import com.example.pinme.R
import com.example.pinme.model.entity.PinDetails
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng

class PinViewAdapter(var pinDetailsList: ArrayList<PinDetails>) : RecyclerView.Adapter<PinViewAdapter.PinViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): PinViewHolder {

        var view = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_item_layout, viewGroup, false)
        var viewHolder = PinViewHolder(view)

        return viewHolder

    }

    override fun getItemCount(): Int {
        return pinDetailsList.count()
    }

    override fun onBindViewHolder(viewHolder: PinViewHolder, pos: Int) {

        viewHolder.name.text = pinDetailsList.get(pos).name
        viewHolder.description.text = pinDetailsList.get(pos).description

        //on list item click
        viewHolder.layout.setOnClickListener(View.OnClickListener { _: View? ->

            MainActivity.pinDetails = pinDetailsList.get(pos)
            var lat = MainActivity.pinDetails.latitude
            var lon = MainActivity.pinDetails.longitude
            var latLong = LatLng(lat, lon)

            //Zooming the camera to the location on list item click
            var cameraPosition =
                CameraPosition.Builder().target(LatLng(latLong)).zoom(20.0).bearing(180.0).tilt(30.0).build()
            MainActivity.mapBoxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000);

        })
    }

    //PinViewHolder class
    class PinViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var name = view.findViewById<TextView>(R.id.name)
        var description = view.findViewById<TextView>(R.id.description)
        var layout = view.findViewById<ConstraintLayout>(R.id.constraint)

    }
}