package com.johny.musicplayer.instances.section;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.johny.musicplayer.R;
import com.johny.musicplayer.utils.Themes;
import com.johny.musicplayer.view.EnhancedAdapters.EnhancedViewHolder;
import com.johny.musicplayer.view.EnhancedAdapters.HeterogeneousAdapter;
import com.johny.musicplayer.view.MaterialProgressDrawable;

public class LoadingSingleton extends HeterogeneousAdapter.SingletonSection<Void> {

    public static final int ID = 79;

    public LoadingSingleton(Void data) {
        super(ID, data);
    }

    @Override
    public EnhancedViewHolder<Void> createViewHolder(HeterogeneousAdapter adapter,
                                                     ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.instance_loading, parent, false));
    }

    public static class ViewHolder extends EnhancedViewHolder<Void> {

        private MaterialProgressDrawable spinner;

        public ViewHolder(View itemView) {
            super(itemView);
            ImageView spinnerView = (ImageView) itemView.findViewById(R.id.loadingDrawable);
            spinner = new MaterialProgressDrawable(itemView.getContext(), spinnerView);
            spinner.setColorSchemeColors(Themes.getAccent(), Themes.getPrimary());
            spinner.updateSizes(MaterialProgressDrawable.LARGE);
            spinner.setAlpha(255);
            spinnerView.setImageDrawable(spinner);
            spinner.start();
        }

        @Override
        public void update(Void item, int sectionPosition) {
            spinner.stop();
            spinner.start();
        }
    }
}
