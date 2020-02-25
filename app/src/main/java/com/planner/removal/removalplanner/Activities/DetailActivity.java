package com.planner.removal.removalplanner.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.planner.removal.removalplanner.Fragments.TaskDetailFragment;
import com.planner.removal.removalplanner.R;

public class DetailActivity extends AppCompatActivity {

    public static DetailActivity instance;
    TaskDetailFragment fragment;
    public static final String ARG_TASK_ID = "task_id";
    String currentTaskId;
    boolean didAddTopBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.e("DEBUG", "onCreate DetailActivity");
        super.onCreate(savedInstanceState);

       // requestWindowFeature(Window.FEATURE_NO_TITLE);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_detail);

        // Show the Up button in the action bar.
        /*ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
*/
        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            fragment = new TaskDetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.task_detail_container, fragment)
                    .commit();
        }

        currentTaskId = getIntent().getStringExtra(TaskDetailFragment.TASK_ID);
        instance = this;
        Log.e("DEBUG", "onCreate DetailActivity End");
    }

    @Override
    protected void onPause() {
        Log.e("DEBUG", "DetailActivity onPause");
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("DEBUG", "DetailActivity onNewIntent");
        currentTaskId = intent.getStringExtra(TaskDetailFragment.TASK_ID);
        Log.e("DEBUG", "DetailActivity onNewIntent End");
    }

    @Override
    protected void onResume()
    {
        Log.e("DEBUG", "onResume DetailActivity");
        super.onResume();

        Bundle arguments = new Bundle();
        arguments.putString(TaskDetailFragment.TASK_ID, currentTaskId);
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
                findViewById(R.id.toolbar_wrapper_detail).setVisibility(View.VISIBLE);
                this.didAddTopBar = true;
            }
        } else {
            if(findViewById(R.id.toolbar_wrapper_detail) != null)
                findViewById(R.id.toolbar_wrapper_detail).setVisibility(View.GONE);
        }
    }

    public boolean onOptionsItemSelectedDetail(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.back:
                //this.finish();
                exit();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
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
        // this.finish();
        exit();
        return true;
    }

    private void exit() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
}
