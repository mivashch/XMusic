from ytmusicapi import YTMusic
import json
import os

def Ytauth(path):
    ytmusic = YTMusic(path)
    YTM = {}
    list = []
    lib = {}
    # Отримання плейлистів користувача
    playlists = ytmusic.get_library_playlists()
    if playlists is None:
        print("Error: playlists is None")
        return

    # Виведення інформації про плейлисти
    for playlist in playlists:
        print(f"Playlist: {playlist['title']}, ID: {playlist['playlistId']}")
        data = {
            "title":playlist['title'],
            "id":playlist['playlistId'],
            "tracks":{}
        }
        plst = {
            playlist['playlistId']: data
        }

        # YTM[playlist['playlistId']] = data
        list.append(plst)
        # Отримання треків у плейлисті
        tracks = ytmusic.get_playlist(playlist['playlistId'])['tracks']
        if tracks is None:
            print(f"Error: tracks is None for playlist ID: {playlist['playlistId']}")
            continue

        for track in tracks:
            if track is None:
                print("Error: track is None")
                continue

            id = track.get('videoId')
            title = track.get('title')
            artists = track.get('artists')
            duration = track.get('duration')
            if title is None or artists is None or len(artists) == 0:
                print(f"Error: Missing title or artists for track: {track}")
                continue

            artist_name = artists[0].get('name')
            if artist_name is None:
                print(f"Error: Missing artist name for track: {track}")
                continue

            print(f"Track: {title}, Artist: {artist_name}")
            trck = {
                "title": title,
                "artists": artists,
                "duration": duration
            }
            # YTM[playlist['playlistId']]['tracks'][id] = trck
            list[-1][playlist['playlistId']]["tracks"][id] = trck
    lib["playlists"] = list
    temp_dir = os.getenv('TMPDIR', '/tmp')
    path = os.path.join(temp_dir, "ytdata.json")
    with open(path,"w") as file:
        json.dump(lib, file)
    print(path)
