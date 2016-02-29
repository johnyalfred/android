package com.johny.musicplayer.activity.instance;

import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.johny.musicplayer.R;
import com.johny.musicplayer.activity.BaseActivity;
import com.johny.musicplayer.instances.AutoPlaylist;
import com.johny.musicplayer.instances.Library;
import com.johny.musicplayer.instances.Playlist;
import com.johny.musicplayer.instances.Song;
import com.johny.musicplayer.instances.section.LibraryEmptyState;
import com.johny.musicplayer.instances.section.PlaylistSongSection;
import com.johny.musicplayer.instances.section.SongSection;
import com.johny.musicplayer.instances.viewholder.DragDropSongViewHolder;
import com.johny.musicplayer.utils.Navigate;
import com.johny.musicplayer.utils.Themes;
import com.johny.musicplayer.view.BackgroundDecoration;
import com.johny.musicplayer.view.DividerDecoration;
import com.johny.musicplayer.view.EnhancedAdapters.DragBackgroundDecoration;
import com.johny.musicplayer.view.EnhancedAdapters.DragDividerDecoration;
import com.johny.musicplayer.view.EnhancedAdapters.DragDropAdapter;
import com.johny.musicplayer.view.EnhancedAdapters.DragDropDecoration;
import com.johny.musicplayer.view.EnhancedAdapters.HeterogeneousAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlaylistActivity extends BaseActivity implements PopupMenu.OnMenuItemClickListener,
        DragDropSongViewHolder.OnRemovedListener {

    public static final String PLAYLIST_EXTRA = "playlist";
    private final List<Song> data = new ArrayList<>();
    private Playlist reference;
    private HeterogeneousAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instance);

        reference = getIntent().getParcelableExtra(PLAYLIST_EXTRA);

        if (reference != null) {
            data.addAll(Library.getPlaylistEntries(this, reference));
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(reference.getPlaylistName());
            }
        }

        RecyclerView list = (RecyclerView) findViewById(R.id.list);

        if (reference instanceof AutoPlaylist) {
            adapter = new HeterogeneousAdapter().addSection(new SongSection(data));
            list.setAdapter(adapter);
            list.addItemDecoration(new BackgroundDecoration(Themes.getBackgroundElevated()));
            list.addItemDecoration(new DividerDecoration(this, R.id.empty_layout));
        } else {
            DragDropAdapter hAdapter = new DragDropAdapter();
            hAdapter.setDragSection(new PlaylistSongSection(data, this, this, reference));
            hAdapter.attach(list);
            adapter = hAdapter;

            list.addItemDecoration(new DragBackgroundDecoration(Themes.getBackgroundElevated()));
            list.addItemDecoration(new DragDividerDecoration(this, R.id.empty_layout));
        }

        list.addItemDecoration(new DragDropDecoration((NinePatchDrawable) getDrawableCompat(
                (Themes.isLight(this))
                        ? R.drawable.list_drag_shadow_light
                        : R.drawable.list_drag_shadow_dark)));

        adapter.setEmptyState(new LibraryEmptyState(this) {
            @Override
            public String getEmptyMessage() {
                if (reference instanceof AutoPlaylist) {
                    return getString(R.string.empty_auto_playlist);
                } else {
                    return getString(R.string.empty_playlist);
                }
            }

            @Override
            public String getEmptyMessageDetail() {
                if (reference instanceof AutoPlaylist) {
                    return getString(R.string.empty_auto_playlist_detail);
                } else {
                    return getString(R.string.empty_playlist_detail);
                }
            }

            @Override
            public String getEmptyAction1Label() {
                if (reference instanceof AutoPlaylist) {
                    return getString(R.string.action_edit_playlist_rules);
                } else {
                    return "";
                }
            }

            @Override
            public void onAction1() {
                if (reference instanceof AutoPlaylist
                        && Library.hasRWPermission(PlaylistActivity.this)) {
                    Navigate.to(PlaylistActivity.this, AutoPlaylistEditActivity.class,
                            AutoPlaylistEditActivity.PLAYLIST_EXTRA, reference);
                } else {
                    super.onAction1();
                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_playlist, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (reference == null) {
            return super.onOptionsItemSelected(item);
        }

        if (item.getItemId() == R.id.action_sort) {
            PopupMenu sortMenu = new PopupMenu(this, findViewById(R.id.action_sort), Gravity.END);
            sortMenu.inflate(
                    (reference instanceof AutoPlaylist)
                            ? R.menu.sort_options_auto_playlist
                            : R.menu.sort_options);
            sortMenu.setOnMenuItemClickListener(this);
            sortMenu.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        final ArrayList<Song> unsortedData = new ArrayList<>(data);
        int sortFlag = -1;
        String result;

        switch (item.getItemId()) {
            case R.id.action_sort_random:
                Collections.shuffle(data);
                result = getResources().getString(R.string.message_sorted_playlist_random);
                sortFlag = AutoPlaylist.Rule.Field.ID;
                break;
            case R.id.action_sort_name:
                Collections.sort(data);
                result = getResources().getString(R.string.message_sorted_playlist_name);
                sortFlag = AutoPlaylist.Rule.Field.NAME;
                break;
            case R.id.action_sort_artist:
                Collections.sort(data, Song.ARTIST_COMPARATOR);
                result = getResources().getString(R.string.message_sorted_playlist_artist);
                break;
            case R.id.action_sort_album:
                Collections.sort(data, Song.ALBUM_COMPARATOR);
                result = getResources().getString(R.string.message_sorted_playlist_album);
                break;
            case R.id.action_sort_play:
                Collections.sort(data, Song.PLAY_COUNT_COMPARATOR);
                result = getResources().getString(R.string.message_sorted_playlist_play);
                sortFlag = AutoPlaylist.Rule.Field.PLAY_COUNT;
                break;
            case R.id.action_sort_skip:
                Collections.sort(data, Song.SKIP_COUNT_COMPARATOR);
                result = getResources().getString(R.string.message_sorted_playlist_skip);
                sortFlag = AutoPlaylist.Rule.Field.SKIP_COUNT;
                break;
            case R.id.action_sort_date_added:
                Collections.sort(data, Song.DATE_ADDED_COMPARATOR);
                result = getResources().getString(R.string.message_sorted_playlist_date_added);
                sortFlag = AutoPlaylist.Rule.Field.DATE_ADDED;
                break;
            case R.id.action_sort_date_played:
                Collections.sort(data, Song.DATE_PLAYED_COMPARATOR);
                result = getResources().getString(R.string.message_sorted_playlist_date_played);
                sortFlag = AutoPlaylist.Rule.Field.DATE_PLAYED;
                break;
            default:
                return false;
        }

        if (reference instanceof AutoPlaylist) {
            ((AutoPlaylist) reference).setSortMethod(sortFlag);
            Library.editAutoPlaylist(this, (AutoPlaylist) reference);
        } else {
            Library.editPlaylist(this, reference, data);
        }

        adapter.notifyDataSetChanged();

        Snackbar
                .make(
                        findViewById(R.id.list),
                        String.format(result, reference),
                        Snackbar.LENGTH_LONG)
                .setAction(
                        getResources().getString(R.string.action_undo),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                data.clear();
                                data.addAll(unsortedData);
                                Library.editPlaylist(
                                        PlaylistActivity.this, reference, unsortedData);
                                adapter.notifyDataSetChanged();
                            }
                        })
                .show();

        return true;
    }

    @Override
    public void onItemRemoved(final int index) {
        final Song removed = data.remove(index);

        Library.editPlaylist(PlaylistActivity.this, reference, data);
        adapter.notifyItemRemoved(index);

        Snackbar
                .make(
                        findViewById(R.id.list),
                        getResources().getString(
                                R.string.message_removed_song,
                                removed.getSongName()),
                        Snackbar.LENGTH_LONG)
                .setAction(R.string.action_undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        data.add(index, removed);
                        Library.editPlaylist(PlaylistActivity.this, reference, data);
                        if (data.size() > 1) {
                            adapter.notifyItemInserted(index);
                        } else {
                            adapter.notifyItemChanged(index);
                        }
                    }
                })
                .show();
    }
}
