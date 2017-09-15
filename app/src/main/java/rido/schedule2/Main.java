package rido.schedule2;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import rido.schedule2.Common.Common;


public class Main extends AppCompatActivity implements View.OnClickListener {

    Button btnSignIn,btnSignUp;
    TextView txtSlogan, txtForgotPass;
    EditText input_email,input_password;

    RelativeLayout activity_main;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn = (Button)findViewById(R.id.btnSignIn);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);

        txtSlogan = (TextView)findViewById(R.id.txtSlogan);
        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/tahoma.ttf");
        txtSlogan.setTypeface(face);

        txtForgotPass = (TextView)findViewById(R.id.txtForgotPass);
        txtForgotPass.setTypeface(face);

        input_email = (EditText)findViewById(R.id.login_email);
        input_password = (EditText)findViewById(R.id.login_password);

        activity_main = (RelativeLayout)findViewById(R.id.activity_main) ;


        btnSignUp.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
        txtForgotPass.setOnClickListener(this);

        //Init Firebase Auth
        auth = FirebaseAuth.getInstance();

        //Check already exists session, if ok -> Dashboard
        if(auth.getCurrentUser() !=null){
            startActivity(new Intent(Main.this,HomePage.class));
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


       /* btnSignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent SignUp = new Intent(Main.this,SignUp.class);
                startActivity(SignUp);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent signIn = new Intent(Main.this,signin.class);
                startActivity(signIn);
            }
        });*/
    }



    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.txtForgotPass)
        {
            startActivity(new Intent(Main.this,ForgotPassword.class));
        }
        else if(v.getId() == R.id.btnSignUp)
        {
            startActivity(new Intent(Main.this,SignUp.class));
            finish();
        }
        else if(v.getId() == R.id.btnSignIn)
        {
            loginUser(input_email.getText().toString(),input_password.getText().toString());
        }
    }

    private void loginUser(String email, final String password) {
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()) {
                            if (password.length() < 6) {
                                Toast.makeText(Main.this, "Password length must be over 6", Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(Main.this, "Sign In Error!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            if (auth.getCurrentUser().isEmailVerified()) {
                                startActivity(new Intent(Main.this,HomePage.class));
                              //  finish();
                            }
                            else
                            {
                                auth.getCurrentUser().sendEmailVerification();
                                Toast.makeText(Main.this, "Email Verification required!", Toast.LENGTH_SHORT).show();
                            }
                          /*  startActivity(new Intent(Main.this,HomePage.class));
                            finish();*/
                        }
                    }
                });
    }
}
