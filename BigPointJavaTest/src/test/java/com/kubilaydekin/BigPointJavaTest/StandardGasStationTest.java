package com.kubilaydekin.BigPointJavaTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.bigpoint.assessment.gasstation.GasPump;
import net.bigpoint.assessment.gasstation.GasType;
import net.bigpoint.assessment.gasstation.exceptions.GasTooExpensiveException;
import net.bigpoint.assessment.gasstation.exceptions.NotEnoughGasException;

class StandardGasStationTest {

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
    void testBuyGas() throws NotEnoughGasException, GasTooExpensiveException {
        double totalPrice = gasStation.buyGas(GasType.REGULAR, 50, 10);
        assertEquals(250.0, totalPrice);
        
        assertEquals(950.0, regularPump.getRemainingAmount());
        assertEquals(500.0, superPump.getRemainingAmount());
        
        assertEquals(250.0, gasStation.getRevenue());
        assertEquals(1, gasStation.getNumberOfSales());
    }

    @Test
    void testBuyGasNotEnoughGasException() {
        assertThrows(NotEnoughGasException.class, () -> {
            gasStation.buyGas(GasType.SUPER, 600, 10);
        });
        
        assertEquals(1000.0, regularPump.getRemainingAmount());
        assertEquals(500.0, superPump.getRemainingAmount());
        
        assertEquals(0.0, gasStation.getRevenue());
        assertEquals(0, gasStation.getNumberOfSales());
        assertEquals(1, gasStation.getNumberOfCancellationsNoGas());
    }

    @Test
    void testBuyGasGasTooExpensiveException() {
        assertThrows(GasTooExpensiveException.class, () -> {
            gasStation.buyGas(GasType.SUPER, 50, 5);
        });
        
        assertEquals(1000.0, regularPump.getRemainingAmount());
        assertEquals(500.0, superPump.getRemainingAmount());
        
        assertEquals(0.0, gasStation.getRevenue());
        assertEquals(0, gasStation.getNumberOfSales());
        assertEquals(1, gasStation.getNumberOfCancellationsTooExpensive());
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
