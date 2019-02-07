package org.simplesns.simplesns.activity.main.favorite.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gigamole.navigationtabstrip.NavigationTabStrip;

import org.simplesns.simplesns.R;

public class MyPostFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static String TAG = MyPostFragment.class.getSimpleName();

    private SwipeRefreshLayout swipeLayout;

    public static MyPostFragment newInstance() {

        // TODO Parameters
        return new MyPostFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_mypost, container, false);

        swipeLayout = view.findViewById(R.id.mypost_swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override public void onRefresh() {
        Log.d(TAG, "Refreshing...");
        new Handler().postDelayed(() -> swipeLayout.setRefreshing(false), 2000);
    }
}