package com.tl.pro.musicapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.tl.pro.musicapp.fragment.MyFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	private ViewPager mViewPager;
	private MyPagerIndicator mIndicator;

	private List<String> mTitles = Arrays.asList("短信", "收藏", "推荐", "短信1", "收藏1", "推荐1", "短信2", "收藏2", "推荐2");
	private List<MyFragment> mContens = new ArrayList<>();
	private FragmentPagerAdapter adapter;

	private PagerAdapter adapter2;
	private FragmentManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main2);
		initViews();
		initDatas();
		mViewPager.setAdapter(adapter);
		mIndicator.setVisibleCount(4);
		mIndicator.addPageChangeListener(new MyPagerIndicator.PageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
		mIndicator.setTabItemTitles(mTitles);

		mIndicator.setViewPager(mViewPager, 8);
	}

	private void initDatas() {
		for (String title : mTitles){
			MyFragment frg = MyFragment.newInstance(title);
			mContens.add(frg);
		}

		adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public Fragment getItem(int position) {
				return mContens.get(position);
			}

			@Override
			public int getCount() {
				return mContens.size();
			}
		};

		adapter2 = new PagerAdapter() {
			@Override
			public int getCount() {
				return 0;
			}

			@Override
			public boolean isViewFromObject(View view, Object object) {
				return view == object;
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				return super.instantiateItem(container, position);
			}

			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				super.destroyItem(container, position, object);
			}
		};
	}

	private void initViews() {
		mIndicator = (MyPagerIndicator) findViewById(R.id.indicator);
		mViewPager = (ViewPager) findViewById(R.id.viewPager);
	}

}
