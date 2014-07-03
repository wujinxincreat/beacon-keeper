package com.m039.ibeacon.keeper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.m039.ibeacon.keeper.content.IBeaconEntity;
import com.m039.ibeacon.keeper.receiver.IBeaconReceiver;

@SuppressLint("UseSparseArrays")
public class DemoActivity extends Activity {

    public static final String TAG = "m039";
    public static final int REQUEST_CODE = 0;

    private HashMap<Integer, String> mReceivedData =
        new HashMap<Integer, String>();

    private ArrayList<String> mItems = new ArrayList<String>();

    private TextView mText;
    private ListView mList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_demo);

        mText = (TextView) findViewById(R.id.text);
        mList = (ListView) findViewById(R.id.list);

        if (mList != null) {
            mList.setAdapter(new ArrayAdapter<String>(DemoActivity.this, R.layout.e_list, mItems));
        }
    }

    private BroadcastReceiver mIBeaconReceiver = new IBeaconReceiver() {

            @SuppressWarnings("unchecked")
            @Override
            protected void onFoundIBeacon(IBeaconEntity iBeaconEntity) {
                mReceivedData.put(iBeaconEntity.hashCode(),
                                  String.format("iBeaconEntity %x\n" +
                                                "proximityUuid: %s\n" +
                                                "major: %s\n" +
                                                "minor: %s\n" +
                                                "txPower: %s\n" +
                                                "producer: %s",
                                                iBeaconEntity.hashCode(),
                                                iBeaconEntity.getProximityUuid(),
                                                iBeaconEntity.getMajor(),
                                                iBeaconEntity.getMinor(),
                                                iBeaconEntity.getTxPower(),
                                                iBeaconEntity.getProducer()));

                if (mList != null) {
                    ArrayAdapter<String> adapter = (ArrayAdapter<String>) mList.getAdapter();
                    if (adapter != null) {
                        mItems.clear();
                        mItems.addAll(mReceivedData.values());
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            protected void onBleEnabled() {
                if (mText != null) {
                    mText.setText(R.string.a_demo__ble_enabled);
                }
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void onBleDisabled() {
                if (mText != null) {
                    mText.setText(R.string.a_demo__ble_disabled);
                }

                if (mList != null) {
                    ArrayAdapter<String> adapter = (ArrayAdapter<String>) mList.getAdapter();
                    if (adapter != null) {
                        mItems.clear();
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        };


    @Override
    protected void onStart() {
        super.onStart();

        IBeaconReceiver.registerReceiver(this, mIBeaconReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();

        IBeaconReceiver.unregisterReceiver(this, mIBeaconReceiver);
    }
}
