package me.HariboPenguin.uTrade;

import org.bukkit.plugin.java.JavaPlugin;

import com.iCo6.system.Accounts;
import com.iCo6.system.Holdings;

public class uTrade extends JavaPlugin {
    
    private uTradeCommandExecutor uTradeExecutor;

    @Override
    public void onDisable() {
        
    }

    @Override
    public void onEnable() {
        
        getConfig().options().copyDefaults(true);
        saveConfig();
        
        uTradeExecutor = new uTradeCommandExecutor(this);
        getCommand("offer").setExecutor(uTradeExecutor);
        getCommand("utrade").setExecutor(uTradeExecutor);
        
    }
}
