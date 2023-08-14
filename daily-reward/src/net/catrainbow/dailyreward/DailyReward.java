package net.catrainbow.dailyreward;

import cn.hoyobot.sdk.event.villa.VillaSendMessageEvent;
import cn.hoyobot.sdk.network.protocol.mihoyo.MsgContentInfo;
import cn.hoyobot.sdk.network.protocol.type.MessageEntityType;
import cn.hoyobot.sdk.network.protocol.type.TextType;
import cn.hoyobot.sdk.plugin.Plugin;
import cn.hoyobot.sdk.utils.Config;
import net.catrainbow.economy.UserProvider;

import java.time.LocalDateTime;
import java.util.HashMap;

public class DailyReward extends Plugin {

    public Config config;
    public String moneyName;
    public int moneyCount;
    public String success;
    public String failed;
    public String command;
    public HashMap<String, Long> lastUpdate = new HashMap<>();

    @Override
    public void onEnable() {
        this.getLogger().pluginInfo("打卡插件加载中...");
        this.saveResource("config.yml");
        this.config = new Config(this.getDataPath() + "/config.yml", 2);
        this.moneyName = this.config.getString("money-name");
        this.moneyCount = this.config.getInt("money-count");
        this.success = this.config.getString("success");
        this.failed = this.config.getString("failed");
        this.command = this.config.getString("command");
        this.getBotProxy().getEventManager().subscribe(VillaSendMessageEvent.class, this::onMemberMessageEvent);
    }

    public void onMemberMessageEvent(VillaSendMessageEvent sendMessageEvent) {
        this.getBotProxy().getBot().setVillaID(String.valueOf(sendMessageEvent.getVilla().getId()));
        String updaterName = sendMessageEvent.getVilla().getId() + ":" + sendMessageEvent.getRoomID();
        if (!lastUpdate.containsKey(updaterName)) this.lastUpdate.put(updaterName, System.currentTimeMillis() - 2000L);
        if (System.currentTimeMillis() - this.lastUpdate.get(updaterName) < 1000L) return;
        this.lastUpdate.put(updaterName, System.currentTimeMillis());
        String message = sendMessageEvent.getMsgContentInfo().getValue().split("\\s+", 2)[1];
        message = message.replace("/", "");
        String[] subCommands = message.split("\\s+", 2);
        String messageID = sendMessageEvent.getMsgID();
        int sendAt = sendMessageEvent.getSendAt();
        StringBuilder stringBuilder;
        if (subCommands[0].equals(this.command)) {
            UserProvider userProvider = new UserProvider(sendMessageEvent.getSenderByMember().getUid());
            if (!userProvider.getData().containsKey("plugin.dailyReward.update")) {
                userProvider.getData().put("plugin.dailyReward.update", TimeUtil.formatTime(TimeUtil.getNowTime().minusDays(10)));
                userProvider.getData().save();
            }
            LocalDateTime now = TimeUtil.getNowTime();
            LocalDateTime lastReward = TimeUtil.stringToTime(userProvider.getString("plugin.dailyReward.update"));
            if (now.getDayOfYear() > lastReward.getDayOfYear()) {
                userProvider.getData().put("plugin.dailyReward.update", TimeUtil.formatTime(now));
                if (!userProvider.getData().containsKey("plugin.dailyReward.money"))
                    userProvider.getData().set("plugin.dailyReward.money", 0);
                int count = userProvider.getInt("plugin.dailyReward.money");
                userProvider.getData().set("plugin.dailyReward.money", (count + this.moneyCount));
                userProvider.getData().save();
                stringBuilder = new StringBuilder(this.success.replace("{money-name}", this.moneyName).replace("{money-count}", String.valueOf(this.moneyCount)));
            } else {
                stringBuilder = new StringBuilder(this.failed);
            }
            MsgContentInfo msgContentInfo = new MsgContentInfo("\n").appendMentionedMessage(sendMessageEvent.getSenderByMember().getUid(), MessageEntityType.MENTIONED_USER).append(stringBuilder.toString()).setQuotedParent(messageID, sendAt);
            this.getBotProxy().getBot().sendMessage(sendMessageEvent.getRoomID(), msgContentInfo, TextType.MESSAGE);
        }
    }


}
