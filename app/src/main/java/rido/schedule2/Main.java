package rido.schedule2;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Main extends AppCompatActivity {

    Button btnSignIn,btnSignUp;
    TextView txtSlogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn = (Button)findViewById(R.id.btnSignIn);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);

        txtSlogan = (TextView)findViewById(R.id.txtSlogan);
        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/tahoma.ttf");
        txtSlogan.setTypeface(face);

        btnSignUp.setOnClickListener(new View.OnClickListener(){
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
        });






    }
}
