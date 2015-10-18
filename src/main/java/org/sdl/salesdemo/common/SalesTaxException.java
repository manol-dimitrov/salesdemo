
package org.sdl.salesdemo.common;
import org.sdl.salesdemo.common.SalesTaxConstants.*;

/**
 * The following is an exception which will be thrown to the client
 * when an error occurs processing the sales tax.  This will include
 * the http response which will be set on the Http Response to the client
 * @author shannonlal
 */
public class SalesTaxException extends Exception{

    private String clientMessage;
    private HttpResponseCode httpResponseCode;
    
    /**
     * 
     * @param message
     * @param code 
     */
    public SalesTaxException( String message, HttpResponseCode code ){
        clientMessage = message;
        httpResponseCode = code;
    }

    /**
     * 
     * @return 
     */
    public String getClientMessage() {
        return clientMessage;
    }

    /**
     * 
     * @param clientMessage 
     */
    public void setClientMessage(String clientMessage) {
        this.clientMessage = clientMessage;
    }

    /**
     * 
     * @return 
     */
    public HttpResponseCode getHttpResponseCode() {
        return httpResponseCode;
    }

    /**
     * 
     * @param httpResponseCode 
     */
    public void setHttpResponseCode(HttpResponseCode httpResponseCode) {
        this.httpResponseCode = httpResponseCode;
    }
}
