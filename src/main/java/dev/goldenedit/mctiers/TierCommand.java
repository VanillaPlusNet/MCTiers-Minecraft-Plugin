package dev.goldenedit.mctiers;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class TierCommand implements CommandExecutor {
    String uuid = "";
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


            String username = args.length > 0 ? args[0] : sender.getName();
            Player p = Bukkit.getServer().getPlayer(username);

            Bukkit.getScheduler().runTaskAsynchronously(MCTiers.plugin, task -> {
                try {
                    URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + username);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();

                    int responseCode = conn.getResponseCode();
                    if (responseCode != 200) {
                        throw new Exception("User not found");
                    }
                    StringBuilder inline = new StringBuilder();
                    Scanner scanner = new Scanner(url.openStream());
                    while (scanner.hasNext()) {
                        inline.append(scanner.nextLine());
                    }
                    scanner.close();

                    JSONParser parser = new JSONParser();
                    JSONObject profileData = (JSONObject) parser.parse(inline.toString());

                    uuid = profileData.get("id").toString();

                } catch (Exception e) {
                    sender.sendMessage("§cThat is not a valid username");
                    task.cancel();
                }


                try {
                    URL url = new URL("https://mctiers.com/api/rankings/" + uuid);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();

                    int responseCode = conn.getResponseCode();
                    if (responseCode != 200) {
                        throw new Exception("User not found");
                    }
                    StringBuilder inline = new StringBuilder();
                    Scanner scanner = new Scanner(url.openStream());
                    while (scanner.hasNext()) {
                        inline.append(scanner.nextLine());
                    }
                    scanner.close();

                    JSONParser parser = new JSONParser();
                    JSONObject tierData = (JSONObject) parser.parse(inline.toString());

                    JSONObject vanillaData = (JSONObject) tierData.get("vanilla");

                    long tier = (long) vanillaData.get("tier");
                    long pos = (long) vanillaData.get("pos");

                    String tierString = pos == 0 ? "HT" : "LT";
                    sender.sendMessage("§x§0§2§a§c§f§b" + username + " is §x§E§0§0§0§E§0" + tierString + tier);
                } catch (Exception e) {
                    sender.sendMessage("§cThat user has not been tier tested yet or there was an API Error");
                    task.cancel();
                }

            });
        return true;
    }

}

