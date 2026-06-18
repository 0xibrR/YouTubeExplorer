package com.example.youtubeexplorer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.youtubeexplorer.R
import com.example.youtubeexplorer.model.Video

class VideoAdapter(
    private var videos: MutableList<Video>,
    private val onClick: (Video) -> Unit
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgThumbnail: ImageView = itemView.findViewById(R.id.imgThumbnail)
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val txtDate: TextView = itemView.findViewById(R.id.txtDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_video, parent, false)

        return VideoViewHolder(view)
    }

    override fun getItemCount(): Int = videos.size

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {

        val video = videos[position]

        holder.txtTitle.text = video.title
        holder.txtDate.text = video.publishedAt.take(10)

        Glide.with(holder.itemView.context)
            .load(video.thumbnail)
            .into(holder.imgThumbnail)

        holder.itemView.setOnClickListener {
            onClick(video)
        }
    }

    fun updateList(newList: List<Video>) {
        videos.clear()
        videos.addAll(newList)
        notifyDataSetChanged()
    }
}