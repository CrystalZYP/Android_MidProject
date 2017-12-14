package com.stone.transition;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class DynamicWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText1 = context.getString(R.string.appwidget_text);
        CharSequence widgetText2 = context.getString(R.string.add_widget);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.dynamic_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText1);
        views.setTextViewText(R.id.appwidget_poem, widgetText2);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        Intent intent = new Intent(context, WidgetActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //实例化RemoteViews对应的Widget布局
        RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.dynamic_widget);
        updateViews.setOnClickPendingIntent(R.id.appWidget, pendingIntent);
        ComponentName componentName = new ComponentName(context, DynamicWidget.class);
        appWidgetManager.updateAppWidget(componentName, updateViews);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

