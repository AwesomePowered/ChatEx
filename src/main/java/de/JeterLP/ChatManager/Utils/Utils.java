package de.JeterLP.ChatManager.Utils;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;
import de.JeterLP.ChatManager.ChatEX;
import de.JeterLP.ChatManager.ChatListener;
import de.JeterLP.ChatManager.Plugins.PluginManager;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author TheJeterLP
 */
public class Utils {

        private static final Pattern chatColorPattern = Pattern.compile("(?i)&([0-9A-F])");
        private static final Pattern chatMagicPattern = Pattern.compile("(?i)&([K])");
        private static final Pattern chatBoldPattern = Pattern.compile("(?i)&([L])");
        private static final Pattern chatStrikethroughPattern = Pattern.compile("(?i)&([M])");
        private static final Pattern chatUnderlinePattern = Pattern.compile("(?i)&([N])");
        private static final Pattern chatItalicPattern = Pattern.compile("(?i)&([O])");
        private static final Pattern chatResetPattern = Pattern.compile("(?i)&([R])");
        private static final String permissionChatColor = "chatex.chat.color";
        private static final String permissionChatMagic = "chatex.chat.magic";
        private static final String permissionChatBold = "chatex.chat.bold";
        private static final String permissionChatStrikethrough = "chatex.chat.strikethrough";
        private static final String permissionChatUnderline = "chatex.chat.underline";
        private static final String permissionChatItalic = "chatex.chat.italic";
        private static final String permissionChatReset = "chatex.chat.reset";

        public static String translateColorCodes(String string, Player p) {
                if (string == null) {
                        return "";
                }
                String newstring = string;
                if (p.hasPermission(permissionChatColor)) newstring = chatColorPattern.matcher(newstring).replaceAll("\u00A7$1");
                if (p.hasPermission(permissionChatMagic)) newstring = chatMagicPattern.matcher(newstring).replaceAll("\u00A7$1");
                if (p.hasPermission(permissionChatBold)) newstring = chatBoldPattern.matcher(newstring).replaceAll("\u00A7$1");
                if (p.hasPermission(permissionChatStrikethrough)) newstring = chatStrikethroughPattern.matcher(newstring).replaceAll("\u00A7$1");
                if (p.hasPermission(permissionChatUnderline)) newstring = chatUnderlinePattern.matcher(newstring).replaceAll("\u00A7$1");
                if (p.hasPermission(permissionChatItalic)) newstring = chatItalicPattern.matcher(newstring).replaceAll("\u00A7$1");
                if (p.hasPermission(permissionChatReset)) newstring = chatResetPattern.matcher(newstring).replaceAll("\u00A7$1");
                return newstring;
        }

        public static String replaceColors(String message) {
                return message.replaceAll("&((?i)[0-9a-fk-or])", "§$1");
        }

        public static List<Player> getLocalRecipients(Player sender) {
                Location playerLocation = sender.getLocation();
                List<Player> recipients = new LinkedList<Player>();
                double squaredDistance = Math.pow(Config.RANGE.getDouble(), 2);
                for (Player recipient : Bukkit.getServer().getOnlinePlayers()) {
                        if (!recipient.getWorld().equals(sender.getWorld())) {
                                continue;
                        }
                        if (playerLocation.distanceSquared(recipient.getLocation()) > squaredDistance) {
                                continue;
                        }
                        recipients.add(recipient);
                }
                return recipients;
        }

        public static String replacePlayerPlaceholders(Player player, String format) {
                String result = format;
                result = result.replace("%displayname", player.getDisplayName());
                result = result.replace("%prefix", PluginManager.getInstance().getPrefix(player));
                result = result.replace("%suffix", PluginManager.getInstance().getSuffix(player));
                result = result.replace("%player", player.getDisplayName());
                result = result.replace("%world", player.getWorld().getName());
                result = result.replace("%group", PluginManager.getInstance().getGroupNames(player)[0]);
                result = replaceTime(result);
                result = replaceFaction(player, result);
                result = replaceColors(result);
                return result;
        }

        private static String replaceTime(String message) {
                Calendar calendar = Calendar.getInstance();
                if (message.contains("%time")) {
                        DateFormat date = new SimpleDateFormat("HH:mm:ss");
                        message = message.replace("%time", date.format(calendar.getTime()));
                }
                if (message.contains("%h")) {
                        final String hour = String.valueOf(calendar.get(Calendar.HOUR));
                        message = message.replace("%h", hour);
                }
                if (message.contains("%H")) {
                        final String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
                        message = message.replace("%H", hour);
                }
                if (message.contains("%i")) {
                        final String minute = String.valueOf(calendar.get(Calendar.MINUTE));
                        message = message.replace("%i", minute);
                }
                if (message.contains("%s")) {
                        final String second = String.valueOf(calendar.get(Calendar.SECOND));
                        message = message.replace("%s", second);
                }
                if (message.contains("%a")) {
                        message = message.replace("%a", (calendar.get(Calendar.AM_PM) == 0) ? "am" : "pm");
                }
                if (message.contains("%A")) {
                        message = message.replace("%A", (calendar.get(Calendar.AM_PM) == 0) ? "AM" : "PM");
                }
                if (message.contains("%m")) {
                        final String month = String.valueOf(calendar.get(Calendar.MONTH));
                        message = message.replace("%m", month);
                }
                if (message.contains("%M")) {
                        String month = "";
                        final int monat = calendar.get(Calendar.MONTH) + 1;
                        switch (monat) {
                                case 1:
                                        month = "January";
                                        break;
                                case 2:
                                        month = "February";
                                        break;
                                case 3:
                                        month = "March";
                                        break;
                                case 4:
                                        month = "April";
                                        break;
                                case 5:
                                        month = "May";
                                        break;
                                case 6:
                                        month = "June";
                                        break;
                                case 7:
                                        month = "July";
                                        break;
                                case 8:
                                        month = "August";
                                        break;
                                case 9:
                                        month = "September";
                                        break;
                                case 10:
                                        month = "October";
                                        break;
                                case 11:
                                        month = "November";
                                        break;
                                case 12:
                                        month = "December";
                                        break;
                                default:
                                        month = Locales.COMMAND_CLEAR_UNKNOWN.getString();
                                        break;
                        }
                        message = message.replace("%M", month);
                }

                if (message.contains("%y")) {
                        final String year = String.valueOf(calendar.get(Calendar.YEAR));
                        message = message.replace("%m", year);
                }

                if (message.contains("%Y")) {
                        int year = calendar.get(Calendar.YEAR);
                        String year_new = String.valueOf(year);
                        year_new = year_new.replace("19", "").replace("20", "");
                        message = message.replace("%Y", year_new);
                }

                if (message.contains("%d")) {
                        final String day = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH));
                        message = message.replace("%d", day);
                }

                if (message.contains("%D")) {
                        final String day = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
                        message = message.replace("%D", day);
                }

                message = replaceColors(message);
                return message;
        }

        public static boolean registerListener() {
                try {
                        String prio = Config.EVENTPRIORITY.getString();
                        Object listener = Class.forName("de.JeterLP.ChatManager.Listeners." + prio).newInstance();
                        if (listener instanceof ChatListener) {
                                ChatListener l = (ChatListener) listener;
                                l.register();
                                ChatEX.getInstance().getLogger().info("Listener registered with Priority: " + prio);
                                return true;
                        } else {
                                ChatEX.getInstance().getLogger().severe("Listener is not an instance of the Listener Class.");
                                return false;
                        }
                } catch (Throwable ex) {
                        ex.printStackTrace();
                        return false;
                }
        }

        public static String replaceFaction(Player player, String msg) {
                if (!HookManager.checkFactions()) {
                        return msg.replace("%faction", "");
                }
                final UPlayer uplayer = UPlayer.get(player);
                final Faction faction = uplayer.getFaction();
                return msg.replace("%faction", faction.getName());
        }
}
