package com.example.materialtodo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.materialtodo.R;
import com.example.materialtodo.models.Task;

public class TasksListAdapter extends ListAdapter<Task, TasksListAdapter.TasksViewHolder> {

    private OnItemClickListener listener;

    private static final DiffUtil.ItemCallback<Task> DIFF_CALLBACK = new DiffUtil.ItemCallback<Task>() {
        @Override
        public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription());
        }
    };

    public TasksListAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public TasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TasksViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TasksViewHolder holder, int position) {
        holder.textTitle.setText(getItem(position).getTitle());
        holder.textDescription.setText(getItem(position).getDescription());
    }

    public Task getTaskAt(int position) {
        return getItem(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    class TasksViewHolder extends RecyclerView.ViewHolder {

        private TextView textTitle;
        private TextView textDescription;

        public TasksViewHolder(@NonNull View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.text_view_task_title);
            textDescription = itemView.findViewById(R.id.text_view_task_description);

            // edit task
            itemView.findViewById(R.id.item_task_layout)
                    .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onClick(getItem(getAdapterPosition()));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onClick(Task task);
    }
}
