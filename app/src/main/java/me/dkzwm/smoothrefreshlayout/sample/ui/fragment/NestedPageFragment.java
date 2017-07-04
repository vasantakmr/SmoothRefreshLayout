package me.dkzwm.smoothrefreshlayout.sample.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import me.dkzwm.smoothrefreshlayout.MaterialSmoothRefreshLayout;
import me.dkzwm.smoothrefreshlayout.RefreshingListenerAdapter;
import me.dkzwm.smoothrefreshlayout.SmoothRefreshLayout;
import me.dkzwm.smoothrefreshlayout.indicator.IIndicator;
import me.dkzwm.smoothrefreshlayout.sample.R;
import me.dkzwm.smoothrefreshlayout.sample.adapter.RecyclerViewAdapter;
import me.dkzwm.smoothrefreshlayout.sample.util.DataUtil;

/**
 * Created by dkzwm on 2017/6/2.
 *
 * @author dkzwm
 */

public class NestedPageFragment extends Fragment {
    private int mColor;
    private int mCount;
    private Handler mHandler = new Handler();
    private MaterialSmoothRefreshLayout mRefreshLayout;
    private RecyclerViewAdapter mAdapter;

    public static NestedPageFragment newInstance(int color) {
        NestedPageFragment fragment = new NestedPageFragment();
        fragment.mColor = color;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nested_page, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id
                .recyclerView_nested_page_fragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        mAdapter = new RecyclerViewAdapter(inflater);
        recyclerView.setAdapter(mAdapter);
        mRefreshLayout = (MaterialSmoothRefreshLayout) view.findViewById(R.id
                .smoothRefreshLayout_nested_page_fragment);
        mRefreshLayout.materialStyle();
        mRefreshLayout.setOnRefreshListener(new RefreshingListenerAdapter() {
            @Override
            public void onRefreshBegin(final boolean isRefresh) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isRefresh) {
                            mCount = 0;
                            List<String> list = DataUtil.createList(mCount, 20);
                            mCount += 20;
                            mAdapter.updateData(list);
                        } else {
                            List<String> list = DataUtil.createList(mCount, 20);
                            mCount += 20;
                            mAdapter.appendData(list);
                        }
                        mRefreshLayout.refreshComplete();
                    }
                }, 2000);
            }
        });
        mRefreshLayout.setOnUIPositionChangedListener(new SmoothRefreshLayout.OnUIPositionChangedListener() {
            @Override
            public void onChanged(byte status, IIndicator indicator) {
                if (indicator.getMovingStatus() == IIndicator.MOVING_FOOTER) {
                    mRefreshLayout.setEnablePinContentView(false);
                } else {
                    mRefreshLayout.setEnablePinContentView(true);
                    mRefreshLayout.setEnablePinRefreshViewWhileLoading(true);
                }
            }
        });
        mRefreshLayout.setBackgroundColor(mColor);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
