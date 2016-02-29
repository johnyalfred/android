package com.johny.musicplayer.instances.viewholder;

import android.view.MenuItem;
import android.view.View;

import com.johny.musicplayer.PlayerController;
import com.johny.musicplayer.R;
import com.johny.musicplayer.activity.instance.AlbumActivity;
import com.johny.musicplayer.activity.instance.ArtistActivity;
import com.johny.musicplayer.instances.AutoPlaylist;
import com.johny.musicplayer.instances.Library;
import com.johny.musicplayer.instances.Playlist;
import com.johny.musicplayer.instances.PlaylistDialog;
import com.johny.musicplayer.instances.Song;
import com.johny.musicplayer.utils.Navigate;

import java.util.List;

public class PlaylistSongViewHolder extends DragDropSongViewHolder {

    private OnRemovedListener removedListener;
    private boolean isReferenceAuto;

    public PlaylistSongViewHolder(View itemView, List<Song> playlistEntries, Playlist reference,
                                  OnRemovedListener listener) {
        super(itemView, playlistEntries,
                (reference instanceof AutoPlaylist)
                        ? R.array.edit_auto_playlist_options
                        : R.array.edit_playlist_options);
        this.removedListener = listener;
        this.isReferenceAuto = reference instanceof AutoPlaylist;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case 0: //Queue this song next
                PlayerController.queueNext(reference);
                return true;
            case 1: //Queue this song last
                PlayerController.queueLast(reference);
                return true;
            case 2: //Go to artist
                Navigate.to(
                        itemView.getContext(),
                        ArtistActivity.class,
                        ArtistActivity.ARTIST_EXTRA,
                        Library.findArtistById(reference.getArtistId()));
                return true;
            case 3: // Go to album
                Navigate.to(
                        itemView.getContext(),
                        AlbumActivity.class,
                        AlbumActivity.ALBUM_EXTRA,
                        Library.findAlbumById(reference.getAlbumId()));
                return true;
            case 4:
                if (isReferenceAuto) { // Add to playlist
                    PlaylistDialog.AddToNormal.alert(itemView, reference,
                            itemView.getResources().getString(
                                    R.string.header_add_song_name_to_playlist, reference));
                } else { // Remove
                    removedListener.onItemRemoved(getAdapterPosition());
                }
                return true;
        }
        return false;
    }

}
