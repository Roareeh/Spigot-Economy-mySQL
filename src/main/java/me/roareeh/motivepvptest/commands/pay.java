package me.roareeh.motivepvptest.commands;

import me.roareeh.motivepvptest.MotivePVPTest;
import me.roareeh.motivepvptest.mysql;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class pay implements CommandExecutor {
    MotivePVPTest plugin = MotivePVPTest.getPlugin(MotivePVPTest.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if(args.length == 2){
                String targetName = args[0];
                String pname = player.getName();
                double BalToPay = 0;
                double PCurrentBal = 0;
                double TCurrentBal = 0;
                double PNewBal;
                double TNewBal;
                boolean Number = false;
                String text = args[1];
                if(text.matches("^[0-9.]*$")){      //Check if number contains any letters
                    Number=true;
                    PCurrentBal = mysql.getBal(player.getUniqueId());
                    BalToPay = Double.parseDouble(args[1]);
                }
                Player target = Bukkit.getPlayer(targetName);
                if(target != null) {
                    //Online
                    TCurrentBal = mysql.getBal(target.getUniqueId());
                    if(PCurrentBal>=BalToPay){
                        if(BalToPay > 0 && !pname.equalsIgnoreCase(targetName)&& Number){
                            PNewBal = PCurrentBal - BalToPay; //Calculate players balance
                            TNewBal = TCurrentBal + BalToPay; //Calculate targets balance
                            mysql.setBal(target.getUniqueId(),TNewBal);
                            mysql.setBal(player.getUniqueId(),PNewBal);
                            player.sendMessage(ChatColor.YELLOW + "You sent " + ChatColor.GREEN + "$" + BalToPay + ChatColor.YELLOW + " to "+ ChatColor.AQUA+ targetName);
                            target.sendMessage(ChatColor.YELLOW + "You received "+ChatColor.GREEN+ "$" + BalToPay +ChatColor.YELLOW+ " from "+ChatColor.AQUA + player.getName());
                            mysql.UpdateScoreBoard(target);
                            mysql.UpdateScoreBoard(player);
                        }else{
                            player.sendMessage(ChatColor.RED + "You can't do that!");
                        }
                    }else{
                        player.sendMessage(ChatColor.RED + "You don't have enough money!");
                    }
                }
                else {
                    //Offline
                    if(mysql.playerExists(targetName)){  //Check if player exists in database
                        TCurrentBal = mysql.getBalName(targetName);
                        if(PCurrentBal>=BalToPay){
                            if(BalToPay > 0 && Number){
                                PNewBal = PCurrentBal - BalToPay;
                                TNewBal = TCurrentBal + BalToPay;
                                mysql.setBalName(targetName,TNewBal);
                                mysql.setBal(player.getUniqueId(),PNewBal);
                                mysql.UpdateScoreBoard(player);
                                player.sendMessage(ChatColor.YELLOW + "You sent " + ChatColor.GREEN + "$" + BalToPay + ChatColor.YELLOW + " to "+ ChatColor.AQUA+ targetName);
                            }else{
                                player.sendMessage(ChatColor.RED + "You can't do that!");
                            }
                        }else{
                            player.sendMessage(ChatColor.YELLOW + "You don't have enough money!");
                        }
                    }else{
                        player.sendMessage(ChatColor.RED + "That player has never joined the server!");
                    }
                    }
            }else{
                player.sendMessage(ChatColor.RED + "Correct Usage: " + ChatColor.GOLD + "/pay [Player] [Amount]");
            }
            }
        return false;
    }

}
