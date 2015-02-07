package com.huewu.pla.sample;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import com.huewu.pla.R;
import com.huewu.pla.lib.MultiColumnListView;
import com.huewu.pla.lib.MultiColumnPullToRefreshListView;

import java.net.URL;
import java.text.SimpleDateFormat;

public class PullToRefreshSampleActivity extends SampleActivity {

	private class MySimpleAdapter extends ArrayAdapter<String> {
		public MySimpleAdapter(Context context, int layoutRes) {
			super(context, layoutRes, android.R.id.text1);
		}
	}
    private MultiColumnPullToRefreshListView multiColumnPullToRefreshListView = null;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sample_pull_to_refresh_act);
        multiColumnPullToRefreshListView = (MultiColumnPullToRefreshListView) findViewById(R.id.list);
        super.multiColumnListView = this.multiColumnPullToRefreshListView;
        this.multiColumnPullToRefreshListView.setShowLastUpdatedText(true);
        this.multiColumnPullToRefreshListView.setLastUpdatedDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        this.multiColumnPullToRefreshListView.setOnRefreshListener(new MultiColumnPullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PullToRefreshSampleActivity.super.initAdapter();
                //setting load flag
                PullToRefreshSampleActivity.this.multiColumnPullToRefreshListView.onRefreshComplete();
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, 1001, 0, "Load More Contents");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}//end of class
