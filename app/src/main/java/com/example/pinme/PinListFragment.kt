package com.example.pinme

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pinme.adapter.PinViewAdapter
import com.example.pinme.model.LocalDatabase
import com.example.pinme.model.entity.PinDetails
import java.util.concurrent.Executors

class PinListFragment : Fragment(),BackPressed {

    private val TAG=this.javaClass.name

    private var localDB: LocalDatabase? = null

    private var pinViewAdapter: PinViewAdapter?= null

    private lateinit var recyclerView: RecyclerView
    private var pinList: ArrayList<PinDetails> = ArrayList()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.list_layout, container, false)

        recyclerView = view.findViewById(R.id.listView)

        var linearLayout = LinearLayoutManager(this.context)
        recyclerView.layoutManager = linearLayout

        localDB = LocalDatabase.getDB(activity as Context)

        pinViewAdapter = PinViewAdapter(pinList)

        //setting the adapter
        recyclerView.adapter = pinViewAdapter

        val executor = Executors.newSingleThreadExecutor()
        executor.execute(Runnable {

            //fetch all data's from the database
            loadData()

        })
        return view;

    }

    fun loadData() {

        pinList.clear()
        pinList.addAll(localDB!!.dao().fetchAllPinDetails())
        Log.d(TAG, pinList.toString())

        // Run on ui thread on fetching data few times
        activity?.runOnUiThread(Runnable {
            pinViewAdapter?.notifyDataSetChanged()
        })
    }

    override fun OnBackPressed() {
        fragmentManager?.popBackStack()
    }
}