package rido.schedule2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;


public class Main extends FragmentActivity implements View.OnClickListener {




    SignInButton sib;
    Button btnSignIn,btnSignUp;
    TextView txtSlogan, txtForgotPass;
    EditText input_email,input_password;

    private final static int RC_SIGN_IN = 2;

    RelativeLayout activity_main;

    private FirebaseAuth auth;

    GoogleApiClient mGoogleApiClient;

    FirebaseAuth.AuthStateListener lAuth;

    protected void onStart() {
        super.onStart();

        auth.addAuthStateListener(lAuth);
    }

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

        sib = (SignInButton) findViewById(R.id.googleBtn);
        sib.setSize(SignInButton.SIZE_STANDARD);

        sib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GooglesignIn();
            }
        });


        btnSignUp.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
        txtForgotPass.setOnClickListener(this);

        //Init Firebase Auth
        auth = FirebaseAuth.getInstance();
        lAuth = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!= null){
                    startActivity(new Intent(Main.this,HomePage.class));
                }
            }
        };

        //Check already exists session, if ok -> Dashboard
     /*   if(auth.getCurrentUser() !=null){
            startActivity(new Intent(Main.this,HomePage.class));
        }*/


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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(Main.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                Toast.makeText(Main.this, "Auth went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {


                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                           // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(Main.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                          //  updateUI(null);
                        }

                        // ...
                    }
                });

    }


    private void GooglesignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.txtForgotPass)
        {
            startActivity(new Intent(Main.this,ForgotPassword.class));
            finish();
        }
        else if(v.getId() == R.id.btnSignUp)
        {
            startActivity(new Intent(Main.this,SignUp.class));
            finish();
        }
        else if(v.getId() == R.id.btnSignIn)
        {
                // Toast.makeText(this, "Type an email, please;)", Toast.LENGTH_SHORT).show();
                loginUser(input_email.getText().toString(), input_password.getText().toString()); //TODO: Check this line

        }
    }

    private void loginUser(String email, final String password) {
        if(!Objects.equals(email, "")) {
            auth.signInWithEmailAndPassword(email, password) //TODO: Check this line
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                if (password.length() < 6) {
                                    Toast.makeText(Main.this, "Password length must be over 6", Toast.LENGTH_SHORT).show();
                                }
                                Toast.makeText(Main.this, "Sign In Error!", Toast.LENGTH_SHORT).show();
                            } else {
                                if (auth.getCurrentUser().isEmailVerified()) {
                                    startActivity(new Intent(Main.this, HomePage.class));
                                    //  finish();
                                } else {
                                    auth.getCurrentUser().sendEmailVerification();
                                    Toast.makeText(Main.this, "Email Verification required!", Toast.LENGTH_SHORT).show();
                                }
                          /*  startActivity(new Intent(Main.this,HomePage.class));
                            finish();*/
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Type an email, please;)", Toast.LENGTH_SHORT).show();
        }

    }
}
