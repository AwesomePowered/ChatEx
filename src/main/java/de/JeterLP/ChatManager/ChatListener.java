package de.JeterLP.ChatManager;

import de.JeterLP.ChatManager.Utils.Config;
import de.JeterLP.ChatManager.Utils.FileUtils;
import de.JeterLP.ChatManager.Utils.Utils;
import de.JeterLP.ChatManager.Plugins.PluginManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author TheJeterLP
 */
public abstract class ChatListener implements Listener {

        public void register() {
                Bukkit.getServer().getPluginManager().registerEvents(this, ChatEX.getInstance());
        }

        protected void execute(AsyncPlayerChatEvent event) {
                String format = PluginManager.getInstance().getMessageFormat(event.getPlayer());
                boolean localChat = Config.RANGEMODE.getBoolean();
                boolean global = false;
                Player player = event.getPlayer();
                String chatMessage = event.getMessage();
                if (localChat) {
                        ChatEX.debug("Local chat is enabled!");
                        if (chatMessage.startsWith("!") && player.hasPermission("chatex.chat.global")) {
                                ChatEX.debug("Global message!");
                                chatMessage = chatMessage.replaceFirst("!", "");
                                format = PluginManager.getInstance().getGlobalMessageFormat(event.getPlayer());
                                global = true;
                        }
                        if (!global) {
                                event.getRecipients().clear();
                                ChatEX.debug("Adding recipients to the message...");
                                event.getRecipients().addAll(Utils.getLocalRecipients(player));
                        }
                }
                format = format.replace("%message", "%2$s").replace("%player", "%1$s");
                format = Utils.replacePlayerPlaceholders(player, format);
                ChatEX.debug("Setting format");
                event.setFormat(format);
                chatMessage = Utils.translateColorCodes(chatMessage, player);
                ChatEX.debug("Setting message!");
                event.setMessage(chatMessage);
                ChatEX.debug("Logging chatmessage...");
                FileUtils.writeToFile(event.getPlayer().getName(), event.getMessage());
        }

}
