package me.roareeh.motivepvptest;

import me.roareeh.motivepvptest.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class MotivePVPTest extends JavaPlugin{
    //Define important variables for mySQL connection
    private Connection connection;
    public String host,database,username,password,table;
    public int port;

    @Override
    public void onEnable() {
        // Plugin startup logic
        loadConfig();
        mysqlSetup();
        this.getServer().getPluginManager().registerEvents(new mysql(), this);
        getCommand("pay").setExecutor(new pay());
        getCommand("bal").setExecutor(new bal());
        getCommand("eco").setExecutor(new eco());
        getCommand("withdraw").setExecutor(new withdraw());
    }

    //Load the plugin config file so it can be accessed
    public void loadConfig(){
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    //Function for connecting to the mySQL server
    public void mysqlSetup(){
        //Define connection variables from config
        host = this.getConfig().getString("host");
        port = this.getConfig().getInt("port");
        database = this.getConfig().getString("database");
        username = this.getConfig().getString("username");
        password = this.getConfig().getString("password");
        table = this.getConfig().getString("table");
        try{
            synchronized (this){
                if(getConnection()!=null && !getConnection().isClosed()){
                    return;
                }
                //Connect to the specific address and log to console
                Class.forName("com.mysql.jdbc.Driver");
                setConnection(DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username,this.password));
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "mySQL Connected");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

}
