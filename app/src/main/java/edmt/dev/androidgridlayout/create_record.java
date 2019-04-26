package edmt.dev.androidgridlayout;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class create_record extends AppCompatActivity implements View.OnClickListener{

    EditText add_pass, add_user;
    Button add, back;
    Spinner status;
    String username, pass, role;
    private DatabaseReference usersRef, myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_record);

        add_pass = findViewById(R.id.add_pass);
        add_user = findViewById(R.id.add_user);
        status = findViewById(R.id.status);
        add = findViewById(R.id.add_btn);
        back = findViewById(R.id.back_btn);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");


        add.setOnClickListener(this);
        back.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_btn:
                username = add_user.getText().toString();
                pass = add_pass.getText().toString();
                role = status.getSelectedItem().toString();


                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if ((!dataSnapshot.child(username).exists())
                                && (!username.isEmpty()) && (!pass.isEmpty())) {
                            myRef.child(username).child("password").setValue(pass);
                            myRef.child(username).child("status").setValue(role);
                            myRef.child(username).child("Date").setValue("");

                            add_user.setText("");
                            add_pass.setText("");
                        }

                        else if((username.isEmpty()) ||(pass.isEmpty())){
                            Toast.makeText(create_record.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(create_record.this, "User already exists", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });



                break;
            case R.id.back_btn:
                Intent back = new Intent(create_record.this, admin_page.class);
                startActivity(back);
                break;
        }
    }
}
