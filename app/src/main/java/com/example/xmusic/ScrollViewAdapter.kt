package com.example.xmusic

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView

class ScrollViewAdapter(
    private val context: Context,
    private val playlists: List<Map<String, Playlist>>,
    private val onClick: (Playlist) -> Unit
) {
    fun populateScrollView(scrollViewLayout: LinearLayout) {
        val inflater = LayoutInflater.from(context)
        for (playlistMap in playlists) {
            for (playlist in playlistMap.values) {
                val view = inflater.inflate(R.layout.playlist_item, scrollViewLayout, false)
                val textView = view.findViewById<TextView>(R.id.playlist_title)
                textView.text = playlist.title
                textView.setOnClickListener { onClick(playlist) }
                scrollViewLayout.addView(view)
            }
        }
    }
}
