package com.kubilaydekin.BigPointJavaTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.bigpoint.assessment.gasstation.GasPump;
import net.bigpoint.assessment.gasstation.GasType;
import net.bigpoint.assessment.gasstation.exceptions.GasTooExpensiveException;
import net.bigpoint.assessment.gasstation.exceptions.NotEnoughGasException;

class StandardGasStationThreadTest {

    private StandardGasStation gasStation;
    private GasPump regularPump;
    private GasPump superPump;

    @BeforeEach
    void setUp() {
        gasStation = new StandardGasStation();
        
        regularPump = new GasPump(GasType.REGULAR, 1000);
        superPump = new GasPump(GasType.SUPER, 500);

        gasStation.addGasPump(regularPump);
        gasStation.addGasPump(superPump);

        gasStation.setPrice(GasType.REGULAR, 5.0);
        gasStation.setPrice(GasType.SUPER, 6.0);
    }

    @Test
    void testConcurrentBuyGas() throws InterruptedException {
        int numberOfCustomers = 10;
        CountDownLatch latch = new CountDownLatch(numberOfCustomers);
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfCustomers);

        for (int i = 0; i < numberOfCustomers; i++) {
            final int index = i;
            executorService.execute(() -> {
                try {
                    gasStation.buyGas(GasType.REGULAR, 50, 10);
                } catch (NotEnoughGasException | GasTooExpensiveException e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await();
        executorService.shutdown();

        // Check the total revenue
        assertEquals(2500.0, gasStation.getRevenue());
        
        // Check the remaining amount of gas in pumps
        assertEquals(500.0, regularPump.getRemainingAmount());
        assertEquals(500.0, superPump.getRemainingAmount());
        
        // Check the number of sales
        assertEquals(10, gasStation.getNumberOfSales());
        
        // Check the number of cancellations due to not enough gas
        assertEquals(0, gasStation.getNumberOfCancellationsNoGas());
        
        // Check the number of cancellations due to gas being too expensive
        assertEquals(0, gasStation.getNumberOfCancellationsTooExpensive());
    }

    @Test
    void testGetPrice() {
        assertEquals(5.0, gasStation.getPrice(GasType.REGULAR));
        assertEquals(6.0, gasStation.getPrice(GasType.SUPER));
    }

    @Test
    void testSetPrice() {
        gasStation.setPrice(GasType.REGULAR, 6.0);
        assertEquals(6.0, gasStation.getPrice(GasType.REGULAR));
    }
}
