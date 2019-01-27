package com.planner.removal.removalplanner.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.CompoundButton;

import com.planner.removal.removalplanner.R;

/**
 *
 * @author Oliver Tomaske
 * @date 2016-01-25
 *
 */
public class DetailActivity extends AppCompatActivity
        implements CompoundButton.OnCheckedChangeListener {

    public static DetailActivity instance;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        _initializeElements();

        instance = this;
    }


    @Override
    protected void onResume()
    {
        super.onResume();
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


    private void _initializeElements()
    {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { }
}
