package com.example.xmusic

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Library(
    val playlists: List<Map<String, Playlist>>
) : Parcelable

@Parcelize
data class Playlist(
    val title: String,
    val id: String,
    val tracks: Map<String, Track>
) : Parcelable

@Parcelize
data class Track(
    val title: String,
    val artists: List<Artist>,
    val duration: String,
    val ID: String
) : Parcelable

@Parcelize
data class Artist(
    val name: String,
    val id: String?
) : Parcelable

