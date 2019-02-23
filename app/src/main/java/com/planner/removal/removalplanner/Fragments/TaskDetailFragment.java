package com.planner.removal.removalplanner.Fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.planner.removal.removalplanner.Activities.MainActivity;
import com.planner.removal.removalplanner.Helper.Formater;
import com.planner.removal.removalplanner.Model.Prio;
import com.planner.removal.removalplanner.Model.Task;
import com.planner.removal.removalplanner.Model.TaskType;
import com.planner.removal.removalplanner.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a
 * {@link MainActivity} in two-pane mode (on tablets)
 * or a {@link com.planner.removal.removalplanner.Activities.DetailActivity} on handsets.
 */
public class TaskDetailFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener{
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String TASK_ID = "task_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Task task;

    EditText txtName;
    EditText txtDescription;
    CheckBox checkIsDone;
    ImageView imgPrio;
    ImageView imgDelete;
    EditText txtCosts;
    Spinner spinnerDetailType;
    Button btnDatePicker;
    Button btnTimePicker;
    EditText txtDate;
    TextView[] txtLinks;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Date tempDate;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TaskDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(TASK_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            if (getArguments().containsKey(TASK_ID)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                task = Task.TASK_MAP.get(getArguments().getString(TASK_ID));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.detail, container, false);

        if(Formater.currentLocal == null) {
            Formater.setCurrentLocale(rootView.getContext());
        }

        txtName = rootView.findViewById(R.id.detail_name);
        checkIsDone = rootView.findViewById(R.id.detail_isDone);
        imgPrio = rootView.findViewById(R.id.detail_prio);
        txtDescription = rootView.findViewById(R.id.detail_description);
        txtCosts = rootView.findViewById(R.id.detail_costs);
        spinnerDetailType = (Spinner) rootView.findViewById(R.id.detail_type);

        _initLinks(rootView);

        // imgDelete =

        btnDatePicker =(Button) rootView.findViewById(R.id.detail_btn_date);
        btnTimePicker =(Button) rootView.findViewById(R.id.detail_btn_time);
        txtDate =(EditText) rootView.findViewById(R.id.detail_date);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        checkIsDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = rootView.getContext().getResources()
                        .getString(checkIsDone.isChecked() ? R.string.done : R.string.todo);
                task.isDone = checkIsDone.isChecked();
                // MainActivity.SimpleItemRecyclerViewAdapter.class.notify();
                // MainActivity.class.notify();
                MainActivity.notifyTaskChanged();
                Snackbar.make(view, task.name + " " + msg, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (task != null) {
            txtName.setText(task.name);
            txtDescription.setText(task.description);
            checkIsDone.setChecked(task.isDone);
            if (task.prio == Prio.High) {
                imgPrio.setImageResource(android.R.drawable.btn_star_big_on);
            } else {
                imgPrio.setImageResource(android.R.drawable.btn_star_big_off);
            }

            if(task.costs > 0.00)
                txtCosts.setText(Formater.intCentToString(task.costs));

            final TaskType[] categories = TaskType.values();
            final List<String> catsString = new ArrayList<>();
            for (TaskType cat : categories) {
                catsString.add(cat.toString());
            }

            ArrayAdapter _categoryAdapter = new ArrayAdapter<String>(rootView.getContext(), R.layout.simple_spinner_dropdown, catsString);
            spinnerDetailType.setAdapter(_categoryAdapter);
            if(task.type != null)
                spinnerDetailType.setSelection(task.type.getValue());

            if(task.date != null)
                txtDate.setText(Formater.formatDateToSring(task.date));
        }

        return rootView;
    }

    private void _initLinks(View rootView) {
        final HashMap<TextView, String> linkMap = new HashMap<>();
        txtLinks = new TextView[5];
        txtLinks[0] = (TextView) rootView.findViewById(R.id.detail_links_x0);
        txtLinks[1] = (TextView) rootView.findViewById(R.id.detail_links_x1);
        txtLinks[2] = (TextView) rootView.findViewById(R.id.detail_links_x2);
        txtLinks[3] = (TextView) rootView.findViewById(R.id.detail_links_x3);
        txtLinks[4] = (TextView) rootView.findViewById(R.id.detail_links_x4);

        for (final TextView txtLink : txtLinks) {
            txtLink.setMovementMethod(LinkMovementMethod.getInstance());
            if(txtLink != txtLinks[0])
                ((TableRow) txtLink.getParent()).setVisibility(View.GONE);

            txtLink.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!txtLink.hasFocus()) {
                        try {
                            String inputLink = txtLink.getText().toString();
                            inputLink = inputLink.replaceAll("\\s+","");
                            boolean showNewLine = false;
                            String key = linkMap.get(txtLink);
                            if(key == null) {
                                showNewLine = true;
                            } else {
                                task.links.remove(key);
                                linkMap.remove(txtLink);
                            }

                            if(!inputLink.equals("")) {
                                task.links.put(inputLink, inputLink);
                                linkMap.put(txtLink, inputLink);

                                if(showNewLine) {
                                    for (TextView t : txtLinks) {
                                        if(((TableRow) t.getParent()).getVisibility() != View.VISIBLE) {
                                            ((TableRow) t.getParent()).setVisibility(View.VISIBLE);
                                            break;
                                        }
                                    }
                                }
                            }

                            _formatLink(txtLink, inputLink, inputLink);
                        } catch (Exception x) {

                        }
                    }
                }
            });
        }

        if(task.links != null && task.links.size() > 0) {
            int i = 0;
            for (Map.Entry<String, String> entry : task.links.entrySet())
            {
                if(i > txtLinks.length)
                    break;

                linkMap.put(txtLinks[i], entry.getKey());
                _formatLink(txtLinks[i], entry.getValue(), entry.getKey());
                i++;
            }

            if(i < txtLinks.length)
                _formatLink(txtLinks[i], "", "");
        }
    }

    private void _formatLink(TextView linkInput, String href, String displayLink) {
        String link = "";
        if(!href.isEmpty() && !displayLink.isEmpty())
            link += "<a href='" + href + "'>" + displayLink + "</a>  ";

        linkInput.setText(Html.fromHtml(link));
        linkInput.setAutoLinkMask(Linkify.WEB_URLS);
        linkInput.setLinksClickable(true);
        linkInput.setCursorVisible(true);
        ((TableRow) linkInput.getParent()).setVisibility(View.VISIBLE);
    }

    public void onClick(View v) {

        if (v == btnDatePicker) {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this.getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            if(tempDate == null) {
                                tempDate = Calendar.getInstance().getTime(); // stattdessen Zieltermin?
                                tempDate.setHours(0);
                                tempDate.setMinutes(0);
                            }

                            tempDate.setYear(year);
                            tempDate.setMonth(monthOfYear);
                            tempDate.setDate(dayOfMonth);
                            txtDate.setText(Formater.formatDateToSring(tempDate));

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this.getContext(),
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            if(tempDate == null) {
                                tempDate = Calendar.getInstance().getTime(); // stattdessen Zieltermin?
                            }

                            tempDate.setHours(hourOfDay);
                            tempDate.setMinutes(minute);

                            txtDate.setText(Formater.formatDateToSring(tempDate));
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { }

}
