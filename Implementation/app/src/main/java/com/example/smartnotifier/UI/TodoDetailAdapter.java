package com.example.smartnotifier.UI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.smartnotifier.Database.Todo;

import java.util.List;

public class TodoDetailAdapter extends FragmentStatePagerAdapter {
    private List<Todo> myTodos; // Cached copy of words

    public TodoDetailAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        TodoDetailFragment todoDetailFragment= new TodoDetailFragment();
        Todo current = myTodos.get(i);
        Bundle bundle = new Bundle();
        bundle.putString("todo_title", current.getTitle());
        bundle.putString("todo_date", "Date : " + current.getDate());
        bundle.putString("todo_time", "Time : " +current.getTime());
        bundle.putString("todo_priority", "Priority : " +current.getPriority());
        bundle.putString("todo_type", "Date : " +current.getType());

        todoDetailFragment.setArguments(bundle);
        return todoDetailFragment;
    }

    @Override
    public int getCount() {
        if (myTodos != null)
            return myTodos.size();
        else return 0;
    }

    public void setTodos(List<Todo> todos) {
        myTodos = todos;
        notifyDataSetChanged();
    }
}
