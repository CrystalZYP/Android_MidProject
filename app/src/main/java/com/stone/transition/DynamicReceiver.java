package com.stone.transition;

import android.app.Notification;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

/**
 * Created by Crystal on 2017/11/26.
 */

public class DynamicReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("DYNAMICACTION")) {
            Bundle bundle = intent.getExtras();
            String message = bundle.getString("message");
            Intent mIntent = new Intent(context, WidgetActivity.class);
            PendingIntent mPendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentIntent(mPendingIntent);

            //实例化RemoteViews对应的Widget布局
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.dynamic_widget);
            remoteViews.setOnClickPendingIntent(R.id.appWidget, mPendingIntent);
            remoteViews.setTextViewText(R.id.appwidget_poem, message);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            appWidgetManager.updateAppWidget(new ComponentName(context, DynamicWidget.class), remoteViews);
        }
    }
}
