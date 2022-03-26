package com.example.flashcardapp2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class AddCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        ImageView cancel = findViewById(R.id.cancel_button);
        ImageView save = findViewById(R.id.save_button);
        EditText question = findViewById(R.id.edit_question);
        EditText answer = findViewById(R.id.edit_answer);

        cancel.setOnClickListener(v -> finish());

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra("question", question.getText().toString());
                data.putExtra("answer", answer.getText().toString());
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }
}