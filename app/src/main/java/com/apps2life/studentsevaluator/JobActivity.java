package com.apps2life.studentsevaluator;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class JobActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);
    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.Ens:
                if (checked){
                    PreferenceManager.getDefaultSharedPreferences(JobActivity.this).edit().putString("job","Teacher").commit();
                    startActivity(new Intent(JobActivity.this,TeacherActivity.class));}
                    break;
            case R.id.Ele:
                if (checked){
                    PreferenceManager.getDefaultSharedPreferences(JobActivity.this).edit().putString("job","Student").commit();
                    startActivity(new Intent(JobActivity.this,StudentActivity.class));
                }
                    break;
        }
    }
}
