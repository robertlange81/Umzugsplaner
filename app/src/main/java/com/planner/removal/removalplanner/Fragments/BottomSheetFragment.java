package com.planner.removal.removalplanner.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.planner.removal.removalplanner.Activities.MainActivity;
import com.planner.removal.removalplanner.R;

public class BottomSheetFragment extends BottomSheetDialogFragment {
    public static BottomSheetFragment newInstance() {
        return new BottomSheetFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bottomsheet, container, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String x = view.getTag().toString();
                MainActivity.NotifyTaskChanged();
            }
        });

        return view;
    }
}
