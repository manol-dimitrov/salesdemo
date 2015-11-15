package org.sdl.salesdemo.controllers;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sdl.salesdemo.common.SalesDemoException;
import org.sdl.salesdemo.domain.json.JSONOrder;
import org.sdl.salesdemo.domain.json.JSONProduct;
import org.sdl.salesdemo.services.OrderService;
import org.sdl.salesdemo.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The following is the order controller for the Sales Tax application.  This
 * controller will handle all the request to load the pages and process
 * REST requests
 * @author shannonlal
 */
@RequestMapping(value = {"/api"})
@Controller
public class APIController {
    public static final Logger LOGGER = Logger.getLogger(APIController.class.getName());

    @Autowired
    private ProductService productService;
    
    @Autowired
    private OrderService orderService;


    
    /**
     * The following method will handle the REST requests to get a list of the 
     * products.  It will return the products a list of JSON data.  It
     * @return List<JSONProduct>
     * @exception SalesDemoException
     */
    @ResponseStatus( HttpStatus.OK)
    @RequestMapping(value = {"/product/list"}, method = RequestMethod.GET)
    public @ResponseBody List<JSONProduct> getProducts() throws SalesDemoException{
        LOGGER.info("Start get products");
        List<JSONProduct> jsonResult = productService.getProducts();

        return jsonResult;
    }
    
     /**
     * The following method will handle the REST requests to get a list of the 
     * orders.  It will return the products a list of JSON data. 
     * @return List<JSONOrder>
     * @exception SalesDemoException
     */
    @ResponseStatus( HttpStatus.OK)
    @RequestMapping(value = {"/order/list"}, method = RequestMethod.GET)
    public @ResponseBody List<JSONOrder> getOrders() throws SalesDemoException{
        LOGGER.info("Start get products");
        List<JSONOrder> jsonResult = orderService.getOrders();
        return jsonResult;        
    }
    
     /**
     * The following method will handle the REST requests to get a single
     * order.   
     * @return JSONOrder
     * @exception SalesDemoException
     */
    @ResponseStatus( HttpStatus.OK)
    @RequestMapping(value = {"/order/{orderId}"}, method = RequestMethod.GET)
    public @ResponseBody JSONOrder getOrder(@PathVariable("orderId") Long orderId) throws SalesDemoException{
        LOGGER.info("Start get order ->"+ orderId);
        JSONOrder jsonResult = orderService.getOrder(orderId);

        return jsonResult;        
    }
    
    
    /**
     * The following method will handle the request to update an order.  If an 
     * order exists it will create it otherwise it will just update it.
     * @param jsonOrder
     * @return JSONOrder
     * @throws SalesDemoException 
     */    
    @RequestMapping(value = "/order/update", method = RequestMethod.POST)
    public @ResponseBody JSONOrder updateOrder( @RequestBody JSONOrder jsonOrder ) throws SalesDemoException{
        LOGGER.info("Start update order ");
        JSONOrder jsonResult = orderService.updateOrder(jsonOrder);
        
        return jsonResult;
    }
    /**
     * The following method will handle the Sales Tax Exception and set the Http
     * response code
     * @param req
     * @param response
     * @param exception
     * @return
     */
    @ExceptionHandler(SalesDemoException.class)
    @ResponseBody
    public String handleError(HttpServletRequest req, HttpServletResponse response, SalesDemoException exception) {
            LOGGER.log(Level.WARNING, "Request: " + req.getRequestURL() + " raised " + exception);

            String errorMsg = exception.getMessage();

            response.setStatus( exception.getHttpResponseCode().getHttpCode());

            return errorMsg;
    }

    /**
     * The following method will return the product service
     * @return 
     */
    public ProductService getProductService() {
        return productService;
    }

    /**
     * The following method will add the product service
     * @param productService 
     */
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 
     * @return 
     */
    public OrderService getOrderService() {
        return orderService;
    }

    /**
     * 
     * @param orderService 
     */
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }
}
