package com.Util;

import com.SelfHome.Main;
import com.SelfHome.Variable;
import me.casperge.realisticseasons.api.SeasonsAPI;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;






public class TimeAsync
{
  public static void asnycTime() { 
	  
	  if(!Main.JavaPlugin.getConfig().getBoolean("EnableAsnycTime")) {
		  return;
	  }
	  
	  
	  (new BukkitRunnable()
      {
        public void run() {
        	
        	if(Main.JavaPlugin.getConfig().getBoolean("EnableAsnycTime")) {
                String async_world_name = Main.JavaPlugin.getConfig().getString("AsyncTimeWorld");
                if (Bukkit.getWorld(async_world_name) != null) {
                  World async_world = Bukkit.getWorld(async_world_name);
                  for (World world : Bukkit.getWorlds()) {
                    if (HomeAPI.getHome(world.getName()) != null) {
                      Home home = HomeAPI.getHome(world.getName().replace(Variable.world_prefix, ""));
                      if (home.isLocktime()) {
                        continue;
                      }
                      
                      if (Main.JavaPlugin.getConfig().getBoolean("RealisticSeasons")) {
                        SeasonsAPI seasonsapi = SeasonsAPI.getInstance();
                        seasonsapi.setSeason(world, seasonsapi.getSeason(async_world));
                        seasonsapi.setDate(world, seasonsapi.getDate(async_world));
                        world.setTime(async_world.getTime()); continue;
                      } 
                      world.setTime(async_world.getTime());
                    }
                  
                  }
                
                }
        	}else {
        		this.cancel();
        	}
        	
        	

        
        }
      }).runTaskTimer((Plugin)Main.JavaPlugin, 0L, 20L); }
}


