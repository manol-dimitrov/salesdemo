package test.unit.org.sdl.salesdemo.dao.hibernate;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import org.sdl.salesdemo.domain.Product;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sdl.salesdemo.common.SalesDemoDBException;
import org.sdl.salesdemo.dao.hibernate.GenericHibernateDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;



/**
 * The following is the hibernate dao test case for product.  It will 
 * test implementing the different dao methods against a derby db
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ProductHibernateDaoTestCase extends SalesTaxAbstractHibernateDaoTestCase {
    public static final Logger LOGGER = Logger.getLogger(ProductHibernateDaoTestCase.class.getName());
        
           
    private GenericHibernateDao<Product> productDao;

    public ProductHibernateDaoTestCase(){
        super( );
    }

    @Test
    public void shouldGetAListOfProductTypes(){
        LOGGER.info("Start Get Products");
        try{
            assertNotNull(productDao);
            List<Product> products = productDao.getTypes();

            assertNotNull( products );
            assertEquals(3, products.size());
            Product product = products.get(0);
            
            assertNotNull( product );
            assertTrue( product.getId() > 0);
            assertEquals( "Chocolat Bar", product.getName() );
            MathContext context = new MathContext(2, RoundingMode.HALF_DOWN);
            BigDecimal price = new BigDecimal(0.85,context);
            price.setScale(2);
            
            assertEquals( price, product.getPrice());
            assertFalse( product.isSalesTaxExempt());
            assertTrue( product.isImportDutiesExempt());

        }catch(Exception e){
            LOGGER.fatal("Unexpected Error", e);
            fail("Exception Caught" + e.getMessage() );
        }
    }	
    
    @Test
    public void shouldGetAProductBasedOnId(){
        LOGGER.info("Start Get A Product based on Id");
        try{
            assertNotNull(productDao);
            List<Product> products = productDao.getTypes();
            Product p = products.get(0);
            Product product = productDao.findById( p.getId());

            assertNotNull( product );
            assertEquals( p.getName(), product.getName() );
            assertEquals( p.getPrice(), product.getPrice());
            assertEquals(p.isSalesTaxExempt(), product.isSalesTaxExempt());
            assertEquals(p.isImportDutiesExempt(), product.isImportDutiesExempt());


        }catch(SalesDemoDBException e){
            LOGGER.fatal("Unexpected Error", e);
            fail("Exception Caught" + e.getMessage() );
        }
    }
    
    @Test
    public void shouldCreateAProduct(){
        LOGGER.info("Start Create A Product");
        try{
            assertNotNull(productDao);
            Product product = new Product();
            product.setImportDutiesExempt(true);
            product.setSalesTaxExempt(false);
            product.setName("Poutine");
            BigDecimal price = new BigDecimal(13.50);
            price = price.setScale( 2, RoundingMode.HALF_EVEN);
            //price.setScale(2);
            product.setPrice(price);
            Product p = productDao.saveOrUpdate(product);

            assertNotNull( p );
            assertEquals( "Poutine", p.getName() );
            assertEquals( price, p.getPrice());
            assertFalse( p.isSalesTaxExempt());
            assertTrue( p.isImportDutiesExempt());


        }catch(SalesDemoDBException e){
            LOGGER.fatal("Unexpected Error", e);
            fail("Exception Caught" + e.getMessage() );
        }
    }
    
    @Test
    public void shouldUpdateAProduct(){
        LOGGER.info("Start Update A Product");
        try{
            assertNotNull(productDao);
            Product product = new Product();
            product.setImportDutiesExempt(true);
            product.setSalesTaxExempt(false);
            product.setName("Poutine");
            BigDecimal price1 = new BigDecimal( 13.50 );
            price1 = price1.setScale(2, RoundingMode.HALF_EVEN);
            product.setPrice(price1);
            Product p = productDao.saveOrUpdate(product);
            
            BigDecimal price2 = new BigDecimal( 15.00);
            price2 = price2.setScale(2, RoundingMode.HALF_EVEN);
            p.setPrice( price2 );
            p = productDao.saveOrUpdate(p);

            assertNotNull( p );
            assertEquals( "Poutine", p.getName() );
            assertEquals( price2, p.getPrice());
            assertFalse( p.isSalesTaxExempt());
            assertTrue( p.isImportDutiesExempt());


        }catch(SalesDemoDBException e){
            LOGGER.fatal("Unexpected Error", e);
            fail("Exception Caught" + e.getMessage() );
        }
    }
  
    
    @Override
    protected String getDataSetFileName() {
        return "src//test//resources//datasets//Orders-DBUnit.xml";
    }

    public GenericHibernateDao<Product> getProductDao() {
        return productDao;
    }

    @Autowired 
    public void setProductDao(GenericHibernateDao<Product> dao) {
        this.productDao = dao;
        this.productDao.setEntityClass( Product.class);
    }

}
