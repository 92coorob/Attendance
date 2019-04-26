package edmt.dev.androidgridlayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class login_page extends AppCompatActivity implements View.OnClickListener{

    private Button login;
    private TextView forgot_pw;
    private EditText edtUser, edtPw;

    FirebaseDatabase database;
    DatabaseReference dbUsers;

    SharedPreferences username_pref;

    String AES = "AES";
    String encrypt_key = "testencryptionkey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        username_pref = getSharedPreferences("students", MODE_PRIVATE);

        login = (Button) findViewById(R.id.login_btn);
        login.setOnClickListener(this);

        forgot_pw = (TextView) findViewById(R.id.forgot_pw);
        forgot_pw.setOnClickListener(this);

        edtUser = (EditText) findViewById(R.id.username);
        edtPw = (EditText) findViewById(R.id.password);

        database = FirebaseDatabase.getInstance();
        dbUsers = database.getReference("users");

        edtPw.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN)
                        && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    signIn(edtUser.getText().toString(),
                            edtPw.getText().toString());
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.login_btn:
                signIn(edtUser.getText().toString(),
                        edtPw.getText().toString());
                break;
            case R.id.forgot_pw:
                break;
        }
    }

    private void signIn(final String username, final String password) {
        dbUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!username.isEmpty()){
                    if(dataSnapshot.child(username).exists()){
                        String check_pass=  dataSnapshot.child(username).child("password").getValue().toString();
                        try {
                            if(password.equals(check_pass)){
                                Toast.makeText(login_page.this, "Successfully logged in!", Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor edit_name = username_pref.edit();
                                    edit_name.putString("username", username);
                                    edit_name.commit();
                                if (dataSnapshot.child(username).child("status").getValue().toString().equals("admin")){
                                    Intent admin_log = new Intent(login_page.this, admin_page.class);
                                    startActivity(admin_log);
                                }
                                else {
                                    Intent login = new Intent(login_page.this, MainActivity.class);
                                    startActivity(login);
                                }
                            }
                            else{
                                Toast.makeText(login_page.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        Toast.makeText(login_page.this,"Username does not exists", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(login_page.this,"Please enter a username", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //write code here
            }
        });
    }

    //decrypt
    private String decrypt(String password) throws Exception{
        SecretKeySpec key = generateKey(encrypt_key);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE,key);
        byte[] decValue = Base64.decode(password, Base64.DEFAULT);
        byte[] decPass = c.doFinal(decValue);
        String decryptedPass = new String(decPass);
        return decryptedPass;
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
