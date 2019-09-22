package com.example.smartnotifier.UI;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.smartnotifier.Database.Todo;
import com.example.todolist.R;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTodo extends AppCompatDialogFragment {

    private EditText txt_title, txt_date, txt_time;
    RadioGroup priority_group;
    private TodoViewModel todoViewModel;
    private String title;
    private String mode;
    private Bundle extra_data;
    private Spinner spin_type, spin_priority;

    @SuppressLint("ValidFragment")
    AddTodo(String title, String mode, Bundle bundle) {
        this.title = title;
        this.mode = mode;
        this.extra_data = bundle;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_todo, null);
        builder.setView(view).setTitle(this.title).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title     = txt_title.getText().toString();
                String time = txt_time.getText().toString();
                String date = txt_date.getText().toString();

                String priority = "High";
                int checkedId = priority_group.getCheckedRadioButtonId();
                switch (checkedId) {
                    case R.id.radButton1:
                        priority = "High";
                        break;
                    case R.id.radButton2:
                        priority = "Medium";
                        break;
                    case R.id.radButton3:
                        priority = "Low";
                }

                String type = String.valueOf(spin_type.getSelectedItem());


                if (mode == "add") {


                    Todo todo = new Todo(title, date, time, priority,type);
                    todoViewModel.insert(todo);


                    new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Cool...")
                            .setContentText("New todo added")
                            .show();
                } else {
                    Todo todo = new Todo(extra_data.getInt("id"), title, date, time, priority, type);
                    todoViewModel.update(todo);

                    new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Cool...")
                            .setContentText("Todo detail updated")
                            .show();

                }
            }
        });

        todoViewModel = ViewModelProviders.of(this).get(TodoViewModel.class);

        txt_title = view.findViewById(R.id.todo_title);
        txt_date = view.findViewById(R.id.todo_date);
        txt_time = view.findViewById(R.id.todo_time);
        spin_type = view.findViewById(R.id.spinner_type);
        priority_group = view.findViewById(R.id.radioGroup);

        if (this.mode == "edit") {
            txt_title.setText(this.extra_data.getString("title"));
            txt_date.setText(this.extra_data.getString("date"));
            txt_time.setText(this.extra_data.getString("time"));

//            spin_priority.setSelection(((ArrayAdapter<String>)spin_priority.getAdapter()).getPosition(this.extra_data.getString("priority")));
            spin_type.setSelection(((ArrayAdapter<String>) spin_type.getAdapter()).getPosition(this.extra_data.getString("type")));

            String priority = this.extra_data.getString("priority");
            switch (priority) {
                case "High":
                    priority_group.check(R.id.radButton1);
                    break;
                case "Medium":
                    priority_group.check(R.id.radButton2);
                    break;
                case "Low":
                    priority_group.check(R.id.radButton3);
            }

        }

        final Calendar myCalendar = Calendar.getInstance();

        txt_time.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        txt_time.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        //DATE PICKER
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                updateLabel();
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                txt_date.setText(sdf.format(myCalendar.getTime()));
            }

        };
        txt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        return builder.create();
    }
}
