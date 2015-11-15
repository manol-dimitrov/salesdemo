package test.unit.org.sdl.salesdemo.dao.hibernate;

import java.math.BigDecimal;
import java.util.List;
import org.sdl.salesdemo.domain.Product;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sdl.salesdemo.common.SalesDemoDBException;
import org.sdl.salesdemo.dao.hibernate.GenericHibernateDao;
import org.sdl.salesdemo.domain.Item;
import org.sdl.salesdemo.domain.Order;
import org.sdl.salesdemo.common.SalesTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * The following is the hibernate dao test case for orders. It will test
 * the different dao methods against a derby db
 * 
 * @author shannonlal
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class OrderHibernateDaoTestCase extends SalesTaxAbstractHibernateDaoTestCase {

    public static final Logger LOGGER = Logger.getLogger(OrderHibernateDaoTestCase.class.getName());

    private GenericHibernateDao<Order> orderDao;
    private GenericHibernateDao<Product> productDao;

    public OrderHibernateDaoTestCase() {
        super();
    }
    

    @Test
    public void shouldCreateAnOrderWithOneItem() {
        LOGGER.info("Start Create An Order");
        try {
            assertNotNull(productDao);
            Product product = productDao.getTypes().get(0);
            assertNotNull(product);

            Order o = createDefaultOrder(1);

            assertNotNull(orderDao);

            o = orderDao.saveOrUpdate(o);

            assertNotNull(o);
            assertTrue(o.getId() > 0);

            Order order = orderDao.findById(o.getId());
            assertNotNull(order);
            List<Item> items = order.getItems();
            assertNotNull(items);
            assertFalse(items.isEmpty());
            assertEquals(new Integer(1), new Integer(items.size()));
            Item it = items.get(0);

            assertNotNull(it);
            assertEquals(o.getSalesTax(), order.getSalesTax());
            assertEquals(o.getTotal(), order.getTotal());

        } catch (SalesDemoDBException e) {
            LOGGER.fatal("Unexpected Error", e);
            fail("Exception Caught" + e.getMessage());
        }
    }

    @Test
    public void shouldCreateAnOrderWithNoItems() {
        LOGGER.info("Start Create An Order With No Items");
        try {
            assertNotNull(productDao);
            Product product = productDao.getTypes().get(0);
            assertNotNull(product);

            Order o = createDefaultOrder(0);

            assertNotNull(orderDao);

            o = orderDao.saveOrUpdate(o);

            assertNotNull(o);
            assertTrue(o.getId() > 0);

            Order order = orderDao.findById(o.getId());
            assertNotNull(order);
            List<Item> items = order.getItems();
            assertNotNull(items);
            assertTrue(items.isEmpty());

            assertEquals(o.getSalesTax(), order.getSalesTax());
            assertEquals(o.getTotal(), order.getTotal());

        } catch (SalesDemoDBException e) {
            LOGGER.fatal( "Unexpected Error", e);
            fail("Exception Caught" + e.getMessage());
        }
    }
    @Test
    public void shouldCreateAnOrderWithMultipleItem() {
        LOGGER.info("Start Create An Order With Multiple Items");
        try {
            assertNotNull(productDao);
            Product product = productDao.getTypes().get(0);
            assertNotNull(product);

            Order o = createDefaultOrder(2);

            assertNotNull(orderDao);

            o = orderDao.saveOrUpdate(o);

            assertNotNull(o);
            assertTrue(o.getId() > 0);

            Order order = orderDao.findById(o.getId());
            assertNotNull(order);
            List<Item> items = order.getItems();
            assertNotNull(items);
            assertFalse(items.isEmpty());
            assertEquals(new Integer(2), new Integer(items.size()));
            Item it = items.get(0);
            
            assertNotNull(it);
            assertNotNull( it.getProduct());
            assertEquals(o.getSalesTax(), order.getSalesTax());
            assertEquals(o.getTotal(), order.getTotal());

        } catch (SalesDemoDBException e) {
            LOGGER.fatal("Unexpected Error", e);
            fail("Exception Caught" + e.getMessage());
        }
    }
    
    
    @Test
    public void shouldAddItemToOrder() {
        LOGGER.info("Start Add an Item to an Order");
        try {
            assertNotNull(productDao);

            Order o = createDefaultOrder(1);

            assertNotNull(orderDao);

            o = orderDao.saveOrUpdate(o);

            assertNotNull(o);
            assertTrue(o.getId() > 0);

            Order order = orderDao.findById(o.getId());
            
            
            assertNotNull(order);
            
            Product product = productDao.getTypes().get(1);
            Item item = new Item();
            item.setProduct(product);
            item.setQuantity(1);
            order.addItem(item);
            
            order = orderDao.saveOrUpdate(order);
            assertNotNull(order);
            List<Item> items = order.getItems();
            assertNotNull(items);
            assertFalse(items.isEmpty());
            assertEquals(new Integer(2), new Integer(items.size()));
            Item it = items.get(0);
            
            assertNotNull(it);
            assertNotNull( it.getProduct());
            assertEquals(o.getSalesTax(), order.getSalesTax());
            assertEquals(o.getTotal(), order.getTotal());

        } catch (SalesDemoDBException e) {
            LOGGER.error("Unexpected Error", e);
            fail("Exception Caught" + e.getMessage());
        }
    }

    /**
     * The following method will create an order with the number of products
     *
     * @param numberOfProducts
     * @return
     * @throws SalesTaxDBException
     */
    private Order createDefaultOrder(int numberOfProducts) throws SalesDemoDBException {

        //Create the Order
        Order order = new Order();
        BigDecimal salesTax = new BigDecimal(0);
        BigDecimal total = new BigDecimal(0);
        for (int i = 0; i < numberOfProducts; i++) {
            Product product = productDao.getTypes().get(i);
            Item item = new Item();
            item.setProduct(product);
            item.setQuantity(1);

            BigDecimal tax = product.getPrice().multiply(SalesTaxConstants.Taxes.SALES_TAX.getTaxRate());
            BigDecimal lineTotal = product.getPrice().add(tax);
            item.setLineTotal( lineTotal );
            item.setTaxes(tax);
            order.addItem(item);
            
            salesTax = salesTax.add( tax);
            BigDecimal subTotal = product.getPrice().add(salesTax);
            total = total.add( subTotal);
        }

        order.setSalesTax(salesTax);
        order.setTotal(total);

        return order;

    }

    @Override
    protected String getDataSetFileName() {
        return "src//test//resources//datasets//Orders-DBUnit.xml";
    }

    /**
     *
     * @return
     */
    public GenericHibernateDao<Order> getOrderDao() {
        return orderDao;
    }

    /**
     *
     * @param dao
     */
    @Autowired
    public void setOrderDao(GenericHibernateDao<Order> dao) {
        this.orderDao = dao;
        this.orderDao.setEntityClass(Order.class);
    }

    /**
     *
     * @return
     */
    public GenericHibernateDao<Product> getProductDao() {
        return productDao;
    }

    /**
     *
     * @param dao
     */
    @Autowired
    public void setProductDao(GenericHibernateDao<Product> dao) {
        this.productDao = dao;
        this.productDao.setEntityClass(Product.class);
    }

}
