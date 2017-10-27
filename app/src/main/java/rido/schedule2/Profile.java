package rido.schedule2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import static android.R.attr.id;
import static android.R.attr.thickness;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth auth;
    ImageView imgProfile;
    Button btnSave;
    EditText edtName, edtProfilePassword;
    TextView txtWelcome, txtChangePhoto;
    CoordinatorLayout activity_profile;
    StorageReference mStorage;
    private ProgressDialog pProgressDiag;


    private static final int GALLERY_INTENT = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();


        imgProfile = (ImageView)findViewById(R.id.imgProfile);
        btnSave = (Button)findViewById(R.id.btnSave);
      //btnLogOut = (Button)findViewById(R.id.btnLogOut);
        edtProfilePassword = (EditText)findViewById(R.id.profile_password) ;
        edtName = (EditText)findViewById(R.id.profile_name) ;
        txtWelcome = (TextView)findViewById(R.id.txtWelcome);
        activity_profile = (CoordinatorLayout)findViewById(R.id.activity_profile);
        txtChangePhoto = (TextView)findViewById(R.id.txtChangePhoto);
        pProgressDiag = new ProgressDialog(this);

        btnSave.setOnClickListener(this);
       //btnLogOut.setOnClickListener(this);
        txtChangePhoto.setOnClickListener(this);

        if(auth.getCurrentUser() != null)
        {
            if(!Objects.equals(auth.getCurrentUser().getDisplayName(), "")) {
                txtWelcome.setText("Welcome , " + auth.getCurrentUser().getDisplayName());
            } else {
                txtWelcome.setText("Welcome , " + auth.getCurrentUser().getEmail());
            }
        }

        txtChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Photo
                Intent picfoto = new Intent(Intent.ACTION_PICK);

                picfoto.setType("image/*");

                startActivityForResult(picfoto,GALLERY_INTENT);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        StorageReference load = mStorage.child("users").child(auth.getCurrentUser().getUid()).child("Avatar").child("avatar.png");

        load.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(Profile.this).load(uri.toString()).into(imgProfile);
            }
        });


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
        if (view.getId() == R.id.btnSave) {
            changePassword(edtProfilePassword.getText().toString());
            changeName(edtName.getText().toString());
        }
    }

     private void changeName(final String mName) {
        final FirebaseUser user =  auth.getCurrentUser();
        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(mName)
                .build();
        assert user != null;

       user.updateProfile(userProfileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Profile.this, "Success. Username: " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(Profile.this,Profile.class));
                }

                else {
                    throw new Error(task.getException().getMessage(),task.getException().getCause());
                }
            }
        });
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

    //Profile Photo UPLOAD
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){

            pProgressDiag.setMessage("Uploading...");
            pProgressDiag.show();

            Uri uri = data.getData();

            StorageReference filepath = mStorage.child("users").child(auth.getCurrentUser().getUid()).child("Avatar").child("avatar.png");

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){


                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Profile.this, "Upload Done....", Toast.LENGTH_LONG).show();
                    pProgressDiag.dismiss();
                    finish();
                    startActivity(new Intent(Profile.this,Profile.class));
                    //TODO Picasso/Glide
                    /*mStorage.child("users/" + auth.getCurrentUser().getUid() + "/Avatar/" + "avatar.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                        }
                    }))*/
                    /*StorageReference load = mStorage.child("users").child(auth.getCurrentUser().getUid()).child("Avatar").child("avatar.png");

                    ImageView Avatar = imgProfile;

                    Glide.with(Profile.this).load(load).into(Avatar);*/

                }
            });
        }
    }

}


