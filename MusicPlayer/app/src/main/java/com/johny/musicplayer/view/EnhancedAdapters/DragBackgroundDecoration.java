package com.johny.musicplayer.view.EnhancedAdapters;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.johny.musicplayer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A modified version of {@link com.johny.musicplayer.view.BackgroundDecoration} intended to be used
 * when a {@link DragDropAdapter} is attached to a {@link RecyclerView}
 */
public class DragBackgroundDecoration extends RecyclerView.ItemDecoration {

    protected Drawable mBackground;
    protected NinePatchDrawable mShadow;
    private List<Rect> mRectPool;

    /**
     * Create an ItemDecorator for use with a RecyclerView
     * @param color the color of the background
     */
    public DragBackgroundDecoration(int color) {
        mBackground = new ColorDrawable(color);
        mRectPool = new ArrayList<>();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int sectionCount = 0;

        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        if (mShadow == null) {
            //noinspection deprecation
            mShadow = (NinePatchDrawable) parent.getContext().getResources()
                    .getDrawable(R.drawable.list_shadow);
        }

        Rect shadowPadding = new Rect();
        mShadow.getPadding(shadowPadding);

        int layoutCount = parent.getChildCount();
        for (int i = 0; i < layoutCount; i++) {

            View topView = parent.getChildAt(i);
            if (!includeView(topView)) {
                continue;
            }

            View bottomView = topView;

            do {
                i++;
                View checkingView = parent.getChildAt(i);
                if (checkingView != null && includeView(checkingView)
                        && areViewsConnected(bottomView, checkingView)) {
                    bottomView = checkingView;
                } else {
                    i--;
                    break;
                }
            } while(i + 1 < layoutCount);

            RecyclerView.LayoutParams topParams =
                    (RecyclerView.LayoutParams) topView.getLayoutParams();
            RecyclerView.LayoutParams bottomParams =
                    (RecyclerView.LayoutParams) bottomView.getLayoutParams();

            int top = topView.getTop() - topParams.topMargin + (int) bottomView.getTranslationY();
            int bottom;

            if ((i == layoutCount - 1 || parent.getChildAdapterPosition(bottomView)
                    == parent.getAdapter().getItemCount() - 1)
                    && bottomView.getTranslationY() == 0) {
                // If this is the last item in the adapter or last visible view,
                // fill the parent
                bottom = parent.getBottom();
            } else {
                // Otherwise, fill to the bottom of the last item in the section
                bottom = bottomView.getBottom() + bottomParams.bottomMargin
                        + (int) bottomView.getTranslationY();
            }

            /*
            Instead of drawing the entire background and shadow at once like in
            BackgroundDecoration, setup a list of Rects so that the shadows can be drawn underneath
            the solid color of the background.

            This is done to prevent shadows from appearing above the elevated surface when views
            are close together. This is especially noticeable because even if two items in a list
            are touching each other, their view parameters may cause this decoration to think
            they're not part of the same surface and not group them together. When this happens,
            each view will get its own shadow (which unfortunately does cause overdraw), but
            because of this modified implementation, it won't leave visible anomalies.
             */
            Rect section = null;
            if (sectionCount < mRectPool.size()) {
                section = mRectPool.get(sectionCount);
            }
            if (section == null) {
                section = new Rect(left, top, right, bottom);
                mRectPool.add(section);
            } else {
                section.set(left, top, right, bottom);
            }
            sectionCount++;
        }

        // If the last item in the list is being moved, make sure to draw the
        // background to fill the view
        View lastView = parent.getChildAt(parent.getChildCount() - 1);
        if (sectionCount == 0 || mRectPool.get(sectionCount - 1).bottom + lastView.getHeight()
                < parent.getBottom()) {
            addRect(sectionCount, left, lastView.getBottom(), right, parent.getBottom());
            sectionCount++;
        }

        // Draw all the shadows first
        for (int i = 0; i < sectionCount; i++) {
            Rect section = mRectPool.get(i);
            mShadow.setBounds(left - shadowPadding.left, section.top - shadowPadding.top,
                    right + shadowPadding.right, section.bottom + shadowPadding.bottom);
            mShadow.draw(c);
        }

        // Fill the rest of the background
        for (int i = 0; i < sectionCount; i++) {
            Rect section = mRectPool.get(i);
            mBackground.setBounds(section);
            mBackground.draw(c);
        }
    }

    private void addRect(int count, int left, int top, int right, int bottom) {
        Rect section = null;
        if (count < mRectPool.size()) {
            section = mRectPool.get(count);
        }
        if (section == null) {
            section = new Rect(left, top, right, bottom);
            mRectPool.add(section);
        } else {
            section.set(left, top, right, bottom);
        }
    }

    private boolean includeView(View view) {
        return !(view.getTag() instanceof DragDropAdapter.DragMarker);
    }

    private boolean areViewsConnected(View previous, View check) {
        if (previous == check) {
            return true;
        }
        int previousBottom = previous.getBottom() + (int) previous.getTranslationY();
        int checkTop = check.getTop() - (int) check.getTranslationY();
        // The views are connected if there's no more than 1dp of separation between them
        return Math.abs(previousBottom - checkTop)
                <= previous.getResources().getDisplayMetrics().density;
    }
}
