package com.example.xmusic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
//import kotlinx.coroutines.flow.internal.NoOpContinuation.context
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context


class PlaylistAdapter(
    private val context: Context,
    private val library: Library,
//    private val playlists: List<Map<String, Playlist>>,
    private val onClick: (Playlist) -> Unit
) : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    class PlaylistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.playlist_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_item, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlistMap = library.playlists[position]
        val playlist = playlistMap.values.first()
        holder.title.text = playlist.title

        holder.itemView.setOnClickListener {
            val tracksFragment = TracksFragment.newInstance(playlist.id, library)


            val fragmentManager = (context as AppCompatActivity).supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, tracksFragment)
                .addToBackStack(null)
                .commit()
        }
    }


    override fun getItemCount() = library.playlists.size
}


