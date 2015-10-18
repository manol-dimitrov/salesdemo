package org.sdl.salesdemo.services;

import java.util.List;

import org.sdl.salesdemo.common.SalesTaxException;
import org.sdl.salesdemo.domain.json.JSONProduct;

/**
 * The following interface defines the service methods for
 * a product
 * @author shannonlal
 */
public interface ProductService {
    
    public List<JSONProduct> getProducts()throws SalesTaxException;
}
