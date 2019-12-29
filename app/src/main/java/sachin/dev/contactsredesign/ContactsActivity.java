package sachin.dev.contactsredesign;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import sachin.dev.contactsredesign.adapters.ContactsAdapter;
import sachin.dev.contactsredesign.models.ContactModel;

public class ContactsActivity extends AppCompatActivity {
    @BindView(R.id.phone_tab)
    TextView phoneTab;
    @BindView(R.id.contacts_tab)
    TextView contactsTab;
    @BindView(R.id.fav_tab)
    TextView favTab;
    @BindView(R.id.contactsRecycler)
    RecyclerView contactsRecycler;
    @BindView(R.id.search)
    EditText search;
    private ArrayList<ContactModel> contactsList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        ButterKnife.bind(this);
        setOnClickListeners();
        contactsList=new ArrayList<>();
        fetchContacts(null);
    }
    private void fetchContacts(String args){
        contactsList.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor c=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, args!=null?ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" LIKE '%"+args+"%'":null,null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" COLLATE NOCASE ASC");
                while(c.moveToNext()){
                    ContactModel model=new ContactModel();
                    model.setId(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID)));
                    model.setName(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                    contactsList.add(model);
                }
                c.close();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ContactsAdapter adapter=new ContactsAdapter(ContactsActivity.this,contactsList);
                        contactsRecycler.setLayoutManager(new LinearLayoutManager(ContactsActivity.this, RecyclerView.VERTICAL,false));
                        contactsRecycler.setAdapter(adapter);
                    }
                });
            }
        }).start();
    }
    private void setOnClickListeners(){
        phoneTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ContactsActivity.this,MainActivity.class));
                overridePendingTransition(R.anim.from_left_in,R.anim.from_right_out);
                finish();
            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                fetchContacts(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_CONTACTS,Manifest.permission.READ_CONTACTS},10901);
        }
    }
}
