package com.derma.sebacia;

import android.content.Intent;
import android.sax.EndElementListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SurveyQuestionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_questions);

        Intent i = getIntent();
        final String email = i.getStringExtra("email");
        //get text views
        final TextView questionOne = (TextView) findViewById(R.id.quesitonOne);
        final TextView questionTwo = (TextView) findViewById(R.id.questionTwo);
        final TextView questionThree = (TextView) findViewById(R.id.questionThree);
        final EditText answerOne = (EditText) findViewById(R.id.answerOne);
        final EditText answerTwo = (EditText) findViewById(R.id.answerTwo);
        final EditText answerThree = (EditText) findViewById(R.id.answerThree);

        Button finishSurvey = (Button) findViewById(R.id.finishSurveyButton);
        finishSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open intent to send emails
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setType("message/rfc822");
                sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Sebacia Patient Request");
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Patient Survery\n___________________________\n\n" +
                        questionOne.getText() + "\n" + answerOne.getText() +
                        "\n\n" + questionTwo.getText() + "\n" + answerTwo.getText() + "\n\n +" +
                        questionThree.getText() + "\n" + answerThree.getText());

                startActivity(Intent.createChooser(sendIntent, "Send Mail using:"));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_survey_questions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
