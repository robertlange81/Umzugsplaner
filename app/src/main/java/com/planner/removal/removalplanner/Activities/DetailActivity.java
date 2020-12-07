package com.planner.removal.removalplanner.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.planner.removal.removalplanner.Fragments.TaskDetailFragment;
import com.planner.removal.removalplanner.Helpers.LoadingTask;
import com.planner.removal.removalplanner.Helpers.Persistance;
import com.planner.removal.removalplanner.R;

import static com.planner.removal.removalplanner.Activities.MainActivity.isAppOnForeground;

public class DetailActivity extends AppCompatActivity {

    public static DetailActivity instance;
    TaskDetailFragment fragment;
    public static final String ARG_TASK_ID = "task_id";
    String currentTaskId;
    boolean didAddTopBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d("DEBUG", "onCreate DetailActivity");
        super.onCreate(savedInstanceState);

        instance = this;
        setContentView(R.layout.activity_detail);
        progress_overlay();

        // Show the Up button in the action bar.
        /*ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        */
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            fragment = new TaskDetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.task_detail_container, fragment)
                    .commit();
        }

        currentTaskId = getIntent().getStringExtra(TaskDetailFragment.TASK_ID);
        Log.d("DEBUG", "onCreate DetailActivity End");
    }

    public static void progress_overlay() {
        if(instance != null)
            new LoadingTask((FrameLayout) instance.findViewById(R.id.progress_overlay)).execute();
    }

    @Override
    protected void onPause() {
        Log.d("DEBUG", "DetailActivity onPause");
        super.onPause();
    }

    public void onStop() {
        if(!isAppOnForeground(this)) {
            Persistance.ReplaceAllTasks(this);
        }
        super.onStop();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("DEBUG", "DetailActivity onNewIntent");
        currentTaskId = intent.getStringExtra(TaskDetailFragment.TASK_ID);
        Log.d("DEBUG", "DetailActivity currentTaskId: " + currentTaskId);
        Log.d("DEBUG", "DetailActivity onNewIntent End");
    }

    @Override
    protected void onResume()
    {
        Log.d("DEBUG", "onResume DetailActivity");
        super.onResume();

        Bundle arguments = new Bundle();
        arguments.putString(TaskDetailFragment.TASK_ID, currentTaskId);

        if (fragment == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            fragment = new TaskDetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.task_detail_container, fragment)
                    .commit();
        }

        fragment.setArguments(arguments);

        if(!MainActivity.mTwoPane) {
            if(didAddTopBar)
                return;

            // Inflate and initialize the top main menu
            ActionMenuView topBar = (ActionMenuView)findViewById(R.id.top_actionmenu_detail);
            if(topBar != null) {
                Menu topMenu = topBar.getMenu();
                getMenuInflater().inflate(R.menu.menu_detail, topMenu);

                for (int i = 0; i < topMenu.size(); i++) {
                    topMenu.getItem(i).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            return onOptionsItemSelectedDetail(item);
                        }
                    });
                }
                this.didAddTopBar = true;
            }
        }
    }

    public boolean onOptionsItemSelectedDetail(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.back:
                exit();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        exit();
    }


    /**
     * On click listener that
     * finish the activity.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);
        exit();
        return true;
    }

    private void exit() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
}
