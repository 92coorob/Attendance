package edmt.dev.androidgridlayout;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
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

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class create_record extends AppCompatActivity implements View.OnClickListener{

    EditText add_pass, add_user;
    Button add, back;
    Spinner status;
    String username, pass, role;
    String AES = "AES";
    String encrypt_key = "testencryptionkey";
    private String enc_password;
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

                try {
                    enc_password = encrypt(pass);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if ((!dataSnapshot.child(username).exists())
                                && (!username.isEmpty()) && (!pass.isEmpty())) {
                            myRef.child(username).child("password").setValue(enc_password);
                            myRef.child(username).child("status").setValue(role);
                            myRef.child(username).child("Date").setValue("");

                            add_user.setText("");
                            add_pass.setText("");

                            Toast.makeText(create_record.this, "User added", Toast.LENGTH_SHORT).show();
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

    private String encrypt(String password) throws Exception{

        SecretKeySpec key = generateKey(encrypt_key);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] encValue = c.doFinal(password.getBytes());
        String encryptedPass = Base64.encodeToString(encValue, Base64.DEFAULT);
        return encryptedPass;
    }

    //generate key
    private SecretKeySpec generateKey(String encrypt) throws Exception{
        final MessageDigest pwDigest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = encrypt.getBytes("UTF-8");
        pwDigest.update(bytes, 0, bytes.length);
        byte[] key = pwDigest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
    }
}
