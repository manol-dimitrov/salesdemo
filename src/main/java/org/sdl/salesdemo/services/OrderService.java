package org.sdl.salesdemo.services;

import java.util.List;

import org.sdl.salesdemo.common.SalesDemoException;
import org.sdl.salesdemo.domain.Order;
import org.sdl.salesdemo.domain.json.JSONOrder;

/**
 * The following is the order service which will define the interface
 * for handling requests for processing an order
 * @author shannonlal
 */
public interface OrderService {
    
    public JSONOrder updateOrder( JSONOrder order )throws SalesDemoException;
    
    public Order calculateOrderTaxesAndTotals( JSONOrder order) throws SalesDemoException;
       
    public List<JSONOrder> getOrders() throws SalesDemoException;
    
    public JSONOrder getOrder( long orderId ) throws SalesDemoException;
}
