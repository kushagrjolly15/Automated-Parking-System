package com.potenza_pvt_ltd.AAPS;

import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kushagr on 14-Aug-16.
 */
public class CustomGrid extends BaseAdapter {
    int[][] tar_arr;
    private Context mContext;
    int columncount;
    int []arr;
    String type;
    int pos;


    public CustomGrid(Context context, int[][] arr,String flag) {
        mContext=context;
        this.tar_arr=arr;
        if(tar_arr!=null){
            columncount=tar_arr[0].length;
        }
        this.type=flag;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        if(tar_arr!=null)
            return tar_arr.length*tar_arr[0].length;
        else return 10;
    }

    @Override
    public Object getItem(int position) {
        return tar_arr[position][position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View grid= LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_single, parent, false);
        final TextView textView = (TextView) grid.findViewById(R.id.textview1);
        if (tar_arr!=null) {
            int xpos = position / columncount;
            int ypos = position % columncount;
            Log.d("pos", String.valueOf(position));
            textView.setText(String.valueOf(tar_arr[xpos][ypos]));
        }
        grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int xpos = position / columncount;
                final int ypos = position % columncount;
                android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(v.getContext());
                final EditText input = new EditText(v.getContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);
                input.setText(String.valueOf(tar_arr[xpos][ypos]));
                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your uid here to execute after dialog
                                pos = position;
                                FirebaseAuth mAuth;
                                final DatabaseReference reference;
                                mAuth = FirebaseAuth.getInstance();
                                reference = FirebaseDatabase.getInstance().getReference();
                                tar_arr[xpos][ypos]= Integer.parseInt(input.getText().toString());
                                Log.d("Array", Arrays.deepToString(tar_arr));
                                Log.d("Type", type);
                                Query query=reference.child("users").child("Tariff_Details").orderByChild("vtype").equalTo(type);
                                query.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        TariffDetails tariffDetails=dataSnapshot.getValue(TariffDetails.class);
                                        Map<String, Object> value = new HashMap<String, Object>();
                                        value.put("arr",tar_arr);
                                        value.put("inc_dur_hrs",tariffDetails.getInc_dur_hrs());
                                        value.put("inslip_tariff",tariffDetails.getInslip_tariff());
                                        value.put("vtype",tariffDetails.getVehicle_type());
                                        value.put("total_slab_hrs",tariffDetails.getTotal_slab_hrs());
                                        value.put("no_of_slab_hrs", tariffDetails.getNo_of_slab_hrs());
                                        reference.child("users").child("Tariff_Details").child(dataSnapshot.getKey()).setValue(value);
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
                        });
                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your uid here to execute after dialog
                                dialog.cancel();
                            }
                        });

                // closed

                // Showing Alert Message
                alertDialog.show();

            }
        });
        return grid;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public int[] getPos() {
        int [] val= new int[2];
        val[0]=columncount;
        val[1]=pos;
        return val;
    }
}
