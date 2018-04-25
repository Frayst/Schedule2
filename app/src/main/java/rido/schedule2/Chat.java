package rido.schedule2;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import rido.schedule2.Model.ChatMessage;

public class Chat extends AppCompatActivity {

    ListView list_of_message;
    private FirebaseListAdapter<ChatMessage> adapter;
    private static final int PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE = 1;
    boolean networkStateGranted;

    FirebaseDatabase firedb;
    DatabaseReference firedbref;
    StorageReference firestoragedb;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getChatPermissions();

        //TODO: Verbindung zu DB einrichten
        firedb = FirebaseDatabase.getInstance();
        firedbref = firedb.getReference("Chats");
        firestoragedb = FirebaseStorage.getInstance().getReference();

                // User is already signed in. Therefore, display
                // a welcome Toast
                Toast.makeText(this,
                        "Welcome " + FirebaseAuth.getInstance()
                                .getCurrentUser()
                                .getDisplayName(),
                        Toast.LENGTH_LONG)
                        .show();

            list_of_message = findViewById(R.id.list_of_messages);

            list_of_message.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
            list_of_message.setStackFromBottom(true);

            FloatingActionButton fab_Button =
                    findViewById(R.id.senden);

            fab_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText input = findViewById(R.id.input);

                    // Read the input field and push a new instance
                    // of ChatMessage to the Firebase database
                    firedbref
                            .push()
                            .setValue(new ChatMessage(input.getText().toString(),
                                    FirebaseAuth.getInstance()
                                            .getCurrentUser()
                                            .getDisplayName())
                            );
                    // Clear the input
                    input.setText("");
                    // Load chat room contents
                }
            });

    displayChatMessages();

    }
    private void getChatPermissions()
    {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_NETWORK_STATE)
                == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                    PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        networkStateGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    networkStateGranted = true;
                }
            }
        }
    }

    private void displayChatMessages()
    {
        ListView listOfMessages = findViewById(R.id.list_of_messages);

        adapter = new FirebaseListAdapter<ChatMessage>(Chat.this, ChatMessage.class,
                R.layout.massage, FirebaseDatabase.getInstance().getReference("Chats")) {
                    @Override
                    protected void populateView(View v, ChatMessage model, int position) {
                        // Get references to the views of message.xml
                        TextView messageText = v.findViewById(R.id.message_text);
                        TextView messageUser = v.findViewById(R.id.message_user);
                        TextView messageTime = v.findViewById(R.id.message_time);

                        // Set their text
                        messageText.setText(model.getMessageText());
                        messageUser.setText(model.getMessageUser());

                        // Format the date before showing it
                        messageTime.setText(DateFormat.format("HH:mm:ss",
                                model.getMessageTime()));
                    }
        };

        listOfMessages.setAdapter(adapter);
    }
}
