package org.sdl.salesdemo.common;

/**
 * The following class defines the generic sales tax exception
 * 
 * @author shannonlal
 */
public class SalesDemoDBException extends Exception{
    
    /**
     * 
     * @param message 
     */
    public SalesDemoDBException(String message){
        super( message);
    }

}
