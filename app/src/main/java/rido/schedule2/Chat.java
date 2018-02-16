package rido.schedule2;

import android.support.design.widget.FloatingActionButton;
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
import com.google.firebase.database.FirebaseDatabase;

import rido.schedule2.Model.ChatMessage;

public class Chat extends AppCompatActivity {

    ListView list_of_message;
    private FirebaseListAdapter<ChatMessage> adapter;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

                // User is already signed in. Therefore, display
                // a welcome Toast
                Toast.makeText(this,
                        "Welcome " + FirebaseAuth.getInstance()
                                .getCurrentUser()
                                .getDisplayName(),
                        Toast.LENGTH_LONG)
                        .show();

                // Load chat room contents
                displayChatMessages();

            list_of_message = findViewById(R.id.list_of_messages);

            list_of_message.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
            list_of_message.setStackFromBottom(true);


            FloatingActionButton fab =
                    findViewById(R.id.send);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText input = findViewById(R.id.input);

                    // Read the input field and push a new instance
                    // of ChatMessage to the Firebase database
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .push()
                            .setValue(new ChatMessage(input.getText().toString(),
                                    FirebaseAuth.getInstance()
                                            .getCurrentUser()
                                            .getDisplayName())
                            );

                    // Clear the input
                    input.setText("");
                }
            });
    }
    private void displayChatMessages()
    {
        ListView listOfMessages = findViewById(R.id.list_of_messages);

        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.massage, FirebaseDatabase.getInstance().getReference()) {
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
