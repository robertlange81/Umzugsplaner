package com.planner.removal.removalplanner.Activities;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.planner.removal.removalplanner.Fragments.TaskDetailFragment;
import com.planner.removal.removalplanner.Model.Task;
import com.planner.removal.removalplanner.Fragments.DetailDialogFragment;
import com.planner.removal.removalplanner.Helper.Formater;
import com.planner.removal.removalplanner.Helper.TaskInitializer;
import com.planner.removal.removalplanner.Helper.Command;
import com.planner.removal.removalplanner.Model.Prio;
import com.planner.removal.removalplanner.R;

import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity implements DetailDialogFragment.DetailDialogListener {

    private boolean mTwoPane;
    private SimpleItemRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(Formater.currentLocal == null) {
            Formater.setCurrentLocale(this);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton addNewAction = findViewById(R.id.fab);
        addNewAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddNewTask(null);
            }
        });

        Date defaultDate = new Date(118, 11, 1, 0, 0, 0);
        TaskInitializer.CreateDefaultTasks(defaultDate);

        if (findViewById(R.id.task_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w800dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openAddNewTask(String id) {
        if(mTwoPane) {
            Intent i = new Intent(this, DetailActivity.class);
            i.putExtra(DetailActivity.ARG_TASK_ID, id);
            startActivity(i);
        }
    }

    public void showDetailDialog() {
        FragmentManager fm = getFragmentManager();
        DialogFragment dialog = new DetailDialogFragment();
        dialog.setRetainInstance(true);
        dialog.show(fm, "fragment_name");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        adapter = new SimpleItemRecyclerViewAdapter(this, Task.TASK_LIST, mTwoPane);
        recyclerView.setAdapter(adapter);
        adapter.startTimerThread();
    }

    public static void notifyTaskChanged () {
        SimpleItemRecyclerViewAdapter.needsUpdate = true;
    }

    public void onDestroy() {
        super.onDestroy();
        adapter.stopTimerThread();
    }

public static class SimpleItemRecyclerViewAdapter
        extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

    private final MainActivity mParentActivity;
    private final List<Task> mValues;
    private final boolean mTwoPane;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Task item = (Task) view.getTag();
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putString(TaskDetailFragment.TASK_ID, item.id);
                TaskDetailFragment fragment = new TaskDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.task_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(TaskDetailFragment.TASK_ID, item.id);

                context.startActivity(intent);
            }
        }
    };

    SimpleItemRecyclerViewAdapter(MainActivity parent, List<Task> aufgaben, boolean twoPane) {
            mValues = aufgaben;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        public void add(Task task) {
            mValues.add(task);
        }

        public void remove(Task task) {
            mValues.remove(task);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rowView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_row_content, parent, false);

            return new ViewHolder(rowView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final Task task = mValues.get(position);
            holder.name.setText(task.name);
            holder.ckBoxTaskDone.setChecked(task.isDone);
            holder.kosten.setText(Formater.intCentToString(task.costs));

            if(task.date != null) {
                String terminTxt = Formater.formatDateToSring(task.date);
                holder.termin.setText(terminTxt);
            }

            if(task.type != null) {
                holder.typ.setText(mValues.get(position).type.toString());
            }

            if (task.prio == Prio.High) {
                holder.imgPrio.setImageResource(android.R.drawable.btn_star_big_on);
            } else {
                holder.imgPrio.setImageResource(android.R.drawable.btn_star_big_off);
            }

            holder.itemView.setTag(task);
            holder.itemView.setOnClickListener(mOnClickListener);

            OnTaskChecked(task, holder.termin, holder.kosten);
            holder.ckBoxTaskDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String msg = SimpleItemRecyclerViewAdapter.this.mParentActivity.getResources()
                            .getString(holder.ckBoxTaskDone.isChecked() ? R.string.done : R.string.todo);
                    task.isDone = holder.ckBoxTaskDone.isChecked();
                    OnTaskChecked(task, holder.termin, holder.kosten);
                    TaskDetailFragment.notifyTaskChanged();
                    Snackbar.make(view, task.name + " " + msg, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

            final int actPosition = position;
            holder.imgPrio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Task act = mValues.get(actPosition);
                    if (act.prio == Prio.High) {
                        holder.imgPrio.setImageResource(android.R.drawable.btn_star_big_off);
                        act.prio = Prio.Normal;
                    } else {
                        holder.imgPrio.setImageResource(android.R.drawable.btn_star_big_on);
                        act.prio = Prio.High;
                    }

                    TaskDetailFragment.notifyTaskChanged();
                    Snackbar snack = Snackbar.make(view, SimpleItemRecyclerViewAdapter.this.mParentActivity.getResources().getString(act.prio == Prio.Normal ? R.string.normalPrioText : R.string.highPrioText) + " " + act.name, Snackbar.LENGTH_LONG);
                    snack.show();
                }
            });

            if(mTwoPane) {
                holder.imgDelete.setVisibility(View.GONE);
            } else {
                holder.imgDelete.setVisibility(View.VISIBLE);
                holder.imgDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        ((View) view.getParent().getParent()).animate().setDuration(300).alpha(0)
                                .withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        Task toRemove = mValues.get(actPosition);
                                        SimpleItemRecyclerViewAdapter.this.remove(toRemove);
                                        SimpleItemRecyclerViewAdapter.this.notifyDataSetChanged();
                                        ((View) view.getParent().getParent()).setAlpha(1);

                                        Snackbar snack = Snackbar.make(
                                                view,
                                                task.name + " " + SimpleItemRecyclerViewAdapter.this.mParentActivity
                                                        .getResources().getString(R.string.deleted),
                                                Snackbar.LENGTH_LONG);

                                        snack.setAction(
                                                R.string.undo,
                                                new Command(Command.CommandTyp.Add, SimpleItemRecyclerViewAdapter.this, toRemove)
                                        );
                                        snack.show();
                                    }
                                });
                    }
                });
            }
        }

        private void OnTaskChecked(Task task, TextView termin, TextView kosten) {
            if(task.isDone) {
                kosten.setTextColor(mParentActivity.getResources().getColor(R.color.colorGreen));
                termin.setTextColor(mParentActivity.getResources().getColor(R.color.colorGreen));
            } else {
                kosten.setTextColor(mParentActivity.getResources().getColor(R.color.colorYellow));
                if(task.date != null && task.date.getTime() < System.currentTimeMillis()) {
                    termin.setTextColor(mParentActivity.getResources().getColor(R.color.colorRed));
                } else {
                    termin.setTextColor(mParentActivity.getResources().getColor(R.color.colorWhite));
                }
            }
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            // rows content of main list
            final TextView name;
            final CheckBox ckBoxTaskDone;
            final TextView kosten;
            final TextView termin;
            final TextView typ;
            final ImageView imgPrio;
            final ImageView imgDelete;

            ViewHolder(View rowView) {
                super(rowView);
                name = rowView.findViewById(R.id.name);
                ckBoxTaskDone = rowView.findViewById(R.id.checkBox);
                termin = rowView.findViewById(R.id.termin);
                kosten = rowView.findViewById(R.id.kosten);
                typ = rowView.findViewById(R.id.typ);
                imgPrio = rowView.findViewById(R.id.icon_fav_haupt);
                imgDelete = rowView.findViewById(R.id.delete_task_icon);
            }
        }

        public static boolean needsUpdate = false;
        Thread updaterThread;
        private Thread startTimerThread() {
            stopTimerThread();
            final Handler handler = new Handler();
            Runnable updater = new Runnable() {
                public void run() {
                    while (true) {
                        try {
                            if(needsUpdate) {
                                handler.post(new Runnable(){
                                    public void run() {
                                        Log.e("needsUpdate", "");
                                        SimpleItemRecyclerViewAdapter.this.notifyDataSetChanged();
                                    }
                                });
                                needsUpdate = false;
                            }
                            Thread.sleep(300);
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            Thread thread = new Thread(updater);
            thread.start();
            return thread;
        }

        private void stopTimerThread() {
            if(updaterThread != null && updaterThread.isAlive())
                updaterThread.interrupt();
        }
    }
}
