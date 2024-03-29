package com.Util;

import com.SelfHome.Main;
import com.SelfHome.Redis;
import com.SelfHome.Variable;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTTileEntity;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;






public class Util
{
  public static void clearCache(final String p, final String papi_name) { (new BukkitRunnable()
      {
        public void run() {
          List<Redis> list = Variable.cache;
          for (int i = 0; i < list.size(); i++) {
            Redis redis = list.get(i);
            if (redis != null)
            {
              
              if (redis.name.equalsIgnoreCase(p) && redis.papi_name.equalsIgnoreCase(papi_name))
                list.remove(redis); 
            }
          } 
          Variable.cache = list;
        }
      }).runTaskLater((Plugin)Main.JavaPlugin, Variable.Lang_YML.getLong("PlaceHolders.RefreshTime") * 20L); }


  
  public static Location getAir(Location loc) {
    Location clo = loc.clone();
    double i = 255.0D;
    while (i >= 0.0D) {
      Location temp = clo.clone();
      temp.setY(i + 20.0D);
      if (loc.getWorld().getBlockAt(temp).getType() != Material.AIR) {
        break;
      }
      i--;
    } 
    if (i != 0.0D) {
      return clo;
    }
    loc.setY(i);
    return loc;
  }


  
  public static Location getNotAir(Location loc) { return loc; }




  
  public static List<Chunk> getchunkmap(Location loc1, Location loc2, Location loc3, Location loc4) {
    List<Chunk> chunkmap = new ArrayList<>();
    double Ax = loc1.getX();
    double Az = loc1.getZ();
    double Bx = loc2.getX();
    double Bz = loc2.getZ();
    double Dx = loc3.getX();
    double Dz = loc3.getZ();
    double Ex = loc4.getX();
    double Ez = loc4.getZ();
    double minX = Math.min(Ax, Math.min(Bx, Math.min(Dx, Ex)));
    double maxX = Math.max(Ax, Math.max(Bx, Math.max(Dx, Ex)));
    double minZ = Math.min(Az, Math.min(Bz, Math.min(Dz, Ez)));
    double maxZ = Math.max(Az, Math.max(Bz, Math.max(Dz, Ez)));
    
    for (double x = minX; x <= maxX; x += 16.0D) {
      for (double z = minZ; z <= maxZ; z += 16.0D) {
        Location temp = loc1.getWorld().getSpawnLocation();
        temp.setX(x);
        temp.setZ(z);
        Chunk chunk = loc1.getWorld().getBlockAt(temp).getChunk();
        chunkmap.add(chunk);
      } 
    } 
    return chunkmap;
  }
  
  public static String getNBTString(BlockState state) {
    NBTTileEntity tent = new NBTTileEntity(state);
    String name = "";
    
    try {
      name = "id:" + state.getType().toString().toUpperCase() + ":" + state.getData() + ",nbt:" + 
        tent.asNBTString().toUpperCase();
    }
    catch (Exception e) {
      name = String.valueOf(state.getType().toString().toUpperCase()) + ":" + state.getData();
    } 


    
    return name;
  }

  
  public static String getItemNBTString(ItemStack i) {
	  
	  if(i == null) {
		  return "AIR";
	  }
	  
	  if(i.getType() == Material.AIR) {
		  return "AIR";
	  }
	  
    NBTItem nbti = new NBTItem(i);
    String name = "";
    
    try {
      name = "id:" + i.getType().toString().toUpperCase() + ":" + i.getDurability() + ",nbt:" + 
        nbti.asNBTString().toUpperCase();
    }
    catch (Exception e) {
      name = String.valueOf(i.getType().toString().toUpperCase()) + ":" + i.getDurability();
    } 

    
    return name;
  }
  
  public static String getAliasName(String name) {
    String result = null;
    if (Variable.Lang_YML.getStringList("PlaceHolders.OtherWorldAlias") == null) {
      result = name;
    } else {
      for (int e = 0; e < Variable.Lang_YML.getStringList("PlaceHolders.OtherWorldAlias").size(); e++) {
        String[] temp = ((String)Variable.Lang_YML.getStringList("PlaceHolders.OtherWorldAlias").get(e)).split(",");
        if (temp[0].equalsIgnoreCase(name)) {
          result = temp[1];
        }
      } 
    } 
    return result;
  }





  
  public static boolean CheckIsHome(String name) {
    if (Variable.list_home.contains(name.replace(Variable.world_prefix, ""))) {
      return true;
    }
    return false;
  }


  
  public static void deleteFile(File file) {
    if (file.exists()) {
      try {
        if (file.isDirectory()) {
          File[] files = file.listFiles();
          if (files.length > 0) {
            int b; int i; File[] arrayOfFile; for (i = (arrayOfFile = files).length, b = 0; b < i; ) { File aFile = arrayOfFile[b];
              deleteFile(aFile);
              
              b++; }
          
          } 
        } 
        file.delete();
      } catch (Exception e) {
        e.printStackTrace();
      } 
    }
  }
  
  public static void copyDir(String oldDir, String newDir) {
    File srcDir = new File(oldDir);
    
    if (!srcDir.exists() || !srcDir.isDirectory()) {
      return;
    }
    File destDir = new File(newDir);
    if (!destDir.exists())
    {
      if (destDir.mkdirs()) {
        
        File[] files = srcDir.listFiles(); int b; int i; File[] arrayOfFile;
        for (i = (arrayOfFile = files).length, b = 0; b < i; ) { File f = arrayOfFile[b];
          
          if (f.isFile()) {
            copyFile(f, new File(newDir, f.getName()));
          } else if (f.isDirectory()) {
            copyDir(String.valueOf(oldDir) + File.separator + f.getName(), String.valueOf(newDir) + File.separator + f.getName());
          } 
          b++; }
      
      } 
    }
  }









  
  public static void copyFile(File oldDir, File newDir) {
    BufferedInputStream bufferedInputStream = null;
    BufferedOutputStream bufferedOutputStream = null;
    byte[] b = new byte[1024];
    
    try {
      bufferedInputStream = new BufferedInputStream(new FileInputStream(oldDir));
      
      bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(newDir));
      
      int len;
      while ((len = bufferedInputStream.read(b)) > -1)
      {
        bufferedOutputStream.write(b, 0, len);
      }
      
      bufferedOutputStream.flush();
    } catch (IOException len) {
      IOException iOException;
    } finally {
      if (bufferedInputStream != null) {
        
        try {
          bufferedInputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        } 
      }
      if (bufferedOutputStream != null) {
        try {
          bufferedOutputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        } 
      }
    } 
  }




  
  public static Boolean CheckOwnerAndManagerAndOP(Player p, String name) {
    boolean return_boolean = false;
    
    if (p.getName().equalsIgnoreCase(name)) {
      return_boolean = true;
    }
    
    if (p.isOp()) {
      return_boolean = true;
    }
    
    if (Variable.bungee) {
      List<String> ops = MySQL.getOP(name);
      for (int e = 0; e < ops.size(); e++) {
        if (((String)ops.get(e)).equalsIgnoreCase(p.getName())) {
          return_boolean = true;
          
          break;
        } 
      } 
    } else {
      File f = new File(Variable.Tempf, String.valueOf(name) + ".yml");
      if (f.exists()) {
        
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
        List<String> OP = yml.getStringList("OP");
        Boolean CheckOP = Boolean.valueOf(false);
        if (OP == null) {
          OP = new ArrayList<>();
        }
        for (int i = 0; i < OP.size(); i++) {
          if (((String)OP.get(i)).equalsIgnoreCase(p.getName())) {
            CheckOP = Boolean.valueOf(true);
          }
        } 
        
        if (CheckOP.booleanValue()) {
          return_boolean = true;
        }
      } else {
        
        return_boolean = false;
      } 
    } 

    
    return Boolean.valueOf(return_boolean);
  }
  
  public static Boolean Check(Player p, String name) {
	  name = name.replace(Variable.world_prefix,"");
    boolean result = false;
    File f = new File(Variable.Tempf, String.valueOf(name) + ".yml");
    
    if (CheckOwnerAndManagerAndOP(p, name).booleanValue()) {
      return Boolean.valueOf(true);
    }
    
    if (Variable.bungee) {
      List<String> ops = MySQL.getMembers(name);
      for (int e = 0; e < ops.size(); e++) {
        if (((String)ops.get(e)).equalsIgnoreCase(p.getName())) {
          result = true;

          
          break;
        } 
      } 
    } else if (f.exists()) {
      YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(f);
      List<String> Members = yamlConfiguration.getStringList("Members");
      Boolean CheckMembers = Boolean.valueOf(false);
      if (Members == null) {
        Members = new ArrayList<>();
      }
      for (int i = 0; i < Members.size(); i++) {
        if (((String)Members.get(i)).equalsIgnoreCase(p.getName()) || ((String)Members.get(i)).equals("*")) {
          result = true;
          
          break;
        } 
      } 
    } else {
      result = false;
    } 

    
    return Boolean.valueOf(result);
  }
  
  public static Boolean CheckBlack(Player p, String name) {
    boolean check = false;
    
    if (Variable.bungee) {
      List<String> ops = MySQL.getDenys(name);
      for (int e = 0; e < ops.size(); e++) {
        if (((String)ops.get(e)).equalsIgnoreCase(p.getName())) {
          check = true;
          
          break;
        } 
      } 
    } else {
      File f = new File(Variable.Tempf, String.valueOf(name) + ".yml");
      if (f.exists()) {
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(f);
        List<String> Members = yamlConfiguration.getStringList("Denys");
        Boolean CheckMembers = Boolean.valueOf(false);
        if (Members == null) {
          Members = new ArrayList<>();
        }
        for (int i = 0; i < Members.size(); i++) {
          if (((String)Members.get(i)).equalsIgnoreCase(p.getName())) {
            check = true;
            
            break;
          } 
        } 
      } else {
        check = false;
      } 
    } 
    
    return Boolean.valueOf(check);
  }

  
  public static Boolean CheckIllegalName(Player p) {
    String name = p.getName();
    if (name.contains("|") || name.contains("&") || name.contains("!") || name.contains("@") || name.contains("^") || 
      name.contains("*") || name.toUpperCase().contains("DIM")) {
      return Boolean.valueOf(true);
    }
    
    for (int i = 0; i < Main.JavaPlugin.getConfig().getStringList("IlleagalName").size(); i++) {
      String temp = Main.JavaPlugin.getConfig().getStringList("IlleagalName").get(i);
      if (p.getName().equalsIgnoreCase(temp)) {
        return Boolean.valueOf(true);
      }
    } 
    
    return Boolean.valueOf(false);
  }





  
  public static void refreshBorder(final World world) { (new BukkitRunnable()
      {

        
        public void run()
        {
          if (!Util.CheckIsHome(world.getName().replace(Variable.world_prefix, ""))) {
            return;
          }
          if (!Main.JavaPlugin.getConfig().getBoolean("HDSwitch") || !Variable.Hologram_switch) {
            return;
          }
          
          int level = 1;


          
          if (Variable.bungee) {
            level = Integer.valueOf(MySQL.getLevel(world.getName().replace(Variable.world_prefix, ""))).intValue();
          } else {
            File f = new File(Variable.Tempf, String.valueOf(world.getName().replace(Variable.world_prefix, "")) + ".yml");
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(f);
            level = yamlConfiguration.getInt("Level");
          } 
          
          int addradius = Main.JavaPlugin.getConfig().getInt("UpdateRadius") / 2;
          Location loc = world.getSpawnLocation();
          
          Location loc1 = loc.clone();
          loc1.setX(loc1.getX() + (addradius * level) + 5.0D);
          loc1.setY(loc1.getY() + 5.0D);

          
          Location loc5 = loc1.clone();
          loc5.setZ(loc5.getZ() + (addradius * level) + 5.0D);
          
          Location loc6 = loc1.clone();
          loc6.setZ(loc6.getZ() - (addradius * level) - 5.0D);
          
          Location loc2 = loc.clone();
          loc2.setX(loc2.getX() - (addradius * level) - 5.0D);
          loc2.setY(loc2.getY() + 5.0D);
          
          Location loc7 = loc2.clone();
          loc7.setZ(loc7.getZ() + (addradius * level) + 5.0D);
          
          Location loc8 = loc2.clone();
          loc8.setZ(loc8.getZ() - (addradius * level) - 5.0D);
          
          Location loc3 = loc.clone();
          loc3.setZ(loc3.getZ() + (addradius * level) + 5.0D);
          loc3.setY(loc3.getY() + 5.0D);
          
          Location loc4 = loc.clone();
          loc4.setZ(loc4.getZ() - (addradius * level) - 5.0D);
          loc4.setY(loc4.getY() + 5.0D);
          
          List<Hologram> hololist = new ArrayList<>();

          
          loc1 = Util.getAir(loc1);
          loc2 = Util.getAir(loc2);
          loc3 = Util.getAir(loc3);
          loc4 = Util.getAir(loc4);
          loc5 = Util.getAir(loc5);
          loc6 = Util.getAir(loc6);
          loc7 = Util.getAir(loc7);
          loc8 = Util.getAir(loc8);
          Hologram holo1 = HologramsAPI.createHologram((Plugin)Main.JavaPlugin, loc1);
          for (int line = 0; line < Variable.Lang_YML.getStringList("HDTagsNorth").size(); line++) {
            String TempLine = Variable.Lang_YML.getStringList("HDTagsNorth").get(line);
            TempLine = PlaceholderAPI.setPlaceholders(null, TempLine);
            holo1.insertTextLine(line, TempLine);
          } 
          holo1.teleport(loc1);
          
          Hologram holo2 = HologramsAPI.createHologram((Plugin)Main.JavaPlugin, loc2);
          for (int line = 0; line < Variable.Lang_YML.getStringList("HDTagsSouth").size(); line++) {
            String TempLine = Variable.Lang_YML.getStringList("HDTagsSouth").get(line);
            TempLine = PlaceholderAPI.setPlaceholders(null, TempLine);
            holo2.insertTextLine(line, TempLine);
          } 
          holo2.teleport(loc2);
          
          Hologram holo3 = HologramsAPI.createHologram((Plugin)Main.JavaPlugin, loc4);
          for (int line = 0; line < Variable.Lang_YML.getStringList("HDTagsEast").size(); line++) {
            String TempLine = Variable.Lang_YML.getStringList("HDTagsEast").get(line);
            TempLine = PlaceholderAPI.setPlaceholders(null, TempLine);
            holo3.insertTextLine(line, TempLine);
          } 
          holo3.teleport(loc4);
          
          Hologram holo4 = HologramsAPI.createHologram((Plugin)Main.JavaPlugin, loc3);
          for (int line = 0; line < Variable.Lang_YML.getStringList("HDTagsWest").size(); line++) {
            String TempLine = Variable.Lang_YML.getStringList("HDTagsWest").get(line);
            TempLine = PlaceholderAPI.setPlaceholders(null, TempLine);
            holo4.insertTextLine(line, TempLine);
          } 
          holo4.teleport(loc3);
          
          Hologram holo5 = HologramsAPI.createHologram((Plugin)Main.JavaPlugin, loc5);
          for (int line = 0; line < Variable.Lang_YML.getStringList("HDTagsNorthWest").size(); line++) {
            String TempLine = Variable.Lang_YML.getStringList("HDTagsNorthWest").get(line);
            TempLine = PlaceholderAPI.setPlaceholders(null, TempLine);
            holo5.insertTextLine(line, TempLine);
          } 
          holo5.teleport(loc5);
          
          Hologram holo6 = HologramsAPI.createHologram((Plugin)Main.JavaPlugin, loc6);
          for (int line = 0; line < Variable.Lang_YML.getStringList("HDTagsNorthEast").size(); line++) {
            String TempLine = Variable.Lang_YML.getStringList("HDTagsNorthEast").get(line);
            TempLine = PlaceholderAPI.setPlaceholders(null, TempLine);
            holo6.insertTextLine(line, TempLine);
          } 
          holo6.teleport(loc6);
          
          Hologram holo7 = HologramsAPI.createHologram((Plugin)Main.JavaPlugin, loc7);
          for (int line = 0; line < Variable.Lang_YML.getStringList("HDTagsWestSouth").size(); line++) {
            String TempLine = Variable.Lang_YML.getStringList("HDTagsWestSouth").get(line);
            TempLine = PlaceholderAPI.setPlaceholders(null, TempLine);
            holo7.insertTextLine(line, TempLine);
          } 
          holo7.teleport(loc7);
          
          Hologram holo8 = HologramsAPI.createHologram((Plugin)Main.JavaPlugin, loc8);
          for (int line = 0; line < Variable.Lang_YML.getStringList("HDTagsEastSouth").size(); line++) {
            String TempLine = Variable.Lang_YML.getStringList("HDTagsEastSouth").get(line);
            TempLine = PlaceholderAPI.setPlaceholders(null, TempLine);
            holo8.insertTextLine(line, TempLine);
          } 
          holo8.teleport(loc8);
          
          hololist.add(holo1);
          hololist.add(holo2);
          hololist.add(holo3);
          hololist.add(holo4);
          hololist.add(holo5);
          hololist.add(holo6);
          hololist.add(holo7);
          hololist.add(holo8);
          
          if (Variable.hololist.containsKey(world.getName())) {
            for (Hologram temp : Variable.hololist.get(world.getName())) {
              temp.delete();
            }
          }
          Variable.hololist.put(world.getName(), hololist);
        }
      }).runTask((Plugin)Main.JavaPlugin); }
}


