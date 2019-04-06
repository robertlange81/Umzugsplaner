package com.planner.removal.removalplanner.Activities;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
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
import com.planner.removal.removalplanner.Helpers.Comparators.ComparatorConfig;
import com.planner.removal.removalplanner.Helpers.Comparators.CostsComparator;
import com.planner.removal.removalplanner.Helpers.Comparators.DateComparator;
import com.planner.removal.removalplanner.Helpers.Comparators.IsDoneComparator;
import com.planner.removal.removalplanner.Helpers.Comparators.NameComparator;
import com.planner.removal.removalplanner.Helpers.Comparators.PriorityComparator;
import com.planner.removal.removalplanner.Helpers.Comparators.TypeComparator;
import com.planner.removal.removalplanner.Helpers.Persistance;
import com.planner.removal.removalplanner.Model.Task;
import com.planner.removal.removalplanner.Fragments.DetailDialogFragment;
import com.planner.removal.removalplanner.Helpers.Formater;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(Formater.currentLocal == null) {
            Formater.setCurrentLocale(this);
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

        if(true) {
            // TODO load saved list
        } else {
            TaskInitializer.CreateDefaultTasks(defaultDate);
        }

        int sortId = Persistance.GetSetting(Persistance.SaveType.Sort, getParent());
        if(sortId > 0)
            SortBy(sortId);

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

        // showDetailDialog();
        // bottomDialogFragment.show(getSupportFragmentManager(), "bla");

        int id = item.getItemId();
        ComparatorConfig.SortType sortType = SortBy(id);
        if(!sortType.equals(ComparatorConfig.SortType.NONE)) {
            NotifyTaskChanged();
            Persistance.SaveSetting(Persistance.SaveType.Sort, sortType.getValue(), getParent());
        }

        return super.onOptionsItemSelected(item);
    }

    public static ComparatorConfig.SortType SortBy(int id) {

        if (id == R.id.sortByCosts || id == ComparatorConfig.SortType.COSTS.getValue()) {

            Collections.sort(Task.TASK_LIST, new CostsComparator());
            //.thenComparing(new PriorityComparator())
            //.thenComparing(new NameComparator()));
            Collections.reverse(Task.TASK_LIST);
            return ComparatorConfig.SortType.COSTS;
        }
        if (id == R.id.sortByDate || id == ComparatorConfig.SortType.DATE.getValue()) {
            Collections.sort(Task.TASK_LIST, new DateComparator());
            return ComparatorConfig.SortType.DATE;
        }
        if (id == R.id.sortByIsDone || id == ComparatorConfig.SortType.IS_DONE.getValue()) {
            Collections.sort(Task.TASK_LIST, new IsDoneComparator());
            Collections.reverse(Task.TASK_LIST);
            return ComparatorConfig.SortType.IS_DONE;
        }
        if (id == R.id.sortByName|| id == ComparatorConfig.SortType.NAME.getValue()) {
            Collections.sort(Task.TASK_LIST, new NameComparator());
            return ComparatorConfig.SortType.NAME;
        }
        if (id == R.id.sortByPriority || id == ComparatorConfig.SortType.PRIORITY.getValue()) {
            Collections.sort(Task.TASK_LIST, new PriorityComparator());
            Collections.reverse(Task.TASK_LIST);
            return ComparatorConfig.SortType.PRIORITY;
        }
        if (id == R.id.sortByType || id == ComparatorConfig.SortType.TYPE.getValue()) {
            Collections.sort(Task.TASK_LIST, new TypeComparator());
            return ComparatorConfig.SortType.TYPE;
        }

        return ComparatorConfig.SortType.NONE;
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
        DividerItemDecoration itemDecor = new DividerItemDecoration(recyclerView.getContext(), VERTICAL);
        itemDecor.setDrawable(recyclerView.getContext().getResources().getDrawable(R.drawable.listview_border));
        recyclerView.addItemDecoration(itemDecor);
        adapter = new SimpleItemRecyclerViewAdapter(this, Task.TASK_LIST, mTwoPane);
        recyclerView.setAdapter(adapter);
        adapter.startTimerThread();
    }

    public static void NotifyTaskChanged() {
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
            holder.name.setText(task.Name);
            holder.ckBoxTaskDone.setChecked(task.Is_Done);
            holder.kosten.setText(Formater.intCentToString(task.Costs));

            if(task.Date != null) {
                String terminTxt = Formater.formatDateToSring(task.Date);
                holder.termin.setText(terminTxt);
            } else {
                holder.termin.setText("");
            }

            String[] str_task_types = mParentActivity.getBaseContext().getResources().getStringArray(R.array.base_task_types);
            if(task.Type != null) {
                String str_type = (String) Array.get(str_task_types, mValues.get(position).Type.getValue());
                holder.typ.setText(str_type);
            }

            if (task.Priority == Priority.High) {
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
                    task.Is_Done = holder.ckBoxTaskDone.isChecked();
                    OnTaskChecked(task, holder.termin, holder.kosten);
                    TaskDetailFragment.notifyTaskChanged();
                    Snackbar.make(view, task.Name + " " + msg, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

            final int actPosition = position;
            holder.imgPrio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Task act = mValues.get(actPosition);
                    if (act.Priority == Priority.High) {
                        holder.imgPrio.setImageResource(android.R.drawable.btn_star_big_off);
                        act.Priority = Priority.Normal;
                    } else {
                        holder.imgPrio.setImageResource(android.R.drawable.btn_star_big_on);
                        act.Priority = Priority.High;
                    }

                    TaskDetailFragment.notifyTaskChanged();
                    Snackbar snack = Snackbar.make(view, SimpleItemRecyclerViewAdapter.this.mParentActivity.getResources().getString(act.Priority == Priority.Normal ? R.string.normalPrioText : R.string.highPrioText) + " " + act.Name, Snackbar.LENGTH_LONG);
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
                                                task.Name + " " + SimpleItemRecyclerViewAdapter.this.mParentActivity
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
            if(task.Is_Done) {
                kosten.setTextColor(mParentActivity.getResources().getColor(R.color.colorGreen));
                termin.setTextColor(mParentActivity.getResources().getColor(R.color.colorGreen));
            } else {
                kosten.setTextColor(mParentActivity.getResources().getColor(R.color.colorYellow));
                if(task.Date != null && task.Date.getTime() < System.currentTimeMillis()) {
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
            Runnable updater = new Runnable() {
                public void run() {
                    while (true) {
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
