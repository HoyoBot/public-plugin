package net.catrainbow.economy;

import cn.hoyobot.sdk.utils.FastConfig;

public class UserProvider {

    public int uid;
    private final FastConfig fastConfig;

    public UserProvider(int uid) {
        this.uid = uid;
        this.fastConfig = new FastConfig(EconomyAPI.getInstance().getDataPath() + "/users/" + this.uid + ".json");
    }

    public void putData(String key, Object value) {
        this.getData().put(key, value);
        this.getData().save();
    }

    public void removeData(String key) {
        if (this.getData().containsKey(key)) {
            this.getData().remove(key);
            this.getData().save();
        }
    }

    public String getString(String key) {
        return this.getData().getString(key);
    }

    public int getInt(String key) {
        return this.getData().getInt(key);
    }

    public double getDouble(String key) {
        return this.getData().getDouble(key);
    }

    public FastConfig getData() {
        return this.fastConfig;
    }

}
