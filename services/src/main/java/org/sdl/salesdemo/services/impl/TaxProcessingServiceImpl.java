package org.sdl.salesdemo.services.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.apache.log4j.Logger;
import org.sdl.salesdemo.common.SalesDemoException;
import org.sdl.salesdemo.common.SalesTaxConstants.*;
import org.sdl.salesdemo.domain.Item;
import org.sdl.salesdemo.domain.Order;
import org.sdl.salesdemo.services.TaxProcessingService;
import org.springframework.stereotype.Service;
/**
 * The following class is the tax processing service implementation.  It will handle the logic
 * of calculating the tax (including rounding to nearest 0.05) and totals for items and orders.  
 * 
 * @author shannonlal
 */
@Service("taxProcessingService")
public class TaxProcessingServiceImpl implements TaxProcessingService{
    private static final Logger LOGGER = Logger.getLogger(TaxProcessingServiceImpl.class.getName());
    

    public TaxProcessingServiceImpl(){
    }
    
    /**
     * The following method will calculate the sales tax based on the order 
     * provided.  The following are the business rules for calculating the tax
     * The following are the business rules
     * for calculating the tax:
     * 1. It will apply the sales ( Taxes.SALES_TAX) to all non exempt products
     * 2. It will apply the import duty (Taxes.IMPORT_TAX ) to all imported products
     * 3. It will apply the rounding rules to the tax and totals
     * 4. It will calculate the totals for each item in the order
     * @param order
     * @return
     * @throws SalesDemoException 
     */
    public Order calculateOrderTotal( Order order )throws SalesDemoException{
        LOGGER.info("Calcuating sales tax for order ->" + order);
        List<Item> items = order.getItems(); //Note. get items will return empty list if no items
        BigDecimal orderTotal= BigDecimal.ZERO;
        BigDecimal orderTax= BigDecimal.ZERO; 
        //Loop through the list of 
        for (Item item : items) {
            
            //Ensure the item is defined
            if( item == null ){
                String msg = "Line Item was null.  Unexpected ";
                LOGGER.info(msg);
                throw new SalesDemoException(msg, HttpResponseCode.HTTP_VALIDATION_ERROR);
            }

            //Ensure the product is defined
            if( item.getProduct() == null ){
                String msg = "Product was not found.";
                LOGGER.info(msg);
                throw new SalesDemoException(msg, HttpResponseCode.HTTP_NOT_FOUND);
            }
            
            // Calcualte the taxes and determine the total
            item = calculateSalesTax(item);
            
            item = calculateImportTaxes(item);
            
            item = calculateLineTotal(item);
            
            //Update the order sales tax and totals
            BigDecimal tax  = item.getTaxes();
            BigDecimal total = item.getLineTotal();
            orderTax = orderTax.add(tax);
            orderTotal = orderTotal.add(total);
        }
        
        orderTax = orderTax.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        orderTotal = orderTotal.setScale( 2, BigDecimal.ROUND_HALF_EVEN);
        order.setTotal( orderTotal);
        order.setSalesTax(orderTax);
        
        return order;
    }
    
    /**
     * The following method will calculate the correct rounded tax
     * It will use Java RoudingMode.HALF_EVEN to round the tax to 
     * the nearest 0.05.  It will multiple the tax by 20, then
     * divide by 20 and round to two decimal places
     *
     * @param tax
     * @return
     */
    public BigDecimal calculateRoundedTax( BigDecimal tax ){
        LOGGER.info("Rounding price for item ->" + tax);

        if( ( tax != null)&& (!BigDecimal.ZERO.equals(tax))){
            double t1 = tax.doubleValue();
            t1 = Math.ceil( t1 * 20)/20;
            tax = new BigDecimal(t1);
            tax = tax.setScale( 2, RoundingMode.HALF_EVEN);
        }

        return tax;
    }
    /**
     * The following method will check to see if sales tax is applicable on 
     * this item.  If it is it will calculate it
     * @param item - Assumed not null
     * @return
     */
    public Item calculateSalesTax( Item item ){
        if( !item.getProduct().isSalesTaxExempt()){
            BigDecimal salesTax = item.getProduct().getPrice().multiply(Taxes.SALES_TAX.getTaxRate());
            salesTax = salesTax.multiply(new BigDecimal(item.getQuantity())) ;
            
            //Apply the rounding to the sales tax
            BigDecimal itemTax = item.getTaxes().add( salesTax);
            itemTax = calculateRoundedTax(itemTax);
            
            item.setTaxes( itemTax  );
        }
        
        return item;
    }
    
    /**
     * The following method will check to see if the import duties is applicable
     * to this item.  If it is it will calculate it
     * @param item - Assumed not null
     * @return
     */
    public Item calculateImportTaxes( Item item ){
        if( !item.getProduct().isImportDutiesExempt()){
            BigDecimal importTax = item.getProduct().getPrice().multiply(Taxes.IMPORT_DUTY_TAX.getTaxRate());
            importTax = importTax.multiply(new BigDecimal(item.getQuantity()));
            
            //Apply the rounding to the sales tax
            BigDecimal itemTax = item.getTaxes().add(importTax);
            itemTax = calculateRoundedTax(itemTax);
            
            item.setTaxes( itemTax );
        }
        
        return item;
    }
    
    /**
     * The following method will calculate the line total
     * to include quantity, product price and taxes
     * @param item
     * @return 
     */
    public Item calculateLineTotal( Item item){
        BigDecimal subTotal = item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity()));

        BigDecimal total = subTotal.add( item.getTaxes());
        total = total.setScale(2, RoundingMode.HALF_EVEN);
        item.setLineTotal(total);
        
        return item;
    }
}
