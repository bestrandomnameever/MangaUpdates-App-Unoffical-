package com.mangaupdates.android.mangaupdates_app_unofficial.fragments

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mangaupdates.android.mangaupdates_app_unofficial.MangaDetailActivity
import com.mangaupdates.android.mangaupdates_app_unofficial.R
import com.mangaupdates.android.mangaupdates_app_unofficial.models.EndlessRecyclerOnScrollListener
import com.mangaupdates.android.mangaupdates_app_unofficial.models.MANGACOVERSPACING
import com.mangaupdates.android.mangaupdates_app_unofficial.models.MINMANGACOVERWIDTH
import com.mangaupdates.android.mangaupdates_app_unofficial.models.MangaCoverLoader
import com.mangaupdates.android.mangaupdates_app_unofficial.network.MangaUpdatesAPI
import com.mangaupdates.android.mangaupdates_app_unofficial.utils.GridSpacingItemDecoration
import com.mangaupdates.android.mangaupdates_app_unofficial.utils.inflate
import com.mangaupdates.android.mangaupdates_app_unofficial.views.adapters.MangaCoversAdapter
import kotlinx.android.synthetic.main.fragment_releases_overview.view.*
import okhttp3.ResponseBody
import retrofit2.Call

class ReleasesOverviewFragment : Fragment(), MangaCoversAdapter.MangaCoversAdapterListener {

    lateinit var releasesRecylerVw: RecyclerView
    lateinit var mangaCoversAdapter: MangaCoversAdapter
    lateinit var mangaCoverLoader: MangaCoverLoader
    lateinit var dividerItemDecoration : RecyclerView.ItemDecoration
    lateinit var layoutManager : GridLayoutManager
    lateinit var endlessLoadingScrollListener : EndlessRecyclerOnScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        activity.title = "Releases"
        var view = container!!.inflate(R.layout.fragment_releases_overview)
        releasesRecylerVw = view.releasesCoversRecyclerView
        val idealColumns = getIdealAmountOfColumns()
        layoutManager = GridLayoutManager(context, idealColumns)
        mangaCoversAdapter = MangaCoversAdapter(this)
        releasesRecylerVw.layoutManager = layoutManager
        releasesRecylerVw.adapter = mangaCoversAdapter

        endlessLoadingScrollListener = object : EndlessRecyclerOnScrollListener(layoutManager) {
            override fun loadMore(page: Int) {
                mangaCoverLoader.loadIds(forPage = page)
            }
        }
        releasesRecylerVw.addOnScrollListener(endlessLoadingScrollListener)

        dividerItemDecoration = GridSpacingItemDecoration(idealColumns, MANGACOVERSPACING, true)
        releasesRecylerVw.addItemDecoration(dividerItemDecoration)

        mangaCoverLoader = object : MangaCoverLoader(mangaCoversAdapter, endlessLoadingScrollListener) {
            override fun makeCallForIds(forPage: Int): Call<ResponseBody> {
                return MangaUpdatesAPI.getReleasesForPage(forPage)
            }

        }
        mangaCoverLoader.loadIds(1)
        return view
    }



    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    fun getIdealAmountOfColumns(): Int{
        val displayWidth = Resources.getSystem().displayMetrics.widthPixels
        return (displayWidth - (displayWidth % MINMANGACOVERWIDTH) ) / MINMANGACOVERWIDTH
    }

    override fun openMangaDetailWithId(id: String) {
        val intent = Intent(context,MangaDetailActivity::class.java)
        context.startActivity(intent)
    }

    companion object {
        fun newInstance(): ReleasesOverviewFragment {
            val fragment = ReleasesOverviewFragment()
            return fragment
        }
    }
}
