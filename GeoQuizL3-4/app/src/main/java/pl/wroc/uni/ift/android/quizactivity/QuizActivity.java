package pl.wroc.uni.ift.android.quizactivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {


    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;

    private TextView mQuestionTextView;
    private TextView mApiTextView ;

    private int mCurrentIndex = 0;
    private int mRightAnswers = 0;
    private int mNumberAnswer = 0;
    private int mTokenNumber  = 3;

    private static final int CHEAT_REQEST_CODE = 0;
    private static final String KEY_INDEX  = "index";


    private Question[] mQuestionsBank = new Question[]{
            new Question(R.string.question_stolica_polski, true),
            new Question(R.string.question_stolica_dolnego_slaska, false),
            new Question(R.string.question_sniezka, true),
            new Question(R.string.question_wisla, true)
    };

    private boolean[] mAnswerQuestion = new boolean[mQuestionsBank.length];
    private boolean[] mCheatArray = new boolean[mQuestionsBank.length];
    //    Bundles are generally used for passing data between various Android activities.
    //    It depends on you what type of values you want to pass, but bundles can hold all
    //    types of values and pass them to the new activity.
    //    see: https://stackoverflow.com/questions/4999991/what-is-a-bundle-in-an-android-application
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        // inflating view objects
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
                        mCurrentIndex = savedInstanceState.getInt(KEY_INDEX);
                        mRightAnswers = savedInstanceState.getInt("right_answer");
                        mNumberAnswer = savedInstanceState.getInt("number_answer");
                        mTokenNumber  = savedInstanceState.getInt("token_number");
                        mAnswerQuestion = savedInstanceState.getBooleanArray("answer_array_boolean");
                        mCheatArray = savedInstanceState.getBooleanArray("cheat_array");
        }

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                boolean currentAnswer = mQuestionsBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, currentAnswer);
                startActivityForResult(intent, CHEAT_REQEST_CODE);

            }


        });

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionsBank.length;
                updateQuestion();
            }

        });
        mApiTextView = (TextView) findViewById(R.id.api_view);
        mApiTextView.setText("Wersja systemu: " + Build.VERSION.RELEASE + "\n" + "Wersja API: " + Float.valueOf(Build.VERSION.SDK_INT));


        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkAnswer(true);
                    }
                }
        );

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);

            }
        });

        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionsBank.length;
                updateQuestion();
            }
        });

        mPrevButton =  (ImageButton) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCurrentIndex ==0){
                    mCurrentIndex = mQuestionsBank.length - 1;
                }
                else {
                    mCurrentIndex = Math.abs(mCurrentIndex - 1) % mQuestionsBank.length;
                }
                updateQuestion();
            }
        });



        updateQuestion();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == CHEAT_REQEST_CODE) {
                boolean answerWasShown = CheatActivity.wasAnswerShown(data);
                if (answerWasShown){
                    showCheat();
                    saveCheating();
                    mTokenNumber -= 1;
                }
                    updateQuestion();


                }
            }

    }


    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putInt(KEY_INDEX, mCurrentIndex);
        outState.putInt("right_answer", mRightAnswers);
        outState.putInt("number_answer", mNumberAnswer);
        outState.putInt("token_number", mTokenNumber);
        outState.putBooleanArray("answer_array_boolean", mAnswerQuestion);
        outState.putBooleanArray("cheat_array", mCheatArray);
        super .onSaveInstanceState(outState);
    }

    private void gameOver(){
        if(mNumberAnswer == mQuestionsBank.length)
        {
            String gameOver_string = getString(R.string.gameOver_toast, mRightAnswers);
            Toast.makeText(this,gameOver_string, Toast.LENGTH_LONG).show();
        }


    }

    private void updateQuestion() {
        int question = mQuestionsBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);

        if (mAnswerQuestion[mCurrentIndex] == true) {
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
        } else {
            mTrueButton.setEnabled(true);
            mFalseButton.setEnabled(true);
        }

        if(mCheatArray[mCurrentIndex] == true || mAnswerQuestion[mCurrentIndex] == true){
            mCheatButton.setEnabled(false);
        }
        else{
            mCheatButton.setEnabled(true);
        }

        if(mTokenNumber == 0)
        {
            mCheatButton.setVisibility(View.INVISIBLE);
        }
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionsBank[mCurrentIndex].isAnswerTrue();

        int toastMessageId = 0;

        if (userPressedTrue == answerIsTrue) {
            toastMessageId = R.string.correct_toast;
            mRightAnswers += 1;
            mAnswerQuestion[mCurrentIndex] = true;

        } else {
            toastMessageId = R.string.incorrect_toast;
            mAnswerQuestion[mCurrentIndex] = true;
        }

        mNumberAnswer += 1;
        updateQuestion();
        gameOver();
        Toast toast = Toast.makeText(this, toastMessageId, Toast.LENGTH_SHORT);
        toast.show();
        toast.setGravity(Gravity.TOP,0,200);

    }

    private void showCheat()
    {
        Toast.makeText(this, R.string.message_for_cheaters, Toast.LENGTH_LONG).show();
    }

    private void saveCheating()
    {
        mCheatArray[mCurrentIndex] = true;;
    }
}
