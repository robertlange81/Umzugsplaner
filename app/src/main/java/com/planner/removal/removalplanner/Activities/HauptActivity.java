package com.planner.removal.removalplanner.Activities;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.planner.removal.removalplanner.Model.Aufgabe;
import com.planner.removal.removalplanner.Adapter.AufgabenAdapter;
import com.planner.removal.removalplanner.Model.AufgabenInitialiser;
import com.planner.removal.removalplanner.DetailDialogFragment;
import com.planner.removal.removalplanner.Helfer;
import com.planner.removal.removalplanner.R;

import java.util.ArrayList;
import java.util.Date;


public class HauptActivity extends AppCompatActivity implements DetailDialogFragment.DetailDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(Helfer.currentLocal == null) {
            Helfer.setCurrentLocale(this);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_haupt);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton addNewAction = findViewById(R.id.fab);
        addNewAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDetailActivity();
            }
        });

        ListView listView = findViewById(R.id.mobile_list);

        Date defaultDate = new Date(118, 11, 1, 0, 0, 0);
        final ArrayList<Aufgabe> aufgabenList = AufgabenInitialiser.GetStandardAufgaben(defaultDate);

        final AufgabenAdapter adapter = new AufgabenAdapter(this, aufgabenList);
        listView.setAdapter(adapter);
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
        if (id == R.id.einstellungen) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showDetailActivity() {
        Intent i = new Intent(this, DetailActivity.class);
        startActivity(i);
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
}
