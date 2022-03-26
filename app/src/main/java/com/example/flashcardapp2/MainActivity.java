package com.example.flashcardapp2;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashcards;

    int currentCardDisplayedIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView question = findViewById(R.id.flashcard_question);
        TextView answer = findViewById(R.id.flashcard_answer);
        ImageView add = findViewById(R.id.add_button);
        ImageView next = findViewById(R.id.next_button);
        flashcardDatabase = new FlashcardDatabase(getApplicationContext());
        allFlashcards = flashcardDatabase.getAllCards();

        if (allFlashcards != null && allFlashcards.size() > 0){
            question.setText(allFlashcards.get(0).getQuestion());
            answer.setText(allFlashcards.get(0).getAnswer());
        }

        question.setOnClickListener(v -> {
            question.setVisibility(View.INVISIBLE);
            answer.setVisibility(View.VISIBLE);
        });

        add.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
            MainActivity.this.startActivityForResult(intent, 100);
        });

        next.setOnClickListener(v -> {
            if (allFlashcards.size() == 0){
                return;
            }

            currentCardDisplayedIndex++;

            if (currentCardDisplayedIndex > allFlashcards.size() - 1){
                currentCardDisplayedIndex = 0;
            }

            question.setText(allFlashcards.get(currentCardDisplayedIndex).getQuestion());
            question.setVisibility(View.VISIBLE);
            answer.setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());
            answer.setVisibility(View.INVISIBLE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TextView questionTextView = findViewById(R.id.flashcard_question);
        TextView answerTextView = findViewById(R.id.flashcard_answer);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            String question = data.getExtras().getString("question");
            String answer = data.getExtras().getString("answer");
            questionTextView.setText(question);
            answerTextView.setText(answer);
            flashcardDatabase.insertCard(new Flashcard(question, answer));
            allFlashcards = flashcardDatabase.getAllCards();
        }
    }
}