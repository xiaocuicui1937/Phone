package com.lol.contacts.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.lol.contacts.R;
import com.lol.contacts.activity.IntimacyActivity;

public class IntimacyDegreeService extends Service {
    private static final String TAG = "IntimacyDegreeService";

    public IntimacyDegreeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //创建服务的时候就开始监听最亲密人之间的通话时间
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setLights(0x126789,500,10);
        builder.setSmallIcon(R.drawable.mm);
        //builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.zisebg));
        builder.setAutoCancel(true);

        //跳转到主ui中
        Intent intent = new Intent(this, IntimacyActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
        builder.setContentIntent(pendingIntent);

        //RemoteViews创建自定义
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.bg_notification);
        Notification notification = builder.build();
        //指定展开状态的视图
        notification.bigContentView = remoteViews;

        //发送通知
        manager.notify(1,notification);
        Log.i(TAG,"收到发送过来的通知");
    }
}
