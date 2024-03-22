package com.kubilaydekin.BigPointJavaTest;

import java.util.Scanner;

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
	public static void main(String[] args) {

        StandardGasStation myGasStation = new StandardGasStation();

        GasPump regularPump = new GasPump(GasType.REGULAR, 1000);
        GasPump superPump = new GasPump(GasType.SUPER, 500);
        GasPump dieselPump = new GasPump(GasType.DIESEL, 800);

        myGasStation.addGasPump(regularPump);
        myGasStation.addGasPump(superPump);
        myGasStation.addGasPump(dieselPump);

        myGasStation.setPrice(GasType.REGULAR, 10);
        myGasStation.setPrice(GasType.SUPER, 15);
        myGasStation.setPrice(GasType.DIESEL, 12);

        Scanner scanner = new Scanner(System.in);

        boolean isValidInput = false;
        boolean continueLoop = true;
        
        while (continueLoop) {
            GasType gasType = null;
            double amountInLiters = 0;
            double maxPricePerLiter = 0;

            isValidInput = false;

            while (!isValidInput) {
                System.out.println("Enter gas type (0 for REGULAR, 1 for SUPER, 2 for DIESEL):");
                int gasTypeIndex = scanner.nextInt();

                if (gasTypeIndex >= 0 && gasTypeIndex <= 2) {
                    gasType = GasType.values()[gasTypeIndex];
                    isValidInput = true;
                } else {
                    System.out.println("Invalid input. Please enter a valid gas type index.");
                }
            }

            System.out.println("Enter amount in liters:");
            amountInLiters = scanner.nextDouble();

            System.out.println("Enter max price per liter:");
            maxPricePerLiter = scanner.nextDouble();

            try {
                double totalPrice = myGasStation.buyGas(gasType, amountInLiters, maxPricePerLiter);
                System.out.println("Total Price: " + totalPrice);
            } catch (NotEnoughGasException e) {
                System.out.println("Not enough gas available.");
            } catch (GasTooExpensiveException e) {
                System.out.println("Gas is too expensive.");
            }

            System.out.println("Do you want to continue? (y/n):");
            String choice = scanner.next();

            if ("n".equalsIgnoreCase(choice)) {
                continueLoop = false;
            }
        }

        scanner.close();

        System.out.println("Total Revenue: " + myGasStation.getRevenue());
        System.out.println("Number of Sales: " + myGasStation.getNumberOfSales());
        System.out.println("Number of Cancellations due to No Gas: " + myGasStation.getNumberOfCancellationsNoGas());
        System.out.println("Number of Cancellations due to Gas being too Expensive: " + myGasStation.getNumberOfCancellationsTooExpensive());
    }
}
