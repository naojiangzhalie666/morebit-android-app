package com.jf.my.utils;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;



public class ViewFinder {

    /**
     * Views indexed with their IDs
     */
    private final SparseArray<View> mViews = new SparseArray<>();
    private final View mView;
    private boolean mLazy;
    private int[] mIds;

    public ViewFinder(final View view, boolean lazy, @IdRes int... ids) {
        mView = view;
        mLazy = lazy;
        mIds = ids;
        if (!lazy) {
            findViews(view, ids);
        }
    }

    public ViewFinder(final View view) {
        this(view, false);
    }

    private void findViews(View view, int[] ids) {
        for (int i = 0; i < ids.length; i++) {
            View viewById = view.findViewById(ids[i]);
            if (viewById != null) {
                mViews.put(ids[i], viewById);
            } else {
                String name = DebugUtils.getResourceEntryName(view, ids[i]);
               // Timber.w("View '%s' with ID %s is null", name, ids[i]);
            }
        }
    }

    public ViewFinder setText(@IdRes int viewId, CharSequence value) {
        TextView view = view(viewId);
        view.setText(value);
        return this;
    }

    public ViewFinder setText(@IdRes int viewId, @StringRes int strId) {
        TextView view = view(viewId);
        view.setText(strId);
        return this;
    }

    /**
     * Will set the image of an ImageView from a resource id.
     *
     * @param viewId     The view id.
     * @param imageResId The image resource id.
     * @return The ViewFinder for chaining.
     */
    public ViewFinder setImageResource(@IdRes int viewId, @DrawableRes int imageResId) {
        ImageView view = view(viewId);
        view.setImageResource(imageResId);
        return this;
    }

    /**
     * Will set background color of a view.
     *
     * @param viewId The view id.
     * @param color  A color, not a resource id.
     * @return The ViewFinder for chaining.
     */
    public ViewFinder setBackgroundColor(@IdRes int viewId, @ColorInt int color) {
        View view = view(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    /**
     * Will set background of a view.
     *
     * @param viewId        The view id.
     * @param backgroundRes A resource to use as a background.
     * @return The ViewFinder for chaining.
     */
    public ViewFinder setBackgroundRes(@IdRes int viewId, @DrawableRes int backgroundRes) {
        View view = view(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    /**
     * Will set text color of a TextView.
     *
     * @param viewId    The view id.
     * @param textColor The text color (not a resource id).
     * @return The ViewFinder for chaining.
     */
    public ViewFinder setTextColor(@IdRes int viewId, @ColorInt int textColor) {
        TextView view = view(viewId);
        view.setTextColor(textColor);
        return this;
    }


    /**
     * Will set the image of an ImageView from a drawable.
     *
     * @param viewId   The view id.
     * @param drawable The image drawable.
     * @return The ViewFinder for chaining.
     */
    public ViewFinder setImageDrawable(@IdRes int viewId, Drawable drawable) {
        ImageView view = view(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    /**
     * Add an action to set the image of an image view. Can be called multiple times.
     */
    public ViewFinder setImageBitmap(@IdRes int viewId, Bitmap bitmap) {
        ImageView view = view(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    /**
     * Add an action to set the alpha of a view. Can be called multiple times.
     * Alpha between 0-1.
     */
    public ViewFinder setAlpha(@IdRes int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            view(viewId).setAlpha(value);
        } else {
            // Pre-honeycomb hack to set Alpha value
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            view(viewId).startAnimation(alpha);
        }
        return this;
    }

    /**
     * Set a view visibility to VISIBLE (true) or GONE (false).
     *
     * @param viewId  The view id.
     * @param visible True for VISIBLE, false for GONE.
     * @return The ViewFinder for chaining.
     */
    public ViewFinder setVisible(@IdRes int viewId, boolean visible) {
        View view = view(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * Add links into a TextView.
     *
     * @param viewId The id of the TextView to linkify.
     * @return The ViewFinder for chaining.
     */
    public ViewFinder linkify(@IdRes int viewId) {
        TextView view = view(viewId);
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    /**
     * Apply the typeface to the given viewId, and enable subpixel rendering.
     */
    public ViewFinder setTypeface(@IdRes int viewId, Typeface typeface) {
        TextView view = view(viewId);
        view.setTypeface(typeface);
        view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        return this;
    }

    /**
     * Apply the typeface to all the given viewIds, and enable subpixel rendering.
     */
    public ViewFinder setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = view(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    /**
     * Sets the progress of a ProgressBar.
     *
     * @param viewId   The view id.
     * @param progress The progress.
     * @return The ViewFinder for chaining.
     */
    public ViewFinder setProgress(@IdRes int viewId, int progress) {
        ProgressBar view = view(viewId);
        view.setProgress(progress);
        return this;
    }

    /**
     * Sets the progress and max of a ProgressBar.
     *
     * @param viewId   The view id.
     * @param progress The progress.
     * @param max      The max value of a ProgressBar.
     * @return The ViewFinder for chaining.
     */
    public ViewFinder setProgress(@IdRes int viewId, int progress, int max) {
        ProgressBar view = view(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    /**
     * Sets the range of a ProgressBar to 0...max.
     *
     * @param viewId The view id.
     * @param max    The max value of a ProgressBar.
     * @return The ViewFinder for chaining.
     */
    public ViewFinder setMax(@IdRes int viewId, int max) {
        ProgressBar view = view(viewId);
        view.setMax(max);
        return this;
    }

    /**
     * Sets the rating (the number of stars filled) of a RatingBar.
     *
     * @param viewId The view id.
     * @param rating The rating.
     * @return The ViewFinder for chaining.
     */
    public ViewFinder setRating(@IdRes int viewId, float rating) {
        RatingBar view = view(viewId);
        view.setRating(rating);
        return this;
    }

    /**
     * Sets the rating (the number of stars filled) and max of a RatingBar.
     *
     * @param viewId The view id.
     * @param rating The rating.
     * @param max    The range of the RatingBar to 0...max.
     * @return The ViewFinder for chaining.
     */
    public ViewFinder setRating(@IdRes int viewId, float rating, int max) {
        RatingBar view = view(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    /**
     * Sets the on click listener of the view.
     *
     * @param viewId   The view id.
     * @param listener The on click listener;
     * @return The ViewFinder for chaining.
     */
    public ViewFinder setOnClickListener(@IdRes int viewId, View.OnClickListener listener) {
        View view = view(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    /**
     * Sets the on touch listener of the view.
     *
     * @param viewId   The view id.
     * @param listener The on touch listener;
     * @return The ViewFinder for chaining.
     */
    public ViewFinder setOnTouchListener(@IdRes int viewId, View.OnTouchListener listener) {
        View view = view(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    /**
     * Sets the on long click listener of the view.
     *
     * @param viewId   The view id.
     * @param listener The on long click listener;
     * @return The ViewFinder for chaining.
     */
    public ViewFinder setOnLongClickListener(@IdRes int viewId, View.OnLongClickListener listener) {
        View view = view(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

    /**
     * Sets the listview or gridview's item click listener of the view
     *
     * @param viewId   The view id.
     * @param listener The item on click listener;
     * @return The ViewFinder for chaining.
     */
    public ViewFinder setOnItemClickListener(@IdRes int viewId, AdapterView.OnItemClickListener listener) {
        AdapterView view = view(viewId);
        view.setOnItemClickListener(listener);
        return this;
    }

    /**
     * Sets the listview or gridview's item long click listener of the view
     *
     * @param viewId   The view id.
     * @param listener The item long click listener;
     * @return The ViewFinder for chaining.
     */
    public ViewFinder setOnItemLongClickListener(@IdRes int viewId, AdapterView.OnItemLongClickListener listener) {
        AdapterView view = view(viewId);
        view.setOnItemLongClickListener(listener);
        return this;
    }

    /**
     * Sets the listview or gridview's item selected click listener of the view
     *
     * @param viewId   The view id.
     * @param listener The item selected click listener;
     * @return The ViewFinder for chaining.
     */
    public ViewFinder setOnItemSelectedClickListener(@IdRes int viewId, AdapterView.OnItemSelectedListener listener) {
        AdapterView view = view(viewId);
        view.setOnItemSelectedListener(listener);
        return this;
    }

    /**
     * Sets the on checked change listener of the view.
     *
     * @param viewId   The view id.
     * @param listener The checked change listener of compound button.
     * @return The ViewFinder for chaining.
     */
    public ViewFinder setOnCheckedChangeListener(@IdRes int viewId, CompoundButton.OnCheckedChangeListener listener) {
        CompoundButton view = view(viewId);
        view.setOnCheckedChangeListener(listener);
        return this;
    }

    /**
     * Sets the tag of the view.
     *
     * @param viewId The view id.
     * @param tag    The tag;
     * @return The ViewFinder for chaining.
     */
    public ViewFinder setTag(@IdRes int viewId, Object tag) {
        View view = view(viewId);
        view.setTag(tag);
        return this;
    }

    /**
     * Sets the tag of the view.
     *
     * @param viewId The view id.
     * @param key    The key of tag;
     * @param tag    The tag;
     * @return The ViewFinder for chaining.
     */
    public ViewFinder setTag(@IdRes int viewId, int key, Object tag) {
        View view = view(viewId);
        view.setTag(key, tag);
        return this;
    }

    /**
     * Sets the checked status of a checkable.
     *
     * @param viewId  The view id.
     * @param checked The checked status;
     * @return The ViewFinder for chaining.
     */
    public ViewFinder setChecked(@IdRes int viewId, boolean checked) {
        View view = view(viewId);
        // View unable cast to Checkable
        if (view instanceof Checkable) {
            ((Checkable) view).setChecked(checked);
        }
        return this;
    }

    /**
     * Sets the adapter of a adapter view.
     *
     * @param viewId  The view id.
     * @param adapter The adapter;
     * @return The ViewFinder for chaining.
     */
    @SuppressWarnings("unchecked")
    public ViewFinder setAdapter(@IdRes int viewId, Adapter adapter) {
        AdapterView view = view(viewId);
        view.setAdapter(adapter);
        return this;
    }

    /**
     * Retrieve view by id
     *
     * @param viewId view id
     * @param <T>    view
     * @return view
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T view(@IdRes int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            if (!mLazy) {
                view = mView.findViewById(viewId);
                mViews.put(viewId, view);
            } else {
                String name = DebugUtils.getResourceEntryName(mView, viewId);
                //Timber.w("View '%s' with ID %s is null", name, viewId);
            }
        }
        return (T) view;
    }

    /**
     * Retrieve view by index of id
     *
     * @param index index of view id
     * @param <T>   view
     * @return view
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T view0(int index) {
        if (mIds != null) {
            return (T) view(mIds[index]);
        } else {
            throw new NullPointerException();
        }
    }
}
