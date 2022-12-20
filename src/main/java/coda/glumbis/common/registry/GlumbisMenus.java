package coda.glumbis.common.registry;

import coda.glumbis.Glumbis;
import coda.glumbis.common.menu.GlumpCoilMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GlumbisMenus {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, Glumbis.MOD_ID);

    public static final RegistryObject<MenuType<GlumpCoilMenu>> GLUMP_COIL = MENU_TYPES.register("glump_coil", () -> IForgeMenuType.create(GlumpCoilMenu::new));
}
