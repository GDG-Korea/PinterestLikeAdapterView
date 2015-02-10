package com.huewu.pla.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.huewu.pla.lib.MultiColumnListView;
import com.huewu.pla.sample.view.SlidingTabLayout;

public class SampleActivity extends ActionBarActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_act);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final SlidingTabLayout tabLayout = (SlidingTabLayout)findViewById(R.id.sliding_tabs);
        tabLayout.setViewPager(mViewPager);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "SECTION " + position;
        }
    }

    public static class PlaceholderFragment extends Fragment {

        private PLAAdapter mAdapter;

        public static PlaceholderFragment newInstance(int i) {
            return new PlaceholderFragment();
        }

        public PlaceholderFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.sample_frag, container, false);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {

            MultiColumnListView listView = (MultiColumnListView) view.findViewById(R.id.list);
            mAdapter = new PLAAdapter(getActivity());
            fillAdapter(mAdapter, 30);
            listView.setAdapter(mAdapter);
        }

        private void fillAdapter(PLAAdapter adapter, int count) {
            for (int i = 0; i < count; ++i) {
                StringBuilder builder = new StringBuilder();
                for (int j = adapter.getCount(), max = (i * 1234) % 500; j < max; j++)
                    builder.append(i).append(' ');
                adapter.add(builder.toString());
            }
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            menu.add(Menu.NONE, 1002, 0, "Load More Contents");
            super.onCreateOptionsMenu(menu, inflater);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            switch (item.getItemId()) {
                case 1002: {
                    fillAdapter(mAdapter, 100);
                }
                break;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    private static class PLAAdapter extends ArrayAdapter<String> {
        public PLAAdapter(Context context) {
            super(context, R.layout.sample_item, android.R.id.text1);
        }
    }
}
