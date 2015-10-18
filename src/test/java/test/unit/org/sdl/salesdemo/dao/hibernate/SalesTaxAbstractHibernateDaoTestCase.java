
package test.unit.org.sdl.salesdemo.dao.hibernate;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.derby.tools.ij;
import org.dbunit.DatabaseTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * The following is the base class for the sales tax hibernate dao tests. This
 * base test class will be used to create the derby database which will be used
 * for testing
 * 
 * @author shannonlal
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/userManagementApplicationContext.xml" })
@Transactional
public abstract class SalesTaxAbstractHibernateDaoTestCase extends DatabaseTestCase {
	public static final Logger LOGGER = Logger.getLogger(SalesTaxAbstractHibernateDaoTestCase.class.getName());

	private final String DB_URL = "jdbc:derby:memory:testDB;create=true";

	private final String DRIVER_CLASS = "org.apache.derby.jdbc.EmbeddedDriver";
	private final String DB_CREATE_SCRIPT = "src//test//resources//sql//createSalesTax-DB-Derby.sql";

	@Autowired
	protected SessionFactory sessionFactory;

	public SalesTaxAbstractHibernateDaoTestCase() {
		super();
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, DRIVER_CLASS);
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, DB_URL);
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, "");
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, "");
	}

	@Override
	@Before
	@Transactional
	public void setUp() throws Exception {
		// Drop and Create Tables
		dropDB();
		createDB();

		// Initialize Data for DB
		IDatabaseConnection conn = getConnection();
		// initialize your dataset here
		IDataSet dataSet = getDataSet();

		try {
			DatabaseOperation.CLEAN_INSERT.execute(conn, dataSet); // Import
																	// your data
		} catch (Exception e) {
			LOGGER.log(Level.INFO, "Unexpected Error ", e);
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, "Unexpected Error ", e);
			}
		}

	}

	@Override
	@After
	public void tearDown() throws Exception {
		dropDB();
	}

	/**
	 * The following method will return the customized DataSet file
	 * 
	 * @return
	 */
	protected abstract String getDataSetFileName();

	@Override
	protected IDataSet getDataSet() throws Exception {
		String fileName = getDataSetFileName();
		FileInputStream file = new FileInputStream(fileName);
		IDataSet dataSet = new FlatXmlDataSet(file);
		return dataSet;
	}

	@Override
	protected IDatabaseConnection getConnection() throws Exception {
		Class.forName(DRIVER_CLASS);
		Connection jdbcConnection = DriverManager.getConnection(DB_URL);

		return new DatabaseConnection(jdbcConnection);
	}

	/**
	 * 
	 * The following method will drop DB
	 */
	private void dropDB() {
		Connection connection = null;
		try {
			Class.forName(DRIVER_CLASS);
			connection = DriverManager.getConnection(DB_URL);
			connection.setAutoCommit(true);
		} catch (Exception e) {
			LOGGER.log(Level.INFO, "Unexpected Error ", e);
		} finally {
			try {
				connection.close();
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, "Unexpected Error ", e);
			}
		}
	}

	/**
	 * The following method will create the database for the test case
	 */
	private void createDB() {
		// Create DB
		Connection connection = null;
		try {
			Class.forName(DRIVER_CLASS);

			connection = DriverManager.getConnection(DB_URL);
			connection.setAutoCommit(true);

			// Check if DB exists
			boolean dbExist = false;
			try {
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery("select * from ORDER");

				if ((rs != null) && (rs.next())) {
					LOGGER.log(Level.SEVERE, "Database already exists");
					dbExist = true;
				}
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, "Database does not exist");
			}

			if (!dbExist) {
				try {
					// DB does not exists
					FileInputStream file = new FileInputStream(DB_CREATE_SCRIPT);
					ij.runScript(connection, file, "UTF-8", System.out, "UTF-8");
				} catch (FileNotFoundException e) {
					LOGGER.log(Level.SEVERE, "Error creating the ->" + e.getMessage());
				} catch (UnsupportedEncodingException e) {
					LOGGER.log(Level.SEVERE, "Error creating the ->" + e.getMessage());
				}
			}

		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Unexpected Error ", e);
		} finally {
			try {
				connection.close();
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, "Unexpected Error ", e);
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * 
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
