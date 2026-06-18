package com.example.youtubeexplorer.network

import android.os.AsyncTask
import android.util.Log
import com.example.youtubeexplorer.model.Video
import org.json.JSONObject
import java.net.URL

class FetchVideosTask(
    private val onResult: (ArrayList<Video>) -> Unit
) : AsyncTask<Void, Void, ArrayList<Video>>() {

    private var errorMessage = ""

    override fun doInBackground(vararg params: Void?): ArrayList<Video> {

        val videos = ArrayList<Video>()

        try {

            val apiKey = "AIzaSyAEk7F_bbhTFUWxwJXDn5fzxviwCJYk7EY"

            val url =
                "https://www.googleapis.com/youtube/v3/search" +
                        "?part=snippet" +
                        "&maxResults=20" +
                        "&q=programming" +
                        "&type=video" +
                        "&key=$apiKey"

            val response = URL(url).readText()

            val json = JSONObject(response)

            val items = json.getJSONArray("items")

            for (i in 0 until items.length()) {

                val item = items.getJSONObject(i)

                val snippet = item.getJSONObject("snippet")

                val title = snippet.getString("title")
                val description = snippet.getString("description")
                val publishedAt = snippet.getString("publishedAt")

                val thumbnail =
                    snippet.getJSONObject("thumbnails")
                        .getJSONObject("medium")
                        .getString("url")

                videos.add(
                    Video(
                        title,
                        description,
                        thumbnail,
                        publishedAt
                    )
                )
            }

        } catch (e: Exception) {
            e.printStackTrace()
            errorMessage = e.message ?: "Unknown Error"
        }

        return videos
    }

    override fun onPostExecute(result: ArrayList<Video>) {

        if (result.isEmpty()) {
            Log.e("YOUTUBE_ERROR", errorMessage)
        }

        onResult(result)
    }
}