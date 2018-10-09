package testingwithhsqldb;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import org.hsqldb.cmdline.SqlFile;
import org.hsqldb.cmdline.SqlToolError;

public class HsqlDBTest {

    private static DataSource myDataSource;
    private static Connection myConnection;

    private SimpleDataAccessObject myObject;

    @Before
    public void setUp() throws IOException, SqlToolError, SQLException {
        // On crée la connection vers la base de test "in memory"
        myDataSource = getDataSource();
        myConnection = myDataSource.getConnection();
        // On crée le schema de la base de test
        executeSQLScript(myConnection, "schema.sql");
        // On y met des données
        executeSQLScript(myConnection, "bigtestdata.sql");

        myObject = new SimpleDataAccessObject(myDataSource);
    }

    private void executeSQLScript(Connection connexion, String filename) throws IOException, SqlToolError, SQLException {
        // On initialise la base avec le contenu d'un fichier de test
        String sqlFilePath = HsqlDBTest.class.getResource(filename).getFile();
        SqlFile sqlFile = new SqlFile(new File(sqlFilePath));
        sqlFile.setConnection(connexion);
        sqlFile.execute();
        sqlFile.closeReader();
    }

    @After
    public void tearDown() throws IOException, SqlToolError, SQLException {
        myConnection.close();
        myObject = null; // Pas vraiment utile

    }

    @Test
    public void findExistingCustomer() throws SQLException {
        String name = myObject.nameOfCustomer(0);
        assertNotNull("Customer exists, name should not be null", name);
        assertEquals("Bad name found !", "Steel", name);
    }

    @Test
    public void nonExistingCustomerReturnsNull() throws SQLException {
        String name = myObject.nameOfCustomer(-1);
        assertNull("name should be null, customer does not exist !", name);
    }

    @Test
    public void testAjoutProduit() throws SQLException {
        myObject.InsertInProduct(142, "OuiOui", 145);
        System.out.println("NOMBRE DE PRODUIT " + " " + myObject.numberOfProduct());
        assertEquals(myObject.numberOfProduct(), 51);
    }
    
    @Test
    public void testTrouverProduit() throws SQLException {
        ProductEntity result = myObject.FindProduct(0);
        System.out.println(result.getName());
        assertEquals("Iron Iron", result.getName());
        
    }
    
    @Test (expected = SQLException.class)
    public void testAjoutProduitNegativePrice() throws SQLException {
        myObject.InsertInProduct(1000000, "negativ", -145);
        System.out.println("NOMBRE DE PRODUIT " + " " + myObject.numberOfProduct());
    }

    public static DataSource getDataSource() throws SQLException {
        org.hsqldb.jdbc.JDBCDataSource ds = new org.hsqldb.jdbc.JDBCDataSource();
        ds.setDatabase("jdbc:hsqldb:mem:testcase;shutdown=true");
        ds.setUser("sa");
        ds.setPassword("sa");
        return ds;
    }
}
