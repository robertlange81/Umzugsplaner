package com.planner.removal.removalplanner.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.planner.removal.removalplanner.Activities.MainActivity;
import com.planner.removal.removalplanner.Model.Task;
import com.planner.removal.removalplanner.Model.TaskDao;
import com.planner.removal.removalplanner.Model.TaskDatabaseClient;

import java.util.List;

public class Persistance {

    public static void ReplaceAllTasks(final Activity activity) {

        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                TaskDao dao = TaskDatabaseClient.getInstance(activity)
                        .getAppDatabase()
                        .taskDao();

                List<Task> oldTasks = dao.getAll();

                for (Task t : oldTasks) {
                    try {
                        dao.delete(t);
                    } catch (Exception e) {
                        Log.e("Error Delete Task: ", e.getMessage());
                    }
                }

                for (Task t : Task.TASK_LIST) {
                    // TODO validate
                    try {
                        Task old = dao.get(t.id);
                        if(old == null) {
                            dao.insert(t);
                        } else {
                            dao.update(t);
                        }
                    } catch (Exception e) {
                        Log.e("Save Task to sqlite", e.getMessage());
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }

        SaveTask st = new SaveTask();
        st.execute();
    }

    public static void SaveTasks(final Activity activity) {

        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                for (Task t : Task.TASK_LIST) {
                    // TODO validate
                    try {
                        TaskDatabaseClient.getInstance(activity).getAppDatabase()
                                .taskDao()
                                .insert(t);
                    } catch (Exception e) {
                        Log.e("Save Task to sqlite", e.getMessage());
                    }
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

    public static void LoadTasks(final Activity activity) {
        class GetTasks extends AsyncTask<Void, Void, List<Task>> {

            @Override
            protected List<Task> doInBackground(Void... voids) {
                List<Task> taskList = TaskDatabaseClient
                        .getInstance(activity)
                        .getAppDatabase()
                        .taskDao()
                        .getAll();
                return taskList;
            }

            @Override
            protected void onPostExecute(List<Task> tasks) {
                super.onPostExecute(tasks);
                Task.TASK_MAP.clear();
                Task.TASK_LIST.clear();
                for (Task t : tasks) {
                    Task.addTask(t);
                }
                MainActivity.NotifyTaskChanged();
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    private void updateTask(final Task task, final Activity activity) {

        class UpdateTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                TaskDatabaseClient.getInstance(activity).getAppDatabase()
                        .taskDao()
                        .update(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }

        UpdateTask ut = new UpdateTask();
        ut.execute();
    }


    private void deleteTask(final Task task, final Activity activity) {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                TaskDatabaseClient.getInstance(activity).getAppDatabase()
                        .taskDao()
                        .delete(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();
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
