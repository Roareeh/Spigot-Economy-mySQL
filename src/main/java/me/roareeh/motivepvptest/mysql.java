package me.roareeh.motivepvptest;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import me.roareeh.motivepvptest.commands.withdraw;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.*;

public class mysql implements Listener {
    static MotivePVPTest plugin = MotivePVPTest.getPlugin(MotivePVPTest.class);

    //Player join event to log the player in the database
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        createPlayer(player.getUniqueId(), player);
        UpdateScoreBoard(player);
    }

    //Event handler for banknotes, check if a player has right clicked a bank note and if so pay them and remove the note
    @EventHandler
    public void hit(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
              if(player.getInventory().contains(withdraw.note)){
                  ItemStack item = player.getInventory().getItemInHand();
                  if(item != null && item.getType() == Material.PAPER) {
                      if(item.getItemMeta().getLore() !=null){
                          List<String> lore = item.getItemMeta().getLore();
                            String Amount = lore.get(0);
                            String AmountT = Amount.substring(3);
                          double AmountD = Double.parseDouble(AmountT);
                          double PBal = mysql.getBal(player.getUniqueId());
                          PBal = PBal + AmountD;
                          mysql.setBal(player.getUniqueId(),PBal);
                          player.sendMessage(ChatColor.GREEN + "+ " + Amount);
                          player.getInventory().removeItem(item);
                          mysql.UpdateScoreBoard(player);
                      }
                  }
              }
        }
        }




    //Search the database to find if a player is already added using their uuid
    public boolean playerExists(UUID uuid) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM " + plugin.table + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                //If player is found in the database
                return true;
            }
            //If the player is not found in the database
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Create a player in the database if they don't already exist
    public void createPlayer(final UUID uuid, Player player) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM " + plugin.table + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            System.out.print(1);
            if (playerExists(uuid) != true) {
                PreparedStatement insert = plugin.getConnection().prepareStatement("INSERT INTO " + plugin.table + " (UUID,NAME,BALANCE) VALUES (?,?,?)");
                insert.setString(1, uuid.toString());
                insert.setString(2, player.getName());
                insert.setInt(3, plugin.getConfig().getInt("defaultbal"));
                insert.executeUpdate();
                //Inserting the player with their UUID, NAME and BALANCE
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Function to update the scoreboard when a players bal changes, or when they join the server
    public static void UpdateScoreBoard(Player player){
        double PBal = mysql.getBal(player.getUniqueId());
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("Title", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.AQUA + "Economy");
        Score score = objective.getScore(ChatColor.YELLOW+ "Balance:");
        Score score3 = objective.getScore(ChatColor.GREEN + "$" + PBal);
        score.setScore(2);
        score3.setScore(1);
        player.setScoreboard(scoreboard);
    }

    //Function to set a players bal on the mySQL database using their uuid
    public static void setBal(UUID uuid,double Bal) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("UPDATE " + plugin.table + " SET BALANCE=? WHERE UUID=?");
            statement.setDouble(1, Bal);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Function to set a players bal on the mySQL database using their name
    public static void setBalName(String name,double Bal) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("UPDATE " + plugin.table + " SET BALANCE=? WHERE NAME=?");
            statement.setDouble(1, Bal);
            statement.setString(2, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Function to get a players bal from the mySQL database using their uuid
    public static double getBal(UUID uuid) {
        double Bal = 0;
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM " + plugin.table + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            Bal = results.getDouble("BALANCE");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Bal;
    }

    //Function to get a players bal from the mySQL database using their name
    public static double getBalName(String name) {
        double Bal = 0;
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM " + plugin.table + " WHERE NAME=?");
            statement.setString(1, name);
            ResultSet results = statement.executeQuery();
            results.next();
            Bal = results.getDouble("BALANCE");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Bal;
    }

    //Function to check if a player is in the database using their name
    public static boolean playerExists(String name) {
        try {
            PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM " + plugin.table + " WHERE NAME=?");
            statement.setString(1, name);
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
