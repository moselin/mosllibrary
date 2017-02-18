package com.moselin.rmlib.widget.ad;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moselin.rmlib.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 自定义AdBanner组件
 */
public class AdBannerView extends LinearLayout
{
    private ViewPager mPager;
    private LinearLayout layout;
    private ArrayList<View> mViews = new ArrayList<>();
    private ArrayList<String> strs = new ArrayList<>();
    private Handler mHandler;
    private Timer mTimer;
    private AdBannerAdapter adapter;
    private int pageCount;
    private Context context;
    private ImageView[] imageViews;
    private BannerTimerTask mTimerTask;
    private TextView tv_banner_name;
    private OnImageClick onImageClick;
    private int checkRec;
    private int noCheckRec;
    private boolean infiniteScroll = true;//是否需要无限滚动
    private boolean isStart;

    @SuppressWarnings("deprecation")
    @SuppressLint("HandlerLeak")
    public AdBannerView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    /**
     * 初始化
     *
     * @param context 上下文
     */
    private void init(Context context)
    {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.adbanner, this);
        mPager = (ViewPager) findViewById(R.id.vpBanner);
        layout = (LinearLayout) findViewById(R.id.llImgGroup);

        adapter = new AdBannerAdapter(mViews);
        mPager.setAdapter(adapter);
        mPager.setOnPageChangeListener(new OnPageChangeListener()
        {

            @Override
            public void onPageSelected(int arg0)
            {
                if (imageViews != null && imageViews.length > 0)
                {
                    // 当viewpager换页时 改掉下面对应的小点
                    int position = arg0;
                    if (infiniteScroll)
                        position = arg0 % imageViews.length;
                    for (int i = 0; i < imageViews.length; i++)
                    {
                        // 设置当前的对应的小点为选中状态
                        imageViews[position].setBackgroundResource(checkRec);
                        if (position != i)
                        {
                            // 设置为非选中状态
                            imageViews[i].setBackgroundResource(noCheckRec);
                        }
                    }
                }
                if (tv_banner_name != null && strs.size() > 0)
                {
                    int position = 0;
                    if (infiniteScroll)
                     position = arg0 % strs.size();
                    tv_banner_name.setText(strs.get(position));
                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {

            }

            @Override
            public void onPageScrollStateChanged(int arg0)
            {
            }
        });
        mPager.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        stopPlay();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (isStart)
                        startPlay();
                        break;
                }
                return false;
            }
        });
        mHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                mPager.setCurrentItem(msg.what);
                super.handleMessage(msg);
            }
        };

        mTimer = new Timer();
        adapter.setOnImageClick(new OnImageClick()
        {

            @Override
            public void onImageClick(int position)
            {
                if (onImageClick != null)
                {
                    onImageClick.onImageClick(position);
                }
            }
        });
    }

    public void setBanners(ArrayList<View> mView)
    {
        setBanners(mView, strs, 0, 0);
    }
    public void setBanners(ArrayList<View> mView,int check, int noCheck)
    {
        setBanners(mView, strs, check, noCheck);
    }

    public void setBanners(ArrayList<View> mView, ArrayList<String> strs)
    {
        setBanners(mView, strs, 0, 0);
    }

    public void setBanners(ArrayList<View> mView, ArrayList<String> strs,
                           int check, int noCheck)
    {
        checkRec = check;
        noCheckRec = noCheck;
        if (strs != null)
        {
            this.strs.clear();
            this.strs.addAll(strs);
        }
        mViews.clear();
        mViews.addAll(mView);
        adapter.notifyDataSetChanged();
        pageCount = mView.size();
        if (checkRec == 0 && noCheckRec == 0)
        {
            layout.setVisibility(GONE);
        } else
        {
            initPoint();
        }

    }

    /**
     * 初始化圆点
     */
    private void initPoint()
    {
        imageViews = new ImageView[pageCount];
        ImageView imageView;
        for (int i = 0; i < pageCount; i++)
        {
            LayoutParams margin = new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            // 设置每个小圆点距离左边的间距
            margin.setMargins(10, 0, 0, 0);
            imageView = new ImageView(context);
            // 设置每个小圆点的宽高
            imageView.setLayoutParams(new LayoutParams(15, 15));
            imageViews[i] = imageView;
            if (i == 0)
            {
                // 默认选中第一张图片
                imageViews[i].setBackgroundResource(checkRec);
            } else
            {
                // 其他图片都设置未选中状态
                imageViews[i].setBackgroundResource(noCheckRec);
            }
            layout.addView(imageViews[i], margin);
        }
    }

    // 启动banner自动轮播
    public void startPlay()
    {
        isStart = true;
        if (mTimer != null)
        {
            if (mTimerTask != null)
                mTimerTask.cancel();
            mTimerTask = new BannerTimerTask();
            mTimer.schedule(mTimerTask, 3000, 3000);// 5秒后执行，每隔5秒执行一次
        }
    }

    // 暂停banner自动轮播
    public void stopPlay()
    {
        if (mTimerTask != null)
            mTimerTask.cancel();
    }

    /**
     * banner定时器
     */
    private class BannerTimerTask extends TimerTask
    {
        @Override
        public void run()
        {
            Message msg = new Message();
            if (mViews.size() <= 1)
                return;
            int currentIndex = mPager.getCurrentItem();
            if (!infiniteScroll)
            {
                if (currentIndex == mViews.size() - 1)
                    msg.what = 0;
                else
                    msg.what = currentIndex + 1;
            }else {
                msg.what = currentIndex + 1;
            }
            mHandler.sendMessage(msg);
        }

    }

    /**
     * 设置小圆点在而已中的位置
     *
     * @param gravity 位置
     */
    public void setPointGravity(int gravity)
    {
        layout.setGravity(gravity);
    }

    /**
     * 设置广告名称
     *
     * @param view textview
     */
    public void setBannerName(TextView view)
    {
        tv_banner_name = view;
    }

    public void setBannerHeight(int height)
    {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, height);
        mPager.setLayoutParams(params);
    }

    public void setInfiniteScroll(boolean infiniteScroll)
    {
        this.infiniteScroll = infiniteScroll;
        adapter.setInfiniteScroll(infiniteScroll);
    }

    public OnImageClick getOnImageClick()
    {
        return onImageClick;
    }

    public void setOnImageClick(OnImageClick onImageClick)
    {
        this.onImageClick = onImageClick;
    }

    public interface OnImageClick
    {
        void onImageClick(int position);
    }
}
