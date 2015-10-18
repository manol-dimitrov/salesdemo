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
import org.springframework.web.servlet.ModelAndView;

/**
 * The following is the order controller for the Sales Tax application.  This
 * controller will handle all the request to load the pages and process
 * REST requests
 * @author shannonlal
 */
@Controller
public class OrderController {
    public static final Logger LOGGER = Logger.getLogger(OrderController.class.getName());

    @Autowired
    private ProductService productService;
    
    @Autowired
    private OrderService orderService;

    /**
     * The following method will return a reference to the main index
     * page
     * @return 
     */
    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public ModelAndView defaultPage() {
        LOGGER.info("Load the default page");
        ModelAndView model = new ModelAndView();
        model.setViewName("index");
        return model;

    }
    
     /**
     * The following method will return a reference to the update order
     * JSP Page
     * @return 
     */
    @RequestMapping(value = {"/viewOrder"}, method = RequestMethod.GET)
    public ModelAndView loadViewOrder() {
        LOGGER.info("Load the update order page");
        ModelAndView model = new ModelAndView();
        model.setViewName("updateOrder");
        return model;

    }

     /**
     * The following method will return a reference to the view products
     * JSP Page
     * @return 
     */
    @RequestMapping(value = {"/viewProducts"}, method = RequestMethod.GET)
    public ModelAndView loadViewProducts() {
        LOGGER.info("Load the view products page");
        ModelAndView model = new ModelAndView();
        model.setViewName("products");
        return model;

    }
    
         /**
     * The following method will return a reference to the orders
     * JSP Page
     * @return 
     */
    @RequestMapping(value = {"/viewOrders"}, method = RequestMethod.GET)
    public ModelAndView loadViewOrders() {
        LOGGER.info("Load the view orders page");
        ModelAndView model = new ModelAndView();
        model.setViewName("orders");
        return model;
    }
    
    /**
     * The following method will handle the REST requests to get a list of the 
     * products.  It will return the products a list of JSON data.  It
     * @return List<JSONProduct>
     * @exception SalesDemoException
     */
    @ResponseStatus( HttpStatus.OK)
    @RequestMapping(value = {"/rest/products"}, method = RequestMethod.GET)
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
    @RequestMapping(value = {"/rest/orders"}, method = RequestMethod.GET)
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
    @RequestMapping(value = {"/rest/orders/{orderId}"}, method = RequestMethod.GET)
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
    @RequestMapping(value = "/rest/orders/update", method = RequestMethod.POST)
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
