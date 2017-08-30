/*
* Copyright (C) 2017 khbtravel.com
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
* twitter.com/ardianys
* August 2017
*/

package com.khbtravel.ardianys.dapoor.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.khbtravel.ardianys.dapoor.RecipeListActivity;
import com.khbtravel.ardianys.dapoor.pojo.Recipe;

import static com.khbtravel.ardianys.dapoor.RecipeListActivity.INTENT_PARCEL_RECIPE;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class RecipeWidgetService extends IntentService {

    public static final String ACTION_UPDATE_WIDGETS = "com.khbtravel.ardianys.dapoor.widget.update_widget";

    public RecipeWidgetService() {
        super("RecipeWidgetService");
    }


    /**
     * Starts this service to perform UpdatePlantWidgets action with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionUpdateWidgets(Context context, Recipe recipe) {
        Intent intent = new Intent(context, RecipeWidgetService.class);
        intent.putExtra(INTENT_PARCEL_RECIPE, recipe);
        intent.setAction(ACTION_UPDATE_WIDGETS);
        context.startService(intent);
    }

    /**
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_WIDGETS.equals(action)) {
                Recipe recipe;
                if (intent.hasExtra(RecipeListActivity.INTENT_PARCEL_RECIPE)) {
                    recipe = intent.getParcelableExtra(RecipeListActivity.INTENT_PARCEL_RECIPE);
                    handleActionUpdateWidgets(recipe);
                }
            }
        }
    }


    /**
     * Handle action UpdatePlantWidgets in the provided background thread
     */
    private void handleActionUpdateWidgets(Recipe recipe) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
        RecipeWidgetProvider.updateAppWidgets(this, appWidgetManager, appWidgetIds, recipe);
    }
}
