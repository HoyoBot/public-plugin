package net.catrainbow.economy;

import cn.hoyobot.sdk.plugin.Plugin;

public class EconomyAPI extends Plugin {

    private static EconomyAPI economyAPI;

    public static EconomyAPI getInstance() {
        return economyAPI;
    }

    @Override
    public void onEnable() {
        economyAPI = this;
        this.getLogger().info("EconomyAPI 数据接口插件加载成功!");
    }

    public UserProvider getUser(int uid) {
        return new UserProvider(uid);
    }

}
