package com.potenza_pvt_ltd.AAPS;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Transporter extends ListActivity implements AdapterView.OnItemClickListener {

    protected EditText et1,et2,et3,et4,et5,et6,et7;
    private FirebaseAuth mAuth;
    DatabaseReference reference;
    final ArrayList<TransporterDetails> list = new ArrayList<TransporterDetails>();
    int count = 0;
    int index=0;
    private MyRecyclerViewAdapter mAdapter;
    ArrayAdapter <String>adapter;
    ListView l;
    ArrayList<String> name=new ArrayList<String>();
    ArrayList key=new ArrayList<>();
    ArrayList<TransporterDetails> dataset;
    String uid=null;
    ProgressBar pb;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transporter);
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        Log.d("In activity","transporter.java");
        et1=(EditText)findViewById(R.id.editText8);
        et2=(EditText)findViewById(R.id.editText9);
        et3=(EditText)findViewById(R.id.editText10);
        et4=(EditText)findViewById(R.id.editText11);
        et5=(EditText)findViewById(R.id.editText12);
        et6=(EditText)findViewById(R.id.editText13);
        et7=(EditText)findViewById(R.id.editText14);
        pb=(ProgressBar)findViewById(R.id.progressBar);
        linearLayout=(LinearLayout)findViewById(R.id.linear_layout);
        l=getListView();
        index = 0;
        count = 0;
        Query queryRef = reference.child("users").child("Transporter_Details");
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                TransporterDetails post = snapshot.getValue(TransporterDetails.class);
                Log.d("In activity",snapshot.getKey());

                post.setKey(snapshot.getKey());
                Log.d("1", post.getKey());
                Log.d("4", post.getSms_no());
                Log.d("5", post.getContact_person());
                Log.d("6", post.getMobile_no());
                Log.d("7", post.getNo_of_vhcl());
                Log.d("8", post.getVehicle_no());
                Log.d("2", post.getName());
                Log.d("3", post.getAddress());
                TransporterDetails obj = new TransporterDetails(post.getKey(), post.getName(), post.getAddress(), post.getSms_no(), post.getContact_person(), post.getMobile_no(), post.getNo_of_vhcl(), post.getVehicle_no());
                Log.d("In activity","bug3");

                list.add(index, obj);
                name.add(index, post.getName());
                key.add(index, post.getKey());
                index++;
                adapter = new ArrayAdapter<String>(getBaseContext(),R.layout.listview,name);
                l.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                pb.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                adapter.clear();
                adapter.notifyDataSetChanged();
                fetchdata();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                adapter.clear();
                adapter.notifyDataSetChanged();
                fetchdata();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });
        l.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        l.setMultiChoiceModeListener(new ModeCallback());
        l.setOnItemClickListener(this);
    }
    public void save(View v){
        if(uid==null) {
            final String name = et1.getText().toString();
            final String address = et2.getText().toString();
            final String sms = et3.getText().toString();
            final String contact = et4.getText().toString();
            final String mobile = et5.getText().toString();
            final String no_of_vhcl = et6.getText().toString();
            final String vehicle_no = et7.getText().toString();
            if(vehicle_no.contains(",")){
                AlertDialog.Builder builder = new AlertDialog.Builder(Transporter.this);
                builder.setMessage("Please Enter Vehicle Number without special characters")
                        .setTitle(R.string.title_msg_dailog_box)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else {
                Map<String, Object> value = new HashMap<String, Object>();
                value.put("name", name);
                value.put("address", address);
                value.put("sms_no", sms);
                value.put("contact_person", contact);
                value.put("mobile_no", mobile);
                value.put("no_of_vhcl", no_of_vhcl);
                value.put("vehicle_no", vehicle_no);
                reference.child("users").child("Transporter_Details").push().setValue(value);
            }
        }
        else{
            final String name = et1.getText().toString();
            final String address = et2.getText().toString();
            final String sms = et3.getText().toString();
            final String contact = et4.getText().toString();
            final String mobile = et5.getText().toString();
            final String no_of_vhcl = et6.getText().toString();
            Map<String, Object> value = new HashMap<String, Object>();
            value.put("name", name);
            value.put("address", address);
            value.put("sms_no", sms);
            value.put("contact_person", contact);
            value.put("mobile_no", mobile);
            value.put("no_of_vhcl", no_of_vhcl);
            reference.child("users").child("Transporter_Details").child(uid).updateChildren(value);

        }
        et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");
        et5.setText("");
        et6.setText("");
        et7.setText("");
    }

    public void add(View v){
        String sms=et3.getText().toString();
        final String vehicle_no=et7.getText().toString();
        if(sms==null){
            AlertDialog.Builder builder = new AlertDialog.Builder(Transporter.this);
            builder.setMessage("Please Enter SMS Number")
                    .setTitle(R.string.title_msg_dailog_box)
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if(vehicle_no.contains(",")){
            if(vehicle_no.contains(",")){
                AlertDialog.Builder builder = new AlertDialog.Builder(Transporter.this);
                builder.setMessage("Please Enter Vehicle Number without special characters")
                        .setTitle(R.string.title_msg_dailog_box)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
        else {
            Query queryRef = reference.child("users").child("Transporter_Details").orderByChild("sms_no").equalTo(sms);
            queryRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                    TransporterDetails post = snapshot.getValue(TransporterDetails.class);

                    String vhl_no;
                    if(post.getVehicle_no()==""){
                        vhl_no=vehicle_no;
                    }
                    else {
                        vhl_no = post.getVehicle_no() + "," + vehicle_no;
                    }
                    Map<String, Object> value = new HashMap<String, Object>();
                    value.put("vehicle_no", vhl_no);
                    reference.child("users").child("Transporter_Details").child(snapshot.getKey()).updateChildren(value);

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError firebaseError) {
                }
            });
        }
    et7.setText("");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dataset=list;
        et1.setText(dataset.get(position).getName());
        et2.setText(dataset.get(position).getAddress());
        et3.setText(dataset.get(position).getSms_no());
        et4.setText(dataset.get(position).getContact_person());
        et5.setText(dataset.get(position).getMobile_no());
        et6.setText(dataset.get(position).getNo_of_vhcl());
        uid=dataset.get(position).getKey();
    }


    private void fetchdata() {
        list.clear();
        name.clear();
        key.clear();
        index = 0;
        count = 0;
        Query queryRef = reference.child("users").child("Transporter_Details").orderByChild("sms_no");
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                TransporterDetails post = snapshot.getValue(TransporterDetails.class);
                post.setKey(snapshot.getKey());
                TransporterDetails obj = new TransporterDetails(post.getKey(), post.getName(), post.getAddress(), post.getSms_no(), post.getContact_person(), post.getMobile_no(), post.getNo_of_vhcl(), post.getVehicle_no());
                list.add(index, obj);
                name.add(index, post.getName());
                key.add(index, post.getKey());
                index++;
                adapter = new ArrayAdapter<String>(getBaseContext(),R.layout.listview,name);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                adapter.clear();
                adapter.notifyDataSetChanged();
                fetchdata();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                adapter.clear();
                adapter.notifyDataSetChanged();
                fetchdata();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed()
    {
        finish();   //finishes the current activity and doesnt save in stock
        Intent i = new Intent(Transporter.this, Masters.class);
        startActivity(i);
    }
    private class ModeCallback implements ListView.MultiChoiceModeListener {

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.list_select_menu, menu);
            mode.setTitle("Select Items");
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.del:
                    final SparseBooleanArray checked=l.getCheckedItemPositions();
                    for (int i = 0; i < l.getAdapter().getCount(); i++) {
                        if (checked.get(i)) {
                            reference.child("users").child("Transporter_Details").child(String.valueOf(key.get(i))).removeValue();
                        }
                    }
                    mode.finish();
                    break;
                default:
                    break;
            }
            return true;
        }

        public void onDestroyActionMode(ActionMode mode) {
        }

        public void onItemCheckedStateChanged(ActionMode mode,
                                              int position, long id, boolean checked) {
            final int checkedCount = getListView().getCheckedItemCount();
            switch (checkedCount) {
                case 0:
                    mode.setSubtitle(null);
                    break;
                case 1:
                    mode.setSubtitle("One item selected");
                    break;
                default:
                    mode.setSubtitle("" + checkedCount + " items selected");
                    break;
            }
        }

    }
}
