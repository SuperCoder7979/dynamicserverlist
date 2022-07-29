package supercoder79.dynamicserverlist;

import net.minecraft.client.multiplayer.ServerData;

public class SyntheticServerData extends ServerData {

    private final String realName;
    private final String realIp;

    public SyntheticServerData(String name, String ip, boolean lan) {
        super(name, ip, lan);
        this.realName = name;
        this.realIp = ip;
    }

    public void refresh() {
        this.name = realName;
        this.ip = realIp;
    }
}
