package pl.wroc.uni.ift.android.quizactivity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static android.R.attr.data;

public class CheatActivity extends AppCompatActivity {

    private final static String EXTRA_KEY_ANSWER = "Answer";
    private final static String EXTRA_KEY_SHOWN = "Show";



    private Button mButton;
    private TextView mTextView;
    private boolean mWasWatched = false ;

    boolean mAnswer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);


        if (savedInstanceState != null) {
            mWasWatched = savedInstanceState.getBoolean("Waswatched");
        }


        mAnswer = getIntent().getBooleanExtra(EXTRA_KEY_ANSWER,false);




        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWasWatched = true;
                if (mAnswer) {
                    mTextView.setText("Prawda");
                } else {
                    mTextView.setText("Fałsz");
                }
                setAnswerShown(true);
            }

        });
        setAnswerShown(false);



        mTextView = (TextView) findViewById(R.id.textView2);
        checkCheat();
    }



    public static Intent newIntent(Context context, boolean answerIsTrue)
    {

        Intent intent = new Intent(context, CheatActivity.class);
        intent.putExtra(EXTRA_KEY_ANSWER , answerIsTrue);
        return intent;
    }

    private void setAnswerShown (boolean isAnswerShown) {
                Intent data = new Intent();
                data.putExtra(EXTRA_KEY_SHOWN, isAnswerShown);
                setResult(RESULT_OK, data);
            }
    public static boolean wasAnswerShown(Intent data)
    {
        return data.getBooleanExtra(EXTRA_KEY_SHOWN, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putBoolean("Waswatched", mWasWatched);
        super .onSaveInstanceState(outState);
    }

    private void checkCheat()
    {
        if(mWasWatched)
        {

            setAnswerShown(true);

            if (mAnswer) {
                mTextView.setText("Prawda");
            } else {
                mTextView.setText("Fałsz");
            }
        }

    }

}
