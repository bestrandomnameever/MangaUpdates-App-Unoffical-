package com.mangaupdates.android.mangaupdates_app_unofficial.feature.discover

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import com.mangaupdates.android.mangaupdates_app_unofficial.R
import com.mangaupdates.android.mangaupdates_app_unofficial.models.MangaAdapterLoader
import com.mangaupdates.android.mangaupdates_app_unofficial.network.MangaUpdatesAPI
import com.mangaupdates.android.mangaupdates_app_unofficial.utils.MangaCoverOverviewFragment
import com.mangaupdates.android.mangaupdates_app_unofficial.utils.MangaCoversAdapter
import com.mangaupdates.android.mangaupdates_app_unofficial.utils.inflate
import kotlinx.android.synthetic.main.fragment_whats_new.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import java.util.*

/**
 * Created by Anthony on 19/03/2017.
 */
class WhatsnewFragement : MangaCoverOverviewFragment(), MangaCoversAdapter.MangaCoversAdapterListener, AdapterView.OnItemSelectedListener {

    lateinit var progressBar : ProgressBar
    lateinit var orderSpinner : Spinner
    lateinit var typeSpinner : Spinner
    lateinit var yearSpinner : Spinner

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = container!!.inflate(R.layout.fragment_whats_new)
        recyclerview = view.whatsNewRecyclerview
        progressBar = view.whatsNewLoading
        orderSpinner = view.orderSpinner
        typeSpinner = view.typeSpinner
        yearSpinner = view.yearSpinner
        initializeSpinners()

        return view
    }

    private fun initializeSpinners() {
        val orderAdapter = ArrayAdapter.createFromResource(context, R.array.whatsNewOrder_array,android.R.layout.simple_spinner_item)
        orderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        orderSpinner.adapter = orderAdapter
        val typeAdapter = ArrayAdapter.createFromResource(context, R.array.whatsNewType_array, android.R.layout.simple_spinner_item)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        typeSpinner.adapter = typeAdapter
        val years = mutableListOf<String>()
        for (i in 2008..Calendar.getInstance().get(Calendar.YEAR)) {
            years.add(i.toString())
        }
        val yearAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, years.reversed())
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        yearSpinner.adapter = yearAdapter
        orderSpinner.onItemSelectedListener = this
        typeSpinner.onItemSelectedListener = this
        yearSpinner.onItemSelectedListener = this
    }

    override fun onItemSelected(adapter: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        reload()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun getMangaAdapterLoader(): MangaAdapterLoader {
        return object : MangaAdapterLoader {
            override fun getIdsForPage(page: Int): Call<ResponseBody> {
                val order = orderSpinner.selectedItem.toString().toLowerCase()
                val type = typeSpinner.selectedItem.toString()
                val year = yearSpinner.selectedItem.toString()
                return MangaUpdatesAPI.getIdsWhatsNewPage(orderBy = order, type = type, year = year, page = page)
            }
        }
    }

    companion object {
        fun newInstance() : Fragment {
            return WhatsnewFragement()
        }
    }
}