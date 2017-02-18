package com.moselin.rmlib.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * @Description 自定义广播接收器
 * @Author MoseLin
 * @Date 2016/7/14.
 */

public class CustomReceiver extends BroadcastReceiver
{

    private String flagAction;
    private ReceiverListener listener;//接收回调
    public CustomReceiver(Context context, String action, ReceiverListener listener){
        flagAction = action;
        context.registerReceiver(this,new IntentFilter(action));
        this.listener = listener;
    }
    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();
        if ( action.equals(flagAction) ){
           if ( listener != null ){
               listener.onReceiverListener(intent);
           }
        }else {
            context.unregisterReceiver(this);
        }
    }

    public interface ReceiverListener
    {
        void onReceiverListener(Intent intent);
    }
}
