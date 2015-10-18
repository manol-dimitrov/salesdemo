
package test.unit.org.sdl.salesdemo.services.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.junit.Before;
import org.sdl.salesdemo.domain.Item;
import org.sdl.salesdemo.domain.Product;
import org.sdl.salesdemo.services.TaxProcessingService;
import org.sdl.salesdemo.services.impl.TaxProcessingServiceImpl;
import org.sdl.salesdemo.common.SalesTaxConstants.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.sdl.salesdemo.common.SalesDemoException;
import org.sdl.salesdemo.domain.Order;

/**
 * The following test case will test the tax processing service.  It will
 * test the different methods of the item tax and total calculations
 * @author shannonlal
 */
public class TaxProcessingServiceImplTestCase {

    private TaxProcessingService taxProcessingService;
    
    @Before
    public void setUp(){
        taxProcessingService = new TaxProcessingServiceImpl();
    }
    
    @Test
    public void shouldCalculateSalesTaxForSingleItem(){
        Product p = new Product();
        p.setId(1L);
        p.setImportDutiesExempt(true);
        p.setName("Candy");
        p.setPrice(new BigDecimal(10.0));
        p.setSalesTaxExempt(false);
        
        Item item = new Item();
        item.setProduct(p);
        item.setQuantity(1);
        
        item = taxProcessingService.calculateSalesTax(item);
        
        assertNotNull( item );
        BigDecimal salesTax = new BigDecimal(10.00).multiply(Taxes.SALES_TAX.getTaxRate());
        salesTax = taxProcessingService.calculateRoundedTax(salesTax);
        
        assertEquals( salesTax, item.getTaxes()  );       
    }
    
    @Test
    public void shouldNotCalculateSalesTaxBecauseItemIsExempt(){
        Product p = new Product();
        p.setId(1L);
        p.setImportDutiesExempt(true);
        p.setName("Candy");
        p.setPrice(new BigDecimal(10.00));
        p.setSalesTaxExempt(true);
        
        Item item = new Item();
        item.setProduct(p);
        item.setQuantity(1);
        
        item = taxProcessingService.calculateSalesTax(item);
        
        assertNotNull( item );
        BigDecimal salesTax = BigDecimal.ZERO;
        
        
        assertEquals( salesTax, item.getTaxes()  );
    }
    
    @Test
    public void shouldCalculateSalesTaxForMultipleQuantity(){
        Product p = new Product();
        p.setId(1L);
        p.setImportDutiesExempt(true);
        p.setName("Candy");
        p.setPrice(new BigDecimal(10.00));
        p.setSalesTaxExempt(false);
        
        int quantity = 3;
        Item item = new Item();
        item.setProduct(p);
        item.setQuantity(quantity);
        
        item = taxProcessingService.calculateSalesTax(item);
        
        assertNotNull( item );
        BigDecimal salesTax = new BigDecimal(10.00).multiply( Taxes.SALES_TAX.getTaxRate());
        salesTax = salesTax.multiply(new BigDecimal(quantity));
               
        salesTax = taxProcessingService.calculateRoundedTax(salesTax);
        assertEquals( salesTax, item.getTaxes()  );       
    }
    
    @Test
    public void shouldCalculateImportTaxForSingleItem(){
        Product p = new Product();
        p.setId(1L);
        p.setImportDutiesExempt(false);
        p.setName("Import Candy");
        p.setPrice(new BigDecimal(10.00));
        p.setSalesTaxExempt(false);
        
        Item item = new Item();
        item.setProduct(p);
        item.setQuantity(1);
        
        item = taxProcessingService.calculateImportTaxes(item);
        
        assertNotNull( item );
        BigDecimal salesTax = new BigDecimal(10.00).multiply(Taxes.IMPORT_DUTY_TAX.getTaxRate());
        salesTax = taxProcessingService.calculateRoundedTax(salesTax);
        assertEquals( salesTax, item.getTaxes()  );       
    }
    
    @Test
    public void shouldNotCalculateImportTaxBecauseProductExempt(){
        Product p = new Product();
        p.setId(1L);
        p.setImportDutiesExempt(true);
        p.setName("Import Candy");
        p.setPrice(new BigDecimal(10.00));
        p.setSalesTaxExempt(false);
        
        Item item = new Item();
        item.setProduct(p);
        item.setQuantity(1);
        
        item = taxProcessingService.calculateImportTaxes(item);
        
        assertNotNull( item );
        BigDecimal salesTax = BigDecimal.ZERO;
        
        assertEquals( salesTax, item.getTaxes()  );       
    }
    
    @Test
    public void shouldCalculateImportTaxForMultipleQuantity(){
        Product p = new Product();
        p.setId(1L);
        p.setImportDutiesExempt(false);
        p.setName("Candy");
        p.setPrice(new BigDecimal(10.00));
        p.setSalesTaxExempt(false);
        
        int quantity = 3;
        Item item = new Item();
        item.setProduct(p);
        item.setQuantity(quantity);
        
        item = taxProcessingService.calculateImportTaxes(item);
        
        assertNotNull( item );
        BigDecimal salesTax = new BigDecimal(10.00).multiply(Taxes.IMPORT_DUTY_TAX.getTaxRate());
        
        salesTax = salesTax.multiply(new BigDecimal(quantity));
        salesTax = taxProcessingService.calculateRoundedTax(salesTax);
        assertEquals( salesTax, item.getTaxes()  );       
    }
    
    
    @Test
    public void shouldCalculateItemTotalWithSingleItemAndNoTaxes(){
        Product p = new Product();
        p.setId(1L);
        p.setImportDutiesExempt(true);
        p.setName("Candy");
        p.setPrice(new BigDecimal(10.00));
        p.setSalesTaxExempt(true);
        
        int quantity = 1;
        Item item = new Item();
        item.setProduct(p);
        item.setQuantity(quantity);
        
        Order order = new Order();
        order.addItem( item );
        
        item = taxProcessingService.calculateLineTotal(item);
        
        assertNotNull( item );
        BigDecimal total = new BigDecimal(10.0).multiply(new BigDecimal(quantity));
        total = total.setScale(2, RoundingMode.HALF_EVEN);
        
        assertEquals( total, item.getLineTotal()  );       
    }
    
    @Test
    public void shouldCalculateItemTotalWithSingleItemAndSalesTaxes(){
        Product p = new Product();
        p.setId(1L);
        p.setImportDutiesExempt(true);
        p.setName("Candy");
        p.setPrice(new BigDecimal(10.00));
        p.setSalesTaxExempt(false);
        
        int quantity = 1;
        Item item = new Item();
        item.setProduct(p);
        item.setQuantity(quantity);
        
        Order order = new Order();
        order.addItem( item );
        item = taxProcessingService.calculateSalesTax(item);
        item = taxProcessingService.calculateLineTotal(item);
        
        assertNotNull( item );
        
        BigDecimal total = new BigDecimal(11.0 );
        total  =total.setScale( 2, RoundingMode.HALF_EVEN);
        BigDecimal lineTotal = item.getLineTotal();
        assertEquals( total, lineTotal  );       
    }
    
    @Test
    public void shouldCalculateItemTotalWithSingleItemAndImportTaxes(){
        Product p = new Product();
        p.setId(1L);
        p.setImportDutiesExempt(false);
        p.setName("Candy");
        p.setPrice(new BigDecimal(10.00));
        p.setSalesTaxExempt(false);
        
        int quantity = 1;
        Item item = new Item();
        item.setProduct(p);
        item.setQuantity(quantity);
        
        Order order = new Order();
        order.addItem( item );
        item = taxProcessingService.calculateImportTaxes(item);
        item = taxProcessingService.calculateLineTotal(item);
        
        assertNotNull( item );

        BigDecimal total = new BigDecimal(10.50 );
        total = total.setScale( 2, RoundingMode.HALF_EVEN);
        
        assertEquals( total, item.getLineTotal()  );       
    }
    
    @Test
    public void shouldCalculateItemTotalWithSingleItemAndSalesAndImportTaxes(){
        Product p = new Product();
        p.setId(1L);
        p.setImportDutiesExempt(false);
        p.setName("Candy");
        p.setPrice(new BigDecimal(10.00));
        p.setSalesTaxExempt(false);
        
        int quantity = 1;
        Item item = new Item();
        item.setProduct(p);
        item.setQuantity(quantity);
        
        Order order = new Order();
        order.addItem( item );
        item = taxProcessingService.calculateSalesTax(item);
        item = taxProcessingService.calculateImportTaxes(item);
        item = taxProcessingService.calculateLineTotal(item);
        
        assertNotNull( item );
        
        BigDecimal total = new BigDecimal(11.50 );

        total = total.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        
        assertEquals( total, item.getLineTotal());       
    }
    
    @Test
    public void shouldCalculateItemTotalWithMultipleItemAndSalesAndImportTaxes(){
        Product p = new Product();
        p.setId(1L);
        p.setImportDutiesExempt(false);
        p.setName("Candy");
        p.setPrice(new BigDecimal(10.00));
        p.setSalesTaxExempt(false);
        
        int quantity = 3;
        Item item = new Item();
        item.setProduct(p);
        item.setQuantity(quantity);
        
        Order order = new Order();
        order.addItem( item );
        item = taxProcessingService.calculateSalesTax(item);
        item = taxProcessingService.calculateImportTaxes(item);
        item = taxProcessingService.calculateLineTotal(item);
        
        assertNotNull( item );

        BigDecimal total = new BigDecimal(34.50 );
        total = total.setScale( 2, BigDecimal.ROUND_HALF_EVEN);
        
        assertEquals( total, item.getLineTotal()  );       
    }
    
    @Test
    public void shouldCalculateOrderTotalWithSingleItemAndNoTaxes(){
        Product p = new Product();
        p.setId(1L);
        p.setImportDutiesExempt(true);
        p.setName("Candy");
        p.setPrice(new BigDecimal(10.01));
        p.setSalesTaxExempt(true);
        
        int quantity = 1;
        Item item = new Item();
        item.setProduct(p);
        item.setQuantity(quantity);
        
        Order order = new Order();
        order.addItem( item );
        try{
            order = taxProcessingService.calculateOrderTotal(order);
        }catch(SalesDemoException e){
            fail("Unexpected exception when calculating order total");
        }
        
        assertNotNull( order );
        BigDecimal total = new BigDecimal(10.01 ).multiply( new BigDecimal(quantity));// + importTax + salesTax;
        total = total.setScale( 2, BigDecimal.ROUND_CEILING);
        assertEquals( total, order.getTotal() );       
    }
    
    @Test
    public void shouldCalculateOrderTotalWithSingleItemSalesTaxes(){
        Product p = new Product();
        p.setId(1L);
        p.setImportDutiesExempt(true);
        p.setName("Candy");
        p.setPrice(new BigDecimal(10.00));
        p.setSalesTaxExempt(false);
        
        int quantity = 1;
        Item item = new Item();
        item.setProduct(p);
        item.setQuantity(quantity);
        
        Order order = new Order();
        order.addItem( item );
        try{
            order = taxProcessingService.calculateOrderTotal(order);
        }catch(SalesDemoException e){
            fail("Unexpected exception when calculating order total");
        }
        
        assertNotNull( order );
        BigDecimal orderTotal = order.getTotal();
        BigDecimal salesOrderTotal = order.getSalesTax();
        Double importTax = 0.0;//(10.01 * Taxes.IMPORT_DUTY_TAX.getTaxRate()) *quantity;
        BigDecimal salesTax = new BigDecimal(10.00).multiply( Taxes.SALES_TAX.getTaxRate());
        salesTax = salesTax.multiply( new BigDecimal(quantity ));
        //salesTax = taxProcessingService.calculateRoundedTax(salesTax);
        BigDecimal total = new BigDecimal(10.00 ).multiply( new BigDecimal(quantity) );
        total = total.add( salesTax );
        total = total.setScale( 2, BigDecimal.ROUND_CEILING);
        assertEquals( total, order.getTotal()  );       
    }
    
    @Test
    public void shouldCalculateOrderTotalWithSingleItemImportTaxes(){
        Product p = new Product();
        p.setId(1L);
        p.setImportDutiesExempt(false);
        p.setName("Candy");
        p.setPrice(new BigDecimal(10.00));
        p.setSalesTaxExempt(true);
        
        int quantity = 1;
        Item item = new Item();
        item.setProduct(p);
        item.setQuantity(quantity);
        
        Order order = new Order();
        order.addItem( item );
        try{
            order = taxProcessingService.calculateOrderTotal(order);
        }catch(SalesDemoException e){
            fail("Unexpected exception when calculating order total");
        }
        
        assertNotNull( order );
        BigDecimal importTax = new BigDecimal(10.00).multiply( Taxes.IMPORT_DUTY_TAX.getTaxRate());
        importTax = importTax.multiply( new BigDecimal(quantity));
        //importTax = taxProcessingService.calculateRoundedTax(importTax);
        BigDecimal salesTax = BigDecimal.ZERO;//(10.01 * Taxes.SALES_TAX.getTaxRate()) *quantity;
        BigDecimal total = new BigDecimal(10.00 ).multiply( new BigDecimal(quantity) );
        total = total.add( importTax ). add(salesTax);
        total = total.setScale( 2, BigDecimal.ROUND_CEILING);
        assertEquals( total, order.getTotal()  );       
    }
    
    @Test
    public void shouldCalculateOrderTotalWithSingleItemSalesAndImportTaxes(){
        Product p = new Product();
        p.setId(1L);
        p.setImportDutiesExempt(false);
        p.setName("Candy");
        p.setPrice(new BigDecimal(10.00));
        p.setSalesTaxExempt(false);
        
        int quantity = 1;
        Item item = new Item();
        item.setProduct(p);
        item.setQuantity(quantity);
        
        Order order = new Order();
        order.addItem( item );
        try{
            order = taxProcessingService.calculateOrderTotal(order);
        }catch(SalesDemoException e){
            fail("Unexpected exception when calculating order total");
        }
        
        assertNotNull( order );
        BigDecimal importTax = new BigDecimal(10.00).multiply( Taxes.IMPORT_DUTY_TAX.getTaxRate());
        importTax = importTax.multiply( new BigDecimal(quantity));
        BigDecimal salesTax = new BigDecimal(10.00).multiply(Taxes.SALES_TAX.getTaxRate());
        salesTax = salesTax.multiply( new BigDecimal(quantity) );

        BigDecimal total = new BigDecimal(10.00 ).multiply(new BigDecimal(quantity)).add(importTax).add(salesTax);
        total = total.setScale( 2, BigDecimal.ROUND_CEILING);
        assertEquals( total, order.getTotal()  );       
    }
    
    @Test
    public void shouldCalculateOrderTotalWithMultipleItemSalesAndImportTaxes(){
        Product p = new Product();
        p.setId(1L);
        p.setImportDutiesExempt(false);
        p.setName("Candy");
        p.setPrice(new BigDecimal( 10.00) );
        p.setSalesTaxExempt(false);
        
        int quantity = 3;
        Item item = new Item();
        item.setProduct(p);
        item.setQuantity(quantity);
        
        Order order = new Order();
        order.addItem( item );
        try{
            order = taxProcessingService.calculateOrderTotal(order);
        }catch(SalesDemoException e){
            fail("Unexpected exception when calculating order total");
        }
        
        assertNotNull( order );
        BigDecimal importTax = new BigDecimal(10.00).multiply( Taxes.IMPORT_DUTY_TAX.getTaxRate());
        importTax = importTax.multiply(new BigDecimal(quantity));

        BigDecimal salesTax = new BigDecimal(10.00).multiply( Taxes.SALES_TAX.getTaxRate());
        salesTax = salesTax.multiply( new BigDecimal(quantity));
        BigDecimal total = new BigDecimal(10.00 ).multiply(new BigDecimal(quantity)).add( importTax).add(salesTax);
        total = total.setScale( 2, BigDecimal.ROUND_CEILING);
        assertEquals( total, order.getTotal()  );       
    }
    
}
