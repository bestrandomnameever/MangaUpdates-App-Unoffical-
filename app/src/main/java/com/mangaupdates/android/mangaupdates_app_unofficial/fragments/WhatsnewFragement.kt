package com.mangaupdates.android.mangaupdates_app_unofficial.fragments

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import com.mangaupdates.android.mangaupdates_app_unofficial.MangaDetailActivity
import com.mangaupdates.android.mangaupdates_app_unofficial.R
import com.mangaupdates.android.mangaupdates_app_unofficial.models.MINMANGACOVERWIDTH
import com.mangaupdates.android.mangaupdates_app_unofficial.models.MangaUpdatesHTMLParser
import com.mangaupdates.android.mangaupdates_app_unofficial.network.MangaUpdatesAPI
import com.mangaupdates.android.mangaupdates_app_unofficial.utils.GridSpacingItemDecoration
import com.mangaupdates.android.mangaupdates_app_unofficial.utils.inflate
import com.mangaupdates.android.mangaupdates_app_unofficial.views.adapters.MangaCoversAdapter
import kotlinx.android.synthetic.main.fragment_whats_new.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * Created by Anthony on 19/03/2017.
 */
class WhatsnewFragement : Fragment(), MangaCoversAdapter.MangaCoversAdapterListener, AdapterView.OnItemSelectedListener {

    lateinit var recyclerview: RecyclerView
    lateinit var mangaCoversAdapter: MangaCoversAdapter
    lateinit var dividerItemDecoration : RecyclerView.ItemDecoration
    lateinit var layoutManager : RecyclerView.LayoutManager
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

        val idealColumns = getIdealAmountOfColumns()
        layoutManager = GridLayoutManager(context, idealColumns)
        mangaCoversAdapter = MangaCoversAdapter(this)
        recyclerview.layoutManager = layoutManager
        recyclerview.adapter = mangaCoversAdapter
        dividerItemDecoration = GridSpacingItemDecoration(idealColumns,20,true)
        recyclerview.addItemDecoration(dividerItemDecoration)
        initializeSpinners()
        loadIds()
        return view
    }

    private fun loadIds() {
        val order = orderSpinner.selectedItem.toString()
        val type = typeSpinner.selectedItem.toString()
        val year = yearSpinner.selectedItem.toString()
        progressBar.visibility = View.VISIBLE
        MangaUpdatesAPI.getIdsWhatsNewPage(order,type,year,1, object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                if (response!!.isSuccessful) {
                    val result = MangaUpdatesHTMLParser.getIds(response.body().string())
                    result.ids.forEachIndexed { index, s ->  mangaCoversAdapter.mangas.put(index, null) }
                    mangaCoversAdapter.notifyDataSetChanged()
                    result.ids.forEachIndexed { index, id ->
                        loadMangaFor(id, index)
                    }
                }else {
                    print("oeps")
                }
            }
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {}
        })
    }

    private fun loadMangaFor(id: String, index: Int) {
        MangaUpdatesAPI.getMangaWithId(id, object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                progressBar.visibility = View.GONE
                if (response!!.isSuccessful) {
                    val manga = MangaUpdatesHTMLParser.loadMangaFromHtml(response.body().string(), id)
                    mangaCoversAdapter.mangas.replace(index, manga)
                    mangaCoversAdapter.notifyItemChanged(index)
                }else {
                    print("Loading manga failed")
                }
            }

            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    override fun openMangaDetailWithId(id: String) {
        val intent = Intent(context, MangaDetailActivity::class.java)
        context.startActivity(intent)
    }

    private fun initializeSpinners() {
        val orderAdapter = ArrayAdapter.createFromResource(context,R.array.whatsNewOrder_array,android.R.layout.simple_spinner_item)
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
        mangaCoversAdapter.mangas.clear()
        mangaCoversAdapter.notifyDataSetChanged()
        loadIds()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    fun getIdealAmountOfColumns(): Int{
        val displayWidth = Resources.getSystem().displayMetrics.widthPixels
        return (displayWidth - (displayWidth % MINMANGACOVERWIDTH) ) / MINMANGACOVERWIDTH
    }

    companion object {
        fun newInstance() : Fragment {
            return WhatsnewFragement()
        }
    }
}