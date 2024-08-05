package com.example.xmusic

data class Library(
    val playlists: List<Map<String, Playlist>>
)

data class Playlist(
    val title: String,
    val id: String,
    val tracks: Map<String, Track>
)

data class Track(
    val title: String,
    val artists: List<Artist>,
    val duration: String
)

data class Artist(
    val name: String,
    val id: String?
)

