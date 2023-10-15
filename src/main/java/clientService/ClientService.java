package clientService;

import dbConfig.DatabasePostgres;
import entities.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientService {
    private final Connection connection;
    private final PreparedStatement createPrepSt;
    private final PreparedStatement getByIdPrepSt;
    private final PreparedStatement getIdByNamePrepSt;
    private final PreparedStatement setNamePrepSt;
    private final PreparedStatement deleteByIdPrepSt;

    public ClientService(DatabasePostgres databasePostgres) throws SQLException {
        connection = databasePostgres.getConnection();
        createPrepSt = connection.prepareStatement("INSERT INTO client (name) VALUES (?)");
        getByIdPrepSt = connection.prepareStatement("SELECT id, name FROM client WHERE id = ?");
        getIdByNamePrepSt = connection.prepareStatement("SELECT id FROM client WHERE name = ?");
        setNamePrepSt = connection.prepareStatement("UPDATE client SET name = ? WHERE id = ?");
        deleteByIdPrepSt = connection.prepareStatement("DELETE FROM client WHERE id = ?");
    }

    public static void main(String[] args) throws SQLException {
        DatabasePostgres database = DatabasePostgres.getInstance();
        ClientService clientService = new ClientService(database);

        System.out.println("All clients \n ---------");
        System.out.println(clientService.listAll().toString());

        System.out.println("------\n New client add\n------");
        System.out.println(clientService.create("Ive")+" - id num for new client Ive");

        System.out.println("------\n Getting client by id\n------");
        System.out.println(clientService.getByID(2));

        System.out.println("------\n Change client name\n------");
        clientService.setName(2, "Petro");
        System.out.println(clientService.getByID(2));

        System.out.println("------\n Deleting client id=6\n------");
        clientService.deleteById(6);
        System.out.println(clientService.getByID(6));
        System.out.println(clientService.listAll().toString());

    }

    public long create (String name) throws SQLException {
        try {
            createPrepSt.setString(1,name);
            createPrepSt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        getIdByNamePrepSt.setString(1,name);
        try(ResultSet rs = getIdByNamePrepSt.executeQuery()){
            if(!rs.next()){
                System.out.println("Client with name " + name + " not found!");
                return -1;
            }
            return rs.getLong("id");
        }  catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public String getByID(long id){
        try {
            getByIdPrepSt.setLong(1, id);
            try(ResultSet rs = getByIdPrepSt.executeQuery()){
                if(!rs.next()){
                    System.out.println("Client with id " + id + " not found!");
                    return null;
                }
                return rs.getLong(1) + " " + rs.getString(2);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setName(long id, String name){
        try{
            setNamePrepSt.setString(1,name);
            setNamePrepSt.setLong(2,id);
            setNamePrepSt.executeUpdate();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    public void deleteById(long id){
        try {
            deleteByIdPrepSt.setLong(1, id);
            deleteByIdPrepSt.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public List<Client> listAll(){
        List<Client> clients = new ArrayList<>();
        String query = "SELECT id, name FROM client ORDER BY id";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                clients.add(new Client(rs.getLong(1), rs.getString(2)));
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return clients;
    }
}
