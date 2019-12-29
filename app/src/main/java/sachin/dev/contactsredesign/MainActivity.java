package sachin.dev.contactsredesign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import sachin.dev.contactsredesign.adapters.CallLogAdapter;
import sachin.dev.contactsredesign.animations.ExpandHeightAnimation;
import sachin.dev.contactsredesign.models.CallLogModel;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.CallLog;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.dialer_layout)
    RelativeLayout dialerLayout;
    @BindView(R.id.dialpad_btn)
    ImageButton dialpadBtn;
    @BindView(R.id.contacts_tab)
    TextView contactsTab;
    @BindView(R.id.call_log_recycler)
    RecyclerView callLogRecycler;
    private ArrayList<CallLogModel> callLogs;
    @BindView(R.id.dialled_num_layout)
    RelativeLayout dialledNumLayout;
    @BindView(R.id.btn1)
    LinearLayout btn1;
    @BindView(R.id.btn2)
    LinearLayout btn2;
    @BindView(R.id.btn3)
    LinearLayout btn3;
    @BindView(R.id.btn4)
    LinearLayout btn4;
    @BindView(R.id.btn5)
    LinearLayout btn5;
    @BindView(R.id.btn6)
    LinearLayout btn6;
    @BindView(R.id.btn7)
    LinearLayout btn7;
    @BindView(R.id.btn8)
    LinearLayout btn8;
    @BindView(R.id.btn9)
    LinearLayout btn9;
    @BindView(R.id.btnstar)
    LinearLayout btnStar;
    @BindView(R.id.btn0)
    LinearLayout btn0;
    @BindView(R.id.btnhash)
    LinearLayout btnHash;

    @BindView(R.id.dialled_num)
    EditText dialled_num;
    @BindView(R.id.clear)
    ImageView clear;

    private RenderScript rs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setOnClickListeners();
        callLogs=new ArrayList<>();
        SubscriptionManager manager=(SubscriptionManager) getApplicationContext().getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},1002);
        }
        SubscriptionInfo sim1=null,sim2=null;
         try{
              sim1=manager.getActiveSubscriptionInfoForSimSlotIndex(0);
              sim2=manager.getActiveSubscriptionInfoForSimSlotIndex(1);
         }catch(Exception e){
             e.printStackTrace();
         }
        //Log.d("sachin","Subs ID of sim1 ="+sim1.getIccId());
        //Log.d("sachin","Subs ID of sim2 ="+sim2.getIccId());
        String sim1IccId=sim1.getIccId();
        String sim2IccId=sim2.getIccId();



        //CallLogFetcher fetcher=new CallLogFetcher(MainActivity.this);
        //fetcher.start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor=managedQuery(CallLog.Calls.CONTENT_URI,null,null,null,CallLog.Calls.DATE+" DESC");
                while(cursor.moveToNext()){
                    CallLogModel model=new CallLogModel();
                    model.setDate(cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE)));
                    model.setNumber(cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)));
                    model.setType(cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE)));
                    model.setDuration(cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION)));
                    switch(Integer.parseInt(model.getType())){
                        case CallLog.Calls.INCOMING_TYPE:model.setDirection("INCOMING");break;
                        case CallLog.Calls.OUTGOING_TYPE:model.setDirection("OUTGOING");break;
                        default:model.setDirection("MISSED");
                    }
                    model.setName(cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)));
                    if(cursor.getString(cursor.getColumnIndex(CallLog.Calls.PHONE_ACCOUNT_ID)).equals(sim1IccId)){
                        model.setSimNumber(1);
                    }else{
                        model.setSimNumber(2);
                    }
                    callLogs.add(model);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CallLogAdapter adapter=new CallLogAdapter(MainActivity.this,callLogs);
                        callLogRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        callLogRecycler.setAdapter(adapter);
                    }
                });

            }
        }).start();


    }


    private void setOnClickListeners(){
        final GestureDetector gestureDetector=new GestureDetector(new GestureListener(MainActivity.this));
        dialerLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);
                return true;
            }
        });
        dialpadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialpadBtn.setVisibility(View.GONE);
                dialerLayout.setVisibility(View.VISIBLE);
                dialerLayout.setAlpha(0);
                dialerLayout.animate().setDuration(200).alpha(1).translationY(0).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                }).start();
            }
        });
        contactsTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ContactsActivity.class));
                overridePendingTransition(R.anim.from_right_in,R.anim.from_left_out);
                finish();
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialled_num.getText().toString().equals(""))expandDialerLayout();
                dialledNumLayout.setVisibility(View.VISIBLE);
                dialled_num.setText(dialled_num.getText()+"1");
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialled_num.getText().toString().equals(""))expandDialerLayout();
                dialledNumLayout.setVisibility(View.VISIBLE);
                dialled_num.setText(dialled_num.getText()+"2");
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialled_num.getText().toString().equals(""))expandDialerLayout();
                dialledNumLayout.setVisibility(View.VISIBLE);
                dialled_num.setText(dialled_num.getText()+"3");
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialled_num.getText().toString().equals(""))expandDialerLayout();
                dialledNumLayout.setVisibility(View.VISIBLE);
                dialled_num.setText(dialled_num.getText()+"4");
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialled_num.getText().toString().equals(""))expandDialerLayout();
                dialledNumLayout.setVisibility(View.VISIBLE);
                dialled_num.setText(dialled_num.getText()+"5");
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialled_num.getText().toString().equals(""))expandDialerLayout();
                dialledNumLayout.setVisibility(View.VISIBLE);
                dialled_num.setText(dialled_num.getText()+"6");
            }
        });
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialled_num.getText().toString().equals(""))expandDialerLayout();
                dialledNumLayout.setVisibility(View.VISIBLE);
                dialled_num.setText(dialled_num.getText()+"7");
            }
        });
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialled_num.getText().toString().equals(""))expandDialerLayout();
                dialledNumLayout.setVisibility(View.VISIBLE);
                dialled_num.setText(dialled_num.getText()+"8");
            }
        });
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialled_num.getText().toString().equals(""))expandDialerLayout();
                dialledNumLayout.setVisibility(View.VISIBLE);
                dialled_num.setText(dialled_num.getText()+"9");
            }
        });
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialled_num.getText().toString().equals(""))expandDialerLayout();
                dialledNumLayout.setVisibility(View.VISIBLE);
                dialled_num.setText(dialled_num.getText()+"0");
            }
        });
        btnStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialled_num.getText().toString().equals(""))expandDialerLayout();
                dialledNumLayout.setVisibility(View.VISIBLE);
                dialled_num.setText(dialled_num.getText()+"*");
            }
        });
        btnHash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialled_num.getText().toString().equals(""))expandDialerLayout();
                dialledNumLayout.setVisibility(View.VISIBLE);
                dialled_num.setText(dialled_num.getText()+"#");
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialled_num.setText(dialled_num.getText().toString().substring(0,dialled_num.getText().toString().length()-1));
                if(dialled_num.getText().toString().equals("")){
                    contractDialerLayout();
                    dialledNumLayout.setVisibility(View.GONE);
                }
            }
        });
        dialled_num.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }

    private void contractDialerLayout() {
        ExpandHeightAnimation animation=new ExpandHeightAnimation(dialerLayout,-120,dialerLayout.getLayoutParams().height);
        animation.setDuration(200);
        dialerLayout.startAnimation(animation);
    }

    private void expandDialerLayout() {
        ExpandHeightAnimation animation=new ExpandHeightAnimation(dialerLayout,120,dialerLayout.getLayoutParams().height);
        animation.setDuration(200);
        dialerLayout.startAnimation(animation);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALL_LOG)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CALL_LOG,Manifest.permission.READ_PHONE_STATE},1001);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}
class GestureListener extends GestureDetector.SimpleOnGestureListener{
    private static final int MIN_DIST=100;
    private static final int MIN_VELOCITY=100;
    private Context context;
    @BindView(R.id.dialer_layout) RelativeLayout dialerLayout;
    @BindView(R.id.dialpad_btn)
    ImageButton dialpadBtn;
    public GestureListener(Context context){
        this.context=context;
        ButterKnife.bind(this,(Activity) context);
    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(e2.getY()-e1.getY()>=MIN_DIST && Math.abs(velocityX)>=MIN_VELOCITY){
           // Toast.makeText(context, "Swipped Down", Toast.LENGTH_SHORT).show();
            dialerLayout.animate().setDuration(200).translationY(250).alpha(0).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    dialerLayout.setVisibility(View.GONE);
                    //dialerLayout.setTranslationY(0);
                    dialpadBtn.setVisibility(View.VISIBLE);
                    dialpadBtn.setScaleX(0);
                    dialpadBtn.setScaleY(0);
                    dialpadBtn.animate().scaleX(1).scaleY(1).setDuration(200).setInterpolator(new OvershootInterpolator()).start();
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            }).start();

        }
        return false;
    }

}




/////======= UNUSED CODE FOR BLURRING BTMAPS ==========///////
/*
* new Thread(new Runnable() {
            @Override
            public void run() {
                rs=RenderScript.create(getApplicationContext());
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;
                Bitmap bmp=Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
                for (int i = 0; i <width ; i++) {
                    for (int j = 0; j <height ; j++) {
                        bmp.setPixel(i,j, Color.BLUE);
                    }
                }
                Bitmap bmp= BitmapFactory.decodeResource(getResources(),R.drawable.download11);
                 Allocation input = Allocation.createFromBitmap(rs, bmp); //use this constructor for best performance, because it uses USAGE_SHARED mode which reuses memory
                 Allocation output = Allocation.createTyped(rs, input.getType());
                 ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
                script.setRadius(25f);
                script.setInput(input);
                script.forEach(output);
                output.copyTo(bmp);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialerLayout.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    }
                });


            }
        }).start();
*
*
*
*
* */
