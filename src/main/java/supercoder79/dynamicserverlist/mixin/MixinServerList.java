package supercoder79.dynamicserverlist.mixin;

import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import supercoder79.dynamicserverlist.DynamicServerList;
import supercoder79.dynamicserverlist.SyntheticServerData;

import java.util.List;

@Mixin(ServerList.class)
public class MixinServerList {
    @Shadow @Final private List<ServerData> serverList;

    @Inject(method = "load", at = @At("TAIL"))
    private void dsl_loadTail(CallbackInfo ci) {
        // Make sure it's added in parse order
        DynamicServerList.INJECT.forEach(SyntheticServerData::refresh);
        for (int i = DynamicServerList.INJECT.size() - 1; i >= 0; i--) {
            this.serverList.add(0, DynamicServerList.INJECT.get(i));
        }
    }

    @Inject(method = "save", at = @At("HEAD"))
    private void dsl_saveHead(CallbackInfo ci) {
        this.serverList.removeAll(DynamicServerList.INJECT);
    }

    @Inject(method = "save", at = @At("TAIL"))
    private void dsl_saveTail(CallbackInfo ci) {
        DynamicServerList.INJECT.forEach(SyntheticServerData::refresh);
        for (int i = DynamicServerList.INJECT.size() - 1; i >= 0; i--) {
            this.serverList.add(0, DynamicServerList.INJECT.get(i));
        }
    }
}
