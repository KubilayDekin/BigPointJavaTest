package com.kubilaydekin.BigPointJavaTest;

import net.bigpoint.assessment.gasstation.GasPump;
import net.bigpoint.assessment.gasstation.GasType;
import net.bigpoint.assessment.gasstation.exceptions.GasTooExpensiveException;
import net.bigpoint.assessment.gasstation.exceptions.NotEnoughGasException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	 StandardGasStation gasStation = new StandardGasStation();
         
         GasPump regularPump = new GasPump(GasType.REGULAR, 1000);
         GasPump superPump = new GasPump(GasType.SUPER, 500);
         
         gasStation.addGasPump(regularPump);
         gasStation.addGasPump(superPump);
         
         gasStation.setPrice(GasType.REGULAR, 5.0);
         gasStation.setPrice(GasType.SUPER, 6.0);

         try {
             double totalPrice = gasStation.buyGas(GasType.REGULAR, 50, 10);
             System.out.println("Total Price: " + totalPrice);
             System.out.println("Remaining Regular Gas: " + regularPump.getRemainingAmount());
             System.out.println("Remaining Super Gas: " + superPump.getRemainingAmount());
             System.out.println("Revenue: " + gasStation.getRevenue());
             System.out.println("Number of Sales: " + gasStation.getNumberOfSales());
         } catch (NotEnoughGasException e) {
             System.out.println("Not enough gas available.");
             System.out.println("Remaining Regular Gas: " + regularPump.getRemainingAmount());
             System.out.println("Remaining Super Gas: " + superPump.getRemainingAmount());
             System.out.println("Revenue: " + gasStation.getRevenue());
             System.out.println("Number of Sales: " + gasStation.getNumberOfSales());
             System.out.println("Number of Cancellations due to no gas: " + gasStation.getNumberOfCancellationsNoGas());
         } catch (GasTooExpensiveException e) {
             System.out.println("Gas is too expensive.");
             System.out.println("Remaining Regular Gas: " + regularPump.getRemainingAmount());
             System.out.println("Remaining Super Gas: " + superPump.getRemainingAmount());
             System.out.println("Revenue: " + gasStation.getRevenue());
             System.out.println("Number of Sales: " + gasStation.getNumberOfSales());
             System.out.println("Number of Cancellations due to expensive gas: " + gasStation.getNumberOfCancellationsTooExpensive());
         }
    }
}
