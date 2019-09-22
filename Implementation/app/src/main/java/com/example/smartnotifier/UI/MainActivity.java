package com.example.smartnotifier.UI;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartnotifier.Database.Todo;
import com.example.todolist.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.squareup.seismic.ShakeDetector;

import java.util.List;


public class MainActivity extends AppCompatActivity implements ShakeDetector.Listener {

    private TodoViewModel todoViewModel;
    ConstraintLayout constraintLayout;
    private Paint p = new Paint();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SETTING APP THEME
        setTheme();

        RecyclerView recyclerView = findViewById(R.id.todo_recyclerview);

        final TodoListAdapter adapter = new TodoListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        todoViewModel = ViewModelProviders.of(this).get(TodoViewModel.class);
        todoViewModel.getAllTodos().observe(this, new Observer<List<Todo>>() {
            @Override
            public void onChanged(@Nullable List<Todo> todos) {
                adapter.setTodos(todos);
            }
        });


        //SWIPABLE
        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    // We are not implementing onMove() in this app.
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    // When the use swipes a word,
                    // delete that word from the database.
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();

                        if (direction == ItemTouchHelper.LEFT) {
                            final Todo currenTodo = adapter.getTodoAtPosition(position);


                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Are you sure?")
                                    .setContentText("You won't be able to recover deleted todo")
                                    .setCancelText("Cancel")

                                    .setConfirmText("Yes,delete it!")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                                    .setTitleText("Cool...")
                                                    .setContentText("Todo deleted successfully")
                                                    .show();
                                            // Delete the word.
                                            todoViewModel.deleteTodo(currenTodo);
                                            sDialog.dismissWithAnimation();
                                        }
                                    }).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                    adapter.notifyDataSetChanged();

                                }
                            })
                                    .show();
                        } else {
                            Todo todo = adapter.getTodoAtPosition(position);
                            Bundle bundle = new Bundle();
                            bundle.putInt("id", todo.getId());
                            bundle.putString("title", todo.getTitle());
                            bundle.putString("time", todo.getTime());
                            bundle.putString("date", todo.getDate());
                            bundle.putString("priority", todo.getPriority());
                            bundle.putString("type", todo.getType());

                            openAddPopup("edit", bundle);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                        Bitmap icon;
                        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                            View itemView = viewHolder.itemView;
                            float height = (float) itemView.getBottom() - (float) itemView.getTop();
                            float width = height / 3;
                            if (dX > 0) {
                                p.setColor(Color.parseColor("#388E3C"));

                                RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop() + 23, dX, (float) itemView.getBottom());
                                c.drawRect(background, p);
                                icon = getBitmap(R.drawable.ic_edit_black_24dp);

                                RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                                c.drawBitmap(icon, null, icon_dest, p);
                            } else {
                                p.setColor(Color.parseColor("#D32F2F"));
                                RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop() + 23, (float) itemView.getRight(), (float) itemView.getBottom());
                                c.drawRect(background, p);
                                icon = getBitmap(R.drawable.ic_delete_black_24dp);
                                RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                                c.drawBitmap(icon, null, icon_dest, p);
                            }
                        }
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    }
                });
        // Attach the item touch helper to the recycler view.
        helper.attachToRecyclerView(recyclerView);

        FloatingActionButton fab_form = findViewById(R.id.add_todo_form);
        final FloatingActionMenu fmen = findViewById(R.id.main_quick_menu);
        fab_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fmen.close(true);
                openAddPopup("add", new Bundle());
            }
        });

        FloatingActionButton fab_qr = findViewById(R.id.add_todo_qr);
        fab_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes
                    startActivityForResult(intent, 0);
                } catch (Exception e) {
                    Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                    startActivity(marketIntent);
                }
            }
        });

        //CHANGING THEME
        FloatingActionButton app_skin = findViewById(R.id.app_skin);
        app_skin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getApplicationContext().getSharedPreferences("TodoPref", 0);
                SharedPreferences.Editor editor = preferences.edit();

                String theme = preferences.getString("app_theme", null);
                if (theme.equals("light")) {
                    editor.putString("app_theme", "dark");
                } else {
                    editor.putString("app_theme", "light");
                }

                editor.commit();
                adapter.notifyDataSetChanged();
                setTheme();
            }
        });


        //EDIT MODE ON TAP
        adapter.setOnItemClickListener(new TodoListAdapter.ClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent i = new Intent(MainActivity.this, TodoDetail.class);
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        adapter.setOnDeleteClickListener(new TodoListAdapter.DeleteClickListener() {
            @Override
            public void onDeleteClick(View v, int position) {
                Todo currenTodo = adapter.getTodoAtPosition(position);
                Toast.makeText(MainActivity.this,
                        "Deleting" + " " +
                                currenTodo.getTitle(), Toast.LENGTH_SHORT).show();

                // Delete the word.
                todoViewModel.deleteTodo(currenTodo);
            }
        });

        adapter.setOnEditClickListener(new TodoListAdapter.EditClickListener() {
            @Override
            public void onEditClick(View v, int position) {
                Todo todo = adapter.getTodoAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putInt("id", todo.getId());
                bundle.putString("title", todo.getTitle());
                bundle.putString("time", todo.getTime());
                bundle.putString("date", todo.getDate());
                bundle.putString("priority", todo.getPriority());
                openAddPopup("edit", bundle);
            }
        });


        //SHAKE DETECTOR
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        ShakeDetector shakeDetector = new ShakeDetector(this);

        shakeDetector.start(sensorManager);
    }

    private void setTheme() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("TodoPref", 0);
        String theme = preferences.getString("app_theme", null);

        constraintLayout = (ConstraintLayout) findViewById(R.id.main_activity_wrapper);
        if (theme.equals("light")) {
            constraintLayout.setBackgroundColor(Color.parseColor("#333333"));
        } else {
            constraintLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }

    private void openAddPopup(String mode, Bundle bundle) {
        String title;

        if (mode == "edit") {
            title = "Edit Todo";
        } else {
            title = "Add Todo";
        }

        AddTodo addTodo = new AddTodo(title, mode, bundle);

        addTodo.show(getSupportFragmentManager(), "Add Todo");
    }

    private Bitmap getBitmap(int drawableRes) {
        Drawable drawable = getResources().getDrawable(drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {

            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");

                String[] strParts = contents.split("\\r?\\n|\\r");
                String title = "", date = "", time = "";
                for (int i = 0; i < strParts.length; i++) {
                    String[] strPart1 = strParts[i].split("=");
                    if (strPart1[0].equals("Title")) {
                        title = strPart1[1];
                    } else if (strPart1[0].equals("Date")) {
                        date = strPart1[1];
                    } else if (strPart1[0].equals("Time")) {
                        time = strPart1[1];
                    }
                }

                Todo todo = new Todo(title, date, time, "Test", "Test");
                todoViewModel.insert(todo);

            }
            if (resultCode == RESULT_CANCELED) {
                //handle cancel
            }
        }
    }

    @Override
    public void hearShake() {
        // open camera
        // refresh the app
        // do any custom business logic
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }

        int random = (int) (Math.random() * 5000 + 1);

        Todo todo = new Todo("My test todo (" + random + ")", "2019-05-15", "2:00", "Medium", "Alarm");
        todoViewModel.insert(todo);
        Toast.makeText(this, "Added test todo", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_calendar_view:
//                Intent intent = new Intent(MainActivity.this, CalendarView.class);
//                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
