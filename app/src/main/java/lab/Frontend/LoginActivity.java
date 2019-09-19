package lab.Frontend;

import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.hotspot2.pps.Credential;
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

import com.google.android.gms.common.util.NumberUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.concurrent.TimeUnit;

import Backend.LocalStorage;
import Backend.Database.Person;
import lab.Frontend.MainView.MainActivity;
import lab.Frontend.New_Reservation.Activities.DetailView;
import lab.Frontend.New_Reservation.Activities.EditorMembers;
import lab.tck.R;

public class LoginActivity extends AppCompatActivity {
    //UI
    private Button loginButton;
    private EditText phoneCodeFirstnameEditText;
    private EditText nameEditText;

    //Firebase
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private int loginCounter = 0;
    private String phoneNumber;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

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
                switch (loginCounter) {
                    case 0: //handle phoneNumber -> send Code
                        phoneNumber = phoneCodeFirstnameEditText.getText().toString();
                        if (!phoneNumber.equals("")) {
                            sendCode();
                        } else {
                            ToastMaker tm = new ToastMaker();
                            tm.createToast(LoginActivity.this, "Bitte geben Sie eine korrekte Telefonnumer ein.");
                        }
                        break;
                    case 1:
                        String code = phoneCodeFirstnameEditText.getText().toString();
                        signInWithPhoneAuthCredential(PhoneAuthProvider.getCredential(mVerificationId, code));
                        break;
                    case 2:
                        saveUserLocal();
                        break;
                    default:
                        ToastMaker tm = new ToastMaker();
                        tm.createToast(LoginActivity.this, "IDK");
                        break;

                }
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential c) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                signInWithPhoneAuthCredential(c);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                ToastMaker tm = new ToastMaker();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    tm.createToast(LoginActivity.this, "Ein Fehler ist aufgetreten. Bitte überprüfe deine Internetverbindung. FEHLER 333");
                   
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    tm.createToast(LoginActivity.this, "Ein Fehler ist aufgetreten. Bitte überprüfe deine Internetverbindung. FEHLER 404");
                }else{
                    tm.createToast(LoginActivity.this, "Ein Fehler ist aufgetreten. Bitte überprüfe deine Internetverbindung. FEHLER 154");
                }



            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                loginCounter = 1;



                phoneCodeFirstnameEditText.setText("");
                phoneCodeFirstnameEditText.setHint("Code");
                loginButton.setText("Code eingeben");
            }
        };


    }


    private void sendCode() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                120,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful() && credential.getSmsCode() != phoneCodeFirstnameEditText.getText().toString()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = task.getResult().getUser();

                            phoneCodeFirstnameEditText.setText("");
                            phoneCodeFirstnameEditText.setHint("Vorname");
                            phoneCodeFirstnameEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                            nameEditText.setVisibility(View.VISIBLE);
                            loginButton.setText("Anmelden");
                            loginCounter = 2;
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
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


        db.collection("Mitglieder").document("list").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                        boolean isMitglied = false;
                        if (task.getResult().get("list").toString().contains(phoneNumber)) {
                            isMitglied = true;
                        }


                        if (nameEditText.getText().toString().length() > 0 && phoneCodeFirstnameEditText.getText().toString().length() > 0) {

                            ToastMaker tm = new ToastMaker();
                            tm.createToast(LoginActivity.this, " Authentifizierung wird ausgeführt");

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);

                            Person p = new Person(phoneCodeFirstnameEditText.getText().toString(), nameEditText.getText().toString(), phoneNumber, isMitglied, null);
                            p.loginUser();

                            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(openFileOutput("user.csv", MODE_PRIVATE)))) {
                                bw.write(p.createCsvString());


                            } catch (Exception x) {
                            }

                        } else {
                            ToastMaker tm = new ToastMaker();
                            tm.createToast(LoginActivity.this, "Ihr Vor und Nachname darf nicht leer sein");
                        }
                } else {
                    ToastMaker tm = new ToastMaker();
                    tm.createToast(LoginActivity.this, "Fehler bei Überprüfung von Mitgliedschaft");
                }
            }
        });
    }
}
