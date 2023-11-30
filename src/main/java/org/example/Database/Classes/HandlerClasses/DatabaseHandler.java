package org.example.Database.Classes.HandlerClasses;

import org.example.Database.Classes.ClassesForDatabase.Tables.*;
import org.example.Database.CursachN1.Const;
import org.example.Database.CursachN1.User;
import org.example.Database.CursachN1.hashClass;
import org.example.Database.Enums.ConfigEnums.DatabaseConfigs;
import org.example.Database.Enums.EnumsForDatabase.Tables.*;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;

public class DatabaseHandler {
    private Connection connection;
    private ResultSet resSet;
    private PreparedStatement prSt;

    private String comandString;

    public Connection getDbConnection() {
        String connectionString = "jdbc:mysql://" + DatabaseConfigs.HOST.getTitle() + ":" + DatabaseConfigs.PORT.getTitle() + "/" + DatabaseConfigs.NAME.getTitle();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            connection = DriverManager.getConnection(connectionString, DatabaseConfigs.USER.getTitle(), DatabaseConfigs.PASSWORD.getTitle());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

        return connection;
    }

    public ResultSet selectWorkers() {
        comandString = "SELECT * FROM "+ Tables.WORKERS.getTitle();
        try {
            prSt = getDbConnection().prepareStatement(comandString);

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resSet;
    }

    public ResultSet selectMakers() {
        comandString = "SELECT * FROM "+ Tables.MAKERS.getTitle();
        try {
            prSt = getDbConnection().prepareStatement(comandString);

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resSet;
    }

    public ResultSet selectContracts() {
        comandString = "SELECT * FROM "+ Tables.CONTRACTS.getTitle();
        try {
            prSt = getDbConnection().prepareStatement(comandString);

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resSet;
    }

    public ResultSet selectGoods() {
        comandString = "SELECT * FROM "+ Tables.GOODS.getTitle();
        try {
            prSt = getDbConnection().prepareStatement(comandString);

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resSet;
    }

    public ResultSet selectLawyers() {
        comandString = "SELECT * FROM "+ Tables.LAWYERS.getTitle();
        try {
            prSt = getDbConnection().prepareStatement(comandString);

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resSet;
    }

    public ResultSet selectLawfirms() {
        comandString = "SELECT * FROM "+ Tables.LAWFIRMS.getTitle();
        try {
            prSt = getDbConnection().prepareStatement(comandString);

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resSet;
    }

    public void deleteMaker(MakerTable makerTable) {
        comandString = "DELETE FROM "+ Tables.MAKERS.getTitle()+" WHERE id=?";
        try {
            prSt = getDbConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(makerTable.getIdMakers()));
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteWorker(WorkerTable workerTable) {
        comandString = "DELETE FROM "+ Tables.WORKERS.getTitle()+" WHERE id=?";
        try {
            prSt = getDbConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(workerTable.getIdWorkers()));
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteContract(ContractTable contractTable) {
        comandString = "DELETE FROM "+ Tables.CONTRACTS.getTitle()+" WHERE id=?";
        try {
            prSt = getDbConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(contractTable.getIdContracts()));
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteGood(GoodTable goodTable) {
        comandString = "DELETE FROM "+ Tables.GOODS.getTitle()+" WHERE id=?";
        try {
            prSt = getDbConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(goodTable.getIdGoods()));
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteLawyer(LawyerTable lawyerTable) {
        comandString = "DELETE FROM "+ Tables.LAWYERS.getTitle()+" WHERE id=?";
        try {
            prSt = getDbConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(lawyerTable.getIdLawyers()));
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteLawfirm(LawfirmTable lawfirmTable) {
        comandString = "DELETE FROM "+ Tables.LAWFIRMS.getTitle()+" WHERE id=?";
        try {
            prSt = getDbConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(lawfirmTable.getIdLawFirms()));
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /////////////////////////////////////
    ////////////////////////////////////
    ////////////////////////////////////
    ////////////////////////////////////


    /*public LawyerTable insertAndGetBrand(LawyerTable brandTable) {
        comandString = "INSERT INTO "+Tables.WORKERS.getTitle()+"("+ Workers.FIO.getTitle()+") VALUES (?)";
        try {
            prSt = getDbConnection().prepareStatement(comandString);
            prSt.setString(1, brandTable.getBrand());
            prSt.executeUpdate();

            resSet=getBrandID(brandTable);
            resSet.next();
            brandTable.setID(resSet.getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return brandTable;
    }

    private ResultSet getBrandID(LawyerTable brandTable) {
        comandString="SELECT * FROM "+Tables.WORKERS.getTitle()+" WHERE "+ Workers.FIO.getTitle()+"=?";
        try {
            prSt = getDbConnection().prepareStatement(comandString);
            prSt.setString(1, brandTable.getBrand());

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resSet;
    }

    public void deleteBrand(LawyerTable brandTable) {
        comandString="DELETE FROM "+Tables.WORKERS.getTitle()+" WHERE "+ Workers.IDWORKERS.getTitle()+"=?";
        try {
            prSt= getDbConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(brandTable.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateBrand(LawyerTable brandTable) {
        System.out.println(brandTable);
        comandString = "UPDATE "+Tables.WORKERS.getTitle()+" SET "+ Workers.FIO.getTitle()+"=? WHERE "+ Workers.IDWORKERS.getTitle()+"=?";
        try {
            prSt = getDbConnection().prepareStatement(comandString);
            prSt.setString(1, brandTable.getBrand());
            prSt.setString(2, String.valueOf(brandTable.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet selectBuyers() {
        comandString="SELECT * FROM "+Tables.LAWFIRMS.getTitle();
        try {
            prSt= getDbConnection().prepareStatement(comandString);

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resSet;
    }

    private ResultSet getBuyerID(BuyerTable buyerTable) {
        comandString="SELECT * FROM "+Tables.LAWFIRMS.getTitle()+" WHERE "+ Lawfirms.PHONE.getTitle()+"=?";
        try {
            prSt = getDbConnection().prepareStatement(comandString);
            prSt.setString(1, buyerTable.getPhone());

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resSet;
    }

    public BuyerTable insertAndGetBuyer(BuyerTable buyerTable) {
        comandString = "INSERT INTO "+Tables.LAWFIRMS.getTitle()+"("+ Lawfirms.FIRMNAME.getTitle()+"," +
                Lawfirms.PHONE.getTitle()+","+ Lawfirms.ADDRESS.getTitle()+") VALUES (?,?,?)";
        try {
            prSt = getDbConnection().prepareStatement(comandString);
            prSt.setString(1, buyerTable.getName());
            prSt.setString(2, buyerTable.getPhone());
            prSt.setString(3, buyerTable.getEmail());
            prSt.executeUpdate();

            resSet=getBuyerID(buyerTable);
            resSet.next();
            buyerTable.setID(resSet.getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return buyerTable;
    }

    public void deleteBuyer(BuyerTable buyerTable) {
        comandString="DELETE FROM "+Tables.LAWFIRMS.getTitle()+" WHERE "+ Lawfirms.IDLAWFIRMS.getTitle()+"=?";
        try {
            prSt= getDbConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(buyerTable.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateBuyer(BuyerTable buyerTable) {
        comandString = "UPDATE "+Tables.LAWFIRMS.getTitle()+" SET "+ Lawfirms.FIRMNAME.getTitle()+"=?,"+ Lawfirms.PHONE.getTitle()+"=?,"+ Lawfirms.ADDRESS.getTitle()+"=? WHERE "+ Lawfirms.IDLAWFIRMS.getTitle()+"=?";
        try {
            prSt = getDbConnection().prepareStatement(comandString);
            prSt.setString(1, buyerTable.getName());
            prSt.setString(2, buyerTable.getPhone());
            prSt.setString(3, buyerTable.getEmail());
            prSt.setString(4, String.valueOf(buyerTable.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet selectConsultants() {
        comandString="SELECT * FROM "+Tables.LAWYERS.getTitle();
        try {
            prSt= getDbConnection().prepareStatement(comandString);

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resSet;
    }

    public void deleteConsultant(ConsultantTable consultantTable) {
        comandString="DELETE FROM "+Tables.LAWYERS.getTitle()+" WHERE "+ Lawfirms.IDLAWFIRMS.getTitle()+"=?";
        try {
            prSt= getDbConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(consultantTable.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ResultSet getConsultantID(ConsultantTable consultantTable) {
        comandString="SELECT * FROM "+Tables.LAWYERS.getTitle()+" WHERE "+ Lawyers.IDLAWFIRMS.getTitle()+"=?";
        try {
            prSt = getDbConnection().prepareStatement(comandString);
            prSt.setString(1, consultantTable.getPhone());

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resSet;
    }

    public ConsultantTable insertAndGetConsultant(ConsultantTable consultantTable) {
        comandString = "INSERT INTO "+Tables.LAWYERS.getTitle()+"("+ Lawyers.FIO.getTitle()+"," +
                Lawyers.IDLAWFIRMS.getTitle()+","+ Lawyers.YEARSOFCOOPERATION.getTitle()+") VALUES (?,?,?)";

        try {
            prSt = getDbConnection().prepareStatement(comandString);
            prSt.setString(1, consultantTable.getName());
            prSt.setString(2, consultantTable.getPhone());
            prSt.setString(3, String.valueOf(consultantTable.getRating()));
            prSt.executeUpdate();

            resSet=getConsultantID(consultantTable);
            resSet.next();
            consultantTable.setID(resSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return consultantTable;
    }

    public void updateConsultant(ConsultantTable consultantTable) {
        comandString = "UPDATE "+Tables.LAWYERS.getTitle()+" SET "+ Lawyers.FIO +"=?,"+ Lawyers.IDLAWFIRMS +"=?,"+ Lawyers.YEARSOFCOOPERATION +"=? WHERE "+ Lawyers.IDLAWYERS +"=?";

        try {
            prSt = getDbConnection().prepareStatement(comandString);
            prSt.setString(1, consultantTable.getName());
            prSt.setString(2, consultantTable.getPhone());
            prSt.setString(3, String.valueOf(consultantTable.getRating()));
            prSt.setString(4, String.valueOf(consultantTable.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }*/














    public ResultSet selectProviders() {
        comandString="SELECT * FROM "+Tables.MAKERS.getTitle();
        try {
            prSt= getDbConnection().prepareStatement(comandString);

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resSet;
    }

    /*public void deleteGadget(GoodsTable gadgetTable) {
        comandString="DELETE FROM "+Tables.GOODS.getTitle()+" WHERE "+ Goods.IDGOODS.getTitle()+"=?";
        try {
            prSt= getDbConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(gadgetTable.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public GoodsTable insertAndGetGadget(GoodsTable gadgetTable) {
        comandString = "INSERT INTO "+Tables.GOODS.getTitle()+"("+ Goods.GOODSNAME.getTitle()+","+
                Goods.COSTPERPIECE.getTitle()+","+
                Goods.IDMAKER.getTitle()+","+
                Goods.LEFTINSTOCK.getTitle();
        try {
            prSt = getDbConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(gadgetTable.getType()));
            prSt.setString(2, gadgetTable.getName());
            prSt.setString(3, String.valueOf(gadgetTable.getBrand()));
            prSt.setString(4, String.valueOf(gadgetTable.getCountry()));
            prSt.setString(5, String.valueOf(gadgetTable.getWarranty()));
            prSt.setString(6, String.valueOf(gadgetTable.getServiceLife()));
            prSt.setString(7, String.valueOf(gadgetTable.getCost()));
            prSt.setString(8, String.valueOf(gadgetTable.getProvider()));
            prSt.executeUpdate();

            resSet=getGadgetID(gadgetTable);
            resSet.next();
            gadgetTable.setID(resSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return gadgetTable;
    }

    private ResultSet getGadgetID(GoodsTable gadgetTable) {
        comandString="SELECT * FROM "+Tables.GOODS.getTitle()+" WHERE "+ Goods.COSTPERPIECE.getTitle()+"=?";
        try {
            prSt = getDbConnection().prepareStatement(comandString);
            prSt.setString(1, gadgetTable.getName());

            resSet=prSt.executeQuery();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resSet;
    }

    public void updateGadget(GoodsTable gadgetTable) {
        comandString = "UPDATE "+Tables.GOODS.getTitle()+" SET "+
                Goods.GOODSNAME.getTitle()+"=?,"+
                Goods.COSTPERPIECE.getTitle()+"=?,"+
                Goods.IDMAKER.getTitle()+"=?,"+
                Goods.LEFTINSTOCK.getTitle()+"=?,";
        try {
            prSt = getDbConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(gadgetTable.getType()));
            prSt.setString(2, gadgetTable.getName());
            prSt.setString(3, String.valueOf(gadgetTable.getBrand()));
            prSt.setString(4, String.valueOf(gadgetTable.getCountry()));
            prSt.setString(5, String.valueOf(gadgetTable.getWarranty()));
            prSt.setString(6, String.valueOf(gadgetTable.getServiceLife()));
            prSt.setString(7, String.valueOf(gadgetTable.getCost()));
            prSt.setString(8, String.valueOf(gadgetTable.getType()));
            prSt.setString(9,String.valueOf(gadgetTable.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void deleteProvider(ProviderTable providerTable) {
        comandString="DELETE FROM "+Tables.MAKERS.getTitle()+" WHERE "+ Makers.IDMAKERS.getTitle()+"=?";
        try {
            prSt= getDbConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(providerTable.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ProviderTable insertAndGetProvider(ProviderTable providerTable) {
        comandString = "INSERT INTO "+Tables.MAKERS.getTitle()+"("+ Makers.MAKERFIRM.getTitle()+","+
                Makers.YEARSOFCOOPERATION.getTitle()+") VALUES (?,?,?,?)";
        try {
            prSt = getDbConnection().prepareStatement(comandString);
            prSt.setString(1, providerTable.getName());
            prSt.setString(2, providerTable.getPhone());
            prSt.setString(3, providerTable.getEmail());
            prSt.setString(4, String.valueOf(providerTable.getCountry()));
            prSt.executeUpdate();

            resSet=insertProviderID(providerTable);
            resSet.next();
            providerTable.setID(resSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return providerTable;
    }

    private ResultSet insertProviderID(ProviderTable providerTable) {
        comandString="SELECT * FROM "+Tables.MAKERS.getTitle()+" WHERE "+ Makers.MAKERFIRM.getTitle()+"=?";
        try {
            prSt = getDbConnection().prepareStatement(comandString);
            prSt.setString(1, providerTable.getName());

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resSet;
    }

    public void updateProvider(ProviderTable providerTable) {
        comandString = "UPDATE " + Tables.MAKERS.getTitle() + " SET " +
                Makers.MAKERFIRM.getTitle() + "=?," +
                Makers.YEARSOFCOOPERATION.getTitle() + "=? WHERE " + Goods.IDGOODS.getTitle() + "=?";
        try {
            prSt = getDbConnection().prepareStatement(comandString);
            prSt.setString(1, providerTable.getName());
            prSt.setString(2, providerTable.getPhone());
            prSt.setString(3, providerTable.getEmail());
            prSt.setString(4, String.valueOf(providerTable.getCountry()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletePurchase(PurchaseTable purchaseTable) {
        comandString="DELETE FROM "+Tables.CONTRACTS.getTitle()+" WHERE "+ Contracts.IDCONTRACTS.getTitle()+"=?";
        try {
            prSt= getDbConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(purchaseTable.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public PurchaseTable insertAndGetPurchase(PurchaseTable purchaseTable) {
        comandString = "INSERT INTO " + Tables.CONTRACTS.getTitle() + "(" + Contracts.CUSTOMER.getTitle() + "," +
                Contracts.QUANTITY.getTitle() + "," +
                Contracts.TOTALCOST.getTitle() + "," +
                Contracts.IDGOOD.getTitle() + "," +
                Contracts.DATEOFSALE.getTitle() + ") VALUES (?,?,?,?,?)";
        try {
            prSt = getDbConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(purchaseTable.getGadget()));
            prSt.setString(2, String.valueOf(purchaseTable.getDate()));
            prSt.setString(3, String.valueOf(purchaseTable.getPayment()));
            prSt.setString(4, String.valueOf(purchaseTable.getBuyer()));
            prSt.setString(5, String.valueOf(purchaseTable.getConsultant()));
            prSt.executeUpdate();

            resSet = selectPurchases();
            int id=0;
            while (resSet.next()) {
                id= resSet.getInt(1);
            }
            purchaseTable.setID(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return purchaseTable;
    }


    public ResultSet selectPurchasesView() {
        comandString = "SELECT * FROM " + Views.GADGETS.getTitle();
        try {
            prSt = getDbConnection().prepareStatement(comandString);

            resSet = prSt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resSet;
    }*/

    public void registrationNewUser() {
        String insertUser = "INSERT INTO" +
                Const.USER_TABLE + "(" +
                Const.USERS_AND_ONLINE_USER_LOGIN + "," +
                Const.USERS_AND_ONLINE_USER_PASSWORD + "," +
                Const.USERS_AND_ONLINE_USER_SALT + "," +
                Const.USERS_AND_ONLINE_USER_MAIL +
                ") SELECT" +
                Const.USERS_AND_ONLINE_USER_LOGIN + "," +
                Const.USERS_AND_ONLINE_USER_PASSWORD + "," +
                Const.USERS_AND_ONLINE_USER_SALT + "," +
                Const.USERS_AND_ONLINE_USER_MAIL +
                " FROM " +
                Const.ONLINE_USER_TABLE;
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insertUser);

            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //поиск пользователя в бд
    public ResultSet findUser(User user) {
        String select = "SELECT * FROM " +
                Const.USER_TABLE +
                " WHERE " +
                Const.USERS_AND_ONLINE_USER_LOGIN +
                "=?";
        System.out.println(select);
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            System.out.println(prSt.getParameterMetaData().toString());
            prSt.setString(1, user.getLogin());
            System.out.println(user.getLogin());
            resSet = prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resSet;
    }

    public void insertOnlineUserAfterAuth(User user) {
        String insertOnlineUser = "INSERT INTO" +
                Const.ONLINE_USER_TABLE + "(" +
                Const.USERS_AND_ONLINE_USER_ID + "," +
                Const.USERS_AND_ONLINE_USER_LOGIN + "," +
                Const.USERS_AND_ONLINE_USER_PASSWORD + "," +
                Const.USERS_AND_ONLINE_USER_SALT + "," +
                Const.USERS_AND_ONLINE_USER_MAIL +
                ") SELECT" +
                Const.USERS_AND_ONLINE_USER_ID + "," +
                Const.USERS_AND_ONLINE_USER_LOGIN + "," +
                Const.USERS_AND_ONLINE_USER_PASSWORD + "," +
                Const.USERS_AND_ONLINE_USER_SALT + "," +
                Const.USERS_AND_ONLINE_USER_MAIL +
                " FROM " +
                Const.USER_TABLE +
                " WHERE " +
                Const.USERS_AND_ONLINE_USER_LOGIN + "=?";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insertOnlineUser);
            prSt.setString(1, user.getLogin());

            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //очистка таблицы действующих пользователей
    public void truncateOnlineUser() {
        String truncate = "TRUNCATE " + Const.ONLINE_USER_TABLE;
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(truncate);
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getOnlineUser() {
        String select = "SELECT * FROM " + Const.ONLINE_USER_TABLE;
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);

            resSet = prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            resSet.next();
            System.out.println(resSet.getString(2));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resSet;
    }

    public void insertOnlineUserAfterRegistration(User user) {
        String insertOnlineAfterRegistration = "INSERT INTO " +
                Const.ONLINE_USER_TABLE + "(" +
                Const.USERS_AND_ONLINE_USER_ID + "," +
                Const.USERS_AND_ONLINE_USER_LOGIN + "," +
                Const.USERS_AND_ONLINE_USER_PASSWORD + "," +
                Const.USERS_AND_ONLINE_USER_SALT + "," +
                Const.USERS_AND_ONLINE_USER_MAIL +
                ") VALUES(?,?,?,?,?)";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insertOnlineAfterRegistration);
            prSt.setString(1, user.getID());
            prSt.setString(2, user.getLogin());
            prSt.setString(3, user.getPassword());
            prSt.setString(4, user.getSalt());
            prSt.setString(5, user.getMail());

            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //поиска ID действуюшего пользователя
    public void insertOnlineIDAfterRegistration(User user) {
        String selectUser = "SELECT" +
                Const.USERS_AND_ONLINE_USER_ID +
                " FROM " +
                Const.USER_TABLE +
                " WHERE " +
                Const.USERS_AND_ONLINE_USER_LOGIN + "=?";


        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(selectUser);
            prSt.setString(1, user.getLogin());

            resSet = prSt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        try {
            resSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String insertOnlineUserID = "UPDATE " +
                Const.ONLINE_USER_TABLE +
                "SET" +
                Const.USERS_AND_ONLINE_USER_ID + "=?";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insertOnlineUserID);
            prSt.setString(1, resSet.getString(1));

            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //обновление логина пользователя
    public void updateUserLogin(User user) {
        String updateUserLogin="UPDATE "+
                Const.USER_TABLE+
                " SET"+
                Const.USERS_AND_ONLINE_USER_LOGIN+"=?"+
                "WHERE"+Const.USERS_AND_ONLINE_USER_ID+"=?";
        try {
            PreparedStatement prSt=getDbConnection().prepareStatement(updateUserLogin);
            prSt.setString(1, user.getLogin());
            prSt.setString(2, user.getID());

            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //обновление пароля пользователя
    public void updateUserPassword(User user) {
        String updateUserPassword ="UPDATE "+
                Const.USER_TABLE+
                " SET "+
                Const.USERS_AND_ONLINE_USER_PASSWORD+"=?, "+
                Const.USERS_AND_ONLINE_USER_SALT+"=?"+
                " WHERE "+Const.USERS_AND_ONLINE_USER_ID+"=?";
        try {
            PreparedStatement prSt=getDbConnection().prepareStatement(updateUserPassword);
            prSt.setString(1, user.getPassword());
            prSt.setString(2, user.getSalt());
            prSt.setString(3, user.getID());

            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //обновление почты пользователя
    public void updateUserMail(User user) {
        String updateUserMail ="UPDATE "+
                Const.USER_TABLE+
                " SET "+
                Const.USERS_AND_ONLINE_USER_MAIL+"=?"+
                " WHERE "+Const.USERS_AND_ONLINE_USER_ID+"=?";
        try {
            PreparedStatement prSt=getDbConnection().prepareStatement(updateUserMail);
            prSt.setString(1, user.getMail());
            prSt.setString(2, user.getID());

            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //поиск пользователя по логину и почте
    public User findUserByLoginAndMail(User user){
        String select = "SELECT * FROM " +
                Const.USER_TABLE +
                " WHERE " +
                Const.USERS_AND_ONLINE_USER_LOGIN +
                "=? AND " +
                Const.USERS_AND_ONLINE_USER_MAIL + "=?";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, user.getLogin());
            prSt.setString(2, user.getMail());
            resSet = prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (resSet.next()) {
                user.setMail(resSet.getString(5));
                user.setID(resSet.getString(1));

                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    //поиск пользователя по паролю и почте
    public User isUserWithPasswordAndMail(User user) {
        String select = "SELECT * FROM " +
                Const.USER_TABLE +
                " WHERE " +
                Const.USERS_AND_ONLINE_USER_MAIL + "=?";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, user.getMail());

            resSet = prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String password;
        String salt;
        String ID;
        String mail;

        try {
            while (resSet.next()) {

                password = resSet.getString(3);
                salt = resSet.getString(4);
                ID = resSet.getString(1);
                mail = resSet.getString(5);

                if (password.equals(hashClass.getSecurePasswordWithSalt(user.getPassword(), salt))) {
                    user.setID(ID);
                    user.setMail(mail);

                    return user;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    //обновление действующего пользователя
    public void insertRecoverOnlineUser(String tableColumn, String ID, String mail) {
        String insertRecover ="INSERT INTO"+
                Const.ONLINE_USER_TABLE+"("+
                tableColumn+","+
                Const.USERS_AND_ONLINE_USER_ID+","+
                Const.USERS_AND_ONLINE_USER_MAIL+") VALUES (?,?,?)";
        System.out.println(insertRecover);
        try {
            PreparedStatement prSt=getDbConnection().prepareStatement(insertRecover);
            prSt.setString(1, Const.recover);
            prSt.setString(2, ID);
            prSt.setString(3, mail);

            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
