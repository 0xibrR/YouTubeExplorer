package com.example.youtubeexplorer.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.youtubeexplorer.MainActivity
import com.example.youtubeexplorer.R
import com.example.youtubeexplorer.adapter.VideoAdapter
import com.example.youtubeexplorer.model.Video
import com.example.youtubeexplorer.network.FetchVideosTask
import com.example.youtubeexplorer.utils.NotificationHelper
import com.example.youtubeexplorer.utils.SharedData

class VideosFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private val allVideos = ArrayList<Video>()
    private lateinit var adapter: VideoAdapter
    private lateinit var swipeRefresh: SwipeRefreshLayout

    var detailsFragment: DetailsFragment? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(
            R.layout.fragment_videos,
            container,
            false
        )

        recyclerView = view.findViewById(R.id.recyclerView)
        progressBar = view.findViewById(R.id.progressBar)
        swipeRefresh = view.findViewById(R.id.swipeRefresh)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        swipeRefresh.setOnRefreshListener {
            loadVideos()
        }

        loadVideos()

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadVideos() {

        progressBar.visibility = View.VISIBLE

        FetchVideosTask { videos ->

            progressBar.visibility = View.GONE
            swipeRefresh.isRefreshing = false

            if (videos.isNotEmpty()) {

                SharedData.selectedVideo = videos[0]

                allVideos.clear()
                allVideos.addAll(videos)

                adapter = VideoAdapter(videos.toMutableList()) { video ->

                    SharedData.selectedVideo = video

                    detailsFragment?.refresh()

                    (requireActivity() as MainActivity)
                        .findViewById<ViewPager2>(R.id.viewPager)
                        .currentItem = 1
                }

                recyclerView.adapter = adapter

                NotificationHelper.showNotification(
                    requireContext(),
                    "Success",
                    "Videos loaded successfully"
                )

            } else {

                NotificationHelper.showNotification(
                    requireContext(),
                    "Error",
                    "Failed to load videos"
                )
            }

        }.execute()
    }

    fun filterVideos(query: String) {

        if (!::adapter.isInitialized) return

        val filtered = allVideos.filter {
            it.title.contains(query, ignoreCase = true)
        }

        adapter.updateList(filtered)
    }
}