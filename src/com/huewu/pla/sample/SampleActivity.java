package com.huewu.pla.sample;

import java.util.Arrays;
import java.util.Random;

import com.huewu.pla.smaple.R;
import com.huewu.pla.lib.internal.PLA_AdapterView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

public class SampleActivity extends Activity {
	
	private class MySimpleAdapter extends ArrayAdapter<String> {

		public MySimpleAdapter(Context context, int layoutRes) {
			super(context, layoutRes, android.R.id.text1);
		}
	}
	
	private PLA_AdapterView<Adapter> mAdapterView = null;
	private MySimpleAdapter mAdapter = null;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_sample);
		mAdapterView = (PLA_AdapterView<Adapter>) findViewById(R.id.list);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("load more contents");
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		int startCount = mAdapter.getCount();
		for( int i = 0; i < 100; ++i){
			//generate 100 random items.

			StringBuilder builder = new StringBuilder();
			builder.append("Hello!![");
			builder.append(startCount + i);
			builder.append("] ");

			char[] chars = new char[mRand.nextInt(100)];
			Arrays.fill(chars, '1');
			builder.append(chars);
			mAdapter.add(builder.toString());
		}
		
		return true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initAdapter();
		mAdapterView.setAdapter(mAdapter);
	}

	private Random mRand = new Random();
	private void initAdapter() {
		mAdapter = new MySimpleAdapter(this, R.layout.item_sample);
		
		for( int i = 0; i < 100; ++i){
			//generate 100 random items.

			StringBuilder builder = new StringBuilder();
			builder.append("Hello!![");
			builder.append(i);
			builder.append("] ");

			char[] chars = new char[mRand.nextInt(100)];
			Arrays.fill(chars, '1');
			builder.append(chars);
			mAdapter.add(builder.toString());
		}
		
	}

}//end of class
