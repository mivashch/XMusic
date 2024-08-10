package com.example.xmusic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xmusic.Library
import com.example.xmusic.R
import com.example.xmusic.Track
import com.example.xmusic.TrackAdapter


class TracksFragment : Fragment() {

    private var playlistId: String? = null
    private var library: Library? = null

    companion object {
        fun newInstance(playlistId: String, library: Library): TracksFragment {
            val fragment= TracksFragment()
            val args = Bundle()
            args.putString("playlistId", playlistId)
            args.putParcelable("library", library)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playlistId = arguments?.getString("playlistId")
        library = arguments?.getParcelable("library")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tracks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tracks = getTracksForPlaylist(playlistId)

        val recyclerView: RecyclerView = view.findViewById(R.id.tracksRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = TrackAdapter(tracks)
        recyclerView.adapter = adapter
    }

    private fun getTracksForPlaylist(playlistId: String?): List<Track> {
        if (playlistId == null || library == null) return emptyList()

        val playlist = library!!.playlists.flatMap { it.values }.find { it.id == playlistId }

        return playlist?.tracks?.values?.toList() ?: emptyList()
    }
}