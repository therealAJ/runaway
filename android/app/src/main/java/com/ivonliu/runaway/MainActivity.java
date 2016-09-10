package com.ivonliu.runaway;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_TYPE = "type";
    public static final int TYPE_CREATE = 0;
    public static final int TYPE_PAST = 1;

    private OnFabClickListener mFabClickListener;
    public void setFabClickListener(OnFabClickListener listener) {
        mFabClickListener = listener;
    }

    private FloatingActionButton mFab;

    public FloatingActionButton getFab() {
        return mFab;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (!isTaskRoot()) {
            toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        }

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setBackgroundTintList(new ColorStateList(new int[][]{{0}}, new int[]{Utils.getThemePrimaryColor(this)}));
        mFab.setRippleColor(Utils.getThemeAccentColor(this));
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFabClickListener != null) {
                    mFabClickListener.onFabClick((FloatingActionButton) view);
                }
            }
        });

        int type = getIntent().getIntExtra(EXTRA_TYPE, TYPE_CREATE);
        switch(type) {
            case TYPE_CREATE:
            default:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, new MainFragment())
                        .commit();
                getSupportActionBar().setTitle("Main Fragment");
                getFab().setVisibility(View.VISIBLE);
                break;
            /*case TYPE_PAST:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, new PastPhotosFragment())
                        .commit();
                getSupportActionBar().setTitle("Past Catches");
                getFab().setVisibility(View.GONE);
                break;*/
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id ==  android.R.id.home) {
            if (!isTaskRoot()) {
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

}
