package com.example.xmusic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(private val tracks: List<Track>) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {class TrackViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val title: TextView = view.findViewById(R.id.trackTitle)
    val artists: TextView = view.findViewById(R.id.trackArtists)
    val duration: TextView = view.findViewById(R.id.trackDuration)
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackViewHolder(view)}

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.title.text = track.title
        holder.artists.text = track.artists.joinToString(", ") { it.name }
        holder.duration.text = track.duration
    }

    override fun getItemCount() = tracks.size
}