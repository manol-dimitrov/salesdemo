
package org.sdl.salesdemo.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;



/**
 *
 * @author shannonlal
 */
public class SalesTaxDatabaseConfig {
    private static Logger LOGGER = Logger.getLogger(SalesTaxDatabaseConfig.class.getName());
    
    private static final String CREATE_PRODUCT_TABLE = "CREATE TABLE PRODUCT (ID INTEGER NOT "
            + "NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), PRODUCT_NAME "
            + "varchar(255) NOT NULL, PRICE DECIMAL(10,2) NOT NULL, SALES_TAX_EXEMPT SMALLINT "
            + "NOT NULL DEFAULT 0, IMPORT_DUTIES SMALLINT NOT NULL DEFAULT 0,CONSTRAINT "
            + "product_pk PRIMARY KEY (ID) )";
    
    private static final String CREATE_ITEM_TABLE = "CREATE TABLE ITEM (ID INTEGER NOT NULL "
            + "GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), PRODUCT_ID INTEGER "
            + "NOT NULL, QUANTITY INTEGER NOT NULL,TOTAL DECIMAL(10,2) NOT NULL, TAXES DECIMAL(10,2) "
            + "NOT NULL,CONSTRAINT product_type_fk FOREIGN KEY (PRODUCT_ID) REFERENCES PRODUCT(ID), "
            + "CONSTRAINT item_pk PRIMARY KEY (ID) )";
    
    private static final String CREATE_ORDERS_TABLE = "CREATE TABLE ORDERS (ID INTEGER NOT NULL "
            + "GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), SALES_TAX DECIMAL(10,2) "
            + "NOT NULL,SALES_TOTAL DECIMAL(10,2) NOT NULL, CONSTRAINT order_pk PRIMARY KEY (ID) )";

    private static final String CREATE_ORDER_ITEM_JOIN_TABLE = "CREATE TABLE ORDER_ITEMS (ORDER_ID "
            + "INTEGER NOT NULL, ITEM_ID INTEGER NOT NULL, SEQUENCE_ID INTEGER NOT NULL, CONSTRAINT "
            + "orders_fk FOREIGN KEY (ITEM_ID) REFERENCES ITEM(ID) ,CONSTRAINT items_fk FOREIGN KEY "
            + "(ITEM_ID) REFERENCES ITEM(ID),CONSTRAINT order_items_pk PRIMARY KEY (ORDER_ID,ITEM_ID) )";


    private BasicDataSource dataSource;
    private SessionFactory sessionFactory;
    private boolean dbCreated;
    
    public SalesTaxDatabaseConfig(){}
    
    /**
     * 
     * @param ds 
     */
    public SalesTaxDatabaseConfig( BasicDataSource ds, SessionFactory factory){
        dataSource = ds;
        dbCreated = false;
        sessionFactory = factory;
        initializeDatabase();
    }
    
    /**
     * The following method will drop the existing database tables,
     * recreate them and add in the product information
     */
    public void initializeDatabase(){
        LOGGER.log(Level.INFO, "Checking if DB is created");
        Connection conn  = null;
        try {
            conn = dataSource.getConnection();
            dropDatabaseTables( conn );
            createDatabase(conn);
            loadDefaultData(conn);
            
        } catch (SQLException ex) {
            //LOGGER.log(Level.INFO, "DB is not created", ex);
        }finally{
            try{
                if( ( conn != null ) && (!conn.isClosed()) ){
                    conn.close();
                }
            }catch( Exception e){}
        }

        
    }
    
    /**
     * The following method will attempt to drop the database tables
     * @param conn 
     */
    private void dropDatabaseTables( Connection conn ){
        dropExistingDatabaseTable("ORDER_ITEMS", conn);
        dropExistingDatabaseTable("ORDERS", conn);
        dropExistingDatabaseTable("ITEM", conn);
        dropExistingDatabaseTable("PRODUCT", conn);
    }
    
    /**
     * The following method will drop the existing data base table
     * @param tableName
     * @param conn 
     */
    private void dropExistingDatabaseTable(String tableName, Connection conn ){
        Statement stmt = null;
        try {
            boolean tableExists = false;
            stmt = conn.createStatement();
            try{
                stmt.executeQuery( "SELECT * FROM "+ tableName );
                tableExists = true;
            }catch( Exception e){
                LOGGER.log(Level.INFO,"Table does not exist  ->" + tableName);
            }
            if( tableExists){
                stmt.execute( "DROP TABLE " + tableName);
            }
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Unpexted exception creating DB Tables", ex);
        }finally{
            try{
                if((stmt != null) && (!stmt.isClosed())){
                    stmt.close();;
                }
            }catch( Exception e){
                LOGGER.log(Level.SEVERE,"Unexpected Exception closing statement",e);
            }
        } 
    }
    
    /**
     * Create the database table
     * @param conn
     */
    private void createDatabase( Connection conn){
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            
            stmt.execute( CREATE_PRODUCT_TABLE );
            stmt.execute( CREATE_ITEM_TABLE );
            stmt.execute( CREATE_ORDERS_TABLE );
            stmt.execute( CREATE_ORDER_ITEM_JOIN_TABLE );
            
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Unpexted exception creating DB Tables", ex);
        }finally{
            try{
                if((stmt != null) && (!stmt.isClosed())){
                    stmt.close();;
                }
            }catch( Exception e){
                LOGGER.log(Level.SEVERE,"Unexpected Exception closing statement",e);
            }
        }  
    }
    
    /**
     * The following method will load the database with the data
     * @param conn 
     */
    private void loadDefaultData(Connection conn){
        Statement stmt = null;
        try{
            stmt = conn.createStatement();

            stmt.execute("INSERT INTO PRODUCT (PRODUCT_NAME, PRICE, SALES_TAX_EXEMPT, IMPORT_DUTIES) VALUES ('book', 12.49, 1, 1)");
            stmt.execute("INSERT INTO PRODUCT (PRODUCT_NAME, PRICE, SALES_TAX_EXEMPT, IMPORT_DUTIES) VALUES ('music CD', 14.99, 0, 1)");
            stmt.execute("INSERT INTO PRODUCT (PRODUCT_NAME, PRICE, SALES_TAX_EXEMPT, IMPORT_DUTIES) VALUES ('chocolate bar', 0.85, 1, 1)");
            stmt.execute("INSERT INTO PRODUCT (PRODUCT_NAME, PRICE, SALES_TAX_EXEMPT, IMPORT_DUTIES) VALUES ('imported box of chocolates', 10.00, 1, 0)");            
            stmt.execute("INSERT INTO PRODUCT (PRODUCT_NAME, PRICE, SALES_TAX_EXEMPT, IMPORT_DUTIES) VALUES ('imported bottle of perfume', 47.50, 0, 0)");            
            stmt.execute("INSERT INTO PRODUCT (PRODUCT_NAME, PRICE, SALES_TAX_EXEMPT, IMPORT_DUTIES) VALUES ('imported bottle of perfume', 27.99, 0, 0)");            
            stmt.execute("INSERT INTO PRODUCT (PRODUCT_NAME, PRICE, SALES_TAX_EXEMPT, IMPORT_DUTIES) VALUES ('bottle of perfume', 18.99, 0, 1)");            
            stmt.execute("INSERT INTO PRODUCT (PRODUCT_NAME, PRICE, SALES_TAX_EXEMPT, IMPORT_DUTIES) VALUES ('packet of headache pills', 9.75, 1, 1)"); 
            stmt.execute("INSERT INTO PRODUCT (PRODUCT_NAME, PRICE, SALES_TAX_EXEMPT, IMPORT_DUTIES) VALUES ('box of imported chocolates', 11.25, 1, 0)"); 
             
        }catch( SQLException e){
            LOGGER.log(Level.SEVERE, "Unexpected error loading products into table", e);
            
        }finally{
            try{
                if( ( stmt != null)&& (!stmt.isClosed())){
                    stmt.close(); 
                }
            }catch( Exception e){
                LOGGER.log(Level.SEVERE, "Unexpected error loading products into table", e);
            }
        }
    }


}
