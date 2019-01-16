package com.dd.chargercounter.View;

public interface IView {


    void setmDischargingMinDataTv(String s);

    void setmDischargingMaxDataTv(String s);

    void setmChargingMaxDataTv(String s);

    void setmStatusTv(String s);

    void setmPluggedTv(String s);

    void setmHealthTv(String s);

    void setmBuildidTv(String s);

    void setmBatteryPropertyCurrentNowTv(String s);

    void setmMilliVoltsTv(String s);

    void setmBatteryPropertyEnergyCounterTv(String s);

    void setmBatteryPropertyChargeCounterTv(String s);

    void setmBatteryPropertyCapacityTv(String s);

    void setmBatteryPropertyStatusTv(String s);

    void setmSpeedometerDischarging(String s);

    void setmSpeedometerCharging(String s);

    //Charging Speedometer
    void setChargingSpeedometerMaxSpeed(int maxSpeed);
    void setChargingSpeedometerMinSpeed(int minSpeed);
    void setChargingSpeedToSpeedometer(int ampereRate, int duration);

    //Discharging Speedometer
    void setDischargingSpeedometerMaxSpeed(int maxSpeed);
    void setDischargingSpeedometerMinSpeed(int minSpeed);
    void setDischargingSpeedToSpeedometer(int ampereRate, int duration);


    void showToast(String s);







    void setmVoltsTv(String s);

    void setChargingTrembleOff();

    void setDischargingTrembleOff();

    void setTechnology(String s);

    void setTemperature(String s);


    void setEnergyCounter(String s);

    void setChargeTimeRemaining(String s);

    void setMaxCapacity(String s);
}
