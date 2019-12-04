package com.planner.removal.removalplanner.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.planner.removal.removalplanner.Fragments.TaskDetailFragment;
import com.planner.removal.removalplanner.R;

public class DetailActivity extends AppCompatActivity {

    public static DetailActivity instance;
    public static final String ARG_TASK_ID = "task_id";
    boolean didAddTopBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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
            Bundle arguments = new Bundle();
            arguments.putString(TaskDetailFragment.TASK_ID,
                    getIntent().getStringExtra(TaskDetailFragment.TASK_ID));
            TaskDetailFragment fragment = new TaskDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.task_detail_container, fragment)
                    .commit();
        }

        instance = this;
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (didAddTopBar)
            return;

        didAddTopBar = true;

        if(!MainActivity.mTwoPane) {
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
                this.finish();
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
        this.finish();
        return true;
    }
}
