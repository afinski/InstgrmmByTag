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

import android.annotation.TargetApi;
import android.content.Context;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

//import com.example.android.common.logger.Log;
import com.myinstagram.afinski.myinstgrmviewer.BuildConfig;
import com.myinstagram.afinski.myinstgrmviewer.R;
import com.myinstagram.afinski.myinstgrmviewer.util.PictureInstagram;
import com.myinstagram.afinski.myinstgrmviewer.util.ImageCache;
import com.myinstagram.afinski.myinstgrmviewer.util.ImageFetcher;
import com.myinstagram.afinski.myinstgrmviewer.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;





/**
 * The main fragment that powers the ImageGridActivity screen. Fairly straight forward GridView
 * implementation with the key addition being the ImageWorker class w/ImageCache to load children
 * asynchronously, keeping the UI nice and smooth and caching thumbnails for quick retrieval. The
 * cache is retained over configuration changes like orientation change so the images are populated
 * quickly if, for example, the user rotates the device.
 */
public class ImageGridFragment extends Fragment implements AdapterView.OnItemClickListener, LoaderCallbacks<List<PictureInstagram>> {
    private static final String TAG = "ImageGridFragment";
    private static final String IMAGE_CACHE_DIR = "thumbs";
    private static final String IMAGE_DATA_EXTRA = "extra_image_data";

    private int mImageThumbSize;
    private int mImageThumbSpacing;
    private ImageAdapter mAdapter;
    private ImageFetcher mImageFetcher;



    private static final String AUTHURL = "https://api.instagram.com/oauth/authorize/";
    //Used for Authentication.
    private static final String TOKENURL ="https://api.instagram.com/oauth/access_token";
    //Used for getting token and User details.
    public static final String APIURL = "https://api.instagram.com/v1";
    //Used to specify the API version which we are going to use.
    public static String CALLBACKURL = " http://instagram.com/afinski/";
//The callback url that we have used while registering the application.

//    private PictureLoader loader;
    private Loader<List<PictureInstagram>> loader;
    private Context mActivityContext;

    private void startLoading(String url) {
        Bundle args = new Bundle();
        args.putString("url", url);
//        loader = getActivity().getLoaderManager().getLoader(0);
        loader =  getLoaderManager().initLoader(0, args, this);
        if(loader != null && !loader.isStarted()){
            loader.forceLoad();
        }else{
//            loader = new PictureLoader(mActivityContext,args);
            loader.forceLoad();
//            loader =  getLoaderManager().initLoader(0, args, this);
        }
    }

    public String getSearchUrl(String hashtag) {
        return "https://api.instagram.com/v1/tags/" + hashtag
                + "/media/recent?client_id=" + getResources().getString(R.string.Client_ID);
    }

    @Override
    public Loader<List<PictureInstagram>> onCreateLoader(int id, Bundle args) {
        return new PictureLoader(mActivityContext, args);
    }

//    @Override
//    public void onLoadFinished(android.content.Loader<Object> loader, Object data) {
//
//    }
//
//    @Override
//    public void onLoaderReset(android.content.Loader<Object> loader) {
//
//    }

    @Override
    public void onLoadFinished(Loader<List<PictureInstagram>> loader, List<PictureInstagram> data) {
        mAdapter.setPictureList(data);
//        mImageFetcher.setExitTasksEarly(true);

        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
//        mImageFetcher = new ImageFetcher(getActivity(), mImageThumbSize);
//        mImageFetcher.setLoadingImage(R.drawable.empty_photo);
//        mImageFetcher.addImageCache(getActivity().getSupportFragmentManager(), cacheParams);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<PictureInstagram>> loader) {

    }


    /**
     * Empty constructor as per the Fragment documentation
     */
    public ImageGridFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mActivityContext = getActivity();

        mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);
        mImageThumbSpacing = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_spacing);

        mAdapter = new ImageAdapter(getActivity());

        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(getActivity(), IMAGE_CACHE_DIR);

        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

//        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
//        ((MainActivity)getActivity()).createImageFatcher(mImageThumbSize);
//        mImageFetcher = ((MainActivity)getActivity()).getImageFetcher();
        mImageFetcher = new ImageFetcher(getActivity(), mImageThumbSize);
        mImageFetcher.setLoadingImage(R.drawable.empty_photo);
        mImageFetcher.addImageCache(getActivity().getSupportFragmentManager(), cacheParams);

        startLoading(getSearchUrl("selfie"));
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.image_grid_fragment, container, false);
        final GridView mGridView = (GridView) v.findViewById(R.id.gridView);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(this);
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                // Pause fetcher to ensure smoother scrolling when flinging
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    // Before Honeycomb pause image loading on scroll to help with performance
                    if (!Utils.hasHoneycomb()) {
                        mImageFetcher.setPauseWork(true);
                    }
                } else {
                    mImageFetcher.setPauseWork(false);
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem,
                    int visibleItemCount, int totalItemCount) {
            }
        });

        // This listener is used to get the final width of the GridView and then calculate the
        // number of columns and the width of each column. The width of each column is variable
        // as the GridView has stretchMode=columnWidth. The column width is used to set the height
        // of each view so we get nice square thumbnails.
        mGridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @TargetApi(VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onGlobalLayout() {
                        if (mAdapter.getNumColumns() == 0) {
                            final int numColumns = (int) Math.floor(
                                    mGridView.getWidth() / (mImageThumbSize + mImageThumbSpacing));
                            if (numColumns > 0) {
                                final int columnWidth =
                                        (mGridView.getWidth() / numColumns) - mImageThumbSpacing;
                                mAdapter.setNumColumns(numColumns);
                                mAdapter.setItemHeight(columnWidth);
                                if (BuildConfig.DEBUG) {
                                    Log.d(TAG, "onCreateView - numColumns set to " + numColumns);
                                }
                                if (Utils.hasJellyBean()) {
                                    mGridView.getViewTreeObserver()
                                            .removeOnGlobalLayoutListener(this);
                                } else {
                                    mGridView.getViewTreeObserver()
                                            .removeGlobalOnLayoutListener(this);
                                }
                            }
                        }
                    }
                });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mImageFetcher.setExitTasksEarly(false);
//        mAdapter.notifyDataSetChanged();
        startLoading(getSearchUrl("selfie"));
    }

    @Override
    public void onPause() {
        super.onPause();
        mImageFetcher.setPauseWork(false);
        mImageFetcher.setExitTasksEarly(true);
        mImageFetcher.flushCache();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImageFetcher.closeCache();
    }

    @TargetApi(VERSION_CODES.JELLY_BEAN)
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

//        final Intent i = new Intent(getActivity(), ImageDetailActivity.class);
//        i.putExtra(ImageDetailActivity.EXTRA_IMAGE, (int) id);
//        if (Utils.hasJellyBean()) {
//            // makeThumbnailScaleUpAnimation() looks kind of ugly here as the loading spinner may
//            // show plus the thumbnail image in GridView is cropped. so using
//            // makeScaleUpAnimation() instead.
//            ActivityOptions options =
//                    ActivityOptions.makeScaleUpAnimation(v, 0, 0, v.getWidth(), v.getHeight());
//            getActivity().startActivity(i, options.toBundle());
//        } else {
//            startActivity(i);
//        }
        ImageDetailFragment pictureDialog = new ImageDetailFragment();
        Bundle args = new Bundle();
        String standard_resolution = ((PictureInstagram)mAdapter.getItem(position)).getUrl_standard_resolution();
        args.putString(IMAGE_DATA_EXTRA, standard_resolution);
        args.putInt("Width", ((PictureInstagram)mAdapter.getItem(position)).getSize_standard_resolution().getImageWidth());
        args.putInt("Height", ((PictureInstagram)mAdapter.getItem(position)).getSize_standard_resolution().getImageHeight());
        pictureDialog.setArguments(args);
        pictureDialog.setTargetFragment(this, 0);
        pictureDialog.show(getChildFragmentManager(), "PictureInstagram" );

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear_cache:
                mImageFetcher.clearCache();
                Toast.makeText(getActivity(), R.string.clear_cache_complete_toast,
                        Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * The main adapter that backs the GridView. This is fairly standard except the number of
     * columns in the GridView is used to create a fake top row of empty views as we use a
     * transparent ActionBar and don't want the real top row of images to start off covered by it.
     */
    private class ImageAdapter extends BaseAdapter {

        private final Context mContext;
        private int mItemHeight = 0;
        private int mNumColumns = 0;
        private int mActionBarHeight = 0;
        private GridView.LayoutParams mImageViewLayoutParams;
        private List<PictureInstagram> mPictures;

        public ImageAdapter(Context context) {
            super();
            mContext = context;
            mPictures = new ArrayList<PictureInstagram>();
            mImageViewLayoutParams = new GridView.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            // Calculate ActionBar height
            TypedValue tv = new TypedValue();
            if (context.getTheme().resolveAttribute(
                    android.R.attr.actionBarSize, tv, true)) {
                mActionBarHeight = TypedValue.complexToDimensionPixelSize(
                        tv.data, context.getResources().getDisplayMetrics());
            }
        }

        public void setPictureList(List<PictureInstagram> pics) {
            this.mPictures = pics;
        }

        @Override
        public int getCount() {
//            // If columns have yet to be determined, return no items
//            if (getNumColumns() == 0) {
//                return 0;
//            }

//            // Size + number of columns for top empty row
//            return Images.imageThumbUrls.length + mNumColumns;
            return this.mPictures.size();
        }

        @Override
        public Object getItem(int position) {
//            return position < mNumColumns ?
//                    null : Images.imageThumbUrls[position - mNumColumns];
            return position < mNumColumns ?
                    null : this.mPictures.get(position);

        }

        @Override
        public long getItemId(int position) {
            return position < mNumColumns ? 0 : position - mNumColumns;
        }

        @Override
        public int getViewTypeCount() {
            // Two types of views, the normal ImageView and the top row of empty views
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return (position < mNumColumns) ? 1 : 0;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            //BEGIN_INCLUDE(load_gridview_item)
            // First check if this is the top row
            if (position < mNumColumns) {
                if (convertView == null) {
                    convertView = new View(mContext);
                }
                // Set empty view with height of ActionBar
                convertView.setLayoutParams(new AbsListView.LayoutParams(
                        LayoutParams.MATCH_PARENT, mActionBarHeight));
                return convertView;
            }

            // Now handle the main ImageView thumbnails
            ImageView imageView;
            if (convertView == null) { // if it's not recycled, instantiate and initialize
                imageView = new RecyclingImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(mImageViewLayoutParams);
            } else { // Otherwise re-use the converted view
                imageView = (ImageView) convertView;
            }

            // Check the height matches our calculated column width
            if (imageView.getLayoutParams().height != mItemHeight) {
                imageView.setLayoutParams(mImageViewLayoutParams);
            }

            // Finally load the image asynchronously into the ImageView, this also takes care of
            // setting a placeholder image while the background thread runs
//            mImageFetcher.loadImage(Images.imageThumbUrls[position - mNumColumns], imageView);
            mImageFetcher.loadImage(mPictures.get(position).getUrl_low_resolution(), imageView);
            return imageView;
            //END_INCLUDE(load_gridview_item)
        }

        /**
         * Sets the item height. Useful for when we know the column width so the height can be set
         * to match.
         *
         * @param height
         */
        public void setItemHeight(int height) {
            if (height == mItemHeight) {
                return;
            }
            mItemHeight = height;
            mImageViewLayoutParams =
                    new GridView.LayoutParams(LayoutParams.MATCH_PARENT, mItemHeight);
            mImageFetcher.setImageSize(height);
            notifyDataSetChanged();
        }

        public void setNumColumns(int numColumns) {
            mNumColumns = numColumns;
        }

        public int getNumColumns() {
            return mNumColumns;
        }
    }



    private static class PictureLoader extends AsyncTaskLoader<List<PictureInstagram>> {
        private final String url;
        private Context context;
//        private List<String> pictureUrls;
        private List<PictureInstagram> pictures;

        public PictureLoader(Context context, Bundle args) {
            super(context);
            this.context = context;
            url = args.getString("url");
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();

            if (pictures != null) {
                deliverResult(pictures);
            }

            if (takeContentChanged() || pictures == null)
                forceLoad();
        }

        @Override
        protected void onStopLoading() {
            super.onStopLoading();
        }

        @Override
        protected void onReset() {
            super.onReset();
            onStopLoading();
            pictures.clear();
        }

        @Override
        public List<PictureInstagram> loadInBackground() {
            pictures = loadPicturesUrl(url);
            return  pictures;
        }

        private List<PictureInstagram> loadPicturesUrl(String url) {
            final JSONObject response = loadUsersData(url);
            List<PictureInstagram> urls = parseConversationData(response);
            return urls;
        }

        private List<PictureInstagram> parseConversationData(JSONObject response) {
            List<PictureInstagram> urls = new ArrayList<PictureInstagram>();
            try {
                JSONArray data = response.getJSONArray("data");
                for (int i = 0; i <data.length() ; i++) {
                    JSONObject obj = data.getJSONObject(i).getJSONObject("images");
                    String picture_id = data.getJSONObject(i).getString("id");
                    String url_thumbnail = obj.getJSONObject("thumbnail").getString("url");
                    String url_low_resolution = obj.getJSONObject("low_resolution").getString("url");
                    String url_standard_resolution = obj.getJSONObject("standard_resolution").getString("url");

                    PictureInstagram pic = new PictureInstagram(picture_id);

                    pic.getSize_low_resolution().setImageWidth(obj.getJSONObject("low_resolution").getInt("width"));
                    pic.getSize_thumbnail().setImageWidth(obj.getJSONObject("thumbnail").getInt("width"));
                    pic.getSize_standard_resolution().setImageWidth(obj.getJSONObject("standard_resolution").getInt("width"));

                    pic.getSize_low_resolution().setImageHeight(obj.getJSONObject("low_resolution").getInt("height"));
                    pic.getSize_thumbnail().setImageHeight(obj.getJSONObject("thumbnail").getInt("height"));
                    pic.getSize_standard_resolution().setImageHeight(obj.getJSONObject("standard_resolution").getInt("height"));

                    pic.setUrl_low_resolution(url_low_resolution);
                    pic.setUrl_standard_resolution(url_standard_resolution);
                    pic.setUrl_thumbnail(url_thumbnail);

                    urls.add(pic);
                }

//                "images":{
//                    "low_resolution":{"url":"http:\/\/scontent-b.cdninstagram.com\/hphotos-xaf1\/t51.2885-15\/10860121_402347009943400_508494121_a.jpg","width":306,"height":306},
//                    "thumbnail":{"url":"http:\/\/scontent-b.cdninstagram.com\/hphotos-xaf1\/t51.2885-15\/10860121_402347009943400_508494121_s.jpg","width":150,"height":150},
//                    "standard_resolution":{"url":"http:\/\/scontent-b.cdninstagram.com\/hphotos-xaf1\/t51.2885-15\/10860121_402347009943400_508494121_n.jpg","width":640,"height":640}
//                },
            } catch (JSONException e) {
                Log.d(Utils.TAG, "parseConversationData");
                e.printStackTrace();
            }
            return urls;
        }

        private String streamToString(InputStream p_is)
        {
            try
            {
                BufferedReader m_br;
                StringBuffer m_outString = new StringBuffer();
                m_br = new BufferedReader(new InputStreamReader(p_is));
                String m_read = m_br.readLine();
                while(m_read != null)
                {
                    m_outString.append(m_read);
                    m_read =m_br.readLine();
                }
                return m_outString.toString();
            }
            catch (Exception e)
            {
                return null;
            }
        }

        private JSONObject loadUsersData(String urlString) {
            String data = null;
            try {
                URL url = new URL(urlString);
                InputStream inputStream = url.openConnection().getInputStream();
                data = streamToString(inputStream);
            } catch (IOException e) {
                Log.d(Utils.TAG, "loadUsersData");
                e.printStackTrace();
            }

            JSONObject responseData = null;
            try {
                responseData = new JSONObject(data);
            } catch (JSONException e) {
                Log.d(Utils.TAG, "responseData");
                e.printStackTrace();
            }

            return responseData;
        }
    }

}
