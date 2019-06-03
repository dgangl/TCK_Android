package lab.tck;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import Backend.LocalStorage;
import Backend.Person;

public class UserFragment extends Fragment {
    private Person user;
    private TextView firstname;
    private TextView lastname;
    private TextView member;
    private Button changeName;
    private Button logout;
    private ImageView avatar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_user, null);

        //Get User
        user = LocalStorage.loadUser();

        //UI
        firstname = root.findViewById(R.id.user_firstname);
        lastname = root.findViewById(R.id.user_lastname);
        member = root.findViewById(R.id.user_isMember);
        changeName = root.findViewById(R.id.user_changename);
        logout = root.findViewById(R.id.user_logout);
        avatar = root.findViewById(R.id.user_avatar);

        //Set Values
        if(user != null) {
            firstname.setText(user.vorname);
            lastname.setText(user.nachname);

            if (user.mitglied == true) {
                member.setText("Jahresmitglied");
            } else {
                member.setText("Kein Mitglied");
            }
        }

        //Buttons
        changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               changeNameFunction(root);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Logout the user
            }
        });



        return root;
    }

    private void changeNameFunction(ViewGroup root){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(root.getContext());
        Context context = root.getContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        buttonLayoutParams.setMargins(20,20,20,20);

        // Add a TextView here for the "Title" label, as noted in the comments
        final EditText nameOne = new EditText(context);
        nameOne.setHint("Vorname");
        nameOne.setLayoutParams(buttonLayoutParams);
        layout.addView(nameOne); // Notice this is an add method

        // Add another TextView here for the "Description" label
        final EditText nameTwo = new EditText(context);
        nameTwo.setHint("Nachname");
        nameTwo.setLayoutParams(buttonLayoutParams);
        layout.addView(nameTwo); // Another add method



        final Button ok = new Button(context);
        ok.setBackgroundColor(Color.parseColor("#021B79"));
        ok.setTextColor(Color.WHITE);
        ok.setText("Namen ändern");
        ok.setLayoutParams(buttonLayoutParams);
        layout.addView(ok); // Another add method

        dialog.setView(layout); // Again this is a set method, not add
        dialog.show();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Person p = new Person(nameOne.getText().toString(), nameTwo.getText().toString(), user.nummer, user.mitglied, user.reference);
                p.loginUser();
                firstname.setText(user.vorname);
                lastname.setText(user.nachname);
            }
        });
    }
}
