package com.johny.musicplayer.activity.instance;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.johny.musicplayer.R;
import com.johny.musicplayer.activity.BaseActivity;
import com.johny.musicplayer.instances.Album;
import com.johny.musicplayer.instances.Library;
import com.johny.musicplayer.instances.Song;
import com.johny.musicplayer.instances.section.LibraryEmptyState;
import com.johny.musicplayer.instances.section.SongSection;
import com.johny.musicplayer.utils.Themes;
import com.johny.musicplayer.view.BackgroundDecoration;
import com.johny.musicplayer.view.DividerDecoration;
import com.johny.musicplayer.view.EnhancedAdapters.HeterogeneousAdapter;

import java.util.ArrayList;
import java.util.List;

public class AlbumActivity extends BaseActivity {

    public static final String ALBUM_EXTRA = "album";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instance_artwork);

        final Album reference = getIntent().getParcelableExtra(ALBUM_EXTRA);
        List<Song> data;
        if (reference != null) {
            data = Library.getAlbumEntries(reference);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(reference.getAlbumName());
            }

            Glide.with(this).load(reference.getArtUri())
                    .centerCrop()
                    .into((ImageView) findViewById(R.id.backdrop));
        } else {
            data = new ArrayList<>();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        HeterogeneousAdapter adapter = new HeterogeneousAdapter();
        adapter.addSection(new SongSection(data))
                .setEmptyState(new LibraryEmptyState(this) {
                    @Override
                    public String getEmptyMessage() {
                        if (reference == null) {
                            return getString(R.string.empty_error_album);
                        } else {
                            return super.getEmptyMessage();
                        }
                    }

                    @Override
                    public String getEmptyMessageDetail() {
                        if (reference == null) {
                            return "";
                        } else {
                            return super.getEmptyMessageDetail();
                        }
                    }

                    @Override
                    public String getEmptyAction1Label() {
                        return "";
                    }
                });

        RecyclerView list = (RecyclerView) findViewById(R.id.list);
        list.setAdapter(adapter);
        list.addItemDecoration(new BackgroundDecoration(Themes.getBackgroundElevated()));
        list.addItemDecoration(new DividerDecoration(this, R.id.empty_layout));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(layoutManager);
    }
}
