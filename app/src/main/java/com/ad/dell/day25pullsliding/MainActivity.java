package com.ad.dell.day25pullsliding;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements PullToRefreshBase.OnRefreshListener,
        Handler.Callback{
    
    private PullToRefreshListView mPullToRefreshListView;
    private List<String > mData=new ArrayList<>();
    private Handler mHandler=new Handler(this);
    private ArrayAdapter<String> mAdapter;
    private ILoadingLayout mPullHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mPullToRefreshListView= (PullToRefreshListView) findViewById(R.id.pullLv);
        
        iniPullLv();
    }

    private void iniPullLv() {
        for (int i = 0; i < 100; i++) {
            mData.add(String.format("HelloWord%s","Android"+i));
        }
        mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
                mData);
        mPullToRefreshListView.setAdapter(mAdapter);

        //设置监听
        mPullToRefreshListView.setOnRefreshListener(this);

        //设置头布局:
        initPullLvHeader();

        //设置lv条目点击事件
        mPullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // 条目position=参数position +1   (点击position==0 的位置,显示的却是1,因为有头布局,所以listView整体的位置都加了个1)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "点击的条目是:"+position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //设置头布局
    private void initPullLvHeader() {
        //LoadingLayout就是正在加载的布局,指Haader  Proxy 代理
        /**
         * 参1: boolean includeStart 是否包含上来
         * 参2: boolean includeEnd 是否包含下来
         * ---->根据ptrMode去定
         */
        mPullHeader = mPullToRefreshListView.getLoadingLayoutProxy(true, false);
//        mPullHeader.setLoadingDrawable(Drawable drawable)不要在代码中设置,会有问题,--->应在布局文件中设置
        //下拉刷新显示的文本
        mPullHeader.setPullLabel("刷新更多哦");
        //设置拉到一定程度显示的文本,只有拉到一定幅度才会显示该文本,松手才会触发刷新
        mPullHeader.setReleaseLabel("放开就可以进行刷新啦");
        //设置正在刷新时显示的文本
        mPullHeader.setRefreshingLabel("拼命加载中...");
        //设置最后一次刷新的时间
        String lastTime= DateUtils.formatDateTime(this,System.currentTimeMillis(),
                DateUtils.FORMAT_ABBREV_TIME|
                DateUtils.FORMAT_SHOW_DATE|
        DateUtils.FORMAT_SHOW_TIME);
        mPullHeader.setLastUpdatedLabel(lastTime);

    }

    //刷新
    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        //获取新的数据,联网请求----在这里模拟一下联网请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    //更新数据--->线程跳转---采用Handler
                    mHandler.sendEmptyMessage(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 200:
                mData.clear();
                for (int i = 0; i < 100; i++) {
                    //网址进行格式化的时候很有用,特别是新网类的网址
                    mData.add(String.format(Locale.CHINA,"第%d条数据%s",i,"香山红叶"));
                }
                //更新适配器
                mAdapter.notifyDataSetChanged();
                //让刷新停止下来
                if (mPullToRefreshListView.isRefreshing()) {
                    mPullToRefreshListView.onRefreshComplete();
                }

                //更新最后一次刷新的时间  不在这再写一遍的话,则更新时间只会执行一次(onCreate里面那次)
                String lastTime= DateUtils.formatDateTime(this,System.currentTimeMillis(),
                        DateUtils.FORMAT_ABBREV_TIME|
                                DateUtils.FORMAT_SHOW_DATE|
                                DateUtils.FORMAT_SHOW_TIME);
                mPullHeader.setLastUpdatedLabel(lastTime);
                break;
            default:
                break;
        }

        return false;
    }
}
