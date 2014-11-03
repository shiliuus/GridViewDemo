package com.example.liu.gridviewdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;


public class MainActivity extends Activity {
    private GridView mGridView;
    private String [] imageThumbUrls = Images.imageThumbUrls;
    private ImageAdapter mImageAdapter;
    private FileUtils fileUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fileUtils = new FileUtils(this);
        mGridView = (GridView) findViewById(R.id.gridView);
        mImageAdapter = new ImageAdapter(this, imageThumbUrls, mGridView);
        mGridView.setAdapter(mImageAdapter);
    }

    @Override
    protected void onDestroy() {
        mImageAdapter.cancelTask();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add("delete cache in the phone");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                fileUtils.deleteFolder();
                Toast.makeText(getApplication(), "delete succeed!", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}