package org.sdl.salesdemo.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.apache.log4j.Logger;
import org.sdl.salesdemo.common.SalesDemoDBException;
import org.sdl.salesdemo.common.SalesDemoException;
import org.sdl.salesdemo.common.SalesTaxConstants.HttpResponseCode;
import org.sdl.salesdemo.dao.AbstractDao;
import org.sdl.salesdemo.domain.Item;
import org.sdl.salesdemo.domain.Order;
import org.sdl.salesdemo.domain.Product;
import org.sdl.salesdemo.domain.json.JSONItem;
import org.sdl.salesdemo.domain.json.JSONOrder;
import org.sdl.salesdemo.services.OrderService;
import org.sdl.salesdemo.services.TaxProcessingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;


/**
 * The following is the order service implementation.  This will handle the request
 * to get an order and get a list of all the orders.  It will also handle requests
 * to create and update a existing order.  
 * @author shannonlal
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService{
    public static final Logger LOGGER = Logger.getLogger(OrderServiceImpl.class.getName());
    private AbstractDao<Order> orderDao;
    
    private AbstractDao<Product> productDao;

    
    @Autowired
    private TaxProcessingService taxProcessingService;

    public OrderServiceImpl(){

    }
    
    /**
     * The following method will calculate the order taxes 
     * and return a JSON Order to the client
     * @param jsonOrder
     * @return JSONOrder
     * @throws SalesDemoException 
     */
    @Transactional(rollbackFor = SalesDemoException.class)
    public JSONOrder updateOrder( JSONOrder jsonOrder )throws SalesDemoException{
        LOGGER.info("Start update order");
        Order order = calculateOrderTaxesAndTotals(jsonOrder); 
        JSONOrder jsonResult = new JSONOrder(order);
        return jsonResult;
    }
    
    /**
     * The following method will manage creating and updating an order.  In 
     * addition it will send the order to the tax processing service to 
     * calculate the total and taxes.  The following is the flow:
     * 1. It will check if the order exists.  If not it will create a new one
     * 2. It will replace the order items with the new items provide in the jsonOrder
     * 3. It will calculate the tax and order totals
     * 4. It will save a copy in the database and return it
     * @param jsonOrder
     * @return Order
     * @throws SalesDemoException 
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Order calculateOrderTaxesAndTotals(JSONOrder jsonOrder) throws SalesDemoException {
        LOGGER.info("Start calculate order taxes and total order");
        //Check if not defined.  Return no data 
        if( jsonOrder == null) {
            String msg = "Order details not defined";
            LOGGER.error(msg);
            throw new SalesDemoException(msg, HttpResponseCode.HTTP_INVALID_DATA);
        }
        
        //Check if create order
        Order order = null;
        try{
            if( ( jsonOrder.getOrderId() == null ) || ( jsonOrder.getOrderId() < 1 ) ){
                //Must create a new order
                order = new Order();
            }else{
                order = orderDao.findById( jsonOrder.getOrderId());
                if( order == null ){
                    String msg = "Order with Id Not found.  ID ->" + jsonOrder.getOrderId();
                    LOGGER.info(msg);
                    //Throw exception and set HTTP Response to NOT Found
                    throw new SalesDemoException(msg, HttpResponseCode.HTTP_NOT_FOUND);
                }
            }

            //Update Order contents
            order = updateOrderDetails(order, jsonOrder);

            //Calculate order totals            
            order = taxProcessingService.calculateOrderTotal(order);
            
            //Update the database 
            order = orderDao.saveOrUpdate(order);

            return order;
            
        }catch( SalesDemoException e){
            throw e;
        }catch( SalesDemoDBException e){
            String msg = "Unexpected Database Exception updating or creating order "+ e.getMessage();
            LOGGER.error( msg, e);
            
            throw new SalesDemoException(msg, HttpResponseCode.HTTP_UNEXPECTED_ERROR);    
        }catch( Exception e){
            String msg = "Unexpected exception occured while updating order";
            LOGGER.error( msg, e);
            throw new SalesDemoException(msg, HttpResponseCode.HTTP_UNEXPECTED_ERROR);
        }
    }
    
    /**
     * The following method will update the order based on the new
     * information in the json order
     * @param order
     * @param jsonOrder
     * @return 
     * @exception SalesDemoException
     */
    private Order updateOrderDetails( Order order, JSONOrder jsonOrder) throws SalesDemoException{
        
        List<JSONItem> jsonItems = jsonOrder.getOrderItems();
        List<Item> items = new ArrayList<Item>();
        List<Product> usedProducts = new ArrayList<Product>();
        try{
            for (JSONItem jsonItem : jsonItems) {
                //Ensure that we don't add duplicate
                Item item = new Item();

                //Get product id and ensure defined
                Long productId = jsonItem.getProductId();
                if( ( productId == null ) || (productId < 1) ){
                    String msg = "Item Product Id was not defined for order";
                    LOGGER.error( msg);
                    throw new SalesDemoException(msg, HttpResponseCode.HTTP_INVALID_DATA);
                }

                Product product = productDao.findById(productId);
                
                if( product == null ){
                    //Product basd on product Id not found
                    String msg = "Product basd on product Id not found "+ productId;
                    LOGGER.error( msg );
            
                    throw new SalesDemoException(msg, HttpResponseCode.HTTP_NOT_FOUND);
                }
                
                //Ensure that there is only one item for each product
                if( usedProducts.contains( product )){
                    //Error.  Order has two line items from same product
                    String msg = "Order has two line items from same product "+ productId;
                    LOGGER.error( msg );
            
                    throw new SalesDemoException(msg, HttpResponseCode.HTTP_VALIDATION_ERROR);
                }
                
                usedProducts.add(product);
                item.setProduct(product);
                item.setQuantity( jsonItem.getQuantity());
                
                items.add(item);

            }
            //Replace the orders existing list 
            order.setItems( items );
            
        }catch( SalesDemoDBException e){
            String msg = "Unexpected Database Exception updating order "+ e.getMessage();
            LOGGER.error( msg, e);
            
            throw new SalesDemoException(msg, HttpResponseCode.HTTP_UNEXPECTED_ERROR);
        }
        return order;
    }

    /**
     * The following method will return a list of all 
     * the orders in the database
     * @return
     * @throws SalesDemoException 
     */
    @Transactional(rollbackFor = SalesDemoException.class)
    public List<JSONOrder> getOrders() throws SalesDemoException {
        LOGGER.info("Getting a list of orders " );
        try{
            List<Order> orders = orderDao.getTypes();
            
            List<JSONOrder> jsonResult = new ArrayList<JSONOrder>();
            for (Order order : orders) {
                JSONOrder jsonOrder=  new JSONOrder(order);
                jsonResult.add( jsonOrder );
            }
            return jsonResult;
        }catch( SalesDemoDBException e){
            String msg = "Unexpected exception getting orders ";
            LOGGER.info(msg ,e);
            throw new SalesDemoException(msg, HttpResponseCode.HTTP_UNEXPECTED_ERROR);
        }
    }

    /**
     * The following method will return a reference to the order. 
     * @param orderId
     * @return
     * @throws SalesDemoException 
     */
    
    @Transactional(rollbackFor = SalesDemoException.class)
    public JSONOrder getOrder(long orderId) throws SalesDemoException {
        LOGGER.info("Getting order for order id ->" + orderId);
        try{
            Order order = orderDao.findById( orderId );
            
            if( order == null ){
                String msg = "Order with Id Not found.  ID ->" + orderId;
                LOGGER.info(msg);
                //Throw exception and set HTTP Response to NOT Found
                throw new SalesDemoException(msg, HttpResponseCode.HTTP_NOT_FOUND);
            }
            JSONOrder jsonResult=  new JSONOrder(order);
            return jsonResult;
        }catch( SalesDemoDBException e){
            String msg = "Unexpected exception getting order -> "+ orderId;
            LOGGER.info(msg ,e);
            throw new SalesDemoException(msg, HttpResponseCode.HTTP_UNEXPECTED_ERROR);
        }
    }

    /**
     * 
     * @return 
     */
    public AbstractDao<Product> getProductDao() {
        return productDao;
    }

    /**
     * 
     * @param dao 
     */
    @Autowired
    public void setProductDao(AbstractDao<Product> dao) {
        this.productDao = dao;
        this.productDao.setEntityClass( Product.class );
    }

    /**
     * 
     * @return 
     */
    public TaxProcessingService getTaxProcessingService() {
        return taxProcessingService;
    }

    /**
     * 
     * @param taxProcessingService 
     */
    public void setTaxProcessingService(TaxProcessingService taxProcessingService) {
        this.taxProcessingService = taxProcessingService;
    }
      
    /**
     * 
     * @return 
     */
    public AbstractDao<Order> getOrderDao() {
        return orderDao;
    }

    /**
     * 
     * @param dao 
     */
    @Autowired
    public void setOrderDao(AbstractDao<Order> dao) {
        this.orderDao = dao;
        this.orderDao.setEntityClass( Order.class );
    }

}
