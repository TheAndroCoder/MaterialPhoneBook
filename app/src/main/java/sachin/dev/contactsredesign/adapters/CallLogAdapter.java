package sachin.dev.contactsredesign.adapters;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import sachin.dev.contactsredesign.R;
import sachin.dev.contactsredesign.models.CallLogModel;

public class CallLogAdapter extends RecyclerView.Adapter<CallLogAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<CallLogModel> callLogs;

    public CallLogAdapter(Context context, ArrayList<CallLogModel> callLogs){
        this.context=context;
        this.callLogs=callLogs;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.call_log_recycler_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Date d=new Date(Long.parseLong(callLogs.get(position).getDate()));
        //Log.d("sachin",sdf.format(d));
        holder.date.setText(setDate(d));
        if(callLogs.get(position).getName()!=null)
            holder.name.setText(callLogs.get(position).getName());
        else
            holder.name.setText("UNKNOWN");
        holder.number.setText(callLogs.get(position).getNumber());
        holder.simNumber.setText(callLogs.get(position).getSimNumber()==1?"(SIM 1)":"(SIM 2)");
        if(callLogs.get(position).getDirection().equals("OUTGOING")){
            holder.direction.setImageResource(R.drawable.ic_call_made_black_24dp);
        }else if(callLogs.get(position).getDirection().equals("INCOMING")){
            holder.direction.setImageResource(R.drawable.ic_call_recv_black_24dp);
        }
    }
    private String setDate(Date d){
        String ret="";
        SimpleDateFormat sdf=new SimpleDateFormat("hh:mm a");
        SimpleDateFormat sdf2=new SimpleDateFormat("dd/MM/yy");
        Date nowdate=new Date();
        if(nowdate.getDate()==d.getDate() && nowdate.getMonth()==d.getMonth() && nowdate.getYear()==d.getYear()){
            ret=sdf.format(d);
        }else if(nowdate.getDate()==d.getDate()-1 && nowdate.getMonth()==d.getMonth() && nowdate.getYear()==d.getYear()){
            ret="Yesterday";
        }else{
            ret=sdf2.format(d);
        }
        return ret;
    }

    @Override
    public int getItemCount() {
        return callLogs.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,number,date,simNumber;
        ImageView direction;
        public MyViewHolder(View view){
            super(view);
            name=view.findViewById(R.id.name);
            number=view.findViewById(R.id.number);
            date=view.findViewById(R.id.date);
            direction=view.findViewById(R.id.direction);
            simNumber=view.findViewById(R.id.simNumber);
        }
    }
}
