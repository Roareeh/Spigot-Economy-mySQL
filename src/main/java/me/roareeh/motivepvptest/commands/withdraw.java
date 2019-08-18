package me.roareeh.motivepvptest.commands;

import me.roareeh.motivepvptest.mysql;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class withdraw implements CommandExecutor {
    public static ItemStack note;

    //Command for players to withdraw money into notes
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player=(Player)sender;
            if (args.length == 1){
                String amount;
                amount = args[0];
                boolean Number = false;
                Double amountnum = null;
                if(amount.matches("^[0-9.]*$")){ //Check no letters are present in number
                    Number=true;
                     amountnum = Double.parseDouble(amount);
                    note = new ItemStack(Material.PAPER,1);
                }
                if(Number){
                    if(hasAvaliableSlot(player)){
                        if (mysql.getBal(player.getUniqueId())>amountnum){
                            if(amountnum > 0){
                                ItemMeta meta = note.getItemMeta();
                                meta.setDisplayName(ChatColor.AQUA + "Bank Note");
                                ArrayList<String> lore = new ArrayList<>();
                                lore.add(ChatColor.GREEN + "$" + amountnum);
                                lore.add(ChatColor.YELLOW + "Right Click to Redeem!");
                                meta.setLore(lore);
                                note.setItemMeta(meta);
                                player.getInventory().addItem(note);
                                Double PBal;
                                PBal = mysql.getBal(player.getUniqueId());
                                Double NewBal;
                                NewBal = PBal - amountnum;
                                mysql.setBal(player.getUniqueId(),NewBal);
                                mysql.UpdateScoreBoard(player);
                            }else{
                                player.sendMessage(ChatColor.RED + "You can't withdraw less than 1!");
                            }
                        }else{
                            player.sendMessage(ChatColor.RED + "You can't withdraw more than your balance!");
                        }
                    }else{
                        player.sendMessage(ChatColor.RED + "You must have space in your inventory to do this!");
                    }
                }else{
                    player.sendMessage(ChatColor.RED + "You can't do this!");
                }
            }else{
                player.sendMessage(ChatColor.RED + "Correct Usage: " + ChatColor.GOLD + "/withdraw [Amount]");
            }
        }
        return false;
    }

    //Function to check if players have inventory space to recieve a note
    public boolean hasAvaliableSlot(Player player){
        Inventory inv = player.getInventory();
        Boolean check=false;
        for (ItemStack item: inv.getContents()) {
            if(item == null) {
                check = true;
                break;
            }
        }
        return check;
    }

}
