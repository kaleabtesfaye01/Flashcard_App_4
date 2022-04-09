package com.example.flashcardapp2;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
            int cx = answer.getWidth() / 2;
            int cy = answer.getHeight() / 2;

            float finalRadius = (float) Math.hypot(cx, cy);

            Animator anim = ViewAnimationUtils.createCircularReveal(answer, cx, cy, 0f, finalRadius);

            question.setVisibility(View.INVISIBLE);
            answer.setVisibility(View.VISIBLE);

            anim.setDuration(3000);
            anim.start();
        });

        add.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
            MainActivity.this.startActivityForResult(intent, 100);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        });

        next.setOnClickListener(v -> {
            if (allFlashcards.size() == 0){
                return;
            }

            currentCardDisplayedIndex++;

            if (currentCardDisplayedIndex > allFlashcards.size() - 1){
                currentCardDisplayedIndex = 0;
            }

            final Animation leftOutAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.left_out);
            final Animation rightInAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.right_in);

            leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    question.startAnimation(leftOutAnim);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    question.startAnimation(rightInAnim);
                    question.setText(allFlashcards.get(currentCardDisplayedIndex).getQuestion());
                    question.setVisibility(View.VISIBLE);
                    answer.setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());
                    answer.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            question.startAnimation(leftOutAnim);
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