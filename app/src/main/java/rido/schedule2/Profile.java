package rido.schedule2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth auth;
    ImageView imgProfile;
    Button btnSave,btnLogOut;
    EditText edtName,edtPhone,edtProfilePassword;
   TextView txtWelcome;
    CoordinatorLayout activity_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance();

        imgProfile = (ImageView)findViewById(R.id.imgProfil);
        btnSave = (Button)findViewById(R.id.btnSave);
        btnLogOut = (Button)findViewById(R.id.btnLogOut);
        edtProfilePassword = (EditText)findViewById(R.id.profile_password) ;
       /* edtName = (EditText)findViewById(R.id.profile_name) ;
        edtPhone = (EditText)findViewById(R.id.profile_phone) ;*/
        txtWelcome = (TextView)findViewById(R.id.txtWelcome);
        activity_profile = (CoordinatorLayout)findViewById(R.id.activity_profile);

        btnSave.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);

        if(auth.getCurrentUser() != null)
        {
            txtWelcome.setText("Welcome , "+auth.getCurrentUser().getEmail());
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings_profile) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSave)
        {

                changePassword(edtProfilePassword.getText().toString());



        }
        else if (view.getId() == R.id.btnLogOut)
        {
            if(auth.getCurrentUser() != null) {
                if (auth.getCurrentUser().getDisplayName() != null) {

                    Toast.makeText(this, "SignOut:" + auth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "SignOut:" + auth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                }
                auth.signOut();
                startActivity(new Intent(Profile.this, Main.class));
            }

        }


    }

    private void changePassword(String newPassword) {
        FirebaseUser user = auth.getCurrentUser();
        if(!Objects.equals(newPassword, "")) {
            user.updatePassword(newPassword).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Profile.this, "Password changed successfully for" + auth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
