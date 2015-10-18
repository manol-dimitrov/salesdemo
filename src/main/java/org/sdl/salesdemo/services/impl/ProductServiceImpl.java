package org.sdl.salesdemo.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.log4j.Logger;
import org.sdl.salesdemo.common.SalesDemoDBException;
import org.sdl.salesdemo.common.SalesDemoException;
import org.sdl.salesdemo.common.SalesTaxConstants.*;
import org.sdl.salesdemo.dao.AbstractDao;
import org.sdl.salesdemo.domain.Product;
import org.sdl.salesdemo.domain.json.JSONProduct;
import org.sdl.salesdemo.services.ProductService;
import org.springframework.cache.annotation.Cacheable;

/**
 * The following is the product service implementation.  It will
 * implement the get products method
 * @author shannonlal
 */
@Service("productService")
public class ProductServiceImpl implements ProductService{
    public static final Logger LOGGER = Logger.getLogger(ProductServiceImpl.class.getName());
    
    public AbstractDao<Product> productDao;
    
    public ProductServiceImpl(){
        
    }

    /**
     * The following method will call the product dao to query the 
     * database to return a list of products
     * @return List<Product>
     * @exception SalesDemoException
     */
    @Cacheable(value="products")
    @Transactional
    public List<JSONProduct> getProducts() throws SalesDemoException{
        //TODO Implement Caching on this method
        LOGGER.info("Getting products");
        try{
            List<Product> products =  productDao.getTypes();
            
            List<JSONProduct> jsonResult = new ArrayList<JSONProduct>();
            for (Product product : products) {
                JSONProduct jsonProduct=  new JSONProduct(product);
                jsonResult.add( jsonProduct );
            }
            return jsonResult;
        }catch(SalesDemoDBException e){
            String msg = "There was an unexpected exception while return a list of products ->" + e.getMessage();
            LOGGER.error(msg ,e);
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
        this.productDao.setEntityClass(Product.class);
    }
}
