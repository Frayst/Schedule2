package rido.schedule2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import rido.schedule2.Common.Common;
import rido.schedule2.Model.User;

public class signin extends AppCompatActivity {
    EditText edtLogin,edtPassword;
    Button btnSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        edtPassword = (MaterialEditText)findViewById(R.id.edtPassword);
        edtLogin = (MaterialEditText)findViewById(R.id.edtLogin);

        btnSignIn = (Button)findViewById(R.id.btnSignIn);

        //Init Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
           public void onClick(View view) {

               final ProgressDialog mDialog = new ProgressDialog(signin.this);
               mDialog.setMessage("Please waiting...");
               mDialog.show();

               table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {
                        //Check if user not exist in database
                        if(dataSnapshot.child(edtLogin.getText().toString()).exists()) {

                            mDialog.dismiss();
                            //Get User information
                            User user = dataSnapshot.child(edtLogin.getText().toString()).getValue(User.class);
                            if (user.getPassword().equals(edtPassword.getText().toString())) {
                                Toast.makeText(signin.this, "Sign in successfully!", Toast.LENGTH_SHORT).show();
                                Intent homeIntent = new Intent (signin.this,HomePage.class);
                                Common.currentUser = user;
                                startActivity(homeIntent);
                                finish();
                            } else {
                                mDialog.dismiss();
                                Toast.makeText(signin.this, "Wrong Password!!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            mDialog.dismiss();
                            Toast.makeText(signin.this, "User not exists in Database", Toast.LENGTH_SHORT).show();
                        }
                    }

                     @Override
                     public void onCancelled(DatabaseError databaseError) {

                     }
                 });
           }
        });
    }
}
