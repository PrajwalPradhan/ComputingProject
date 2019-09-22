package com.example.smartnotifier.UI;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.todolist.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class TodoDetailFragment extends Fragment {


    private TextView todo_title, todo_date, todo_time, todo_priority, todo_type;
    private ConstraintLayout constraintLayout;

    public TodoDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_todo_detail, container, false);
        todo_title = view.findViewById(R.id.todo_title);
        todo_date = view.findViewById(R.id.todo_detail_date);
        todo_time = view.findViewById(R.id.todo_detail_time);
        todo_priority = view.findViewById(R.id.todo_detail_priority);
        todo_type = view.findViewById(R.id.todo_detail_type);

        constraintLayout = view.findViewById(R.id.view_pager_layout);

        String priority = getArguments().getString("todo_priority");
        Log.d("PRIORITY", priority);
        //SETTING PRIORITY COLOR
//        String color;
//        if (priority.equals("High")) {
//            color = "#af0832";
//        } else if (priority.equals("Medium")) {
//            color = "#d6a30a";
//        } else {
//            color = "#078d1f";
//        }
//                Log.d("PRIORITY", color);
//
//        constraintLayout.setBackgroundColor(Color.parseColor(color));
        todo_title.setText(getArguments().getString("todo_title"));
        todo_date.setText(getArguments().getString("todo_date"));
        todo_time.setText(getArguments().getString("todo_time"));
        todo_priority.setText(getArguments().getString("todo_priority"));
        todo_type.setText(getArguments().getString("todo_type"));


        return view;
    }

}
