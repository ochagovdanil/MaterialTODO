package com.example.materialtodo.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.materialtodo.R;
import com.google.android.material.snackbar.Snackbar;

public class EditTaskActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.example.materialtodo.ID";
    public static final String EXTRA_TITLE = "com.example.materialtodo.TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.materialtodo.DESCRIPTION";

    private EditText editTextTitle;
    private EditText editTextDescription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        initApp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.edit_task_menu_item_save) {
            saveTask();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initApp() {
        setTitle(R.string.edit_task_activity_title);

        editTextTitle = findViewById(R.id.edit_text_task_title);
        editTextDescription = findViewById(R.id.edit_text_task_description);

        fillOutFields();
    }

    private void fillOutFields() {
        editTextTitle.setText(getIntent().getStringExtra(EXTRA_TITLE));
        editTextDescription.setText(getIntent().getStringExtra(EXTRA_DESCRIPTION));
    }

    private void saveTask() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();

        if (!title.isEmpty() && !description.isEmpty()) {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_ID, getIntent().getIntExtra(EXTRA_ID, -1));
            intent.putExtra(EXTRA_TITLE, title);
            intent.putExtra(EXTRA_DESCRIPTION, description);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            showSnackbarMessage("You must fill all fields out");
        }
    }

    private void showSnackbarMessage(String msg) {
        Snackbar.make(findViewById(R.id.edit_task_layout), msg, Snackbar.LENGTH_SHORT)
            .setAction("Ok", view -> {
            }).show();
    }
}
