package org.sdl.salesdemo.common;

/**
 * The following class defines the generic sales tax exception
 * 
 * @author shannonlal
 */
public class SalesTaxDBException extends Exception{
    
    /**
     * 
     * @param message 
     */
    public SalesTaxDBException(String message){
        super( message);
    }

}
