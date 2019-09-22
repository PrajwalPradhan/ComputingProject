package com.example.smartnotifier.UI;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.smartnotifier.Database.Todo;
import com.example.smartnotifier.PagerTransformer;
import com.example.todolist.R;

import java.util.List;

public class TodoDetail extends AppCompatActivity {

    private ViewPager viewPager;
    private TodoViewModel todoViewModel;

    private TodoDetailAdapter todoDetailAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_detail);
        final Bundle bundle = getIntent().getExtras();
        viewPager = findViewById(R.id.todo_pager);
        viewPager.setPageTransformer(true, new PagerTransformer());
        todoDetailAdapter = new TodoDetailAdapter(getSupportFragmentManager());
        viewPager.setAdapter(todoDetailAdapter);

        todoViewModel = ViewModelProviders.of(this).get(TodoViewModel.class);
        todoViewModel.getAllTodos().observe(this, new Observer<List<Todo>>() {

            @Override
            public void onChanged(@Nullable List<Todo> todos) {
                todoDetailAdapter.setTodos(todos);
                viewPager.setCurrentItem(bundle.getInt("position"));


            }
        });


        viewPager.setCurrentItem(bundle.getInt("position"));


    }
}
