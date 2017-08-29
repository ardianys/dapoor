package com.khbtravel.ardianys.dapoor.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.khbtravel.ardianys.dapoor.R;
import com.khbtravel.ardianys.dapoor.RecipeDetailActivity;
import com.khbtravel.ardianys.dapoor.RecipeListActivity;
import com.khbtravel.ardianys.dapoor.pojo.Recipe;

import static com.khbtravel.ardianys.dapoor.RecipeDetailActivity.SHARED_PREFS_INGREDIENTS;
import static com.khbtravel.ardianys.dapoor.RecipeListActivity.INTENT_PARCEL_RECIPE;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    public static Recipe recipe;

    static void updateAppWidget(Context context,
                                AppWidgetManager appWidgetManager,
                                int appWidgetId, Recipe mrecipe) {

        Intent intent = new Intent(context, RecipeListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget_provider);

        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPreferences.contains(SHARED_PREFS_INGREDIENTS)){
            String ingredients = sharedPreferences.getString("SHARED_PREFS_INGREDIENTS", "Not set");
            views.setTextViewText(R.id.appwidget_text, ingredients);
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateAppWidgets(Context context,
                                        AppWidgetManager appWidgetManager,
                                        int[] appWidgetIds, Recipe recipe) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipe);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipe);
        }
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

