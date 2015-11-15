package test.unit.org.sdl.salesdemo.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.sdl.salesdemo.common.SalesDemoDBException;
import org.sdl.salesdemo.common.SalesDemoException;
import org.sdl.salesdemo.common.SalesTaxConstants.HttpResponseCode;
import org.sdl.salesdemo.dao.AbstractDao;
import org.sdl.salesdemo.domain.Item;
import org.sdl.salesdemo.domain.Order;
import org.sdl.salesdemo.domain.Product;
import org.sdl.salesdemo.domain.json.JSONItem;
import org.sdl.salesdemo.domain.json.JSONOrder;
import org.sdl.salesdemo.services.impl.OrderServiceImpl;
import org.sdl.salesdemo.services.impl.TaxProcessingServiceImpl;

/**
 * The following is the test case for the order service implementation
 * it will test the service under different conditions
 * 
 * @author shannonlal
 */
public class OrderServiceImplTestCase {

    private OrderServiceImpl orderService;
    private AbstractDao<Order> orderDao;
    private AbstractDao<Product> productDao;
    private TaxProcessingServiceImpl taxProcessingService;
    
    @Before
    public void setUp(){
        taxProcessingService = new TaxProcessingServiceImpl();
        orderDao = mock(AbstractDao.class);
        productDao = mock( AbstractDao.class);
        orderService = new OrderServiceImpl();
        orderService.setOrderDao(orderDao);
        orderService.setProductDao(productDao);
        orderService.setTaxProcessingService(taxProcessingService);
    }
    
    @Test
    public void shouldGetAListOfEmptyOrders(){
        List<JSONOrder> orders;
        try{
            
            when( orderDao.getTypes()).thenReturn(new ArrayList<Order>());

            orders = orderService.getOrders();
            assertNotNull( orders );
            
            verify( orderDao ).getTypes();
        }catch( SalesDemoDBException e){
            fail("Unexpected error getting orders");
        }catch( SalesDemoException e){
            fail("Unexpected error getting orders");
        }
    }
    
    @Test
    public void shouldGetAListOfOrders(){
        Product p = new Product();
        p.setId(1L);
        p.setImportDutiesExempt(true);
        p.setName("Candy");
        p.setPrice(new BigDecimal(10.01));
        p.setSalesTaxExempt(false);
        
        Item item = new Item();
        item.setProduct(p);
        item.setQuantity(1);
        
        Order o = new Order();
        o.addItem(item);
        List<Order> testOrders = new ArrayList<Order>();
        testOrders.add( o );
        
        List<JSONOrder> orders;
        try{
            when( orderDao.getTypes()).thenReturn(testOrders);

            orders = orderService.getOrders();
            assertNotNull( orders );
            
            verify( orderDao ).getTypes();
        }catch( SalesDemoDBException e){
            fail("Unexpected error getting orders");
        }catch( SalesDemoException e){
            fail("Unexpected error getting orders");
        }
    }
    
    @Test
    public void shouldGetAnOrderById(){
        Product p = new Product();
        p.setId(1L);
        p.setImportDutiesExempt(true);
        p.setName("Candy");
        p.setPrice(new BigDecimal(10.01));
        p.setSalesTaxExempt(false);
        
        Item item = new Item();
        item.setProduct(p);
        item.setQuantity(1);
        
        Order o = new Order();
        o.addItem(item);
        
        JSONOrder order;
        try{
            when( orderDao.findById(1L)).thenReturn(o);
            
            order = orderService.getOrder(1L);
            assertNotNull( order );
            
            verify(orderDao).findById(1L);
        }catch( SalesDemoDBException e){
            fail("Unexpected error getting orders");
        }catch( SalesDemoException e){
            fail("Unexpected error getting orders");
        }
    }

    
    @Test
    public void shouldFailGetAnOrderByIdBecauseNotFound(){

        JSONOrder order;
        try{
            try{
                when( orderDao.findById(1L)).thenReturn(null);
                
            }catch( SalesDemoDBException e){
                fail("Unexpected error getting orders");
            }
            order = orderService.getOrder(1L);
            fail("Should have thrown Sales Tax Exception");
            
            
        }catch( SalesDemoException e){
            assertNotNull( e );
            assertEquals( e.getHttpResponseCode(), HttpResponseCode.HTTP_NOT_FOUND);
        }
    }
    
    @Test
    public void shouldFailGetOrderBecauseDBException(){

        JSONOrder order;
        try{
            try{
                when( orderDao.findById(1L)).thenThrow( new SalesDemoDBException("DB Connection issue"));
                
            }catch( SalesDemoDBException e){
                
            }
            order = orderService.getOrder(1L);
            fail("Should have thrown Sales Tax Exception");
            
            
        }catch( SalesDemoException e){
            assertNotNull( e );
            assertEquals( e.getHttpResponseCode(), HttpResponseCode.HTTP_UNEXPECTED_ERROR);
        }
    }
    
    @Test
    public void shouldFailUpdateAnOrderBecauseJSONOrderIsNull(){
        JSONOrder jsonOrder = null;
        try{
            Order order = orderService.calculateOrderTaxesAndTotals(jsonOrder);
            assertNull( order );
        }catch( SalesDemoException e){
            assertNotNull( e );
            
            assertEquals( e.getHttpResponseCode(), HttpResponseCode.HTTP_INVALID_DATA);
        }
    }
    
    @Test
    public void shouldFailUpdateAnOrderBecauseHasNoItems(){
        JSONOrder jsonOrder = new JSONOrder();
        try{
            Order order = orderService.calculateOrderTaxesAndTotals(jsonOrder);
            
        }catch( SalesDemoException e){
            assertNotNull( e );
            
            assertEquals( e.getHttpResponseCode(), HttpResponseCode.HTTP_INVALID_DATA);
        }
    }
    
    @Test
    public void shouldFailBecauseNotFound(){
        JSONOrder jsonOrder = new JSONOrder();
        jsonOrder.setOrderId( 2L );
        JSONItem item = new JSONItem();
        jsonOrder.getOrderItems().add(item);

        try{
            try{
                when( orderDao.findById(2L)).thenReturn(null);
                
            }catch( SalesDemoDBException e){
                
            }
            orderService.calculateOrderTaxesAndTotals(jsonOrder);
            
        }catch( SalesDemoException e){
            assertNotNull( e );
            assertEquals( e.getHttpResponseCode(), HttpResponseCode.HTTP_NOT_FOUND);
        }
    }
    
    @Test
    public void shouldFailBecauseProductIdInvalid(){
        JSONOrder jsonOrder = new JSONOrder();
        JSONItem item = new JSONItem();
        item.setProductId(2L);
        jsonOrder.getOrderItems().add(item);

        try{
            orderService.calculateOrderTaxesAndTotals(jsonOrder);
            
        }catch( SalesDemoException e){
            assertNotNull( e );
            assertEquals( e.getHttpResponseCode(), HttpResponseCode.HTTP_NOT_FOUND);
        }
    }
    
    @Test
    public void shouldFailBecauseProductIdNotFound(){
        JSONOrder jsonOrder = new JSONOrder();
        JSONItem item = new JSONItem();
        item.setProductId(-2L);
        jsonOrder.getOrderItems().add(item);

        try{
            try{
                when( productDao.findById(2L)).thenReturn(null);
                
            }catch( SalesDemoDBException e){
                
            }
            orderService.calculateOrderTaxesAndTotals(jsonOrder);
            
        }catch( SalesDemoException e){
            assertNotNull( e );
            assertEquals( e.getHttpResponseCode(), HttpResponseCode.HTTP_INVALID_DATA);
        }
    }
    
    @Test
    public void shouldFailBecauseDuplicateProductInItemList(){
        
        Product product = new Product();
        product.setImportDutiesExempt(true);
        product.setSalesTaxExempt(true);
        product.setName("Test");
        product.setPrice( new BigDecimal(10.00) );
        product.setId(1L);
        JSONOrder jsonOrder = new JSONOrder();
        
        JSONItem item1 = new JSONItem();
        item1.setProductId(1L);
        jsonOrder.getOrderItems().add(item1);
        
        JSONItem item2 = new JSONItem();
        item2.setProductId(1L);
        jsonOrder.getOrderItems().add(item2);

        try{
            try{
                when( productDao.findById(1L)).thenReturn(product);
                
            }catch( SalesDemoDBException e){
                
            }
            orderService.calculateOrderTaxesAndTotals(jsonOrder);
            
        }catch( SalesDemoException e){
            assertNotNull( e );
            assertEquals( e.getHttpResponseCode(), HttpResponseCode.HTTP_VALIDATION_ERROR);
        }
    }
    
    @Test
    public void shouldCreateOrderWithOneItem(){
        
        Product product = new Product();
        product.setImportDutiesExempt(true);
        product.setSalesTaxExempt(true);
        product.setName("Test");
        product.setPrice( new BigDecimal(10.00) );
        product.setId(1L);
        JSONOrder jsonOrder = new JSONOrder();
        
        JSONItem item1 = new JSONItem();
        item1.setProductId(1L);
        item1.setQuantity(1);
        jsonOrder.getOrderItems().add(item1);
        
        Order order = new Order();
        
        try{
            try{
                when( productDao.findById(1L)).thenReturn(product);
                
                when( orderDao.saveOrUpdate( order )).thenAnswer(new Answer<Order>(){
                   
                    public Order answer( InvocationOnMock invocation ) throws Throwable{
                        Order o = (Order)invocation.getArguments()[0];
                        o.setId(1L);
                        return o;
                    }
                    
                });
                      
            }catch( SalesDemoDBException e){
                
            }
            order = orderService.calculateOrderTaxesAndTotals(jsonOrder);
            assertNotNull( order );
            
            assertEquals( new Long(1), order.getId());
            assertNotNull( order.getItems() );
            assertEquals( new Integer(1), new Integer( order.getItems().size()));
            BigDecimal total = BigDecimal.TEN;
            total = total.setScale( 2, BigDecimal.ROUND_CEILING);
            assertEquals( total, order.getTotal());
            
            BigDecimal tax = BigDecimal.ZERO;
            tax = tax.setScale( 2, BigDecimal.ROUND_CEILING);
            assertEquals( tax, order.getSalesTax());
        }catch( SalesDemoException e){
            assertNotNull( e );
            assertEquals( e.getHttpResponseCode(), HttpResponseCode.HTTP_VALIDATION_ERROR);
        }
    }
    
    @Test
    public void shouldCreateOrderWithOneItemAndSalesTax(){
        
        Product product = new Product();
        product.setImportDutiesExempt(true);
        product.setSalesTaxExempt(false);
        product.setName("Test");
        product.setPrice( new BigDecimal(10.00) );
        product.setId(1L);
        JSONOrder jsonOrder = new JSONOrder();
        
        JSONItem item1 = new JSONItem();
        item1.setProductId(1L);
        item1.setQuantity(1);
        jsonOrder.getOrderItems().add(item1);
        
        Order order = new Order();
        
        try{
            try{
                when( productDao.findById(1L)).thenReturn(product);
                
                when( orderDao.saveOrUpdate( order )).thenAnswer(new Answer<Order>(){
                    
                    public Order answer( InvocationOnMock invocation ) throws Throwable{
                        Order o = (Order)invocation.getArguments()[0];
                        o.setId(1L);
                        return o;
                    }
                    
                });
                      
            }catch( SalesDemoDBException e){
                
            }
            order = orderService.calculateOrderTaxesAndTotals(jsonOrder);
            assertNotNull( order );
            
            assertEquals( new Long(1), order.getId());
            assertNotNull( order.getItems() );
            assertEquals( new Integer(1), new Integer( order.getItems().size()));
            BigDecimal total = new BigDecimal( 11.00);
            total = total.setScale(2, RoundingMode.CEILING);
            assertEquals( total, order.getTotal());
            BigDecimal tax = BigDecimal.ONE;
            tax = tax.setScale(2, RoundingMode.CEILING);
            assertEquals( tax, order.getSalesTax() );
        }catch( SalesDemoException e){
            assertNotNull( e );
            assertEquals( e.getHttpResponseCode(), HttpResponseCode.HTTP_VALIDATION_ERROR);
        }
    }
    
    @Test
    public void shouldCreateOrderWithOneItemAndImportTax(){
        
        Product product = new Product();
        product.setImportDutiesExempt(false);
        product.setSalesTaxExempt(true);
        product.setName("Test");
        BigDecimal price = new BigDecimal(10.00);
        price = price.setScale(2, BigDecimal.ROUND_CEILING);
        product.setPrice( price );
        product.setId(1L);
        JSONOrder jsonOrder = new JSONOrder();
        
        JSONItem item1 = new JSONItem();
        item1.setProductId(1L);
        item1.setQuantity(1);
        jsonOrder.getOrderItems().add(item1);
        
        Order order = new Order();
        
        try{
            try{
                when( productDao.findById(1L)).thenReturn(product);
                
                when( orderDao.saveOrUpdate( order )).thenAnswer(new Answer<Order>(){
                    
                    public Order answer( InvocationOnMock invocation ) throws Throwable{
                        Order o = (Order)invocation.getArguments()[0];
                        o.setId(1L);
                        return o;
                    }
                    
                });
                      
            }catch( SalesDemoDBException e){
                
            }
            order = orderService.calculateOrderTaxesAndTotals(jsonOrder);
            assertNotNull( order );
            
            assertEquals( new Long(1), order.getId());
            assertNotNull( order.getItems() );
            assertEquals( new Integer(1), new Integer( order.getItems().size()));
            BigDecimal total = new BigDecimal(10.50);
            total = total.setScale( 2, BigDecimal.ROUND_CEILING);
            assertEquals( total, order.getTotal());
            BigDecimal tax = new BigDecimal( 0.50 );
            tax = tax.setScale(2, BigDecimal.ROUND_CEILING);
            
            
            assertEquals( tax, order.getSalesTax() );
        }catch( SalesDemoException e){
            assertNotNull( e );
            assertEquals( e.getHttpResponseCode(), HttpResponseCode.HTTP_VALIDATION_ERROR);
        }
    }
    
    @Test
    public void shouldCreateOrderWithMultipleItemsWhichAreTaxEmpty(){
        
        Product p1 = new Product();
        p1.setImportDutiesExempt(true);
        p1.setSalesTaxExempt(true);
        p1.setName("Test");
        p1.setPrice( new BigDecimal( 10.00) );
        p1.setId(1L);
        
        Product p2 = new Product();
        p2.setImportDutiesExempt(true);
        p2.setSalesTaxExempt(true);
        p2.setName("Test 2");
        p2.setPrice( new BigDecimal( 20.00) );
        p2.setId(2L);
        
        JSONOrder jsonOrder = new JSONOrder();
        
        JSONItem item1 = new JSONItem();
        item1.setProductId(1L);
        item1.setQuantity(1);
        jsonOrder.getOrderItems().add(item1);
        
        JSONItem item2 = new JSONItem();
        item2.setProductId(2L);
        item2.setQuantity(1);
        jsonOrder.getOrderItems().add( item2 );
        
        Order order = new Order();
        
        try{
            try{
                when( productDao.findById(1L)).thenReturn(p1);
                
                when( productDao.findById(2L)).thenReturn(p2);
                
                when( orderDao.saveOrUpdate( order )).thenAnswer(new Answer<Order>(){
                    
                    public Order answer( InvocationOnMock invocation ) throws Throwable{
                        Order o = (Order)invocation.getArguments()[0];
                        o.setId(1L);
                        return o;
                    }
                    
                });
                      
            }catch( SalesDemoDBException e){
                
            }
            order = orderService.calculateOrderTaxesAndTotals(jsonOrder);
            assertNotNull( order );
            
            assertEquals( new Long(1), order.getId());
            assertNotNull( order.getItems() );
            assertEquals( new Integer(2), new Integer( order.getItems().size()));
            BigDecimal total = new BigDecimal( 30.00 );
            total = total.setScale( 2, BigDecimal.ROUND_CEILING);
            assertEquals( total, order.getTotal());
            BigDecimal tax = BigDecimal.ZERO;
            tax = tax.setScale(2, BigDecimal.ROUND_CEILING);
            assertEquals( tax, order.getSalesTax());
        }catch( SalesDemoException e){
            assertNotNull( e );
            assertEquals( e.getHttpResponseCode(), HttpResponseCode.HTTP_VALIDATION_ERROR);
        }
    }

}
