package com.apps2life.studentsevaluator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText mEmailField;
    private EditText mPasswordField ;
    private TextView mStatusTextView;
    public ProgressDialog mProgressDialog;
    private static final String TAG="LoginActivity";
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailField=(EditText)findViewById(R.id.emailField);
        mPasswordField=(EditText)findViewById(R.id.passwordField);
        mStatusTextView=(TextView)findViewById(R.id.wrong_entries);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        // [END auth_state_listener]
    }
    public void login(View view){
        String email=mEmailField.getText().toString();
        String pwd=mPasswordField.getText().toString();

        if(emailAndPwdVerification(email,pwd)){
            signIn(email, pwd);
        }

    }

    public void signin(View view){
        String email=mEmailField.getText().toString();
        String pwd=mPasswordField.getText().toString();
        if(emailAndPwdVerification(email,pwd)){
            createAccount(email, pwd);
        }
    }
    private boolean emailAndPwdVerification(String email, String pwd) {

        if(TextUtils.isEmpty(email)||TextUtils.isEmpty(pwd)){
            mStatusTextView.setText("l'email ou le mot de passe est vide");
            return false;
        }else if(!email.contains("@")||!email.contains(".")){
            mStatusTextView.setText("l'adresse email n'est pas valide");
            return false;
        }
        else if (pwd.length()<=8){
            mStatusTextView.setText("Le mot passe doit contenir au moins 8 caractère");
            return false;
        }else
            return true;
    }
    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);

        showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if(!task.isSuccessful()){
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            mStatusTextView.setText(R.string.auth_failed);
                        }
                        if(task.isSuccessful()){
                            startActivity(new Intent(LoginActivity.this,JobActivity.class));
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }
    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);

        showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            mStatusTextView.setText(R.string.auth_failed);
                        }
                        if(task.isSuccessful()){

                            goToTheRightActivity();
                        }
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    // [END on_stop_remove_listener]
    public void goToTheRightActivity(){
        switch(PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).getString("job","Nothing")){
            //case "Nothing": startActivity(new Intent(LoginActivity.this,MainActivity.class));
            //break;
            case "Student": startActivity(new Intent(LoginActivity.this,StudentActivity.class));
                break;
            case "Teacher": startActivity(new Intent(LoginActivity.this,TeacherActivity.class));
                break;
            default :startActivity(new Intent(LoginActivity.this,JobActivity.class));
        }
    }

}
