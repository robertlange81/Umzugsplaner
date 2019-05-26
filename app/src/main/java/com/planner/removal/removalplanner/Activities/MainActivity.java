package com.planner.removal.removalplanner.Activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.planner.removal.removalplanner.Fragments.BottomSheetFragment;
import com.planner.removal.removalplanner.Fragments.TaskDetailFragment;
import com.planner.removal.removalplanner.Helpers.Comparators.ComparatorSortable;
import com.planner.removal.removalplanner.Helpers.Comparators.ComparatorConfig;
import com.planner.removal.removalplanner.Helpers.Persistance;
import com.planner.removal.removalplanner.Model.Task;
import com.planner.removal.removalplanner.Fragments.DetailDialogFragment;
import com.planner.removal.removalplanner.Helpers.TaskFormater;
import com.planner.removal.removalplanner.Helpers.TaskInitializer;
import com.planner.removal.removalplanner.Helpers.Command;
import com.planner.removal.removalplanner.Model.Priority;
import com.planner.removal.removalplanner.R;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static android.widget.LinearLayout.VERTICAL;


public class MainActivity extends AppCompatActivity implements DetailDialogFragment.DetailDialogListener {

    private boolean mTwoPane;
    private SimpleItemRecyclerViewAdapter adapter;
    private BottomSheetFragment bottomDialogFragment;
    private static ComparatorConfig comparatorConfig;
    private static RecyclerView recyclerView;
    MenuItem lastItem, showCostsMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(TaskFormater.currentLocal == null) {
            TaskFormater.setCurrentLocale(this);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        BottomAppBar bottomAppBar = (BottomAppBar) findViewById(R.id.bottom_app_bar);
        if(bottomAppBar != null) {
            Drawable drawable = getBaseContext().getResources().getDrawable(android.R.drawable.ic_menu_sort_by_size);
            bottomAppBar.setOverflowIcon(drawable);
            setSupportActionBar(bottomAppBar);
        }

        bottomDialogFragment = BottomSheetFragment.newInstance();

        FloatingActionButton addNewAction = findViewById(R.id.fab);
        addNewAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setAlpha(1f);
                if(mTwoPane) {
                    Intent i = new Intent(view.getContext(), DetailActivity.class);
                    i.putExtra(DetailActivity.ARG_TASK_ID, Task.maxId + 1);
                    startActivity(i);
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(TaskDetailFragment.TASK_ID, Task.maxId + 1);

                    context.startActivity(intent);
                }
            }
        });

        Date defaultDate = new Date(118, 11, 1, 0, 0, 0);


        int orientation = this.getResources().getConfiguration().orientation;

        if (findViewById(R.id.task_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w800dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            if(orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                return;
            }
        }

        Persistance.LoadTasks(this);
        if(Task.TASK_LIST.size() == 10) {
            TaskInitializer.CreateDefaultTasks(defaultDate);
        }

        comparatorConfig = new ComparatorConfig();

        recyclerView = (RecyclerView) findViewById(R.id.list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        findViewById(R.id.fab).setAlpha(0.75f);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_main, menu);
        getMenuInflater().inflate(R.menu.bottomappbar_menu, menu);

        // Inflate and initialize the top main menu
        ActionMenuView topBar = (ActionMenuView)findViewById(R.id.top_toolbar);
        Menu topMenu = topBar.getMenu();
        getMenuInflater().inflate(R.menu.menu_main, topMenu);
        showCostsMenuItem = topMenu.findItem(R.id.showCostsMenuItem);
        for (int i = 0; i < topMenu.size(); i++) {
            topMenu.getItem(i).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return onOptionsItemSelected(item);
                }
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item == showCostsMenuItem) {
            String msgLabel = getResources().getString(R.string.placeholder_amount);
            String msgValue = TaskFormater.intCentToString(Task.sumCosts());
            showDetailDialog(msgLabel, msgValue);
            return true;
        }

        ComparatorConfig.SortType sortType;
        SpannableString s;

        if(lastItem != null && lastItem != item) {
            s = new SpannableString(lastItem.getTitle());
            s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
            lastItem.setTitle(s);
        }

        s = new SpannableString(item.getTitle());
        s.setSpan(new ForegroundColorSpan(Color.rgb(0,255,60)), 0, s.length(), 0);
        item.setTitle(s);
        lastItem = item;

        int id = item.getItemId();
        switch (id) {
            case R.id.sortByCosts:
                sortType = ComparatorConfig.SortType.COSTS;
                break;
            case R.id.sortByDate:
                sortType = ComparatorConfig.SortType.DATE;
                break;
            case R.id.sortByIsDone:
                sortType = ComparatorConfig.SortType.IS_DONE;
                break;
            case R.id.sortByName:
                sortType = ComparatorConfig.SortType.NAME;
                break;
            case R.id.sortByPriority:
                sortType = ComparatorConfig.SortType.PRIORITY;
                break;
            case R.id.sortByType:
                sortType = ComparatorConfig.SortType.TYPE;
                break;
            default:
                sortType = ComparatorConfig.SortType.IS_DONE;
        }

        if(!sortType.equals(ComparatorConfig.SortType.NONE)) {
            Persistance.SaveSetting(Persistance.SettingType.Sort, sortType.getValue(), this);
            NotifyTaskChanged(null, this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static boolean SortBy(ComparatorConfig.SortType sortType) {

        ComparatorSortable comparatorSortable = comparatorConfig.sortableMap.get(sortType);
        if(comparatorSortable != null) {
            Collections.sort(Task.TASK_LIST, comparatorSortable);
            //.thenComparing(new PriorityComparator())
            //.thenComparing(new NameComparator()));

            if(recyclerView != null)
                recyclerView.smoothScrollToPosition(0);

            return true;
        }

        return false;
    }

    public void showDetailDialog(String msgLabel, String msgValue) {
        FragmentManager fm = getFragmentManager();
        DialogFragment dialog = new DetailDialogFragment();
        Bundle args = new Bundle();
        args.putString("message", msgLabel + ": " + msgValue);
        dialog.setArguments(args);
        dialog.setRetainInstance(true);
        dialog.show(fm, msgLabel);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        DividerItemDecoration itemDecor = new DividerItemDecoration(recyclerView.getContext(), VERTICAL);
        itemDecor.setDrawable(recyclerView.getContext().getResources().getDrawable(R.drawable.listview_border));
        recyclerView.addItemDecoration(itemDecor);
        adapter = new SimpleItemRecyclerViewAdapter(this, Task.TASK_LIST, mTwoPane);
        recyclerView.setAdapter(adapter);
        adapter.startTimerThread();
    }

    public static void NotifyTaskChanged(Task t, Activity a) {
        if(t != null && a != null) {
            Persistance.SaveOrUpdateTask(t, a);
        }
        if(a != null) {
            int sortId = Persistance.LoadSetting(Persistance.SettingType.Sort, a);
            if(sortId > 0 && ComparatorConfig.SortType.values().length > sortId) {
                SortBy(ComparatorConfig.SortType.values()[sortId]);
            }
        }

        SimpleItemRecyclerViewAdapter.needsUpdate = true;
    }

    public void onDestroy() {
        Persistance.CheckQueue();
        super.onDestroy();
        if(adapter != null)
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
            holder.ckBoxTaskDone.setChecked(task.is_Done);
            holder.kosten.setText(TaskFormater.intCentToString(task.costs));

            if(task.date != null) {
                String terminTxt = TaskFormater.formatDateToSring(task.date);
                holder.termin.setText(terminTxt);
            } else {
                holder.termin.setText("");
            }

            String[] str_task_types = mParentActivity.getBaseContext().getResources().getStringArray(R.array.base_task_types);
            if(task.type != null) {
                String str_type = (String) Array.get(str_task_types, mValues.get(position).type.getValue());
                holder.typ.setText(str_type);
            }

            if (task.priority == Priority.High) {
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
                    task.is_Done = holder.ckBoxTaskDone.isChecked();
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
                    if (act.priority == Priority.High) {
                        holder.imgPrio.setImageResource(android.R.drawable.btn_star_big_off);
                        act.priority = Priority.Normal;
                    } else {
                        holder.imgPrio.setImageResource(android.R.drawable.btn_star_big_on);
                        act.priority = Priority.High;
                    }

                    TaskDetailFragment.notifyTaskChanged();
                    Snackbar snack = Snackbar.make(view, SimpleItemRecyclerViewAdapter.this.mParentActivity.getResources().getString(act.priority == Priority.Normal ? R.string.normalPrioText : R.string.highPrioText) + " " + act.name, Snackbar.LENGTH_LONG);
                    snack.show();
                }
            });

            holder.imgDelete.setVisibility(View.VISIBLE);
            holder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    ((View) view.getParent().getParent()).animate().setDuration(300).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                            Persistance.DeleteTask(task, mParentActivity);
                            Task toRemove = mValues.get(actPosition);
                            SimpleItemRecyclerViewAdapter.this.remove(toRemove);
                            SimpleItemRecyclerViewAdapter.this.notifyDataSetChanged();
                            ((View) view.getParent().getParent()).setAlpha(1);
                                try {
                                    if(view != null) {
                                        Snackbar snack = Snackbar.make(
                                                view,
                                                task.name + " " + SimpleItemRecyclerViewAdapter.this.mParentActivity
                                                        .getResources().getString(R.string.deleted),
                                                Snackbar.LENGTH_LONG);

                                        snack.setAction(
                                                R.string.undo,
                                                new Command(Command.CommandTyp.Add, SimpleItemRecyclerViewAdapter.this, toRemove, mParentActivity)
                                        );
                                        snack.show();
                                    }
                                } catch (Exception e) {
                                    Log.e("Main Snack", e.getMessage());
                                }
                            }
                        });
                }
            });
        }

        private void OnTaskChecked(Task task, TextView termin, TextView kosten) {
            if(task.is_Done) {
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
        public static Thread updaterThread;
        private void startTimerThread() {
            stopTimerThread();
            final Handler handler = new Handler();
            final Runnable updater = new Runnable() {
                public void run() {
                    while (!updaterThread.isInterrupted()) {
                        try {
                            synchronized (this) {
                                if(needsUpdate) {
                                    handler.post(new Runnable(){
                                        public void run() {
                                            Log.e("needsUpdate", "true");
                                            SimpleItemRecyclerViewAdapter.this.notifyDataSetChanged();
                                        }
                                    });
                                    needsUpdate = false;
                                }
                                Thread.sleep(300);
                            }
                        }
                        catch (InterruptedException e) {
                            updaterThread.interrupt();
                            String m = e.getMessage();
                            e.printStackTrace();
                        }
                    }
                }
            };
            updaterThread = new Thread(updater);
            updaterThread.start();
        }

        private void stopTimerThread() {
            if(updaterThread != null && updaterThread.isAlive())
                updaterThread.interrupt();
        }
    }
}
