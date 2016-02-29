package com.johny.musicplayer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.johny.musicplayer.R;
import com.johny.musicplayer.instances.Library;
import com.johny.musicplayer.instances.Playlist;
import com.johny.musicplayer.instances.section.LibraryEmptyState;
import com.johny.musicplayer.instances.section.PlaylistSection;
import com.johny.musicplayer.instances.section.SpacerSingleton;
import com.johny.musicplayer.utils.Themes;
import com.johny.musicplayer.view.BackgroundDecoration;
import com.johny.musicplayer.view.DividerDecoration;
import com.johny.musicplayer.view.EnhancedAdapters.HeterogeneousAdapter;

public class PlaylistFragment extends Fragment implements Library.PlaylistChangeListener,
        Library.LibraryRefreshListener {

    private HeterogeneousAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list, container, false);

        int paddingH = (int) getActivity().getResources().getDimension(R.dimen.global_padding);
        view.setPadding(paddingH, 0, paddingH, 0);

        adapter = new HeterogeneousAdapter();
        adapter.addSection(new PlaylistSection(Library.getPlaylists()));
        adapter.addSection(new SpacerSingleton(
                PlaylistSection.ID, (int) getResources().getDimension(R.dimen.list_height)));
        adapter.setEmptyState(new LibraryEmptyState(getActivity()) {
            @Override
            public String getEmptyMessage() {
                return getString(R.string.empty_playlists);
            }

            @Override
            public String getEmptyMessageDetail() {
                return getString(R.string.empty_playlists_detail);
            }

            @Override
            public String getEmptyAction1Label() {
                return "";
            }

            @Override
            public String getEmptyAction2Label() {
                return "";
            }
        });

        RecyclerView list = (RecyclerView) view.findViewById(R.id.list);
        list.addItemDecoration(new BackgroundDecoration(Themes.getBackgroundElevated()));
        list.addItemDecoration(
                new DividerDecoration(getActivity(), R.id.instance_blank, R.id.empty_layout));
        list.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Library.addPlaylistListener(this);
        Library.addRefreshListener(this);
        // Assume this fragment's data has gone stale since it was last in the foreground
        onLibraryRefreshed();
    }

    @Override
    public void onPause() {
        super.onPause();
        Library.removePlaylistListener(this);
        Library.removeRefreshListener(this);
    }

    @Override
    public void onLibraryRefreshed() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPlaylistRemoved(Playlist removed, int index) {
        adapter.notifyItemRemoved(index);
    }

    @Override
    public void onPlaylistAdded(Playlist added, int index) {
        adapter.notifyItemInserted(index);
    }
}
