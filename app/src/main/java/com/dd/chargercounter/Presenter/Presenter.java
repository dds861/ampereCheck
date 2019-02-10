package com.dd.chargercounter.Presenter;

import android.os.BatteryManager;

import com.dd.chargercounter.View.IView;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class Presenter implements IPresenter {

    private IView iView;
    private int minSpeedCharge = 0;
    private int maxSpeedCharge = 1;
    private int minSpeedDischarge = 0;
    private int maxSpeedDischarge = -1;
    private float maxVoltage = 1.0f;


    public Presenter(IView iView) {
        this.iView = iView;


    }


    @Override
    public void onSetStatus(int status) {

        switch (status) {
            case BatteryManager.BATTERY_STATUS_UNKNOWN:
                iView.setmStatusTv("Unknown");
                break;
            case BatteryManager.BATTERY_STATUS_CHARGING:
                iView.setmStatusTv("Charging");
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                iView.setmStatusTv("Discharging");
                break;
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                iView.setmStatusTv("Not Charging");
                break;
            case BatteryManager.BATTERY_STATUS_FULL:
                iView.setmStatusTv("Battery is Full");
                break;
            default:
                iView.setmStatusTv("Unknown");
        }
    }

    @Override
    public void onChargePlug(int chargePlug) {

        switch (chargePlug) {
            case BatteryManager.BATTERY_PLUGGED_AC:
                iView.setmPluggedTv("Ac");
                break;
            case BatteryManager.BATTERY_PLUGGED_USB:
                iView.setmPluggedTv("Usb");
                break;
            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                iView.setmPluggedTv("Wireless");
                break;
            default:
                iView.setmPluggedTv("Not plugged");
        }
    }

    @Override
    public void onCharging(int status, int ampereRate) {


        switch (status) {
            case BatteryManager.BATTERY_STATUS_UNKNOWN:
                iView.setmStatusTv("Unknown");
                break;

            case BatteryManager.BATTERY_STATUS_DISCHARGING:
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:

                //Set Charge meter to 0
                iView.setChargingSpeedToSpeedometer(0, 10000);
                iView.setChargingTrembleOff();


                //find max and min
                if (ampereRate < 0
                        && ampereRate <= maxSpeedDischarge
                        && minSpeedDischarge > maxSpeedDischarge) {
                    maxSpeedDischarge = ampereRate;
                    iView.setDischargingSpeedometerMinSpeed(maxSpeedDischarge);
                } else if (ampereRate < 0
                        && minSpeedDischarge == 0
                        && ampereRate > maxSpeedDischarge) {
                    minSpeedDischarge = ampereRate;
                    iView.setDischargingSpeedometerMaxSpeed(minSpeedDischarge);
                } else if (ampereRate < 0
                        && ampereRate > minSpeedDischarge
                        && minSpeedDischarge > maxSpeedDischarge) {
                    minSpeedDischarge = ampereRate;
                    iView.setDischargingSpeedometerMaxSpeed(minSpeedDischarge);
                }

                if (ampereRate < 0) {
                    iView.setDischargingSpeedToSpeedometer(ampereRate, 10000);
                }
                break;


//                //find max and min
//                if (ampereRate < 0
//                        && ampereRate >= minSpeedDischarge
//                        && minSpeedDischarge < maxSpeedDischarge) {
//                    maxSpeedDischarge = ampereRate;
//                    iView.setDischargingSpeedometerMaxSpeed(maxSpeedDischarge);
//                } else if (ampereRate < 0
//                        && ampereRate < minSpeedDischarge
//                        && minSpeedDischarge < maxSpeedDischarge) {
//                    minSpeedDischarge = ampereRate;
//                    iView.setDischargingSpeedometerMinSpeed(this.minSpeedDischarge);
//                }
//
//                if (ampereRate < 0) {
//                    iView.setDischargingSpeedToSpeedometer(ampereRate, 10000);
//                }
//                break;

            case BatteryManager.BATTERY_STATUS_CHARGING:
            case BatteryManager.BATTERY_STATUS_FULL:

                //Set Discharge meter to 0
                iView.setDischargingSpeedToSpeedometer(0, 10000);
                iView.setDischargingTrembleOff();

                //find max and min
                if (ampereRate > 0
                        && ampereRate >= maxSpeedCharge
                        && minSpeedCharge < maxSpeedCharge) {
                    maxSpeedCharge = ampereRate;
                    iView.setChargingSpeedometerMaxSpeed(maxSpeedCharge);
                } else if (ampereRate > 0
                        && minSpeedCharge == 0
                        && ampereRate < maxSpeedCharge) {
                    minSpeedCharge = ampereRate;
                    iView.setChargingSpeedometerMinSpeed(minSpeedCharge);
                } else if (ampereRate > 0
                        && ampereRate < minSpeedCharge
                        && minSpeedCharge < maxSpeedCharge) {
                    minSpeedCharge = ampereRate;
                    iView.setChargingSpeedometerMinSpeed(minSpeedCharge);
                }

                if (ampereRate > 0) {
                    iView.setChargingSpeedToSpeedometer(ampereRate, 10000);
                }
                break;


        }


    }


    @Override
    public void onSetVoltage(float voltage) {

        //set MiliVolts to Textview
        iView.setmMilliVoltsTv(String.valueOf(voltage));

        // Convert Millivolts to Volts
        double volt = voltage / 1000;
        DecimalFormat newFormat = new DecimalFormat("0.000");
        String temp = String.valueOf(newFormat.format(volt));

        //set Voltage to Textview
        iView.setmVoltsTv(temp);


    }

    @Override
    public void onHealth(int health) {
        switch (health) {
            case BatteryManager.BATTERY_HEALTH_COLD:
                iView.setmHealthTv("Cold");
                break;
            case BatteryManager.BATTERY_HEALTH_DEAD:
                iView.setmHealthTv("Dead");
                break;
            case BatteryManager.BATTERY_HEALTH_GOOD:
                iView.setmHealthTv("Good");
                break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                iView.setmHealthTv("Overheat");
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                iView.setmHealthTv("Over Voltage");
                break;
            case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                iView.setmHealthTv("Unknown");
                break;
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                iView.setmHealthTv("Unspecified Failure");
                break;
            default:
                iView.setmHealthTv("Unknown");
        }
    }

    @Override
    public void onCheckCapacity(int capacity) {
        iView.setmBatteryPropertyCapacityTv(String.valueOf(capacity));
    }

    @Override
    public void onCheckChargeCounter(int chargeCounter) {
        iView.setmBatteryPropertyChargeCounterTv(String.valueOf(chargeCounter / 1000));
    }

    @Override
    public void onTechnology(String technology) {
        iView.setTechnology(technology);
    }

    @Override
    public void onTemperature(int temperature) {
        iView.setTemperature(String.valueOf((float) temperature / 10));
    }

    @Override
    public void onEnergyCounter(int energyCounter) {
        iView.setEnergyCounter(String.valueOf(energyCounter));

    }

    @Override
    public void onChargeTimeRemaining(long chargeTimeRemaining) {
        StringBuilder stringBuilder = new StringBuilder();
        if (chargeTimeRemaining == -2) {
            stringBuilder.append("Requires API level 28");
        } else {
            long minutes = TimeUnit.MILLISECONDS.toMinutes(chargeTimeRemaining);
            stringBuilder.append(minutes);
        }
        iView.setChargeTimeRemaining(stringBuilder.toString());
    }

    @Override
    public void onMaxCapacity(long chargeCounter, int capacity) {
        long maxCapacity = chargeCounter / capacity * 100 / 1000;
        iView.setMaxCapacity(String.valueOf(maxCapacity));
    }


}
