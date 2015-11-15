package org.sdl.salesdemo.common;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * The following interface defines constants to be used within the Sales Tax
 * application
 *
 * @author shannonlal
 */
public interface SalesTaxConstants {

    /**
     * The following is the enumeration for the HTTP Response Codes which are
     * returned from a REST Request
     *
     * @author shannonlal
     *
     */
    public enum Taxes {

        SALES_TAX(0.1f),
        IMPORT_DUTY_TAX(0.05f);

        private BigDecimal taxRate;

        /**
         * The following is the default constructor
         *
         * @param rate
         */
        private Taxes(float rate) {
            MathContext context = new MathContext(2, RoundingMode.HALF_DOWN);
            this.taxRate = new BigDecimal(rate, context);
            
        }

        /**
         * The following method will return the tax rate
         *
         * @return
         */
        public BigDecimal getTaxRate() {
            return taxRate;
        }

    }

    /**
     * The following is the enumeration for the HTTP Response Codes which are
     * returned from a REST Request
     *
     * @author shannonlal
     *
     */
    public enum HttpResponseCode {

        HTTP_GET(200),
        HTTP_CREATE(201),
        HTTP_UPDATE_DELETE(202),
        HTTP_INVALID_DATA(400),
        HTTP_NOT_FOUND(404),
        HTTP_VALIDATION_ERROR(412),
        HTTP_UNEXPECTED_ERROR(500);

        private final int httpCode;

        /**
         * The following is the default constructor
         *
         * @param rate
         */
        private HttpResponseCode(int code) {

            this.httpCode = code;
        }

        /**
         * The following method will return the tax rate
         *
         * @return
         */
        public int getHttpCode() {
            return httpCode;
        }

    }
}
