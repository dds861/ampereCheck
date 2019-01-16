package com.dd.chargercounter.View;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.chargercounter.Presenter.IPresenter;
import com.dd.chargercounter.Presenter.Presenter;
import com.dd.chargercounter.R;
import com.github.anastr.speedviewlib.SpeedView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IView {

    private IPresenter iPresenter;
    private AdView mAdView;
    private SpeedView mSpeedometerDischarging;
    private SpeedView mSpeedometerCharging;
    private BatteryManager battaryManager;
    private RecyclerView recyclerView;

    private List<Product> productList = new ArrayList<>();
    private MyAdapter myAdapter = new MyAdapter(this, productList);
    private TextView mStopwatchTv;
    private int seconds = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Request window feature action bar
        requestWindowFeature(Window.FEATURE_ACTION_BAR);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        battaryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
        iPresenter = new Presenter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myAdapter);


    }

    private Thread thread = null;

    private void startStopwatch() {
        if (thread == null) {

            thread = new Thread() {
                @Override
                public void run() {
                    try {
                        while (!thread.isInterrupted()) {
                            Thread.sleep(1000);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setmStopwatchTv(String.valueOf(seconds));
                                    seconds++;
                                }
                            });
                        }
                    } catch (InterruptedException e) {
                        Log.i("autolog", "e: " + e);

                    }
                }
            };
            thread.start();
        }
    }

    private void pauseStopwatch() {

        if (thread != null) {
            Thread moribund = thread;
            thread = null;
            moribund.interrupt();
        }


    }

    public void setmStopwatchTv(String mStopwatchTv) {
        this.mStopwatchTv.setText(mStopwatchTv);
    }


    private void initView() {
        productList.add(new Product(getResources().getString(R.string.s0_status), getString(R.string.s0_status_description), ""));
        productList.add(new Product(getResources().getString(R.string.s1_plugged), getString(R.string.s1_plugged_description), ""));
        productList.add(new Product(getResources().getString(R.string.s11_BatteryPropertyCapacity), getString(R.string.s11_BatteryPropertyCapacity_description), ""));
        productList.add(new Product(getResources().getString(R.string.s2_health), getString(R.string.s2_health_description), ""));
        productList.add(new Product(getResources().getString(R.string.s3_technology), getString(R.string.s3_technology_description), ""));
        productList.add(new Product(getResources().getString(R.string.s14_temperature), getString(R.string.s14_temperature_description), ""));
        productList.add(new Product(getResources().getString(R.string.s8_volts), getString(R.string.s8_volts_description), ""));
        productList.add(new Product(getResources().getString(R.string.s7_millivolts), getString(R.string.s7_millivolts_description), ""));
        productList.add(new Product(getString(R.string.max_capacity), getString(R.string.max_capacity_description), ""));
        productList.add(new Product(getResources().getString(R.string.s10_BatteryPropertyChargeCounter), getString(R.string.s10_BatteryPropertyChargeCounter_description), ""));
        productList.add(new Product(getResources().getString(R.string.s9_BatteryPropertyEnergyCounter), getString(R.string.s9_BatteryPropertyEnergyCounter_description), ""));
        productList.add(new Product(getString(R.string.chargeTimeRemaining), getString(R.string.chargeTimeRemaining_description), ""));
        productList.add(new Product("", "", ""));

        productList.add(new Product(getResources().getString(R.string.s13_manufacturer), getString(R.string.s13_manufacturer_description), Build.MANUFACTURER));
        productList.add(new Product(getResources().getString(R.string.s4_model), getString(R.string.s4_model_description), Build.MODEL));
        productList.add(new Product(getResources().getString(R.string.s5_android_version), getString(R.string.s5_android_version_description), Build.VERSION.RELEASE));
        productList.add(new Product(getResources().getString(R.string.s6_build_id), getString(R.string.s6_build_id_description), Build.ID));
        productList.add(new Product(getResources().getString(R.string.sdk_version), getString(R.string.sdk_version_description), String.valueOf(Build.VERSION.SDK_INT)));

        mAdView = findViewById(R.id.adView);
        mSpeedometerDischarging = findViewById(R.id.discharging_speedometer);
        mSpeedometerCharging = findViewById(R.id.charging_speedometer);
        recyclerView = findViewById(R.id.list);
        mStopwatchTv = findViewById(R.id.tv_stopwatch);
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

        // Register the broadcast receiver (from google docs)
        getApplicationContext().registerReceiver(mBroadcastReceiver, iFilter);

        startStopwatch();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the broadcast receiver (from google docs)
        getApplicationContext().unregisterReceiver(mBroadcastReceiver);

        pauseStopwatch();
    }

    // Initialize a new BroadcastReceiver instance
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            seconds = 0;

            int ampereRate = battaryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW);
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            iPresenter.onCharging(status, ampereRate);


            //setting Voltage
            float voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
            iPresenter.onSetVoltage(voltage);

            //Status charging/discharging/full
            iPresenter.onSetStatus(status);

            // How are we charging? Ac/USB/Wireless
            int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            iPresenter.onChargePlug(chargePlug);

            // technology
            String technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
            iPresenter.onTechnology(technology);

            // technology
            int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
            iPresenter.onTemperature(temperature);

            //battary health
            int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
            iPresenter.onHealth(health);

            int capacity = battaryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
            iPresenter.onCheckCapacity(capacity);

            int chargeCounter = battaryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER);
            iPresenter.onCheckChargeCounter(chargeCounter);


            int energyCounter = battaryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER);
            iPresenter.onEnergyCounter(energyCounter);


            iPresenter.onMaxCapacity(chargeCounter, capacity);

            // technology
            long chargeTimeRemaining = -2;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                chargeTimeRemaining = battaryManager.computeChargeTimeRemaining();
            }
            iPresenter.onChargeTimeRemaining(chargeTimeRemaining);

        }
    };


    @Override
    public void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }




    @Override
    public void setmDischargingMinDataTv(String s) {

    }

    @Override
    public void setmDischargingMaxDataTv(String s) {

    }

    @Override
    public void setmChargingMaxDataTv(String s) {

    }


    @Override
    public void setmStatusTv(String s) {
        notifyChangeInProductList(s, getResources().getString(R.string.s0_status));
    }

    @Override
    public void setmPluggedTv(String s) {
        notifyChangeInProductList(s, getResources().getString(R.string.s1_plugged));
    }

    @Override
    public void setmHealthTv(String s) {
        notifyChangeInProductList(s, getResources().getString(R.string.s2_health));
    }

    @Override
    public void setmBuildidTv(String s) {
        notifyChangeInProductList(s, getResources().getString(R.string.s6_build_id));
    }

    @Override
    public void setmBatteryPropertyCurrentNowTv(String s) {

    }

    @Override
    public void setmMilliVoltsTv(String s) {
        notifyChangeInProductList(s, getResources().getString(R.string.s7_millivolts));
    }

    @Override
    public void setmBatteryPropertyEnergyCounterTv(String s) {
        notifyChangeInProductList(s, getResources().getString(R.string.s9_BatteryPropertyEnergyCounter));
    }

    @Override
    public void setmBatteryPropertyChargeCounterTv(String s) {
        notifyChangeInProductList(s, getResources().getString(R.string.s10_BatteryPropertyChargeCounter));
    }

    @Override
    public void setmBatteryPropertyCapacityTv(String s) {
        notifyChangeInProductList(s, getResources().getString(R.string.s11_BatteryPropertyCapacity));
    }

    @Override
    public void setmBatteryPropertyStatusTv(String s) {
        notifyChangeInProductList(s, getResources().getString(R.string.sdk_version));

    }

    @Override
    public void setmSpeedometerDischarging(String s) {
    }

    @Override
    public void setmSpeedometerCharging(String s) {
    }


    @Override
    public void setChargingSpeedometerMaxSpeed(int maxSpeed) {
        mSpeedometerCharging.setMaxSpeed(maxSpeed);
    }

    @Override
    public void setChargingSpeedometerMinSpeed(int minSpeed) {
        mSpeedometerCharging.setMinSpeed(minSpeed);
    }

    @Override
    public void setChargingSpeedToSpeedometer(int ampereRate, int duration) {
        mSpeedometerCharging.speedTo(ampereRate, duration);

    }

    @Override
    public void setDischargingSpeedometerMaxSpeed(int maxSpeed) {
        mSpeedometerDischarging.setMaxSpeed(maxSpeed);
    }

    @Override
    public void setDischargingSpeedometerMinSpeed(int minSpeed) {
        mSpeedometerDischarging.setMinSpeed(minSpeed);
    }

    @Override
    public void setDischargingSpeedToSpeedometer(int ampereRate, int duration) {
        mSpeedometerDischarging.speedTo(ampereRate, duration);

    }

    @Override
    public void setmVoltsTv(String s) {
        notifyChangeInProductList(s, getResources().getString(R.string.s8_volts));

    }

    @Override
    public void setChargingTrembleOff() {
        mSpeedometerCharging.setWithTremble(false);
        mSpeedometerDischarging.setWithTremble(true);
    }

    @Override
    public void setDischargingTrembleOff() {
        mSpeedometerCharging.setWithTremble(true);
        mSpeedometerDischarging.setWithTremble(false);
    }

    @Override
    public void setTechnology(String s) {
        notifyChangeInProductList(s, getResources().getString(R.string.s3_technology));
    }

    private void notifyChangeInProductList(String value, String constantName) {
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getConstantName().equals(constantName)) {
                productList.get(i).setValue(value);
            }
        }
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void setTemperature(String s) {
        notifyChangeInProductList(s, getResources().getString(R.string.s14_temperature));

    }

    @Override
    public void setEnergyCounter(String s) {
        notifyChangeInProductList(s, getResources().getString(R.string.s9_BatteryPropertyEnergyCounter));
    }

    @Override
    public void setChargeTimeRemaining(String s) {
        notifyChangeInProductList(s, getResources().getString(R.string.chargeTimeRemaining));

    }

    @Override
    public void setMaxCapacity(String s) {
        notifyChangeInProductList(s, getResources().getString(R.string.max_capacity));
    }
}
