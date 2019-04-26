package edmt.dev.androidgridlayout;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edmt.dev.androidgridlayout.Adaptors.UserListAdapter;
import edmt.dev.androidgridlayout.model.UserListModel;

public class all_records extends AppCompatActivity {

    private ListView listViewRestaurants;
    private ArrayList<UserListModel> userList;
    private ArrayAdapter<UserListModel> userAdapter;
    DatabaseReference userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_records);

        //adapter
        userList = new ArrayList<>();
        userAdapter = new UserListAdapter(this, userList);
        this.listViewRestaurants = findViewById( R.id.records_list );
        this.listViewRestaurants.setAdapter(userAdapter);
        userData = FirebaseDatabase.getInstance().getReference("users");

        userData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot restSnapshot : dataSnapshot.getChildren()) {

                    //set variables
                    final String status = restSnapshot.child("status").getValue().toString();

                    if (status.equals("student")){
                        final String student_name = restSnapshot.getKey();
                        final String last_seen = restSnapshot.child("Date").getValue().toString();
                        userList.add(new UserListModel(student_name, last_seen));
                    }
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
