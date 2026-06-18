package com.example.youtubeexplorer.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.youtubeexplorer.R
import com.example.youtubeexplorer.utils.SharedData

class DetailsFragment : Fragment() {

    private lateinit var img: ImageView
    private lateinit var title: TextView
    private lateinit var date: TextView
    private lateinit var description: TextView


    override fun onResume() {
        super.onResume()
        refresh()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(
            R.layout.fragment_details,
            container,
            false
        )

        img = view.findViewById(R.id.imgDetails)
        title = view.findViewById(R.id.txtTitleDetails)
        date = view.findViewById(R.id.txtDate)
        description = view.findViewById(R.id.txtDescription)

        refresh()

        return view
    }

    fun refresh() {
        if (!isAdded) return

        SharedData.selectedVideo?.let { video ->

            title.text = video.title
            date.text = video.publishedAt
            description.text = video.description

            Glide.with(requireContext())
                .load(video.thumbnail)
                .into(img)
        }
    }
}