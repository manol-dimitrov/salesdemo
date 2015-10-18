package org.sdl.salesdemo.services;

import java.util.List;

import org.sdl.salesdemo.common.SalesTaxException;
import org.sdl.salesdemo.domain.Order;
import org.sdl.salesdemo.domain.json.JSONOrder;

/**
 * The following is the order service which will define the interface
 * for handling requests for processing an order
 * @author shannonlal
 */
public interface OrderService {
    
    public JSONOrder updateOrder( JSONOrder order )throws SalesTaxException;
    
    public Order calculateOrderTaxesAndTotals( JSONOrder order) throws SalesTaxException;
       
    public List<JSONOrder> getOrders() throws SalesTaxException;
    
    public JSONOrder getOrder( long orderId ) throws SalesTaxException;
}
