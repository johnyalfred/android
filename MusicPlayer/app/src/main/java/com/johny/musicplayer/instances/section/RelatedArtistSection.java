package com.johny.musicplayer.instances.section;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.johny.musicplayer.R;
import com.johny.musicplayer.activity.instance.ArtistActivity;
import com.johny.musicplayer.instances.Artist;
import com.johny.musicplayer.instances.Library;
import com.johny.musicplayer.lastfm.ImageList;
import com.johny.musicplayer.lastfm.LArtist;
import com.johny.musicplayer.utils.Navigate;
import com.johny.musicplayer.utils.Themes;
import com.johny.musicplayer.view.EnhancedAdapters.EnhancedViewHolder;
import com.johny.musicplayer.view.EnhancedAdapters.HeterogeneousAdapter;

import java.util.List;

public class RelatedArtistSection extends HeterogeneousAdapter.ListSection<LArtist> {

    public static final int ID = 634;

    public RelatedArtistSection(@NonNull List<LArtist> data) {
        super(ID, data);
    }

    @Override
    public EnhancedViewHolder<LArtist> createViewHolder(HeterogeneousAdapter adapter,
                                                        ViewGroup parent) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.instance_artist_suggested, parent, false));
    }

    public static class ViewHolder extends EnhancedViewHolder<LArtist>
            implements View.OnClickListener {

        private Artist localReference;

        private Context context;
        private ImageView artwork;
        private TextView artistName;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            ((CardView) itemView).setCardBackgroundColor(Themes.getBackgroundElevated());
            itemView.setOnClickListener(this);

            artwork = (ImageView) itemView.findViewById(R.id.imageArtwork);
            artistName = (TextView) itemView.findViewById(R.id.textArtistName);
        }

        @Override
        public void update(LArtist item, int sectionPosition) {
            localReference = Library.findArtistByName(item.getName());

            final String artURL = item.getImageURL(ImageList.SIZE_MEDIUM);

            Glide.with(context)
                    .load(artURL)
                    .asBitmap()
                    .error(R.drawable.album_art)
                    .into(new BitmapImageViewTarget(artwork) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(
                                            context.getResources(),
                                            resource);
                            circularBitmapDrawable.setCircular(true);
                            artwork.setImageDrawable(circularBitmapDrawable);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(
                                            context.getResources(),
                                            ((BitmapDrawable) errorDrawable).getBitmap());
                            circularBitmapDrawable.setCircular(true);
                            artwork.setImageDrawable(circularBitmapDrawable);
                        }
                    });

            artistName.setText(item.getName());
        }

        @Override
        public void onClick(View v) {
            if (localReference != null) {
                Navigate.to(context, ArtistActivity.class,
                        ArtistActivity.ARTIST_EXTRA, localReference);
            }
        }
    }
}
