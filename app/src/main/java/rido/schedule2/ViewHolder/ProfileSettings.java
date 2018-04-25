package rido.schedule2.ViewHolder;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import rido.schedule2.Profile;
import rido.schedule2.R;

public class ProfileSettings extends AppCompatActivity implements View.OnClickListener {

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilsett);

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


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        StorageReference load = mStorage.child("users").child(auth.getCurrentUser().getUid()).child("Avatar").child("avatar.png");

        load.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ProfileSettings.this).load(uri.toString()).into(imgProfile);
            }
        });

    }

    @Override
    public void onClick(View v) {

    }
}
