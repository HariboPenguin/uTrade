package me.HariboPenguin.uTrade;

import com.iCo6.system.Accounts;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class uTradeCommandExecutor implements CommandExecutor {

    private uTrade plugin;
    ArrayList<Trade> trades = new ArrayList<Trade>();

    public uTradeCommandExecutor(uTrade instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {
        Player player = null;
        String Prefix = ChatColor.DARK_GREEN + "[" + ChatColor.YELLOW + "uTrade" + ChatColor.DARK_GREEN + "] ";


        if (sender instanceof Player) {
            player = (Player) sender;
        }

        if (cmd.getName().equalsIgnoreCase("offer")) {

            if (player == null) {
                sender.sendMessage(ChatColor.RED + "This command can only be run by a player!");
            } else {
                if (args.length < 1) {
                    player.sendMessage(Prefix + ChatColor.RED + "No parameters entered! - Correct Usage is:");
                    player.sendMessage(Prefix + ChatColor.RED + "/offer [Player] [Item] [Amount] [Price]");
                    return true;
                } else if (args.length == 1) {
                    player.sendMessage(Prefix + ChatColor.RED + "Not enough arguments! - Correct Usage is:");
                    player.sendMessage(Prefix + ChatColor.RED + "/offer [Player] [Item] [Amount] [Price]");
                    return true;
                } else if (args.length == 2) {
                    player.sendMessage(Prefix + ChatColor.RED + "Not enough arguments! - Correct Usage is:");
                    player.sendMessage(Prefix + ChatColor.RED + "/offer [Player] [Item] [Amount] [Price]");
                    return true;
                } else if (args.length == 3) {
                    player.sendMessage(Prefix + ChatColor.RED + "Not enough arguments! - Correct Usage is:");
                    player.sendMessage(Prefix + ChatColor.RED + "/offer [Player] [Item] [Amount] [Price]");
                    return true;
                } else if (args.length == 4) {
                    Player recipient = plugin.getServer().getPlayer(args[0]);
                    String item = args[1];
                    int amount = Integer.parseInt(args[2]);
                    int price = Integer.parseInt(args[3]);
                    String currency = plugin.getConfig().getString("currency.name");
                    String currencyAbbr = plugin.getConfig().getString("currency.abbreviation");

                    if (recipient == null) {
                        player.sendMessage(Prefix + ChatColor.RED + "That player is not online!");
                        return true;
                    } else {
                        if (player.getInventory().contains(Material.matchMaterial(item), amount)) {

                            if (recipient.getInventory().firstEmpty() == -1) {
                                player.sendMessage(Prefix + ChatColor.RED + "The recipient's inventory is full!");
                                return true;
                            } else {

                                if (new Accounts().get(recipient.getName()).getHoldings().getBalance() > price) {

                                    int ID = (int) Math.random();

                                    trades.add(new Trade(recipient, player, new ItemStack(Material.matchMaterial(item), amount), price, amount, item));
                                    
                                    player.sendMessage(Prefix + ChatColor.GREEN + "You have offered " + recipient.getName() + " " + price + currencyAbbr + " for " + amount + " " + item);

                                    recipient.sendMessage(Prefix + ChatColor.GREEN + player.getName() + " has offered you " + amount + " " + item + " for " + price + currencyAbbr);
                                    recipient.sendMessage(Prefix + ChatColor.GREEN + "Accept with /utrade accept - Reject with /utrade reject");
                                    
                                    
                                } else {
                                    player.sendMessage(Prefix + ChatColor.RED + "That player does not have enough money!");
                                    
                                }
                                return true;
                            }
                        } else {
                            player.sendMessage(Prefix + ChatColor.RED + "You do not have enough of that item in your inventory!");
                            return true;
                        }
                    }
                } else if (args.length > 4) {
                    player.sendMessage(Prefix + ChatColor.RED + "Too many arguments! - Correct Usage is:");
                    player.sendMessage(Prefix + ChatColor.RED + "/offer [Player] [Item] [Amount] [Price]");
                    return true;
                }
            }
        } else if (cmd.getName().equalsIgnoreCase("utrade")) {
            if (args.length >= 1) {

                if (args[0].equalsIgnoreCase("accept")) {

                    String currency = plugin.getConfig().getString("currency.name");
                    String currencyAbbr = plugin.getConfig().getString("currency.abbreviation");
                    
                    Trade trade = null;

                    for (Trade t : trades) {

                        if (t.recipient == player) {
                            trade = t;
                        }
                    }

                    if (trade == null) {
                        sender.sendMessage("ERROR");
                        return true;
                    }

                    Player playerSendingItems = trade.playerSender;
                    Player recipient = trade.recipient;
                    ItemStack items = trade.itemStack;
                    int price = trade.price;
                    int amount = trade.amount;
                    String item = trade.item;

                    new Accounts().get(recipient.getName()).getHoldings().subtract(price);
                    new Accounts().get(playerSendingItems.getName()).getHoldings().add(price);

                    playerSendingItems.getInventory().removeItem(items);
                    recipient.getInventory().addItem(items);
                    playerSendingItems.sendMessage(ChatColor.GREEN + recipient.getName() + " has accepted your offer of " + price + currencyAbbr + " for " + amount + " " + item);
                    recipient.sendMessage(ChatColor.GREEN + "You have accepted " + player.getName() + "'s offer of " + amount + " " + item + " for " + price + currencyAbbr);
                    
                    trades.remove(trade);

                    return true;
                } else if (args[0].equalsIgnoreCase("reject")) {
                    
                    String currency = plugin.getConfig().getString("currency.name");
                    String currencyAbbr = plugin.getConfig().getString("currency.abbreviation");
                    
                    Trade trade = null;

                    for (Trade t : trades) {

                        if (t.recipient == player) {
                            trade = t;
                        }
                    }

                    if (trade == null) {
                        sender.sendMessage("ERROR");
                        return true;
                    }
                    
                    Player playerSendingItems = trade.playerSender;
                    Player recipient = trade.recipient;
                    int price = trade.price;
                    int amount = trade.amount;
                    String item = trade.item;
                    
                    playerSendingItems.sendMessage(ChatColor.RED + recipient.getName() + " has rejected your offer of " + price + currencyAbbr + " for " + amount + " " + item);
                    recipient.sendMessage(ChatColor.RED + "You have rejected " + player.getName() + "'s offer of " + price + currencyAbbr + " for " + amount + " " + item);
                    
                    trades.remove(trade);
                    
                    return true;
                } else if (args[0].equalsIgnoreCase("help")) {
                    sender.sendMessage(ChatColor.DARK_GREEN + "------------------ " + ChatColor.YELLOW + "uTrade Help Menu" + ChatColor.DARK_GREEN + " ------------------");
                    sender.sendMessage(ChatColor.RED + "/offer [Player] [Item] [Amount] [Price]" + ChatColor.DARK_GREEN + " - " + ChatColor.GRAY + "Offer an item to a player");
                    sender.sendMessage(ChatColor.RED + "/utrade accept" + ChatColor.DARK_GREEN + " - " + ChatColor.GRAY + "Accepts an offer");
                    sender.sendMessage(ChatColor.RED + "/utrade reject" + ChatColor.DARK_GREEN + " - " + ChatColor.GRAY + "Rejects an offer");
                    sender.sendMessage(ChatColor.RED + "/utrade help" + ChatColor.DARK_GREEN + " - " + ChatColor.GRAY + "Display's this help menu");
                    return true;
                }

            } else {
            }
        }
        return false;
    }
}
