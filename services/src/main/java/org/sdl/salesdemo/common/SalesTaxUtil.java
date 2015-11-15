
package org.sdl.salesdemo.common;

import java.text.DecimalFormat;

/**
 * The following class is a utility class which encapsulates common 
 * functions and utilities across the Sales Tax application
 * @author shannonlal
 */
public class SalesTaxUtil {

    public static final DecimalFormat SALES_TAX_NUMBER_FORMAT = initDecimalFormat();
        /**
     * The following method will init the decimal format
     * to include
     * @return 
     */
    private static DecimalFormat initDecimalFormat(){
        DecimalFormat df = new DecimalFormat("#.##");
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        return df;
    }
}
