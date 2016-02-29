package com.johny.musicplayer.instances.viewholder;

import android.view.MenuItem;
import android.view.View;

import com.johny.musicplayer.PlayerController;
import com.johny.musicplayer.R;
import com.johny.musicplayer.activity.instance.AlbumActivity;
import com.johny.musicplayer.activity.instance.ArtistActivity;
import com.johny.musicplayer.instances.Library;
import com.johny.musicplayer.instances.PlaylistDialog;
import com.johny.musicplayer.instances.Song;
import com.johny.musicplayer.utils.Navigate;

import java.util.List;

public class QueueSongViewHolder extends DragDropSongViewHolder {

    private OnRemovedListener removedListener;
    private View nowPlayingIndicator;

    public QueueSongViewHolder(View itemView, OnRemovedListener listener) {
        super(itemView, null, R.array.edit_queue_options);
        this.removedListener = listener;
        this.nowPlayingIndicator = itemView.findViewById(R.id.instancePlayingIndicator);
    }

    @Override
    public void update(Song item, int position) {
        super.update(item, position);

        if (PlayerController.getQueuePosition() == position) {
            nowPlayingIndicator.setVisibility(View.VISIBLE);
        } else {
            nowPlayingIndicator.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case 0: //Go to artist
                Navigate.to(
                        itemView.getContext(),
                        ArtistActivity.class,
                        ArtistActivity.ARTIST_EXTRA,
                        Library.findArtistById(reference.getArtistId()));
                return true;
            case 1: // Go to album
                Navigate.to(
                        itemView.getContext(),
                        AlbumActivity.class,
                        AlbumActivity.ALBUM_EXTRA,
                        Library.findAlbumById(reference.getAlbumId()));
                return true;
            case 2:
                PlaylistDialog.AddToNormal.alert(itemView, reference,
                        itemView.getResources().getString(
                                R.string.header_add_song_name_to_playlist, reference));
                return true;
            case 3: // Remove
                List<Song> editedQueue = PlayerController.getQueue();
                if (editedQueue != null) {
                    int queuePosition = PlayerController.getQueuePosition();
                    int itemPosition = getAdapterPosition();

                    editedQueue.remove(itemPosition);
                    removedListener.onItemRemoved(itemPosition);

                    PlayerController.editQueue(
                            editedQueue,
                            (queuePosition > itemPosition)
                                    ? queuePosition - 1
                                    : queuePosition);

                    if (queuePosition == itemPosition) {
                        PlayerController.begin();
                    }
                }
                return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v == itemView) {
            PlayerController.changeSong(index);
        } else {
            super.onClick(v);
        }
    }
}
