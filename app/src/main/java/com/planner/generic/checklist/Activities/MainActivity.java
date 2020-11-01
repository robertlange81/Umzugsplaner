package com.planner.generic.checklist.Activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.planner.generic.checklist.Fragments.DialogFragmentCosts;
import com.planner.generic.checklist.Fragments.DialogFragmentInit;
import com.planner.generic.checklist.Fragments.DialogFragmentInit.InitDialogListener;
import com.planner.generic.checklist.Fragments.TaskDetailFragment;
import com.planner.generic.checklist.Helpers.Command;
import com.planner.generic.checklist.Helpers.Comparators.ComparatorConfig;
import com.planner.generic.checklist.Helpers.ExportService;
import com.planner.generic.checklist.Helpers.ExportPdfService;
import com.planner.generic.checklist.Helpers.ImportService;
import com.planner.generic.checklist.Helpers.LoadingTask;
import com.planner.generic.checklist.Helpers.OpenFileListener;
import com.planner.generic.checklist.Helpers.Persistance;
import com.planner.generic.checklist.Helpers.TaskFormater;
import com.planner.generic.checklist.Helpers.TaskInitializer;
import com.planner.generic.checklist.Helpers.TasksObserver;
import com.planner.generic.checklist.Model.Location;
import com.planner.generic.checklist.Model.Priority;
import com.planner.generic.checklist.Model.Task;
import com.planner.generic.checklist.Model.TaskContract;
import com.planner.generic.checklist.R;

import java.lang.reflect.Array;
import java.util.List;
import java.util.UUID;

import static android.widget.LinearLayout.VERTICAL;
import static com.planner.generic.checklist.Model.TaskContract.TaskData.list;
import static com.planner.generic.checklist.Model.TaskContract.TaskData.item;
import static com.planner.generic.checklist.Model.TaskContract.TaskData.list_self;

public class MainActivity extends AppCompatActivity implements InitDialogListener, LifecycleObserver, Refreshable {

    public static MainActivity instance;
    public static boolean mTwoPane;
    private static SimpleItemRecyclerViewAdapter adapter;
    private static RecyclerView recyclerView;
    BottomAppBar bottomAppBar;
    MenuItem initDialogMenuItem, hideDoneTasksMenuItem, hideNormalPrioMenuItem, lastMenuItem, deleteAllMenuItem, searchByTitleFilterMenuItem;
    TextView showOverview;
    DialogFragment dialog;
    Menu topMenu;
    private static final String _START_FROM_PAUSED_ACTIVITY_FLAG = "START_FROM_PAUSED_ACTIVITY_FLAG";
    private final static int _SAF_EXPORT_BIN = 200;
    private final static int _SAF_IMPORT_BIN = 250;
    private final static int _SAF_EXPORT_PDF = 300;

    ImportExportReceiver receiver;

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
                Persistance.PruneAllTasks(this, true, initdialog.getRemovalDate(), null, null);
            } else {
                TaskInitializer.InitTasks(initdialog.getRemovalDate(), null, this, null);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (isStartedFromBackgroundActivity())
            moveTaskToBack(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        new LoadingTask((FrameLayout) findViewById(R.id.progress_overlay)).execute();
        instance = this;

        if(TaskFormater.currentLocal == null) {
            TaskFormater.setCurrentLocale(this);
        }

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
                    NotifyTaskChanged(null, null, new Long[] {item});
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
            mTwoPane = true;
            /*
            if(this.getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                return;
            }
            */
        }

        /*
        int sortId = Persistance.LoadSetting(Persistance.SettingType.Sort, this);
        if(sortId > 0 && ComparatorConfig.SortType.values().length > sortId) {
            Task.lock.lock();
            Task.SortBy(ComparatorConfig.SortType.values()[sortId]);
            Task.lock.unlock();
        }*/

        recyclerView = (RecyclerView) findViewById(R.id.list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);
    }

    private boolean isStartedFromBackgroundActivity() {
        return getIntent().getBooleanExtra(_START_FROM_PAUSED_ACTIVITY_FLAG, false);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        Intent intent = new Intent(this, IntroActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        this.startActivity(intent);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        findViewById(R.id.fab).setAlpha(0.75f);

        /*
        if(newList) {
            newList = false;
            int sortId = Persistance.LoadSetting(Persistance.SettingType.Sort, this);
            if(sortId > 0 && ComparatorConfig.SortType.values().length > sortId) {
                Task.lock.lock();
                Task.SortBy(ComparatorConfig.SortType.values()[sortId]);
                Task.lock.unlock();
            }
        }
        */

        /*
        try {
            AppRater.app_launched(
                    MainActivity.this,
                    MainActivity.this.getPackageName(),
                    getResources().getString(R.string.app_name)
            );
        } catch (Exception x) {
            Log.e("cache", x.getMessage());
        }
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        // Inflate and initialize the top main menu
        ActionMenuView topBar = (ActionMenuView)findViewById(R.id.top_actionmenu);

        if(topMenu != null)
            return true;

        topMenu = topBar.getMenu();
        getMenuInflater().inflate(R.menu.menu_main, topMenu);
        SpannableString s;

        SharedPreferences prefs = MainActivity.instance.getSharedPreferences("checklist", 0);

        MenuItem goTo = topMenu.findItem(R.id.goToLocation);
        if(prefs != null &&
          (prefs.getString(Location.PLACE, null) != null
            || prefs.getString(Location.PLACE, null) != null
            || prefs.getString(Location.STREET, null) != null
          )) {
            if(goTo != null) {
                goTo.setVisible(true);
            }
        } else {
            goTo.setVisible(false);
        }

        int taskCount = 0, done = 0;
        for(Task task: Task.getTaskListClone()) {
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
            showOverview.setMaxWidth(width / 5);
            showOverview.setPadding(8,0,0,0);
            instance.showOverview.setText(done + "|" + taskCount);
            if (Build.VERSION.SDK_INT < 23) {
                instance.showOverview.setTextAppearance(getApplicationContext(), R.style.ActionButton);
            } else {
                instance.showOverview.setTextAppearance(R.style.ActionButton);
            }
            if(done < taskCount) {
                instance.showOverview.setTextColor(Color.WHITE);
            } else {
                instance.showOverview.setTextColor(Color.rgb(0,255,60));
            }
        }

        searchByTitleFilterMenuItem = topMenu.findItem(R.id.searchMenuItem);

        initDialogMenuItem = topMenu.findItem(R.id.start_new);
        if(initDialogMenuItem != null) {
            s = new SpannableString(initDialogMenuItem.getTitle());
            s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorYellow)), 0, s.length(), 0);
            initDialogMenuItem.setTitle(s);
        }

        deleteAllMenuItem = topMenu.findItem(R.id.delete_all);
        if(deleteAllMenuItem != null) {
            s = new SpannableString(deleteAllMenuItem.getTitle());
            s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorRed)), 0, s.length(), 0);
            deleteAllMenuItem.setTitle(s);
        }

        hideDoneTasksMenuItem = topMenu.findItem(R.id.show_open_only);
        hideDoneTasksMenuItem.setChecked(getHideDoneTasksChecked());

        if(hideDoneTasksMenuItem != null && !hideDoneTasksMenuItem.isChecked()) {
            s = new SpannableString(hideDoneTasksMenuItem.getTitle());
            s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
        } else {
            s = new SpannableString(hideDoneTasksMenuItem.getTitle());
            s.setSpan(new ForegroundColorSpan(Color.rgb(0,255,60)), 0, s.length(), 0);
            hideDoneTasksMenuItem.setTitle(s);
        }

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
            hideNormalPrioMenuItem.setTitle(s);
        }

        int sortId = Persistance.LoadSetting(Persistance.SettingType.Sort, this);
        if(sortId > 0 && ComparatorConfig.SortType.values().length > sortId) {

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
            } else if (sortId == ComparatorConfig.SortType.TYPE.getValue()) {
                item = topMenu.findItem(R.id.sortByType);
            }

            int itemColor = Color.rgb(255, 255, 255);
            if(Task.getTaskList().size() > 0 && Task.SortBy(ComparatorConfig.SortType.values()[sortId])) {
                itemColor = Color.rgb(0, 255, 60);
            }

            if (item != null) {
                s = new SpannableString(item.getTitle());
                s.setSpan(new ForegroundColorSpan(itemColor), 0, s.length(), 0);
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

        if (item == null)
            return false;

        if(item == showOverview) {
            // TODO Overlay to show progress / date
            return true;
        }

        if(item == deleteAllMenuItem) {
            MainActivity.showDeleteAllTasksDialog(this, instance);
            return true;
        }

        if(item == searchByTitleFilterMenuItem) {
            item.setChecked(!item.isChecked());
            if(item.isChecked()) {
                MainActivity.showFilterTitleDialog(this, instance, item);
                return true;
            }

            Drawable drawable = item.getIcon();
            if(drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
            }

            // unset filter
            instance.adapter.setTitleFilter(null);
            instance.adapter.notifyDataSetChanged();
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
                Task.SortBy(ComparatorConfig.SortType.values()[sortId]);
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
                Task.SortBy(ComparatorConfig.SortType.values()[sortId]);
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
                return true;
            case R.id.goToLocation:
                goToLocation();
                return true;
            case R.id.exportToPdf:
                selectFileForExport("application/pdf", "pdf");
                return true;
            case R.id.save:
                selectFileForExport("file/*", "list");
                return true;
            case R.id.load:
                selectFileForImport("file/*");
                return true;
            default:
                sortType = ComparatorConfig.SortType.IS_DONE;
        }

        if(!sortType.equals(ComparatorConfig.SortType.NONE)) {
            Persistance.SaveSetting(Persistance.SettingType.Sort, sortType.getValue(), this);
            Task.SortBy(sortType);
            adapter.notifyDataSetChanged();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void selectFileForExport(String type, String fileEnding) {
        Intent fileIntent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        fileIntent.addCategory(Intent.CATEGORY_OPENABLE);
        fileIntent.setType(type);
        fileIntent.putExtra(Intent.EXTRA_TITLE, "lockdown." + fileEnding);
        startActivityForResult(fileIntent, type.equals("application/pdf") ? _SAF_EXPORT_PDF : _SAF_EXPORT_BIN);
    }

    private void selectFileForImport(String type) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(type);
        startActivityForResult(intent, _SAF_IMPORT_BIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intend) {

        if (resultCode == RESULT_OK && intend != null) {
            if (requestCode == _SAF_EXPORT_PDF || requestCode == _SAF_EXPORT_BIN) {
                exportData(intend.getData(), requestCode == _SAF_EXPORT_PDF);
            }
            if (requestCode == _SAF_IMPORT_BIN) {
                importData(intend.getData());
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, intend);
    }

    private void exportData(Uri fileUri, boolean isPDFExport) {
        Log.d("DEBUG", "exportData");
        receiver = new ImportExportReceiver(this, new Handler());
        Intent exportService = new Intent(
          this,
          isPDFExport ? ExportPdfService.class : ExportService.class
        );
        exportService.setData(fileUri);
        exportService.putExtra("receiver", receiver);
        startService(exportService);
    }

    private void importData(Uri fileUri) {
        Log.d("DEBUG", "importData");
        receiver = new ImportExportReceiver(this, new Handler());
        Intent importService = new Intent(
          this, ImportService.class
        );
        importService.setData(fileUri);
        importService.putExtra("receiver", receiver);
        startService(importService);
    }

    private void goToLocation() {
        SharedPreferences prefs = MainActivity.instance.getSharedPreferences("checklist", 0);

        String url = "http://maps.google.com/maps?q=";
        if(prefs != null) {
            url += prefs.getString(Location.STREET, "") +
                    "+" + prefs.getString(Location.STREETNUMBER, "") +
                    ",+" + prefs.getString(Location.POSTAL, "") +
                    "+" + prefs.getString(Location.PLACE, "");
        }

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
            Uri.parse(url)
        );

        startActivity(intent);
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

        adapter = new SimpleItemRecyclerViewAdapter(this, Task.getTaskList(), mTwoPane);
        adapter.setHideDone(getHideDoneTasksChecked());
        recyclerView.setAdapter(adapter);

        // Register for Item Changes
        instance.getContentResolver().
                registerContentObserver(
                        ContentUris.withAppendedId(TaskContract.TaskData.CONTENT_URI, item),
                        true,
                        new TasksObserver(new Handler(), instance));

        // Register for List Changes
        instance.getContentResolver().
          registerContentObserver(
            ContentUris.withAppendedId(TaskContract.TaskData.CONTENT_URI, list_self),
            true,
            new TasksObserver(new Handler(), instance));
    }


    public void Refresh() {
        adapter.notifyDataSetChanged();
    }

    /**
     *
     * @param task task to update
     * @param activity Activity
     * @param refreshIds what should be updated
     */
    public static void NotifyTaskChanged(Task task, Activity activity, Long[] refreshIds) {
        if(task != null && activity != null) {
            Persistance.SaveOrUpdateTask(task, activity);
        }

        int taskCount = 0, done = 0;

        if(activity != null && MainActivity.instance != null) {
            int sortId = Persistance.LoadSetting(Persistance.SettingType.Sort,
                    (activity instanceof DetailActivity ? MainActivity.instance : activity)
            );

            if(sortId > 0 && ComparatorConfig.SortType.values().length > sortId) {
                Task.SortBy(ComparatorConfig.SortType.values()[sortId]);
            }

            for(Task taskItem: Task.getTaskListClone()) {
                // TODO use t.id
                if(taskItem.id.equals(SimpleItemRecyclerViewAdapter.activeRowItemId)) {
                    if(recyclerView != null)
                        recyclerView.smoothScrollToPosition(taskCount);
                }
                if(taskItem.is_Done) {
                    done++;
                }

                taskCount++;
            }
        } else {
            for(Task t: Task.getTaskListClone()) {
                if(t.is_Done) {
                    done++;
                }
                taskCount++;
            }
        }

        if(instance != null && instance.showOverview != null) {
            instance.showOverview.setText(done + "|" + taskCount);
            if(done < taskCount) {
                instance.showOverview.setTextColor(Color.WHITE);
            } else {
                instance.showOverview.setTextColor(Color.rgb(0,255,60));
            }
        }

        if (instance == null)
            return;

        for (long refreshId:refreshIds) {
            instance.getContentResolver().notifyChange(
              ContentUris.withAppendedId(
                TaskContract.TaskData.CONTENT_URI,
                refreshId),
              null
            );
        }
    }

    public void onDestroy() {
        Persistance.CheckQueue();
        Persistance.SaveTasks(this);
        super.onDestroy();
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
            int rowCount = Task.getTaskList().size();
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
        private String titleFilter;
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
        for (Fragment fragment : mParentActivity.getSupportFragmentManager().getFragments()) {
            mParentActivity.getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    SimpleItemRecyclerViewAdapter(MainActivity parent, List<Task> taskList, boolean twoPane) {
            mValues = taskList;
            mParentActivity = parent;
            mTwoPane = twoPane;
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
        public void onBindViewHolder(final ViewHolder holder, int position) {

            final Task task = mValues.get(holder.getAdapterPosition());

            if(task.is_Done && this.hideDoneTasks
                    || task.priority == Priority.Normal && this.hideNormalPrio
                    || this.titleFilter != null && !this.titleFilter.isEmpty() && !task.name.toLowerCase().contains(this.titleFilter)) {
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
                activeRowPos = holder.getAdapterPosition();

                if(recyclerView != null)
                    recyclerView.smoothScrollToPosition(activeRowPos);
            }
            else
            {
                holder.row.setBackgroundColor(Color.TRANSPARENT);
            }

            if(task.date != null) {
                String terminTxt = TaskFormater.formatDateToString(task.date);
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
                    View parent = (View) view.getParent();
                    while(parent.getTag() == null) {
                        parent = (View) parent.getParent();
                    }
                    Task item = (Task) parent.getTag();
                    if(item != null)
                        activeRowItemId = item.id;
                    OnTaskChecked(task, holder.date, holder.costs);
                    // FIXME only One
                    notifyDataSetChanged();
                    Snackbar.make(view, task.name + " " + msg, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    NotifyTaskChanged(
                      task,
                      mParentActivity,
                      new Long[] {100L}
                    );

                    if(task.is_Done && hideDoneTasks)
                        notifyDataSetChanged();
                }
            });

            final int actPosition = holder.getAdapterPosition();
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

                    // FIXME only One
                    // if(act.priority == Priority.Normal && hideNormalPrio)
                    // notifyDataSetChanged();
                    NotifyTaskChanged(task, mParentActivity, new Long[] {item});
                    Snackbar snack = Snackbar.make(view, SimpleItemRecyclerViewAdapter.this.mParentActivity.getResources().getString(act.priority == Priority.Normal ? R.string.normalPrioText : R.string.highPrioText) + " " + act.name, Snackbar.LENGTH_LONG);
                    snack.show();
                }
            });

            holder.imgDelete.setVisibility(View.VISIBLE);
            holder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
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
                                    for(Task task: Task.getTaskList()) {
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
                    } else {
                        Persistance.DeleteTask(task, mParentActivity);
                        Task toRemove = mValues.get(actPosition);
                        SimpleItemRecyclerViewAdapter.this.remove(toRemove);
                        SimpleItemRecyclerViewAdapter.this.notifyDataSetChanged();
                        if(mTwoPane) {
                            removeFragments();
                        }
                    }
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

        public void setTitleFilter(String titleFilter) {
            this.titleFilter = titleFilter != null ? titleFilter.toLowerCase() : titleFilter;
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

    public static void showFilterTitleDialog(Context context, final MainActivity instance, final MenuItem menuItem)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setTitle(context.getString(R.string.find_by_name));

        LinearLayout container = new LinearLayout(instance);
        container.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(30, 0, 30, 0);
        final EditText input = new EditText(context);
        input.setLayoutParams(lp);
        input.setGravity(android.view.Gravity.TOP|android.view.Gravity.LEFT);
        input.setLines(1);
        input.setMaxLines(1);
        input.setHint(context.getString(R.string.name_of_task));
        container.addView(input);

        // Set an EditText view to get user input
        alert.setView(container);

        alert.setPositiveButton(context.getString(R.string.filter), new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                String value = input.getText().toString();
                Drawable drawable = menuItem.getIcon();
                drawable.mutate();
                if(value.isEmpty()) {
                    menuItem.setChecked(false);
                    drawable.setColorFilter(instance.getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
                } else {
                    menuItem.setChecked(true);
                    drawable.setColorFilter(instance.getResources().getColor(R.color.colorGreen), PorterDuff.Mode.SRC_ATOP);
                }
                instance.adapter.setTitleFilter(value);
                instance.adapter.notifyDataSetChanged();
            }
        });

        alert.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                Drawable drawable = menuItem.getIcon();
                if(drawable != null) {
                    drawable.mutate();
                    drawable.setColorFilter(instance.getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
                }
                menuItem.setChecked(false);
                instance.adapter.setTitleFilter(null);
                instance.adapter.notifyDataSetChanged();
            }
        });

        alert.show();
    }

    public static void showDeleteAllTasksDialog(Context context, final MainActivity instance)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setTitle(context.getString(R.string.deleteAllTasks));

        alert.setPositiveButton(context.getString(R.string.delete_all), new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                Persistance.PruneAllTasks(instance, false,null, null, null);
                NotifyTaskChanged(null, null, new Long[] {list});
                Intent intent = new Intent(instance, IntroActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                instance.startActivity(intent);
            }
        });

        alert.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                // Canceled.
            }
        });

        alert.show();
    }

    private class ImportExportReceiver extends ResultReceiver {

        Activity activity;

        ImportExportReceiver(Activity mainActivity, Handler handler) {
            super(handler);
            activity = mainActivity;
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            View parentLayout;
            Snackbar snack;
            switch (resultCode) {
                case ExportService.EXPORT_ERROR:
                    Toast.makeText(getApplicationContext(), R.string.errorSaving,
                      Toast.LENGTH_SHORT).show();
                    break;

                case ExportPdfService.EXPORT_PDF_ERROR:
                    Toast.makeText(getApplicationContext(), R.string.errorExportPDF,
                      Toast.LENGTH_SHORT).show();
                    break;

                case ImportService.IMPORT_ERROR:
                    Toast.makeText(getApplicationContext(), R.string.errorLoading,
                      Toast.LENGTH_SHORT).show();
                    break;

                case ExportPdfService.EXPORT_PDF_SUCCESS:

                    parentLayout = findViewById(android.R.id.content);
                    snack = Snackbar.make(
                            parentLayout,
                            R.string.ExportNotificationFinishedMessage,
                            Snackbar.LENGTH_LONG);

                    snack.setAction(
                            R.string.openPDF,
                            new OpenFileListener(activity, resultData.getString("URI"))
                    );
                    snack.show();
                    break;

                case ExportService.EXPORT_SUCCESS:

                    parentLayout = findViewById(android.R.id.content);
                    snack = Snackbar.make(
                      parentLayout,
                      R.string.SaveNotificationFinishedMessage,
                      Snackbar.LENGTH_LONG);

                    snack.show();
                    break;

                case ImportService.IMPORT_SUCCESS:

                    parentLayout = findViewById(android.R.id.content);
                    snack = Snackbar.make(
                      parentLayout,
                      R.string.ImportNotificationFinishedMessage,
                      Snackbar.LENGTH_LONG);

                    snack.show();
                    break;
            }
            super.onReceiveResult(resultCode, resultData);
        }

    }
}
