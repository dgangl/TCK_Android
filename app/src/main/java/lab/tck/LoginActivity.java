package lab.tck;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.concurrent.TimeUnit;

import Backend.LocalStorage;
import Backend.Person;

public class LoginActivity extends AppCompatActivity {
    //UI
    private Button loginButton;
    private EditText phoneCodeFirstnameEditText;
    private EditText nameEditText;

    //Firebase
    FirebaseFirestore db;
    private String codeSent;
    private static String TAG = "PhoneAuth";
    private String phoneVerificationId;
    private PhoneAuthProvider.ForceResendingToken token;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        saveTestUser();

        //Hide TitleBar & StatusBar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        //UI
        loginButton = (Button) findViewById(R.id.login_button);
        phoneCodeFirstnameEditText = (EditText) findViewById(R.id.phoneNumber_editText);
        nameEditText = (EditText) findViewById(R.id.name_editText);
        nameEditText.setVisibility(View.GONE);

        //Login User
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneCodeFirstnameEditText.getText().toString();
                if (loginButton.getText().equals("Login")) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    //sendCode();
                } else if (phoneCodeFirstnameEditText.getHint().toString().equals("okay")) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{

                }
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                loginButton.setText("Error");
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken resendingToken) {
                super.onCodeSent(verificationId, resendingToken);

                phoneVerificationId = verificationId;
                token = resendingToken;


                phoneCodeFirstnameEditText.setText("");
                phoneCodeFirstnameEditText.setHint("Code");
                loginButton.setText("Verify Code");

            }
        };
    }


    public void sendCode(){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneCodeFirstnameEditText.getText().toString(),     // Phone number to verify
                60,                                              // Timeout duration
                TimeUnit.SECONDS,                                  // Unit of timeout
                LoginActivity.this,                       // Activity (for callback binding)
                mCallbacks);                                     // OnVerificationStateChangedCallbacks
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            phoneCodeFirstnameEditText.setText("");
                            phoneCodeFirstnameEditText.setHint("Vorname");
                            nameEditText.setVisibility(View.VISIBLE);
                            loginButton.setText("okay");

                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                loginButton.setText("Error");
                            }
                        }
                    }
                });
    }

    private void saveUserLocal(){

        System.out.println("Saving User");
        Person p = new Person(nameEditText.getText().toString(), "Nachname", phoneCodeFirstnameEditText.getText().toString(), false, null);

        LocalStorage.saveUser(p);
        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(openFileOutput("user.csv",MODE_PRIVATE)))){
            bw.write(p.createCsvString());
        }catch (Exception x){}
    }

    private void saveTestUser(){
        System.err.println("DELETE THIS METHOD");
        Person p = new Person("Vorname", "Nachname", "+4367761043479", false, null);

        LocalStorage.saveUser(p);
        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(openFileOutput("user.csv",MODE_PRIVATE)))){
            bw.write(p.createCsvString());
        }catch (Exception x){}
    }


}
