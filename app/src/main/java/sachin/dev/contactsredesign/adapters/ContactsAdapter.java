package sachin.dev.contactsredesign.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import sachin.dev.contactsredesign.R;
import sachin.dev.contactsredesign.models.ContactModel;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<ContactModel> contacts;

    public ContactsAdapter(Context context, ArrayList<ContactModel> contacts){
        this.contacts=contacts;
        this.context=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.contacts_recycler_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(contacts.get(position).getName());
        holder.first_letter.setText(contacts.get(position).getName().toUpperCase().charAt(0)+"");
        holder.ff.setBackgroundTintList(ColorStateList.valueOf(generateAutoColor()));
    }

    private int generateAutoColor() {
        int[] colorsArray={Color.RED, Color.BLUE,Color.GREEN,Color.CYAN,Color.GRAY,Color.rgb(113,126,250),Color.rgb(150,193,128),Color.rgb(246,136,10),Color.rgb(247,85,73),Color.rgb(10,144,32)};
        int random =new Random().nextInt(colorsArray.length);
        Log.d("sachin","Random Generated "+random);
        return colorsArray[random];
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,first_letter;
        RelativeLayout ff;
        public MyViewHolder(View view){
            super(view);
            name=view.findViewById(R.id.name);
            first_letter=view.findViewById(R.id.first_letter);
            ff=view.findViewById(R.id.ff);
        }
    }
}
