package suryajeet945.com.nevainternapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by SuryaJeet on 27-02-2018.
 */

public class MyListViewAdapter extends BaseAdapter {

    List<MainActivity.Person> list;
    Context context;
    public MyListViewAdapter(Context context, List<MainActivity.Person> list1){
        this.context=context;
        list=list1;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        if(row==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.layout_list_view, parent,false);
        }
        ImageView imageView=(ImageView)row.findViewById(R.id.imageViewImage);
        TextView textViewName=(TextView)row.findViewById(R.id.textViewName);
        TextView textViewSkills=(TextView)row.findViewById(R.id.textViewSkills);

        imageView.setImageBitmap(list.get(position).getImage());
        textViewName.setText(list.get(position).getName());
        textViewSkills.setText(list.get(position).getSkills());


        return row;
    }
}