package com.planner.removal.removalplanner.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.planner.removal.removalplanner.Model.Task;
import com.planner.removal.removalplanner.Model.TaskDatabaseClient;

public class Persistance {

    public static void SaveTasks(final Activity activity) {

        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                for (Task t : Task.TASK_LIST) {
                    //adding to database
                    TaskDatabaseClient.getInstance(activity).getAppDatabase()
                            .taskDao()
                            .insert(t);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                // finish();
                // startActivity(new Intent(getApplicationContext(), MainActivity.class));
                // Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }

        SaveTask st = new SaveTask();
        st.execute();
    }

    public enum SettingType {
        Sort(0);

        private int value;

        SettingType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static boolean SaveSetting(SettingType settingType, int value, Activity activity) {

        try {
            SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(settingType.toString(), value);
            editor.commit();
            return true;
        } catch (Exception ex) {
            Log.e("Save setting", ex.getMessage());
        }

        return false;
    }

    public static int GetSetting(SettingType settingType, Activity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getInt(settingType.toString(), 0);
    }
}
