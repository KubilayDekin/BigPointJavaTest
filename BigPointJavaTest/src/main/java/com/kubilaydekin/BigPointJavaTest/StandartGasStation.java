package com.kubilaydekin.BigPointJavaTest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.bigpoint.assessment.gasstation.GasPump;
import net.bigpoint.assessment.gasstation.GasStation;
import net.bigpoint.assessment.gasstation.GasType;
import net.bigpoint.assessment.gasstation.exceptions.GasTooExpensiveException;
import net.bigpoint.assessment.gasstation.exceptions.NotEnoughGasException;


public class StandartGasStation implements GasStation {

    private Map<GasType, Double> gasPrices;
    private Map<GasType, GasPump> gasPumps;
    private double revenue;
    private int numberOfSales;
    private int numberOfCancellationsNoGas;
    private int numberOfCancellationsTooExpensive;
    private final Object lock = new Object();

    public StandartGasStation() {
        this.gasPrices = new HashMap<>();
        this.gasPumps = new HashMap<>();
        this.revenue = 0;
        this.numberOfSales = 0;
        this.numberOfCancellationsNoGas = 0;
        this.numberOfCancellationsTooExpensive = 0;
    }

    @Override
    public void addGasPump(GasPump pump) {
        synchronized (lock) {
            gasPumps.put(pump.getGasType(), pump);
        }
    }

    @Override
    public Collection<GasPump> getGasPumps() {
        synchronized (lock) {
            return gasPumps.values();
        }
    }

    @Override
    public double buyGas(GasType type, double amountInLiters, double maxPricePerLiter)
            throws NotEnoughGasException, GasTooExpensiveException {
        synchronized (lock) {
            GasPump pump = gasPumps.get(type);
            if (pump == null || pump.getRemainingAmount() < amountInLiters) {
                numberOfCancellationsNoGas++;
                throw new NotEnoughGasException();
            }

            double price = getPrice(type);
            if (price > maxPricePerLiter) {
                numberOfCancellationsTooExpensive++;
                throw new GasTooExpensiveException();
            }

            double totalPrice = price * amountInLiters;
            pump.pumpGas(amountInLiters);
            revenue += totalPrice;
            numberOfSales++;
            return totalPrice;
        }
    }

    @Override
    public double getRevenue() {
        synchronized (lock) {
            return revenue;
        }
    }

    @Override
    public int getNumberOfSales() {
        synchronized (lock) {
            return numberOfSales;
        }
    }

    @Override
    public int getNumberOfCancellationsNoGas() {
        synchronized (lock) {
            return numberOfCancellationsNoGas;
        }
    }

    @Override
    public int getNumberOfCancellationsTooExpensive() {
        synchronized (lock) {
            return numberOfCancellationsTooExpensive;
        }
    }

    @Override
    public double getPrice(GasType type) {
        synchronized (lock) {
            return gasPrices.getOrDefault(type, 0.0);
        }
    }

    @Override
    public void setPrice(GasType type, double price) {
        synchronized (lock) {
            gasPrices.put(type, price);
        }
    }
}