package com.coding.driver;

public class DriverManagerFactory {
	
	public static DriverManager getManager(DriverType type) {

        DriverManager driverManager;

        switch (type) {
            case CHROME:
                driverManager = new ChromeDriverManager();
                break;
            /**
             * Add other Driver like firefox , IE, safari for testing in different browser
             */
            default:
                driverManager = new ChromeDriverManager();
                break;
        }
        return driverManager;

    }

}
