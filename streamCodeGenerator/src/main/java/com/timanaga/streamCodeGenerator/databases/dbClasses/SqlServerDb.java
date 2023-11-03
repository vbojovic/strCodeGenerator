package com.timanaga.streamCodeGenerator.databases.dbClasses;
import com.timanaga.streamCodeGenerator.databases.dbModels.DatabaseModel;
import com.timanaga.streamCodeGenerator.databases.dbModels.DatabaseTypeEnum;

import java.sql.DriverManager;

public class SqlServerDb extends ADb {
    public SqlServerDb() throws Exception {
        super();
        this.dbType = DatabaseTypeEnum.mssql;
        m_elementReader = new SqlServerDbElementReader(this);
    }
    @Override
    public void connect() throws Exception {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://" + this.settings.getHost() + ":"
                    + Integer.toString(this.settings.getPort()) + ";databaseName=" + this.settings.getDataBase();
            this.connection = DriverManager.getConnection(url, this.settings.getLogin(), this.settings.getPass());
        } catch (Exception e) {
            throw new Exception("Failed to connect to the SQL Server database: " + e.getMessage());
        }
    }

    @Override
    public void disconnect() throws Exception {
        if (this.connection != null && !this.connection.isClosed()) {
            this.connection.close();
        }
    }

    @Override
    public void loadFromDatabase() throws Exception {
        if (this.settings == null)
            throw new Exception("Database settings are missing!");

        this.setModel(new DatabaseModel());

        if (this.connection.isClosed())
            throw new Exception("Connection is not opened");
        //TODO ubacit da se loadaju i ostali objekti
    }

    public DatabaseModel getModel() throws Exception {
        if (this.settings == null) {
            throw new Exception("Model cannot be generated because database settings are missing!");
        }
        this.connect();
        loadFromDatabase();
        this.disconnect();
        return this.model;
    }
}
