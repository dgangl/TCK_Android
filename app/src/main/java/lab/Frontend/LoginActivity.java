package lab.Frontend;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.concurrent.TimeUnit;

import Backend.LocalStorage;
import Backend.Database.Person;
import lab.Frontend.MainView.MainActivity;
import lab.tck.R;

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
    private String phoneNumber;
    private PhoneAuthProvider.ForceResendingToken token;
    private int loginSteps = 0;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //Hide TitleBar & StatusBar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#021B79"));
        }

        //UI
        loginButton = findViewById(R.id.login_button);
        phoneCodeFirstnameEditText = findViewById(R.id.phoneNumber_editText);
        nameEditText = findViewById(R.id.name_editText);
        nameEditText.setVisibility(View.GONE);

        //Login User
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginSteps == 0 && !phoneCodeFirstnameEditText.getText().toString().equals("")) {
                    phoneNumber = phoneCodeFirstnameEditText.getText().toString();
                    sendVerificationCode(phoneNumber);
                } else if (loginSteps == 1 /**&& phoneCodeFirstnameEditText.getText().toString().equals(phoneVerificationId)**/) {
                    verifyCode(phoneCodeFirstnameEditText.getText().toString());
                } else if (!phoneCodeFirstnameEditText.getText().toString().equals("") && !nameEditText.getText().toString().equals("")) {
                    saveUserLocal();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    ToastMaker tm = new ToastMaker();
                    tm.createToast(LoginActivity.this, "Das Feld darf nicht leer sein.");
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
                ToastMaker tm = new ToastMaker();
                tm.createToast(LoginActivity.this, phoneAuthCredential.getSmsCode());
                String code = phoneAuthCredential.getSmsCode();
                if(code != null){
                    verifyCode(code);
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
               ToastMaker tm = new ToastMaker();
               tm.createToast(LoginActivity.this, "Authentifizierung fehlgeschlagen");
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken resendingToken) {
                super.onCodeSent(verificationId, resendingToken);
                loginSteps++;
                phoneVerificationId = verificationId;
                phoneCodeFirstnameEditText.setText("");
                phoneCodeFirstnameEditText.setHint("Code");
                loginButton.setText("Code Verifizieren");

                token = resendingToken;
            }
        };
    }

    public void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phoneVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    public void sendVerificationCode(String phoneNumber) {
        ToastMaker tm = new ToastMaker();
        tm.createToast(LoginActivity.this, "Send Verification Code");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,                                       // Phone number to verify
                120,                                            // Timeout duration
                TimeUnit.SECONDS,                                  // Unit of timeout
                TaskExecutors.MAIN_THREAD,                         // Activity (for callback binding)
                mCallbacks);                                       // OnVerificationStateChangedCallbacks
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            loginSteps++;
                            FirebaseUser user = task.getResult().getUser();
                            phoneCodeFirstnameEditText.setText("");
                            phoneCodeFirstnameEditText.setHint("Vorname");
                            phoneCodeFirstnameEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                            nameEditText.setVisibility(View.VISIBLE);
                            loginButton.setText("Anmelden");
                            loginSteps++;
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                ToastMaker tm = new ToastMaker();
                                tm.createToast(LoginActivity.this, "Code Authentifizierung fehlgeschlagen");
                            }
                        }
                    }
                });
    }

    private void saveUserLocal() {

        System.out.println("Saving User...");

        db.collection("Mitglieder").document("list").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                boolean isMitglied = false;

                if (task.isSuccessful()) {
                    String mitglieder = (String) task.getResult().getData().get("list");

                    if (mitglieder.contains(phoneCodeFirstnameEditText.getText().toString())) {
                        isMitglied = true;
                    }
                }

                Person p = new Person(nameEditText.getText().toString(), phoneCodeFirstnameEditText.getText().toString(), phoneCodeFirstnameEditText.getText().toString(), isMitglied, null);

                p.loginUser();
                LocalStorage.saveUser(p);
                try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(openFileOutput("user.csv", MODE_PRIVATE)))) {
                    bw.write(p.createCsvString());
                } catch (Exception x) {
                }

            }
        });
    }

    private void saveTestUser() {
        System.err.println("DELETE THIS METHOD");

        db.collection("Mitglieder").document("list").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                boolean isMitglied = false;

                if (task.isSuccessful()) {
                    String mitglieder = (String) task.getResult().getData().get("list");

                    if (mitglieder.contains(phoneCodeFirstnameEditText.getText().toString())) {
                        isMitglied = true;
                    }
                }

                Person p = new Person("Paul", "Krenn", "+4367761043478", isMitglied, null);

                p.loginUser();
                LocalStorage.saveUser(p);
                try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(openFileOutput("user.csv", MODE_PRIVATE)))) {
                    bw.write(p.createCsvString());
                } catch (Exception x) {
                }

            }
        });
    }


}
