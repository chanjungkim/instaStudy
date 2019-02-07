package org.simplesns.simplesns.activity.main.favorite;

import android.arch.lifecycle.Lifecycle;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebViewFragment;

import com.gigamole.navigationtabstrip.NavigationTabStrip;

import org.simplesns.simplesns.R;
import org.simplesns.simplesns.activity.main.favorite.fragment.FollowingFragment;
import org.simplesns.simplesns.activity.main.favorite.fragment.MyPostFragment;

public class FavoriteFragment extends Fragment {
    private static String TAG = FavoriteFragment.class.getSimpleName();

    int FRAGMENT_COUNT = 2;
    private ViewPager mViewPager;
    PagerAdapter mPagerAdapter;
    private NavigationTabStrip navigationTabStrip;

    public static FavoriteFragment newInstance() {
        // TODO Parameters
        return new FavoriteFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewPager = view.findViewById(R.id.vp);
        navigationTabStrip = view.findViewById(R.id.nts);
        navigationTabStrip.setTitles("FOLLOWING", "MY POST");
        navigationTabStrip.setTabIndex(0, true);
        navigationTabStrip.setStripColor(Color.BLACK);
        navigationTabStrip.setInactiveColor(getResources().getColor(R.color.grey));
        navigationTabStrip.setActiveColor(Color.BLACK);
        navigationTabStrip.setTitleSize(30);
        navigationTabStrip.setStripWeight(2); // allows you to set weight(height) of strip.
        navigationTabStrip.setStripFactor(2); // allows you to set strip resize factor.
        navigationTabStrip.setStripType(NavigationTabStrip.StripType.LINE); // allows you to set strip type - line or point.
        navigationTabStrip.setStripGravity(NavigationTabStrip.StripGravity.BOTTOM);
//        navigationTabStrip.setTypeface("fonts/typeface.ttf");
        navigationTabStrip.setCornersRadius(0); // allows you to set corners radius of strip.
        navigationTabStrip.setAnimationDuration(100);
        setUI();
        navigationTabStrip.setViewPager(mViewPager, 0);
        navigationTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        navigationTabStrip.setOnTabStripSelectedIndexListener(new NavigationTabStrip.OnTabStripSelectedIndexListener() {
            @Override
            public void onStartTabSelected(String title, int index) {

            }

            @Override
            public void onEndTabSelected(String title, int index) {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        return view;
    }

    private void setUI() {
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return FRAGMENT_COUNT;
            }

            @Override
            public boolean isViewFromObject(final View view, final Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(final View container, final int position, final Object object) {
                ((ViewPager) container).removeView((View) object);
            }

            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {
                final View view = new View(getContext());
                container.addView(view);
                return view;
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private String TAG = ViewPagerAdapter.class.getSimpleName();

        int pageCount = 0;
        String titles[] = {"FOLLOWING", "MY POST"};

        ViewPagerAdapter(FragmentManager manager, int _pageCount) {
            super(manager);
            Log.d(TAG, "_pageCount: " + _pageCount);
            pageCount = _pageCount;
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(TAG, "getItem: " + position);
            switch (position) {
                case 0:
                    return new FollowingFragment();
                default:
                    return new MyPostFragment();
            }
        }

        @Override
        public int getCount() {
            return pageCount;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}