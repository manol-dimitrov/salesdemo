

package org.sdl.salesdemo.services;

import java.math.BigDecimal;

import org.sdl.salesdemo.common.SalesDemoException;
import org.sdl.salesdemo.domain.Item;
import org.sdl.salesdemo.domain.Order;

/**
 * The following is the tax processing service interface which defines the methods 
 * for processing the tax on an order
 * 
 * @author shannonlal
 */
public interface TaxProcessingService {
    
    public Order calculateOrderTotal( Order order )throws SalesDemoException;
    
    public BigDecimal calculateRoundedTax( BigDecimal tax );
    
    public Item calculateSalesTax( Item item );
    
    public Item calculateImportTaxes( Item item );
    
    public Item calculateLineTotal( Item item);
    
}
