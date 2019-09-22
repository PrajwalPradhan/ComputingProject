package com.example.smartnotifier.UI;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartnotifier.Database.Todo;
import com.example.todolist.R;

import java.util.List;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoViewHolder> {

    private final LayoutInflater mInflater;
    private List<Todo> myTodos; // Cached copy of words
    private static ClickListener clickListener;
    private static DeleteClickListener deleteClickListener;
    private static EditClickListener editClickListener;

    TodoListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(R.layout.todo_item, viewGroup, false);
        return new TodoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TodoViewHolder todoViewHolder, int i) {
        if (myTodos != null) {
            //SSETTING BACKGORUND
            SharedPreferences preferences = todoViewHolder.itemView.getContext().getSharedPreferences("TodoPref", 0);
            String theme = preferences.getString("app_theme", null);
            if (theme.equals("light")) {
                todoViewHolder.todoItemDesc.setTextColor(Color.parseColor("#333333"));
                todoViewHolder.todoItemView.setTextColor(Color.parseColor("#333333"));
                todoViewHolder.wrapper.setBackgroundDrawable(ContextCompat.getDrawable(todoViewHolder.itemView.getContext(), R.drawable.gradient));
            } else {
                todoViewHolder.todoItemDesc.setTextColor(Color.parseColor("#FFFFFF"));
                todoViewHolder.todoItemView.setTextColor(Color.parseColor("#FFFFFF"));
                todoViewHolder.wrapper.setBackgroundDrawable(ContextCompat.getDrawable(todoViewHolder.itemView.getContext(), R.drawable.gradient_black));

            }

            Todo current = myTodos.get(i);
            todoViewHolder.todoItemView.setText(current.getTitle());
            todoViewHolder.todoItemDesc.setText(current.getDate() + "   " + current.getTime());

            //ADDING IMAGE
            if (current.getType().equals("Alarm")) {
                if (theme.equals("dark"))
                    todoViewHolder.image_type.setImageResource(R.drawable.ic_alarm_white);
                else
                    todoViewHolder.image_type.setImageResource(R.drawable.ic_alarm_black_24dp);
            } else if (current.getType().equals("Remainder")) {
                if (theme.equals("dark"))
                    todoViewHolder.image_type.setImageResource(R.drawable.ic_event_white);
                else
                    todoViewHolder.image_type.setImageResource(R.drawable.ic_event_available_black_24dp);
            } else {
                if (theme.equals("dark"))
                    todoViewHolder.image_type.setImageResource(R.drawable.ic_reciept_white);
                else
                    todoViewHolder.image_type.setImageResource(R.drawable.ic_receipt_black_24dp);
            }


            //SETTING PRIORITY COLOR
            String color;
            if (current.getPriority().equals("High")) {
                color = "#af0832";
            } else if (current.getPriority().equals("Medium")) {
                color = "#d6a30a";
            } else {
                color = "#078d1f";
            }

            todoViewHolder.priority_indicator.setBackgroundColor(Color.parseColor(color));
        } else {

            // Covers the case of data not being ready yet.
            todoViewHolder.todoItemView.setText("No todo Added");
        }
    }

    @Override
    public int getItemCount() {
        if (myTodos != null)
            return myTodos.size();
        else return 0;
    }

    public class TodoViewHolder extends RecyclerView.ViewHolder {
        private final TextView todoItemView;
        private final TextView todoItemDesc;
        private final View priority_indicator;
        private final ImageView image_type;
        private LinearLayout wrapper;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);

            wrapper = itemView.findViewById(R.id.todo_each_wrapper);
            todoItemView = itemView.findViewById(R.id.todo_item_title);
            todoItemDesc = itemView.findViewById(R.id.todo_item_desc);
            priority_indicator = itemView.findViewById(R.id.priority_indicator);
            image_type = itemView.findViewById(R.id.image_type);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(view, getAdapterPosition());
                }
            });

        }
    }

    void setTodos(List<Todo> todo) {
        myTodos = todo;
        notifyDataSetChanged();
    }

    public Todo getTodoAtPosition(int position) {
        return myTodos.get(position);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        TodoListAdapter.clickListener = clickListener;
    }

    public void setOnDeleteClickListener(DeleteClickListener clickListener) {
        TodoListAdapter.deleteClickListener = clickListener;
    }

    public void setOnEditClickListener(EditClickListener clickListener) {
        TodoListAdapter.editClickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }

    public interface DeleteClickListener {
        void onDeleteClick(View v, int position);
    }

    public interface EditClickListener {
        void onEditClick(View v, int position);
    }
}
