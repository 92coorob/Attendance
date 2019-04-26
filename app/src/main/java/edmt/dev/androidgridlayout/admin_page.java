package edmt.dev.androidgridlayout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;

public class admin_page extends AppCompatActivity {

    CardView add_record, check_record;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        add_record = findViewById(R.id.add_user);
        check_record = findViewById(R.id.attend_admin);
        logout = findViewById(R.id.button_logout);

        add_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMain = new Intent(admin_page.this, create_record.class);
                startActivity(goToMain);
            }
        });

        check_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMain = new Intent(admin_page.this, all_records.class);
                startActivity(goToMain);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(admin_page.this, login_page.class);
                startActivity(back);
            }
        });

    }

    @Override
    public void onBackPressed() {
        //do nothing on backpress
    }

}
