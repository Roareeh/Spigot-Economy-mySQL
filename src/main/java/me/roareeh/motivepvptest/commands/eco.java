package me.roareeh.motivepvptest.commands;

import me.roareeh.motivepvptest.MotivePVPTest;
import me.roareeh.motivepvptest.mysql;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class eco implements CommandExecutor {
    MotivePVPTest plugin = MotivePVPTest.getPlugin(MotivePVPTest.class);
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player)sender;
            if(player.hasPermission("MotivePVPTest.admin")){ //Correct permission can be used here in a real situation
                if(args.length == 3){
                    Player target = Bukkit.getPlayer(args[1]);
                    double balchange = 0;
                    boolean numbers = false;
                    double PCurrent;
                    double PNewBal = 0;
                    String text = args[2];
                    String targetName = args[1];
                    if(text.matches("^[0-9.]*$")){  //Check if number contains any letters
                        numbers=true;
                        balchange = Double.parseDouble(args[2]);
                    }
                    if(target != null) {
                        if(balchange>=0 && numbers == true){
                            PCurrent = mysql.getBal(target.getUniqueId());
                            PNewBal = PCurrent + balchange; //Calculate new balance
                            if(args[0].equalsIgnoreCase("Add")){
                                mysql.setBal(target.getUniqueId(),PNewBal);
                                player.sendMessage(ChatColor.YELLOW +"Successfully added " +ChatColor.GREEN+"$" + balchange + ChatColor.YELLOW+" to " +ChatColor.AQUA+ target.getName());
                                target.sendMessage(ChatColor.YELLOW + "An admin added "+ChatColor.GREEN+"$"+balchange+ChatColor.YELLOW+" to your balance" );
                                mysql.UpdateScoreBoard(target);
                            }
                            if(args[0].equalsIgnoreCase("Set")){
                                mysql.setBal(target.getUniqueId(),balchange);
                                player.sendMessage(ChatColor.YELLOW+"Successfully set "+ChatColor.AQUA + target.getName() +"'s "+ChatColor.YELLOW+ "balance to " + ChatColor.GREEN+"$" + balchange);
                                target.sendMessage(ChatColor.YELLOW + "An admin set your balance to "+ChatColor.GREEN+"$"+balchange);
                                mysql.UpdateScoreBoard(target);
                            }
                        }else{
                            player.sendMessage(ChatColor.RED + "A players bal cannot be below 0 and cannot contain letters!");
                        }
                    }else{
                    if(mysql.playerExists(targetName)){  //Check if player exists in database
                        PCurrent = mysql.getBalName(targetName);
                        PNewBal = PCurrent + balchange;
                        if(balchange>=0 && numbers == true){
                            if(args[0].equalsIgnoreCase("Add")){
                                mysql.setBalName(targetName,PNewBal);
                                player.sendMessage(ChatColor.YELLOW +"Successfully added " +ChatColor.GREEN+"$" + balchange + ChatColor.YELLOW+" to " +ChatColor.AQUA+ targetName);
                            }
                            if(args[0].equalsIgnoreCase("Set")){
                                mysql.setBalName(targetName,balchange);
                                player.sendMessage(ChatColor.YELLOW+"Successfully set "+ChatColor.AQUA + targetName +"'s "+ChatColor.YELLOW+ "balance to " + ChatColor.GREEN+"$" + balchange);
                            }
                        }else{
                            player.sendMessage(ChatColor.RED + "A players bal cannot be below 0 and cannot contain letters!");
                        }
                        }else{
                        player.sendMessage(ChatColor.RED+"That player has never joined the server!");
                    }
                    }
                }else{
                    player.sendMessage(ChatColor.RED + "Correct Usage: " + ChatColor.GOLD + "/eco [Add/Set] [Player] [Amount]");
                }
            }else{
                player.sendMessage(ChatColor.RED + "You don't have permission to do this!");
            }
        }
        return false;
    }
}
