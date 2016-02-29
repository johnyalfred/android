package com.johny.musicplayer.instances.section;

import android.view.ViewGroup;

import com.johny.musicplayer.R;
import com.johny.musicplayer.instances.Song;
import com.johny.musicplayer.view.EnhancedAdapters.DragDropAdapter;
import com.johny.musicplayer.view.EnhancedAdapters.EnhancedViewHolder;
import com.johny.musicplayer.view.EnhancedAdapters.HeterogeneousAdapter;

import java.util.List;

public abstract class EditableSongSection extends DragDropAdapter.DragSection<Song> {

    protected List<Song> mData;

    public EditableSongSection(int typeId, List<Song> data) {
        super(typeId);
        mData = data;
    }

    @Override
    public int getDragHandleId() {
        return R.id.handle;
    }

    @Override
    protected void onDrag(int from, int to) {
        // Avoid the array copy in ArrayList by swapping the items manually
        // Since the difference between from and to is never more than 1, we don't have
        // to worry about shifting other items in the List's backing array
        Song fromObject = mData.get(from);
        Song toObject = mData.get(to);

        mData.set(to, fromObject);
        mData.set(from, toObject);
    }

    @Override
    protected abstract void onDrop(int from, int to);

    @Override
    public abstract EnhancedViewHolder<Song> createViewHolder(HeterogeneousAdapter adapter,
                                                     ViewGroup parent);

    @Override
    public int getSize(HeterogeneousAdapter adapter) {
        return mData.size();
    }

    @Override
    public Song get(int position) {
        return mData.get(position);
    }
}
