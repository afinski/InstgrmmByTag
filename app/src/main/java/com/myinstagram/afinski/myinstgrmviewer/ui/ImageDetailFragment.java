/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.myinstagram.afinski.myinstgrmviewer.ui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.myinstagram.afinski.myinstgrmviewer.util.ImageCache;
import com.myinstagram.afinski.myinstgrmviewer.util.ImageWorker;
import com.myinstagram.afinski.myinstgrmviewer.R;
import com.myinstagram.afinski.myinstgrmviewer.util.ImageFetcher;
import com.myinstagram.afinski.myinstgrmviewer.util.Utils;

public class ImageDetailFragment extends DialogFragment{
    private static final String IMAGE_DATA_EXTRA = "extra_image_data";
    private static final String IMAGE_CACHE_DIR = "standard";
    private String mImageUrl;
    private ImageView mImageView;
    private ImageFetcher mImageFetcher;
    private Integer mImageWidth;
    private Integer mImageHeight;

    /**
     * Factory method to generate a new instance of the fragment given an image number.
     *
     * @param imageUrl The image url to load
     * @return A new instance of ImageDetailFragment with imageNum extras
     */
    public static ImageDetailFragment newInstance(String imageUrl) {
        final ImageDetailFragment f = new ImageDetailFragment();

        final Bundle args = new Bundle();
        args.putString(IMAGE_DATA_EXTRA, imageUrl);
        f.setArguments(args);

        return f;
    }

    /**
     * Empty constructor as per the Fragment documentation
     */
    public ImageDetailFragment() {}

    /**
     * Populate image using a url from extras, use the convenience factory method
     * {@link ImageDetailFragment#newInstance(String)} to create this fragment.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        mImageUrl = getArguments() != null ? getArguments().getString(IMAGE_DATA_EXTRA) : null;
        mImageWidth = getArguments() != null ? getArguments().getInt("Width") : null;
        mImageHeight = getArguments() != null ? getArguments().getInt("Height") : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate and locate the main ImageView
        final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
        mImageView = (ImageView) v.findViewById(R.id.imageView);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Use the parent activity to load the image asynchronously into the ImageView (so a single
        // cache can be used over all pages in the ViewPager
        if (MainActivity.class.isInstance(getActivity())) {
//            mImageFetcher = ((MainActivity) getActivity()).getImageFetcher();
            ImageCache.ImageCacheParams cacheParams =
                    new ImageCache.ImageCacheParams(getActivity(), IMAGE_CACHE_DIR);

            cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory
            mImageFetcher = new ImageFetcher(getActivity(),mImageWidth, mImageHeight);
            mImageFetcher.setLoadingImage(R.drawable.empty_photo);
            mImageFetcher.addImageCache(getActivity().getSupportFragmentManager(), cacheParams);
            mImageFetcher.loadImage(mImageUrl, mImageView);
        }

        // Pass clicks on the ImageView to the parent activity to handle
        if (OnClickListener.class.isInstance(getActivity()) && Utils.hasHoneycomb()) {
            mImageView.setOnClickListener((OnClickListener) getActivity());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mImageView != null) {
            // Cancel any pending image work
            ImageWorker.cancelWork(mImageView);
            mImageView.setImageDrawable(null);
        }
    }
}
