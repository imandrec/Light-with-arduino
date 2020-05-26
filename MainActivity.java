package com.example.bluetoothlightcontrol;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT =0;
    private static final int REQUEST_DISCOVER_BT =1;
    public static String EXTRA_ADDRESS = "device_address";

    TextView mStatusBluetoothTv;
    ListView mPairedlv;
    ImageView mBluetoothIv;
    Button mOnBtn,mOffBtn,mPairedBtn,mDiscoverbaleBtn;
    BluetoothAdapter mBluetoothAdapater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStatusBluetoothTv = findViewById(R.id.statusbluetoothTv);
        mPairedlv= findViewById(R.id.pairedlv);
        mBluetoothIv=findViewById(R.id.bluetoothiv);
        mOnBtn=findViewById(R.id.onbtn);
        mOffBtn=findViewById(R.id.offbtn);
        mPairedBtn=findViewById(R.id.pairedbtn);
        mDiscoverbaleBtn=findViewById(R.id.discoverablebtn);
        // adapter
        mBluetoothAdapater=BluetoothAdapter.getDefaultAdapter();

        //checking bluetooth available or not
        if (mBluetoothAdapater== null){
            mStatusBluetoothTv.setText("Bluetooth is not available");
        }
        else {
            mStatusBluetoothTv.setText("Bluetooth is available");
        }

        // set image according to bluetooth On/off
        if (mBluetoothAdapater.isEnabled()){
            mBluetoothIv.setImageResource(R.drawable.ic_action_on);
        }
        else{
            mBluetoothIv.setImageResource(R.drawable.ic_action_off);
        }

        // on button click
        mOnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBluetoothAdapater.isEnabled()){
                    showtoast("Turning on Bluetooth...");
                    // Intent to on Bluetooth
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent,REQUEST_ENABLE_BT);
                }
                else{
                    showtoast("Bluetooth is already on ");
                }

            }
        });
        // discoverable button click
        mDiscoverbaleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBluetoothAdapater.isDiscovering()){
                    showtoast("making the device dicoverable");
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    startActivityForResult(intent,REQUEST_DISCOVER_BT);
                }
            }
        });
        // off button click
        mOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothAdapater.isEnabled()){
                    mBluetoothAdapater.disable();
                    showtoast("Turning off Bluetooth");
                    mBluetoothIv.setImageResource(R.drawable.ic_action_off);
                }
                else{
                    showtoast("Bluetooth is already off ");
                }

            }
        });
        // get paired devices button click
        mPairedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pairedDevicesList();
            }
        });

    }
    private void pairedDevicesList()
    {
        Set<BluetoothDevice> devices = mBluetoothAdapater.getBondedDevices();
        ArrayList list = new ArrayList();

        if (devices.size()>0)
        {
            for(BluetoothDevice device : devices)
            {
                list.add(device.getName() + "\n" + device.getAddress()); //Get the device's name and the address
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        mPairedlv.setAdapter(adapter);
        mPairedlv.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked


    }
    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick (AdapterView<?> av, View v, int arg2, long arg3)
        {
            // Get the device MAC address, the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            // Make an intent to start next activity.
            Intent i = new Intent(MainActivity.this, ledControl.class);

            //Change the activity.
            i.putExtra(EXTRA_ADDRESS, address); //this will be received at ledControl (class) Activity
            startActivity(i);
            finish();
        }
    };
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_device_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        // if (id == R.id.action_settings) {
        //    return true;
        // }

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case REQUEST_ENABLE_BT:
                if (resultCode==RESULT_OK){
                    //bluetooth is On
                    mBluetoothIv.setImageResource(R.drawable.ic_action_on);
                    showtoast("Bluetooth is ON");
                }
                else {
                    //mBluetoothIv.setImageResource(R.drawable.ic_action_off);
                    showtoast("could't on bluetooth");
                }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // toast message function
    private void showtoast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}

/* 1. turn On- Off bluetooth
2. Make bluetooth discoverable
3. display paired device
4. check if bluetooth available or not
 Permissiom required- bluetooth , bluetooth admin*/


