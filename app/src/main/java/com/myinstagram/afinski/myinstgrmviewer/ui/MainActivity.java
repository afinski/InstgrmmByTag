package com.myinstagram.afinski.myinstgrmviewer.ui;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.myinstagram.afinski.myinstgrmviewer.BuildConfig;
import com.myinstagram.afinski.myinstgrmviewer.R;
import com.myinstagram.afinski.myinstgrmviewer.util.ImageFetcher;
import com.myinstagram.afinski.myinstgrmviewer.util.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;

public class MainActivity extends FragmentActivity {
    private static final String TAG = "ImageGridActivity";
    private ImageFetcher mImageFetcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Utils.enableStrictMode();
        }
        super.onCreate(savedInstanceState);

        if (getSupportFragmentManager().findFragmentByTag(TAG) == null) {
            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(android.R.id.content, new ImageGridFragment(), TAG);
            ft.commit();
        }
    }

//    /**
//     * Called by the ViewPager child fragments to load images via the one ImageFetcher
//     */
//    public ImageFetcher getImageFetcher() {
//        return mImageFetcher;
//    }
//
//    public void createImageFatcher(int mImageThumbSize) {
//        mImageFetcher = new ImageFetcher(this, mImageThumbSize);
//    }
}
//
//public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<List<String>> {
//
//
//
//
//}
