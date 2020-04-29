package com.planner.removal.removalplanner.Activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.planner.removal.removalplanner.BuildConfig;
import com.planner.removal.removalplanner.Fragments.DialogFragmentCosts;
import com.planner.removal.removalplanner.Fragments.DialogFragmentInit;
import com.planner.removal.removalplanner.Fragments.DialogFragmentInit.InitDialogListener;
import com.planner.removal.removalplanner.Fragments.TaskDetailFragment;
import com.planner.removal.removalplanner.Helpers.Command;
import com.planner.removal.removalplanner.Helpers.Comparators.ComparatorConfig;
import com.planner.removal.removalplanner.Helpers.Comparators.ComparatorSortable;
import com.planner.removal.removalplanner.Helpers.Persistance;
import com.planner.removal.removalplanner.Helpers.TaskFormater;
import com.planner.removal.removalplanner.Helpers.TaskInitializer;
import com.planner.removal.removalplanner.Model.AppRater;
import com.planner.removal.removalplanner.Model.Priority;
import com.planner.removal.removalplanner.Model.Task;
import com.planner.removal.removalplanner.R;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static android.widget.LinearLayout.VERTICAL;

public class MainActivity extends AppCompatActivity implements InitDialogListener, LifecycleObserver {

    public static MainActivity instance;
    public static boolean mTwoPane;
    private SimpleItemRecyclerViewAdapter adapter;
    private static ComparatorConfig comparatorConfig;
    private static RecyclerView recyclerView;
    BottomAppBar bottomAppBar;
    MenuItem initDialogMenuItem, hideDoneTasksMenuItem, hideNormalPrioMenuItem, lastMenuItem, deleteAllMenuItem;
    TextView showOverview;
    DialogFragment dialog;
    Menu topMenu;
    public static final String START_FROM_PAUSED_ACTIVITY_FLAG = "START_FROM_PAUSED_ACTIVITY_FLAG";

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onEnterForeground() {
        Log.d("AppController", "Foreground");
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onEnterBackground() {
        Log.d("AppController", "Background");
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        if(dialog != null && dialog instanceof DialogFragmentInit) {
            DialogFragmentInit initdialog = (DialogFragmentInit) dialog;
            if(initdialog.getRemovalDate() == null) {
                Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.placeholder_init_no_date), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

            if(initdialog.getDeleteOld()) {
                Persistance.PruneAllTasks(this, true, initdialog.getRemovalDate(), null);
            } else {
                TaskInitializer.InitTasks(initdialog.getRemovalDate(), null, this);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (isStartedFromBackgroundActivity())
            moveTaskToBack(true);

        instance = this;
        if(TaskFormater.currentLocal == null) {
            TaskFormater.setCurrentLocale(this);
        }

        setContentView(R.layout.list);

        bottomAppBar = (BottomAppBar) findViewById(R.id.bottom_app_bar);
        if(bottomAppBar != null) {
            Drawable drawable = getBaseContext().getResources().getDrawable(android.R.drawable.ic_menu_sort_by_size);
            bottomAppBar.setOverflowIcon(drawable);
            setSupportActionBar(bottomAppBar);
        }

        FloatingActionButton addNewAction = findViewById(R.id.fab);
        addNewAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setAlpha(1f);
                if(mTwoPane) {
                    Task _task = new Task("", "");
                    Task.addTask(_task);
                    NotifyTaskChanged(null, null);
                    adapter.setFragmentTwoPane(_task);
                    adapter.activeRowItemId = _task.id;
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, DetailActivity.class);
                    UUID id = UUID.randomUUID();
                    intent.putExtra(TaskDetailFragment.TASK_ID, id.toString());
                    adapter.activeRowItemId = id;
                    context.startActivity(intent);
                }
            }
        });

        if (getBaseContext().getResources().getInteger(R.integer.orientation) == 0) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w800dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            if(this.getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                return;
            }
        }

        Persistance.LoadTasks(this);
        comparatorConfig = new ComparatorConfig();

        recyclerView = (RecyclerView) findViewById(R.id.list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);
        // moveTaskToBack(true);
    }

    private boolean isStartedFromBackgroundActivity() {
        return getIntent().getBooleanExtra(START_FROM_PAUSED_ACTIVITY_FLAG, false);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        findViewById(R.id.fab).setAlpha(0.75f);

        SharedPreferences prefs = MainActivity.this.getSharedPreferences("removal", 0);
        if (!prefs.getBoolean("did_init", false)) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("did_init", true);
            editor.commit();
            showDetailDialog(DialogFragmentInit.class, new String[0]);
            return;
        }

        try {
            AppRater.app_launched(
                    MainActivity.this,
                    MainActivity.this.getPackageName(),
                    getResources().getString(R.string.app_name)
            );
        } catch (Exception x) {
            Log.e("cache", x.getMessage());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate and initialize the top main menu
        ActionMenuView topBar = (ActionMenuView)findViewById(R.id.top_actionmenu);

        if(topMenu != null)
            return true;

        topMenu = topBar.getMenu();
        getMenuInflater().inflate(R.menu.menu_main, topMenu);
        SpannableString s;

        int taskCount = 0, done = 0;
        for(Task task: Task.TASK_LIST) {
            if(task.is_Done) {
                done++;
            }
            taskCount++;
        }
        s = new SpannableString(done + "/" + taskCount);
        if(done < taskCount) {
            s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
        } else {
            s.setSpan(new ForegroundColorSpan(Color.rgb(0,255,60)), 0, s.length(), 0);
        }
        showOverview = findViewById(R.id.menuText);
        if(showOverview != null) {
            instance.showOverview.setText(done + "/" + taskCount);
            if(done < taskCount) {
                instance.showOverview.setTextColor(Color.WHITE);
            } else {
                instance.showOverview.setTextColor(Color.rgb(0,255,60));
            }
        }
        initDialogMenuItem = topMenu.findItem(R.id.start_new);

        if(initDialogMenuItem != null) {
            s = new SpannableString(initDialogMenuItem.getTitle());
            s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorYellow)), 0, s.length(), 0);
        }
        initDialogMenuItem.setTitle(s);

        deleteAllMenuItem = topMenu.findItem(R.id.delete_all);
        if (BuildConfig.DEBUG) {
            if(deleteAllMenuItem != null) {
                s = new SpannableString(deleteAllMenuItem.getTitle());
                s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorRed)), 0, s.length(), 0);
            }
            deleteAllMenuItem.setTitle(s);
        } else {
            deleteAllMenuItem.setVisible(false);
        }

        hideDoneTasksMenuItem = topMenu.findItem(R.id.show_open_only);
        hideDoneTasksMenuItem.setChecked(getHideDoneTasksChecked());

        if(hideDoneTasksMenuItem != null && !hideDoneTasksMenuItem.isChecked()) {
            s = new SpannableString(hideDoneTasksMenuItem.getTitle());
            s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
        } else {
            s = new SpannableString(hideDoneTasksMenuItem.getTitle());
            s.setSpan(new ForegroundColorSpan(Color.rgb(0,255,60)), 0, s.length(), 0);
        }
        hideDoneTasksMenuItem.setTitle(s);

        hideNormalPrioMenuItem = topMenu.findItem(R.id.show_high_prio_only);

        if(getHideNormalPrioTasksChecked()) {
            hideNormalPrioMenuItem.setChecked(true);
            if(adapter != null) {
                adapter.setHideNormalPrio(true);
            }
        }

        if(!hideNormalPrioMenuItem.isChecked()) {
            s = new SpannableString(hideNormalPrioMenuItem.getTitle());
            s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
        } else {
            s = new SpannableString(hideNormalPrioMenuItem.getTitle());
            s.setSpan(new ForegroundColorSpan(Color.rgb(0,255,60)), 0, s.length(), 0);
        }
        hideNormalPrioMenuItem.setTitle(s);

        int sortId = Persistance.LoadSetting(Persistance.SettingType.Sort, this);
        if(sortId > 0 && ComparatorConfig.SortType.values().length > sortId) {
            SortBy(ComparatorConfig.SortType.values()[sortId]);

            MenuItem item = null;
            if (sortId == ComparatorConfig.SortType.COSTS.getValue()) {
                item = topMenu.findItem(R.id.sortByCosts);
            } else if (sortId == ComparatorConfig.SortType.DATE.getValue()) {
                item = topMenu.findItem(R.id.sortByDate);
            } else if (sortId == ComparatorConfig.SortType.IS_DONE.getValue()) {
                item = topMenu.findItem(R.id.sortByIsDone);
            } else if (sortId == ComparatorConfig.SortType.NAME.getValue()) {
                item = topMenu.findItem(R.id.sortByName);
            } else if (sortId == ComparatorConfig.SortType.PRIORITY.getValue()) {
                item = topMenu.findItem(R.id.sortByPriority);
            }

            if (item != null) {
                s = new SpannableString(item.getTitle());
                s.setSpan(new ForegroundColorSpan(Color.rgb(0, 255, 60)), 0, s.length(), 0);
                item.setTitle(s);
                lastMenuItem = item;
            }
        }

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

        SpannableString s;

        if(item == showOverview) {
            // TODO Overlay to show progress / date
            return true;
        }

        if(item == initDialogMenuItem) {
            String[] init = new String[0];
            showDetailDialog(DialogFragmentInit.class, init);

            return true;
        }

        if(item == hideDoneTasksMenuItem) {
            item.setChecked(!item.isChecked());
            setHideDoneTasksChecked(item.isChecked());
            adapter.setHideDone(item.isChecked());
            adapter.notifyDataSetChanged();

            if(!item.isChecked()) {
                s = new SpannableString(item.getTitle());
                s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
            } else {
                s = new SpannableString(item.getTitle());
                s.setSpan(new ForegroundColorSpan(Color.rgb(0,255,60)), 0, s.length(), 0);
            }
            item.setTitle(s);

            int sortId = Persistance.LoadSetting(Persistance.SettingType.Sort, this);
            if(sortId > 0 && ComparatorConfig.SortType.values().length > sortId) {
                SortBy(ComparatorConfig.SortType.values()[sortId]);
            }

            return true;
        }

        if(item == hideNormalPrioMenuItem) {
            item.setChecked(!item.isChecked());
            setHideNormalPrioTasksChecked(item.isChecked());
            adapter.setHideNormalPrio(item.isChecked());
            adapter.notifyDataSetChanged();

            int sortId = Persistance.LoadSetting(Persistance.SettingType.Sort, this);
            if(sortId > 0 && ComparatorConfig.SortType.values().length > sortId) {
                SortBy(ComparatorConfig.SortType.values()[sortId]);
            }

            if(!item.isChecked()) {
                s = new SpannableString(item.getTitle());
                s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
            } else {
                s = new SpannableString(item.getTitle());
                s.setSpan(new ForegroundColorSpan(Color.rgb(0,255,60)), 0, s.length(), 0);
            }
            item.setTitle(s);

            return true;
        }

        if(item == deleteAllMenuItem) {
            Persistance.PruneAllTasks(MainActivity.this, false,null, null);
        }

        // assume sorting select
        ComparatorConfig.SortType sortType = null;

        if(lastMenuItem != null && lastMenuItem != item) {
            s = new SpannableString(lastMenuItem.getTitle());
            s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
            lastMenuItem.setTitle(s);
        }

        s = new SpannableString(item.getTitle());
        s.setSpan(new ForegroundColorSpan(Color.rgb(0,255,60)), 0, s.length(), 0);
        item.setTitle(s);
        lastMenuItem = item;
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
            case R.id.showCostsMenuItem:
                showCosts();
                break;
            default:
                sortType = ComparatorConfig.SortType.IS_DONE;
        }

        if(sortType != null && !sortType.equals(ComparatorConfig.SortType.NONE)) {
            Persistance.SaveSetting(Persistance.SettingType.Sort, sortType.getValue(), this);
            SortBy(sortType);
            adapter.notifyDataSetChanged();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean showCosts() {
        long sumAll     = Task.sumCosts(false);
        long sumDone    = Task.sumCosts(true);
        long sumUndone  = sumAll - sumDone;

        String sumAllString     = TaskFormater.intDecimalsToString(sumAll);
        String sumDoneString    = TaskFormater.intDecimalsToString(sumDone);
        String sumUndoneString  = TaskFormater.intDecimalsToString(sumUndone);

        String[] costs = new String[] {sumUndoneString, sumDoneString, sumAllString};
        showDetailDialog(DialogFragmentCosts.class, costs);

        return true;
    }

    public static boolean SortBy(ComparatorConfig.SortType sortType) {

        if(comparatorConfig.sortableMap != null) {
            ComparatorSortable comparatorSortable = comparatorConfig.sortableMap.get(sortType);
            if(comparatorSortable != null) {
                Collections.sort(Task.TASK_LIST, comparatorSortable);
                // JAVA 8
                //.thenComparing(new PriorityComparator())
                //.thenComparing(new NameComparator()));
                return true;
            }
        }

        return false;
    }

    public void showDetailDialog(Class<?> clazz, String[] msg) {
        FragmentManager fm = getFragmentManager();
        dialog = null;

        if (clazz == DialogFragmentCosts.class) {
            dialog = new DialogFragmentCosts();
        } else if (clazz == DialogFragmentInit.class) {
            dialog = new DialogFragmentInit();

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M)
                ((DialogFragmentInit) dialog).setmListener(this);
        }

        if (dialog != null) {
            Bundle args = new Bundle();
            args.putStringArray("message", msg);
            dialog.setArguments(args);
            // dialog.setRetainInstance(true);
            dialog.show(fm, "Kosten");
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        DividerItemDecoration itemDecor = new DividerItemDecoration(recyclerView.getContext(), VERTICAL);
        itemDecor.setDrawable(recyclerView.getContext().getResources().getDrawable(R.drawable.listview_border));
        recyclerView.addItemDecoration(itemDecor);

        adapter = new SimpleItemRecyclerViewAdapter(this, Task.TASK_LIST, mTwoPane);
        adapter.setHideDone(getHideDoneTasksChecked());
        recyclerView.setAdapter(adapter);
        adapter.startTimerThread();
    }

    public static void NotifyTaskChanged(Task t, Activity a) {
        NotifyTaskChanged(t, a, true);
    }

    public static void NotifyTaskChanged(Task t, Activity a, boolean doUpdate) {
        if(t != null && a != null) {
            Persistance.SaveOrUpdateTask(t, a);
        }

        int taskCount = 0, done = 0;

        if(a != null) {
            int sortId = Persistance.LoadSetting(Persistance.SettingType.Sort,
                    (a instanceof DetailActivity ? MainActivity.instance : a)
            );

            if(sortId > 0 && ComparatorConfig.SortType.values().length > sortId) {
                SortBy(ComparatorConfig.SortType.values()[sortId]);
            }

            for(Task task: Task.TASK_LIST) {
                // TODO use t.id
                if(task.id.equals(SimpleItemRecyclerViewAdapter.activeRowItemId)) {
                    if(recyclerView != null)
                        recyclerView.smoothScrollToPosition(taskCount);
                }
                if(task.is_Done) {
                    done++;
                }

                taskCount++;
            }
        } else {
            for(Task task: Task.TASK_LIST) {
                if(task.is_Done) {
                    done++;
                }
                taskCount++;
            }
        }

        if(instance != null && instance.showOverview != null) {
            instance.showOverview.setText(done + "/" + taskCount);
            if(done < taskCount) {
                instance.showOverview.setTextColor(Color.WHITE);
            } else {
                instance.showOverview.setTextColor(Color.rgb(0,255,60));
            }
        }

        SimpleItemRecyclerViewAdapter.needsUpdate = doUpdate;
    }

    public void onDestroy() {
        Persistance.CheckQueue();
        Persistance.SaveTasks(this);
        super.onDestroy();
        if(adapter != null)
            adapter.stopTimerThread();
    }

    public void onStop() {
        if(!isAppOnForeground(this)) {
            Persistance.ReplaceAllTasks(this);
        }
        super.onStop();
    }

    public boolean getHideDoneTasksChecked() {
        int hideDone = Persistance.LoadSetting(Persistance.SettingType.HideDone, this);
        return hideDone > 0;
    }

    public void setHideDoneTasksChecked(boolean hideNormalPrioChecked) {
        Persistance.SaveSetting(Persistance.SettingType.HideDone, hideNormalPrioChecked ? 1 : 0, this);
    }

    public boolean getHideNormalPrioTasksChecked() {
        int hideDone = Persistance.LoadSetting(Persistance.SettingType.HideNormalPrio, this);
        return hideDone > 0;
    }

    public void setHideNormalPrioTasksChecked(boolean hideNormalPrioChecked) {
        Persistance.SaveSetting(Persistance.SettingType.HideNormalPrio, hideNormalPrioChecked ? 1 : 0, this);
    }

    public static class SimpleItemRecyclerViewAdapter
        extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

    private final MainActivity mParentActivity;
    private List<Task> mValues;
    private final boolean mTwoPane;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            view.setBackgroundColor(Color.parseColor("#000000"));
            int rowCount = Task.TASK_LIST.size();
            for(int i = 0; i < rowCount; i++) {
                View v = recyclerView.getLayoutManager().findViewByPosition(i);
                if(v != null && v != view)
                    v.setBackgroundColor(Color.TRANSPARENT);
            }

            Task item = (Task) view.getTag();
            activeRowItemId = item.id;
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putString(TaskDetailFragment.TASK_ID, item.id.toString());
                TaskDetailFragment fragment = new TaskDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.task_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, DetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra(TaskDetailFragment.TASK_ID, item.id.toString());

                context.startActivity(intent);
            }
        }
    };
        private boolean hideDoneTasks, hideNormalPrio;
        private int activeRowPos;

        public void setFragmentTwoPane (Task t) {
        Bundle arguments = new Bundle();

        arguments.putString(TaskDetailFragment.TASK_ID, t.id.toString());
        TaskDetailFragment fragment = new TaskDetailFragment();
        fragment.setArguments(arguments);
        mParentActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.task_detail_container, fragment)
                .commit();
    }

    public void removeFragments() {
        for (Fragment fragment :  mParentActivity.getSupportFragmentManager().getFragments()) {
            mParentActivity.getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    SimpleItemRecyclerViewAdapter(MainActivity parent, List<Task> taskList, boolean twoPane) {
            mValues = taskList;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        public void updateData(List<Task> list){
            mValues = list;
        }

        public void add(Task task) {
            mValues.add(task);
        }

        static UUID activeRowItemId = null;

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
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            final Task task = mValues.get(position);

            if(task.is_Done && this.hideDoneTasks || task.priority == Priority.Normal && this.hideNormalPrio) {
                holder.itemView.setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                holder.row.setBackgroundColor(Color.TRANSPARENT);
                return;
            } else {
                holder.itemView.setVisibility(View.VISIBLE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }

            holder.name.setText(task.name);
            holder.ckBoxTaskDone.setChecked(task.is_Done);

            holder.costs.setText(TaskFormater.intDecimalsToString(task.costs));

            if(task.id.equals(activeRowItemId)){
                holder.row.setBackgroundColor(Color.parseColor("#000000"));
                activeRowPos = position;

                if(recyclerView != null)
                    recyclerView.smoothScrollToPosition(activeRowPos);
            }
            else
            {
                holder.row.setBackgroundColor(Color.TRANSPARENT);
            }

            if(task.date != null) {
                String terminTxt = TaskFormater.formatDateToSring(task.date);
                holder.date.setText(terminTxt);
            } else {
                holder.date.setText("");
            }

            String[] str_task_types = mParentActivity.getBaseContext().getResources().getStringArray(R.array.base_task_types);
            if(task.type != null && task.type.getValue() < str_task_types.length) {
                holder.type.setText((String) Array.get(str_task_types, task.type.getValue()));
            }

            if (task.priority == Priority.High) {
                holder.imgPrio.setImageResource(android.R.drawable.btn_star_big_on);
            } else {
                holder.imgPrio.setImageResource(android.R.drawable.btn_star_big_off);
            }

            holder.itemView.setTag(task);
            holder.itemView.setOnClickListener(mOnClickListener);

            OnTaskChecked(task, holder.date, holder.costs);
            holder.ckBoxTaskDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String msg = SimpleItemRecyclerViewAdapter.this.mParentActivity.getResources()
                            .getString(holder.ckBoxTaskDone.isChecked() ? R.string.done : R.string.todo);
                    task.is_Done = holder.ckBoxTaskDone.isChecked();
                    OnTaskChecked(task, holder.date, holder.costs);
                    TaskDetailFragment.notifyTaskChanged();
                    Snackbar.make(view, task.name + " " + msg, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    NotifyTaskChanged(task, mParentActivity, false);

                    if(task.is_Done && hideDoneTasks)
                        notifyDataSetChanged();
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

                    if(act.priority == Priority.Normal && hideNormalPrio)
                        notifyDataSetChanged();

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
                            if(mTwoPane) {
                                removeFragments();
                            }
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

                                int done = 0, taskCount = 0;
                                for(Task task: Task.TASK_LIST) {
                                    if(task.is_Done) {
                                        done++;
                                    }
                                    taskCount++;
                                }

                                if(instance.showOverview != null) {
                                    instance.showOverview.setText(done + "/" + taskCount);
                                    if(done < taskCount) {
                                        instance.showOverview.setTextColor(Color.WHITE);
                                    } else {
                                        instance.showOverview.setTextColor(Color.rgb(0,255,60));
                                    }
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

        public void setHideDone(boolean hideDone) {
            hideDoneTasks = hideDone;
        }

        public void setHideNormalPrio(boolean hideNormalPrio) {
            this.hideNormalPrio = hideNormalPrio;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            // rows content of main list
            final RelativeLayout row;
            final TextView name;
            final CheckBox ckBoxTaskDone;
            final TextView costs;
            final TextView date;
            final TextView type;
            final ImageView imgPrio;
            final ImageView imgDelete;

            ViewHolder(View rowView) {
                super(rowView);
                row = rowView.findViewById(R.id.list_row_content);
                name = rowView.findViewById(R.id.name);
                ckBoxTaskDone = rowView.findViewById(R.id.checkBox);
                date = rowView.findViewById(R.id.date_detail);
                costs = rowView.findViewById(R.id.costs_detail);
                type = rowView.findViewById(R.id.type);
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
                                            Log.d("needsUpdate", "true");
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

    public static boolean isAppOnForeground(Context context) {
        boolean ret = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if(appProcesses != null){
            String packageName = context.getPackageName();
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                        && appProcess.processName.equals(packageName)) {
                    ret = true;
                }
            }
        }
        return ret;
    }
}
