package testingwithhsqldb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

public class SimpleDataAccessObject {

    private final DataSource myDataSource;

    public SimpleDataAccessObject(DataSource dataSource) {
        myDataSource = dataSource;
    }

    /**
     * Renvoie le nom d'un client à partir de son ID
     *
     * @param id la clé du client à chercher
     * @return le nom du client (LastName) ou null si pas trouvé
     * @throws SQLException
     */
    public String nameOfCustomer(int id) throws SQLException {
        String result = null;

        String sql = "SELECT LastName FROM Customer WHERE ID = ?";
        try (Connection myConnection = myDataSource.getConnection();
                PreparedStatement statement = myConnection.prepareStatement(sql)) {
            statement.setInt(1, id); // On fixe le 1° paramètre de la requête
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // est-ce qu'il y a un résultat ? (pas besoin de "while", 
                    // il y a au plus un enregistrement)
                    // On récupère les champs de l'enregistrement courant
                    result = resultSet.getString("LastName");
                }
            }
        }
        // dernière ligne : on renvoie le résultat
        System.out.println(result);
        return result;
    }

    public void InsertInProduct(int idProduit, String NomProduit, int PrixProduit) throws SQLException {
        String sql = "INSERT INTO PRODUCT VALUES(?,?,?)";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Définir la valeur du paramètre
            stmt.setInt(1, idProduit);
            stmt.setString(2, NomProduit);
            stmt.setInt(3, PrixProduit);
            stmt.executeUpdate();

        }

    }

    public ProductEntity FindProduct(int idProduit) throws SQLException {
        String sql = "SELECT * FROM PRODUCT WHERE id = ? ";
        ProductEntity PE = null;
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql);) {
            stmt.setInt(1, idProduit);
            try (ResultSet rs = stmt.executeQuery();) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("Name");
                    int prix = rs.getInt("Price");
                    PE = new ProductEntity(id, name, prix);
                }
            }
        }
        return PE;
    }

    public int numberOfProduct() throws SQLException {
        int result = 0;

        String sql = "SELECT COUNT(*) AS NUMBER FROM PRODUCT";
        // Syntaxe "try with resources" 
        // cf. https://stackoverflow.com/questions/22671697/try-try-with-resources-and-connection-statement-and-resultset-closing
        try (Connection connection = myDataSource.getConnection(); // Ouvrir une connexion
                Statement stmt = connection.createStatement(); // On crée un statement pour exécuter une requête
                ResultSet rs = stmt.executeQuery(sql) // Un ResultSet pour parcourir les enregistrements du résultat
                ) {
            if (rs.next()) { // Pas la peine de faire while, il y a 1 seul enregistrement
                // On récupère le champ NUMBER de l'enregistrement courant
                result = rs.getInt("NUMBER");
            }
        }

        return result;
    }

}
