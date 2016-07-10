package com.example.kushagr_jolly.potenza_pvt_ltd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TypeofOperator extends AppCompatActivity {

    private Button receiptbutton,collectionbutton,shiftclose_button;
    protected Button calculateButton;
    Firebase mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_typeof_operator);
        final String postid=getIntent().getStringExtra("UniqueID");
        receiptbutton=(Button)findViewById(R.id.button_mainoperator);
        collectionbutton=(Button)findViewById(R.id.button_collection);
        calculateButton=(Button)findViewById(R.id.button_calculate_fare);
        shiftclose_button=(Button)findViewById(R.id.button_shiftclose);
        receiptbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TypeofOperator.this,SubmitActivity.class);
                intent.putExtra("UniqueID",postid);
                startActivity(intent);

            }
        });
        mRef=new Firebase(Constants.FIREBASE_URL);
        collectionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TypeofOperator.this,Collections.class);
                intent.putExtra("UniqueID",postid);
                startActivity(intent);
            }
        });
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TypeofOperator.this, CalculateFare.class);
                intent.putExtra("UniqueID",postid);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        shiftclose_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TypeofOperator.this, ShiftClose.class);
                intent.putExtra("UniqueID",postid);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            /*  Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                currenttime = System.currentTimeMillis();
                timetocharge=currenttime-globalmillis;
                calendar.setTimeInMillis(timetocharge);
                String t = sdf.format(calendar.getTime());
                if(timetocharge>=18*60*1000 && timetocharge<24*60*1000){
                    cost+=75;
                }
                else if(timetocharge>=24*60*1000 && timetocharge<48*60*1000){
                    cost+=150;
                }
                Map<String, Object> graceNickname = new HashMap<>();
                graceNickname.put("Cost", cost);
                mRef.child("users").child("data").child(postid).updateChildren(graceNickname);
            }*/
        });
    }

}
