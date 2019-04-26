package edmt.dev.androidgridlayout.Adaptors;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import edmt.dev.androidgridlayout.R;
import edmt.dev.androidgridlayout.model.UserListModel;

public class UserListAdapter extends ArrayAdapter<UserListModel> {

    private Context context;
    private List<UserListModel> users;

    public UserListAdapter(Context context, List<UserListModel> users){
        super(context, R.layout.user_list_template, users);
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.user_list_template, parent, false);

        /*
        ImageView imageViewPhoto = view.findViewById(R.id.restaurant_image);
        String url = users.get(position).getPic();
        Picasso.get()
                .load(url)
                .into(imageViewPhoto);
        */
        TextView textViewname = view.findViewById(R.id.student_name);
        textViewname.setText(users.get(position).getName());

        TextView textViewdate = view.findViewById(R.id.student_date);
        textViewdate.setText("Last Seen: " + users.get(position).getDate());
        return view;
    }
}