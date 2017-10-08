package com.bleedbytes.webview;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
//onesignal
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
//json
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //webview intialization
    private WebView mWebview;
    ProgressBar bar;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        //onesignal
        OneSignal.startInit(this)
                    .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
                .init();

/* OneSignal.startInit(this)
        .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
        .unsubscribeWhenNotificationsAreDisabled(true)
        .init();*/

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //noinspection deprecation
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //calling declared webview
        mWebview=(WebView) findViewById(R.id.myWebview);
        //WebSettings.
        WebSettings webSettings=mWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebview.loadUrl("http://bleedbytes.in");
        mWebview.setWebViewClient(new myViewClient());
        //progress bar
        bar=(ProgressBar) findViewById(R.id.progressBar1);

        //performance
        //noinspection deprecation
        mWebview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mWebview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //only loads cache when network available
        mWebview.getSettings().setAppCacheEnabled(true);
        mWebview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webSettings.setDomStorageEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        //noinspection deprecation
        webSettings.setEnableSmoothTransition(true);
//end of performance tweaks

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_howto) {
            // Handle the how to section action
            mWebview.loadUrl("http://bleedbytes.in/category/how-to/");
        } else if (id == R.id.nav_geek) {
        //geek section
            mWebview.loadUrl("http://bleedbytes.in/category/geek/");

        } else if (id == R.id.nav_os) {
            //opensource section
            mWebview.loadUrl("http://bleedbytes.in/category/open-source/");

        } else if (id == R.id.nav_tools) {
            //tools section
            mWebview.loadUrl("http://bleedbytes.in/category/tools/");
        } else if (id == R.id.nav_about) {
            //about section
            mWebview.loadUrl("http://bleedbytes.in/about/");

        } else if (id == R.id.nav_contact) {
            //contact sectoin
            mWebview.loadUrl("http://bleedbytes.in/contact/");

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //back button app close fixing
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getAction()==KeyEvent.ACTION_DOWN)
        {
            switch (keyCode)
            {
               case  KeyEvent.KEYCODE_BACK:
                if(mWebview.canGoBack()){
                    mWebview.goBack();
                }
                else
                {
                    finish();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);

    }

    private class myViewClient extends WebViewClient
    {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            bar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    //onesignal class

    // Fires when a notificaiton is opened by tapping on it or one is received while the app is runnning.
    private class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
      //  @Override
        public void notificationOpened(String message, JSONObject additionalData, boolean isActive) {
            try {
                if (additionalData != null) {
                    if (additionalData.has("actionSelected"))
                        Log.d("OneSignalExample", "OneSignal notification button with id " + additionalData.getString("actionSelected") + " pressed");

                    Log.d("OneSignalExample", "Full additionalData:\n" + additionalData.toString());
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        @Override
        public void notificationOpened(OSNotificationOpenResult result) {

        }
    }
}
