package com.dd.chargercounter.Presenter;

public interface IPresenter {


    void onSetVoltage(float voltage);

    void onSetStatus(int status);

    void onChargePlug(int chargePlug);

    void onCharging(int status, int ampereRate);


    void onHealth(int health);

    void onCheckCapacity(int capacity);

    void onCheckChargeCounter(int chargeCounter);


    void onTechnology(String technology);

    void onTemperature(int temperature);


    void onEnergyCounter(int energyCounter);

    void onChargeTimeRemaining(long chargeTimeRemaining);

    void onMaxCapacity(long chargeCounter, int capacity);
}
