package com.ad.dell.swiperefresh;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    // layout:  包裹具体控件
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mLv;
    private List<String> mData=new ArrayList<>();
    private BaseAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mSwipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.srl);
        mLv = (ListView) findViewById(R.id.lv);
        
        initData();

        initLv();

        initSwipe();

    }

    private void initSwipe() {
        //设置加载圈的背景颜色   SchemeColor:配色方案
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.YELLOW);
        //修改加载圈的颜色
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED,Color.GREEN,Color.BLUE);
        // 设置加载圈的大小  ---只有两种Large,Default
        mSwipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        // Trigger 扳机--->触发的意思
        int distance=getResources().getDisplayMetrics().heightPixels/8; //手机屏幕高度的1/8
        //设置向下拉动多少,触发下拉刷新---设置触发刷新的距离
        mSwipeRefreshLayout.setDistanceToTriggerSync(distance);
        // 加载的视图(指加载圈)在哪里进行展示
        mSwipeRefreshLayout.setProgressViewEndTarget(true,300);//第一个参数:是否进行缩放 boolean scale
        // 设置下拉的监听
        mSwipeRefreshLayout.setOnRefreshListener(this);

    }

    private void initLv() {
        mAdapter =new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                mData);
        mLv.setAdapter(mAdapter);
    }

    private void initData() {
        for (int i = 0; i < 100; i++) {
            mData.add("老夫聊发少年狂,左牵黄,右擎苍");
        }
    }

    @Override
    public void onRefresh() {
        //刷新
        //联网获取新的数据
        //模拟联网请求
        //网络请求是耗时操作,所以使用开子线程的方式来模拟
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    //在主线程更新数据
                    //切换到主线程(三种方式:post,handler,runOnUiThread)
                    mLv.post(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < 10; i++) {
                                //参1 把数据加到哪个位置
                                mData.add(0,"明月照纱窗格格孔明诸葛亮");//把新数据加到前面
                            }
                            mAdapter.notifyDataSetChanged();

                            //停止刷新操作
                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
