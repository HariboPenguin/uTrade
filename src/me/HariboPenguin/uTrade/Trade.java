
package me.HariboPenguin.uTrade;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Trade {
    
    public Trade(Player r, Player pS, ItemStack iS, int p, int am, String itemEntered) {
        recipient = r;
        playerSender = pS;
        itemStack = iS;
        price = p;
        amount = am;
        item = itemEntered;
    }
    
    public Player recipient;
    public Player playerSender;
    public ItemStack itemStack;
    public int price;
    public int amount;
    public String item;
    
}
