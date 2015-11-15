package test.unit.org.sdl.salesdemo.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.sdl.salesdemo.dao.AbstractDao;
import org.sdl.salesdemo.domain.Product;
import org.sdl.salesdemo.services.impl.ProductServiceImpl;
import org.sdl.salesdemo.common.SalesDemoDBException;
import org.sdl.salesdemo.common.SalesDemoException;
import org.sdl.salesdemo.common.SalesTaxConstants.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import org.sdl.salesdemo.domain.json.JSONProduct;

/**
 * The following is the test case for the product service implementation
 * It will test cases of getting products and an error case
 * @author shannonlal
 */
public class ProductServicesImplTestCase {
    
    private AbstractDao<Product> productDao;
    private ProductServiceImpl productService;
    
    @Before
    public void setUp(){
        productDao = Mockito.mock(AbstractDao.class);
        productService = new ProductServiceImpl();
        productService.setProductDao(productDao);
    }
    
    @Test
    public void shouldGetAListOfProducts(){
        List<Product> products = new ArrayList<Product>();
        Product p = new Product();
        p.setId(1L);
        p.setImportDutiesExempt(true);
        p.setName("Candy");
        BigDecimal bd = new BigDecimal(10.01);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        p.setPrice(bd);
        p.setSalesTaxExempt(false);
        products.add(p);
        
        Product p2 = new Product();
        p2.setId(2L);
        p2.setImportDutiesExempt(true);
        p2.setName("Salmon");
        BigDecimal salmonPrice = new BigDecimal(15.01);
        salmonPrice.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        p2.setPrice( salmonPrice );
        p2.setSalesTaxExempt(false);
        
        products.add( p2);
        try{
            when( productDao.getTypes()).thenReturn(products);
            
            List<JSONProduct> serviceProducts = productService.getProducts();
            assertNotNull( serviceProducts );
            assertEquals( new Integer( 2), new Integer( serviceProducts.size()));
            
            verify(productDao).getTypes();
        }catch( SalesDemoException e){
            fail("Unexpected Exception when getting products");
        }catch( SalesDemoDBException e){
            fail("Unexpected DB Exception when getting products");            
        }
    }
    
    @Test
    public void shouldFailGettingListOfProductsDueToDBException(){

        try{
            when( productDao.getTypes()).thenThrow(new SalesDemoDBException("Exception"));
            
            productService.getProducts();

        }catch( SalesDemoException e){
            assertNotNull(e);
            assertEquals( e.getHttpResponseCode(), HttpResponseCode.HTTP_UNEXPECTED_ERROR);
        }catch( SalesDemoDBException e){
            fail("Unexpected DB Exception when getting products");            
        }
    }
}
