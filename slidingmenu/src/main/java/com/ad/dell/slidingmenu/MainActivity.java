package com.ad.dell.slidingmenu;

import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //SlidingMenu其实就是一个菜单,将这个菜单加到Activity里面就可以了,这个菜单是通过滑动来显示菜单
    private SlidingMenu mSlidingMenu;

    //菜单里面的Button
    private Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //设置
        initSlidingMenu();
        //button
        initButton();

        //设置window的背景图片
        getWindow().setBackgroundDrawableResource(R.mipmap.p17);

    }

    private void initButton() {
        bt= (Button) findViewById(R.id.clearCache);
        bt.setOnClickListener(this);
    }

    private void initSlidingMenu() {
        mSlidingMenu=new SlidingMenu(this);
        //任何一个控件,想使用它其实就是对它进行一个设置
        //设置菜单的模式,让它从左侧划出
        mSlidingMenu.setMode(SlidingMenu.LEFT);
        //设置产生滑动效果的触摸模式: 用户点到边界的时候才让菜单划出 --->有三种模式:边界,全屏,NONE
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        // 设置菜单  可以是一个布局视图,也可以是一个view
        mSlidingMenu.setMenu(R.layout.menu);
        // SldingMenu它是一个布局,刚才仅仅是对其进行创建
        // 让这个菜单挂载在Activity上进行显示,和Fragment类似,Fragment也需要挂载在Activity上进行显示
        //关联Activity,显示到Activity上
        /**
         * 参1: Activity activity,
         * 参2: int slideStyle  侧滑样式
         * 参2的值有两种:
         * SLIDING_CONTENT  --侧滑的时候,ActionBar不会跟着滑动
         * SLIDING_WINDOW  --侧滑的时候,ActionBar会跟着滑动,因为ActionBar是Window的一部分
         */
        mSlidingMenu.attachToActivity(this,SlidingMenu.SLIDING_CONTENT);
        /**
         * 前面SlidingMenu已经添加到Activity上了,MainActivity对应着一个布局
         * SlidingMenu和咱们Activity的布局就有上面下面这一说了
         * Behind对应菜单,Above对应Activity的布局
         */
        //菜单拉出来之后,距离屏幕右边200px
        mSlidingMenu.setBehindOffset(200);
        //设置滑动时的视差效果 范围0--1.0  0;左侧菜单被覆盖住的感觉,看不出是划出来的,1:视差效果最明显拉多少,显示多少
        mSlidingMenu.setBehindScrollScale(0.5f);
        //设置菜单往外滑动的时候对应的动画效果
        // behind 对应菜单
        mSlidingMenu.setBehindCanvasTransformer(new SlidingMenu.CanvasTransformer() {
                /**
                 * 参数1: 画布  canvas决定着菜单滑出来时,如何进行绘制
                 * 参数2: 打开的百分比 0 到1.0
                 */
            @Override
            public void transformCanvas(Canvas canvas, float percentOpen) {
                float sx=0.5f+percentOpen*0.5f;
                canvas.scale(sx,sx,canvas.getWidth()/2,canvas.getHeight()/2);
//                //根据拉出的百分比一直让它旋转(旋转的最大角度1080)
//                canvas.rotate(1080*percentOpen,canvas.getWidth()/2,canvas.getHeight()/2);
//
                //scale 和 rotate两个动画效果 不会覆盖,而是会叠加
            }
        });
        //设置Activity的布局界面的动画
        /**
         * 作者没写Activity的布局界面动画
         * 因此需要修改源码,添加一个Activity的布局界面的动画
         */
        mSlidingMenu.setAboveCanvasTransformer(new SlidingMenu.CanvasTransformer() {
            @Override
            public void transformCanvas(Canvas canvas, float percentOpen) {
                //菜单往外滑一点点放大,对应Acivity应该一点点缩小
                float sx=1-0.5f*percentOpen;
                float sy=sx;
                /**
                 * x轴中心点给的0 使用的相对于它自己,这样可使在执行动画时,Activity的界面最后挨着菜单的界面
                 */
                canvas.scale(sx,sy,0,canvas.getHeight()/2);
            }
        });
    }

    //菜单里面的按钮的点击事件
    @Override
    public void onClick(View v) {
        Toast.makeText(this,"清除数据成功",Toast.LENGTH_SHORT).show();

        //清除数据后将菜单进行关闭
        mSlidingMenu.toggle();
    }

    //Activity布局文件里面的按钮的点击事件
    public void show(View view) {
        mSlidingMenu.toggle();   //toggle 开关,切换开关
    }
}
