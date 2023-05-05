package me.madcat.mixin;

import java.util.Map;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.launch.MixinBootstrap;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.Name()
public class MixinLoader implements IFMLLoadingPlugin
{
    public MixinLoader() {
        System.out.println("Loading mixins by MadCat");
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.madcat.json");
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
        System.out.println(MixinEnvironment.getDefaultEnvironment().getObfuscationContext());
    }
    
    public String[] getASMTransformerClass() {
        return new String[0];
    }
    
    public String getModContainerClass() {
        return null;
    }
    
    public String getSetupClass() {
        return null;
    }
    
    public void injectData(final Map<String, Object> data) {
        final boolean isObfuscatedEnvironment = (boolean) data.get("runtimeDeobfuscationEnabled");
    }
    
    public String getAccessTransformerClass() {
        return null;
    }
}
