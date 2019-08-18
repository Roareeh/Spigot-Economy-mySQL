package me.roareeh.motivepvptest.commands;

import me.roareeh.motivepvptest.MotivePVPTest;
import me.roareeh.motivepvptest.mysql;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class bal implements CommandExecutor {
    MotivePVPTest plugin = MotivePVPTest.getPlugin(MotivePVPTest.class);
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player)sender;
            if(args.length == 1){
                double BalToSend;
                Player target = Bukkit.getPlayer(args[0]);
                String targetName;
                targetName = args[0];
                if(target != null) { //Check if player is online
                    BalToSend = mysql.getBal(target.getUniqueId());
                    player.sendMessage(ChatColor.AQUA + "" + target.getName() + ChatColor.YELLOW+ " has " + ChatColor.GREEN + "$" + BalToSend);
                }else{
                    if (mysql.playerExists(targetName)){ //Check if player exists in database
                        BalToSend = mysql.getBalName(targetName);
                        player.sendMessage(ChatColor.AQUA + "" + targetName + ChatColor.YELLOW+ " has " + ChatColor.GREEN + "$" + BalToSend);
                    }else{
                        player.sendMessage(ChatColor.RED+"That player has never joined the server!");
                    }
                }
            }else{
                if(args.length == 0){
                    double BalToSend;
                    BalToSend = mysql.getBal(player.getUniqueId());
                    player.sendMessage(ChatColor.YELLOW + "You have " + ChatColor.GREEN + "$" + BalToSend);
                }
                if (args.length>1){
                    player.sendMessage(ChatColor.RED + "Correct Usage:" + ChatColor.GOLD + "/bal [Player]");
                }
            }
        }
        return false;
    }
}
