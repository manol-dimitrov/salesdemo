package org.sdl.salesdemo.controllers;

import java.util.logging.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * The following is the view controller for the Sales Demo application.  This
 * controller will handle all the request to load the pages 
 * 
 * @author shannonlal
 */
@Controller
public class ViewController {
    public static final Logger LOGGER = Logger.getLogger(ViewController.class.getName());


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

}
