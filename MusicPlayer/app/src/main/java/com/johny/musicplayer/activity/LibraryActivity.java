package com.johny.musicplayer.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.johny.musicplayer.BuildConfig;
import com.johny.musicplayer.R;
import com.johny.musicplayer.activity.instance.AutoPlaylistEditActivity;
import com.johny.musicplayer.fragments.AlbumFragment;
import com.johny.musicplayer.fragments.ArtistFragment;
import com.johny.musicplayer.fragments.GenreFragment;
import com.johny.musicplayer.fragments.PlaylistFragment;
import com.johny.musicplayer.fragments.SongFragment;
import com.johny.musicplayer.instances.Library;
import com.johny.musicplayer.instances.PlaylistDialog;
import com.johny.musicplayer.utils.Navigate;
import com.johny.musicplayer.utils.Prefs;
import com.johny.musicplayer.view.FABMenu;

public class LibraryActivity extends BaseActivity implements View.OnClickListener {

    private PagerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        // Setup the FAB
        FABMenu fab = (FABMenu) findViewById(R.id.fab);
        fab.addChild(R.drawable.ic_add_24dp, this, R.string.playlist);
        fab.addChild(R.drawable.ic_add_24dp, this, R.string.playlist_auto);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);

        SharedPreferences prefs = Prefs.getPrefs(this);
        int page = Integer.parseInt(prefs.getString(Prefs.DEFAULT_PAGE, "1"));
        if (page != 0 || !Library.hasRWPermission(this)) {
            fab.setVisibility(View.GONE);
        }

        adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.setFloatingActionButton(fab);
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(adapter);
        ((TabLayout) findViewById(R.id.tabs)).setupWithViewPager(pager);

        pager.setCurrentItem(page);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_library, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Navigate.to(this, SettingsActivity.class);
                return true;
            case R.id.action_refresh_library:
                if (Library.hasRWPermission(LibraryActivity.this)) {
                    Library.scanAll(this);

                    Snackbar
                            .make(
                                    findViewById(R.id.list),
                                    R.string.confirm_refresh_library,
                                    Snackbar.LENGTH_SHORT)
                            .show();
                } else {
                    Snackbar
                            .make(
                                    findViewById(R.id.list),
                                    R.string.message_refresh_library_no_permission,
                                    Snackbar.LENGTH_LONG)
                            .setAction(
                                    R.string.action_open_settings,
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent()
                                                .setAction(
                                                        Settings.
                                                                ACTION_APPLICATION_DETAILS_SETTINGS)
                                                .setData(Uri.fromParts(
                                                        "package",
                                                        BuildConfig.APPLICATION_ID,
                                                        null));
                                            startActivity(intent);
                                        }
                                    })
                            .show();
                }
                return true;
            case R.id.search:
                Navigate.to(this, SearchActivity.class);
                return true;
            case R.id.action_about:
                Navigate.to(this, AboutActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() != null) {
            if (v.getTag().equals("fab-" + getString(R.string.playlist))) {
                PlaylistDialog.MakeNormal.alert(findViewById(R.id.coordinator_layout));
            } else if (v.getTag().equals("fab-" + getString(R.string.playlist_auto))) {
                Navigate.to(this, AutoPlaylistEditActivity.class,
                        AutoPlaylistEditActivity.PLAYLIST_EXTRA, null);
            }
        }
    }

    public class PagerAdapter extends FragmentPagerAdapter
            implements ViewPager.OnPageChangeListener {

        private PlaylistFragment playlistFragment;
        private SongFragment songFragment;
        private ArtistFragment artistFragment;
        private AlbumFragment albumFragment;
        private GenreFragment genreFragment;

        private FABMenu fab;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setFloatingActionButton(FABMenu view) {
            fab = view;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (playlistFragment == null) {
                        playlistFragment = new PlaylistFragment();
                    }
                    return playlistFragment;
                case 1:
                    if (songFragment == null) {
                        songFragment = new SongFragment();
                    }
                    return songFragment;
                case 2:
                    if (artistFragment == null) {
                        artistFragment = new ArtistFragment();
                    }
                    return artistFragment;
                case 3:
                    if (albumFragment == null) {
                        albumFragment = new AlbumFragment();
                    }
                    return albumFragment;
                case 4:
                    if (genreFragment == null) {
                        genreFragment = new GenreFragment();
                    }
                    return genreFragment;
            }
            return new Fragment();
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.header_playlists);
                case 1:
                    return getResources().getString(R.string.header_songs);
                case 2:
                    return getResources().getString(R.string.header_artists);
                case 3:
                    return getResources().getString(R.string.header_albums);
                case 4:
                    return getResources().getString(R.string.header_genres);
                default:
                    return "Page " + position;
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            // Hide the fab when outside of the Playlist fragment

            // Don't show the FAB if we can't write to the library
            if (!Library.hasRWPermission(LibraryActivity.this)) return;

            // If the fab isn't supposed to change states, don't animate anything
            if (position != 0 && fab.getVisibility() == View.GONE) return;

            if (position == 0) {
                fab.show();
            } else {
                fab.hide();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
