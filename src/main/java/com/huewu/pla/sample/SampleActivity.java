package com.huewu.pla.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.huewu.pla.R;
import com.huewu.pla.lib.MultiColumnListView;
import com.huewu.pla.lib.internal.PLA_AbsListView.LayoutParams;
import java.util.Arrays;
import java.util.Random;

public class SampleActivity extends Activity {

    private class MySimpleAdapter extends ArrayAdapter<String> {
        public MySimpleAdapter(Context context, int layoutRes) {
            super(context, layoutRes, android.R.id.text1);
        }
    }

    protected MultiColumnListView multiColumnListView = null;
    private MySimpleAdapter mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_act);
        this.multiColumnListView = (MultiColumnListView) findViewById(R.id.list);
        initHeaderAndFooter();
        this.multiColumnListView.setOnLoadMoreListener(new MultiColumnListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                moreLoadData();
                //setting load flag
                SampleActivity.this.multiColumnListView.onLoadMoreComplete();
            }
        });

    }
    //init  header and footer
    protected void initHeaderAndFooter(){
        {
            for( int i = 0; i < 3; ++i ){
                //add header view.
                TextView tv = new TextView(this);
                tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                tv.setText("Hello Header!! ........................................................................");
                this.multiColumnListView.addHeaderView(tv);
            }
        }
        {
            for( int i = 0; i < 3; ++i ){
                //add footer view.
                TextView tv = new TextView(this);
                tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                tv.setText("Hello Footer!! ........................................................................");
                this.multiColumnListView.addFooterView(tv);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 1001, 0, "Load More Contents");
        menu.add(Menu.NONE, 1002, 0, "Launch Pull-To-Refresh Activity");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case 1001:
            {
                moreLoadData();
            }
            break;
            case 1002:
            {
                Intent intent = new Intent(this, PullToRefreshSampleActivity.class);
                startActivity(intent);
            }
            break;
        }
        return true;
    }

    private void moreLoadData(){
        int startCount = this.mAdapter.getCount();
        for( int i = 0; i < 100; ++i){
            //generate 100 random items.
            StringBuilder builder = new StringBuilder();
            builder.append("Hello!![");
            builder.append(startCount + i);
            builder.append("] ");
            char[] chars = new char[mRand.nextInt(100)];
            Arrays.fill(chars, '1');
            builder.append(chars);
            this.mAdapter.add(builder.toString());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initAdapter();
        this.multiColumnListView.setAdapter(mAdapter);
    }

    private Random mRand = new Random();
    protected void initAdapter() {
        this.mAdapter = new MySimpleAdapter(this, R.layout.sample_item);
        for( int i = 0; i < 30; ++i){
            //generate 30 random items.
            StringBuilder builder = new StringBuilder();
            builder.append("Hello!![");
            builder.append(i);
            builder.append("] ");
            char[] chars = new char[mRand.nextInt(500)];
            Arrays.fill(chars, '1');
            builder.append(chars);
            mAdapter.add(builder.toString());
        }
    }
}//end of class
