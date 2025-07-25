package fr.maxlego08.menu.command.commands.players;

import fr.maxlego08.menu.ZMenuPlugin;
import fr.maxlego08.menu.api.players.Data;
import fr.maxlego08.menu.api.players.DataManager;
import fr.maxlego08.menu.api.utils.Message;
import fr.maxlego08.menu.command.VCommand;
import fr.maxlego08.menu.players.ZData;
import fr.maxlego08.menu.zcore.enums.Permission;
import fr.maxlego08.menu.zcore.utils.commands.CommandType;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;

public class CommandMenuPlayersSet extends VCommand {

    public CommandMenuPlayersSet(ZMenuPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZMENU_PLAYERS);
        this.setDescription(Message.DESCRIPTION_PLAYERS_SET);
        this.addSubCommand("set");
        this.addRequireArg("player");
        this.addRequireArg("key", (a, b) -> plugin.getDataManager().getKeys());
        this.addRequireArg("expire after", (a, b) -> Arrays.asList("0", "60", "120", "300", "600", "900", "1800", "3600"));
        this.addRequireArg("value");
        this.setExtendedArgs(true);
    }

    @Override
    protected CommandType perform(ZMenuPlugin plugin) {

        OfflinePlayer player = this.argAsOfflinePlayer(0);
        String key = this.argAsString(1);
        long seconds = this.argAsLong(2);

        if (this.args.length < 6) {
            return CommandType.SYNTAX_ERROR;
        }

        StringBuilder builder = new StringBuilder();
        for (int index = 5; index < this.args.length; index++) {
            builder.append(this.args[index]).append(" ");
        }

        if (builder.toString().isEmpty()) {
            return CommandType.SYNTAX_ERROR;
        }

        String value = builder.substring(0, builder.length() - 1);

        long expiredAt = seconds <= 0 ? 0 : System.currentTimeMillis() + (1000 * seconds);
        Data data = new ZData(key, value, expiredAt);

        DataManager dataManager = plugin.getDataManager();
        dataManager.addData(player.getUniqueId(), data);

        message(plugin, this.sender, Message.PLAYERS_DATA_SET, "%player%", player.getName(), "%key%", key);

        return CommandType.SUCCESS;
    }

}
