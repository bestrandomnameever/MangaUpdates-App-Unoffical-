package com.mangaupdates.android.mangaupdates_app_unofficial.feature_releases

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mangaupdates.android.mangaupdates_app_unofficial.R
import com.mangaupdates.android.mangaupdates_app_unofficial.utils.MangaCoverOverviewFragment
import com.mangaupdates.android.mangaupdates_app_unofficial.network.MangaUpdatesAPI
import com.mangaupdates.android.mangaupdates_app_unofficial.utils.inflate
import com.mangaupdates.android.mangaupdates_app_unofficial.utils.MangaCoversAdapter
import kotlinx.android.synthetic.main.fragment_releases_overview.view.*
import okhttp3.ResponseBody
import retrofit2.Call

class ReleasesOverviewFragment : MangaCoverOverviewFragment(), MangaCoversAdapter.MangaCoversAdapterListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        activity.title = "Releases"
        var view = container!!.inflate(R.layout.fragment_releases_overview)
        recyclerview = view.releasesCoversRecyclerView
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

    override fun setLoadingMethod(forPage: Int): Call<ResponseBody> {
        return MangaUpdatesAPI.getReleasesForPage(forPage)
    }

    companion object {
        fun newInstance(): ReleasesOverviewFragment {
            val fragment = ReleasesOverviewFragment()
            return fragment
        }
    }
}
