package me.madcat.features.command.commands;

import com.google.gson.JsonParser;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.madcat.MadCat;
import me.madcat.features.command.Command;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Setting;
import me.madcat.manager.ConfigManager;
import me.madcat.manager.ModuleManager;

public class ModuleCommand
extends Command {
    public ModuleCommand() {
        super("module", new String[]{"<module>", "<set/reset>", "<setting>", "<value>"});
    }

    @Override
    public void execute(String[] stringArray) {
        Setting setting;
        if (stringArray.length == 1) {
            ModuleCommand.sendMessage("Modules: ");
            for (Module.Category category : MadCat.moduleManager.getCategories()) {
                StringBuilder string = new StringBuilder(category.getName() + ": ");
                for (Module module : MadCat.moduleManager.getModulesByCategory(category)) {
                    string.append(module.isEnabled() ? ChatFormatting.GREEN : ChatFormatting.RED).append(module.getName()).append(ChatFormatting.WHITE).append(", ");
                }
                ModuleCommand.sendMessage(string.toString());
            }
            return;
        }
        Module module = MadCat.moduleManager.getModuleByDisplayName(stringArray[0]);
        if (module == null) {
            module = ModuleManager.getModuleByName(stringArray[0]);
            if (module == null) {
                ModuleCommand.sendMessage("This module doesnt exist.");
                return;
            }
            ModuleCommand.sendMessage(" This is the original name of the module. Its current name is: " + module.getDisplayName());
            return;
        }
        if (stringArray.length == 2) {
            ModuleCommand.sendMessage(module.getDisplayName() + " : " + module.getDescription());
            for (Setting setting2 : module.getSettings()) {
                ModuleCommand.sendMessage(setting2.getName() + " : " + setting2.getValue() + ", " + setting2.getDescription());
            }
            return;
        }
        if (stringArray.length == 3) {
            if (stringArray[1].equalsIgnoreCase("set")) {
                ModuleCommand.sendMessage("Please specify a setting.");
            } else if (stringArray[1].equalsIgnoreCase("reset")) {
                for (Setting setting3 : module.getSettings()) {
                    setting3.setValue(setting3.getDefaultValue());
                }
            } else {
                ModuleCommand.sendMessage("This command doesnt exist.");
            }
            return;
        }
        if (stringArray.length == 4) {
            ModuleCommand.sendMessage("Please specify a value.");
            return;
        }
        if (stringArray.length == 5 && (setting = module.getSettingByName(stringArray[2])) != null) {
            JsonParser jsonParser = new JsonParser();
            if (setting.getType().equalsIgnoreCase("String")) {
                setting.setValue(stringArray[3]);
                ModuleCommand.sendMessage(ChatFormatting.DARK_GRAY + module.getName() + " " + setting.getName() + " has been set to " + stringArray[3] + ".");
                return;
            }
            try {
                if (setting.getName().equalsIgnoreCase("Enabled")) {
                    if (stringArray[3].equalsIgnoreCase("true")) {
                        module.enable();
                    }
                    if (stringArray[3].equalsIgnoreCase("false")) {
                        module.disable();
                    }
                }
                ConfigManager.setValueFromJson(module, setting, jsonParser.parse(stringArray[3]));
            }
            catch (Exception exception) {
                ModuleCommand.sendMessage("Bad Value! This setting requires a: " + setting.getType() + " value.");
                return;
            }
            ModuleCommand.sendMessage(ChatFormatting.GRAY + module.getName() + " " + setting.getName() + " has been set to " + stringArray[3] + ".");
        }
    }
}

