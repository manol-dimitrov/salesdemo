
package org.sdl.salesdemo.domain.json;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.sdl.salesdemo.common.SalesTaxUtil;
import org.sdl.salesdemo.domain.Item;
import org.sdl.salesdemo.domain.Order;


/**
 * The following class is a JSON light weight order object which will contain
 * the relevant information to be displayed to the client
 * 
 * @author shannonlal
 */
public class JSONOrder {
    private Long orderId;
    private List<JSONItem> orderItems;
    private String salesTax;
    private String salesTotal;
    private static final DecimalFormat decimalFormat = SalesTaxUtil.SALES_TAX_NUMBER_FORMAT;
    
    /**
     * 
     */
    public JSONOrder(){
        orderItems = new ArrayList<JSONItem>();
    }
 
    /**
     * The following constructor will convert the order 
     * object into a JSON object
     * @param order 
     */
    public JSONOrder( Order order ){
        this.orderId = order.getId();
        this.salesTax = decimalFormat.format(order.getSalesTax());
        this.salesTotal = decimalFormat.format( order.getTotal() );
        
        List<Item> items = order.getItems();
        orderItems = new ArrayList<JSONItem>();
        if( ( items!= null ) && (!items.isEmpty()) ){
            for (Item item : items) {
                JSONItem jsonItem = new JSONItem(item);
                orderItems.add( jsonItem );
            }
        }
    }

    /**
     * 
     * @return 
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * 
     * @param orderId 
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * 
     * @return 
     */
    public List<JSONItem> getOrderItems() {
        if( orderItems == null ){
            orderItems = new ArrayList<JSONItem>();
        }
        return orderItems;
    }

    /**
     * 
     * @param orderItems 
     */
    public void setOrderItems(List<JSONItem> orderItems) {
        this.orderItems = orderItems;
    }

    /**
     * 
     * @return 
     */
    public String getSalesTax() {
        return salesTax;
    }

    /**
     * 
     * @param salesTax 
     */
    public void setSalesTax(String salesTax) {
        this.salesTax = salesTax;
    }

    /**
     * 
     * @return 
     */
    public String getSalesTotal() {
        return salesTotal;
    }

    /**
     * 
     * @param salesTotal 
     */
    public void setSalesTotal(String salesTotal) {
        this.salesTotal = salesTotal;
    }
    
    @Override
    public String toString() {
        return "JSONOrder{" + "orderId=" + orderId + ", orderItems=" + orderItems + ", salesTax=" + salesTax + ", salesTotal=" + salesTotal + '}';
    }

    
}
