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

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class Persistance {

    public static Queue<AsyncTask<Void, Void, Void>> queue;

    static void addTaskToQueue(AsyncTask<Void, Void, Void> t) {
        if(queue == null) {
            queue = new LinkedList<AsyncTask<Void, Void, Void>>();
            t.execute();
        } else {
            if (CheckQueue()) {
                t.execute();
            } else {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        CheckQueue();
                    }
                }, 0, 500);
            }
        }
        queue.add(t);
    }

    public static boolean CheckQueue() {

        while(queue != null && queue.peek() != null) {
            AsyncTask<Void, Void, Void> at = queue.peek();

            if(at.getStatus() == AsyncTask.Status.FINISHED) {
                queue.remove();
            }

            if(at.getStatus() == AsyncTask.Status.PENDING) {
                at.execute();
                return false;
            }

            if(at.getStatus() == AsyncTask.Status.RUNNING) {
                return false;
            }
        }

        return true;
    }

    public static void ReplaceAllTasks(final Activity activity) {

        class ReplaceAllTasks extends AsyncTask<Void, Void, Void> {

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

        ReplaceAllTasks rt = new ReplaceAllTasks();
        addTaskToQueue(rt);
    }

    public static void DeleteAllTasks(final Activity activity) {

        class DeleteAllTasks extends AsyncTask<Void, Void, Void> {

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

                Task.clearAll();

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                MainActivity.NotifyTaskChanged(null, activity);
            }
        }

        DeleteAllTasks dt = new DeleteAllTasks();
        addTaskToQueue(dt);
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
                MainActivity.NotifyTaskChanged(null, activity);
                // finish();
                // startActivity(new Intent(getApplicationContext(), MainActivity.class));
                // Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }

        SaveTask st = new SaveTask();
        addTaskToQueue(st);
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
                MainActivity.NotifyTaskChanged(null, activity);
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
        //addTaskToQueue(gt);
    }

    public static void SaveOrUpdateTask(final Task task, final Activity activity) {

        class UpdateTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                TaskDao dao = TaskDatabaseClient.getInstance(activity)
                        .getAppDatabase()
                        .taskDao();

                Task old = dao.get(task.id);

                if(old == null) {
                    dao.insert(task);
                } else {
                    dao.update(task);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }

        UpdateTask ut = new UpdateTask();
        addTaskToQueue(ut);
    }


    public static void DeleteTask(final Task task, final Activity activity) {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                TaskDao dao = TaskDatabaseClient.getInstance(activity)
                        .getAppDatabase()
                        .taskDao();

                Task old = dao.get(task.id);

                if(old != null) {
                    dao.delete(old);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }

        DeleteTask dt = new DeleteTask();
        addTaskToQueue(dt);
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
        } catch (Throwable t) {
            Log.e("Save setting", t.getMessage());
        }

        return false;
    }

    public static int LoadSetting(SettingType settingType, Activity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getInt(settingType.toString(), 0);
    }
}
