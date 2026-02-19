package com.artur114.srptowerdefense.register;

import com.artur114.srptowerdefense.common.init.InitBlocks;
import com.artur114.srptowerdefense.common.init.InitItems;
import com.artur114.srptowerdefense.common.network.client.CPacketSyncBlocksDamage;
import com.artur114.srptowerdefense.common.util.interfaces.IHasModel;
import com.artur114.srptowerdefense.common.util.interfaces.IHasTileEntity;
import com.artur114.srptowerdefense.common.util.interfaces.IIsNeedRegister;
import com.artur114.srptowerdefense.main.TowerDefence;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Objects;

@Mod.EventBusSubscriber
public class Registerer {

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        for (Item item : InitItems.ITEMS_REGISTER_BUSS) {
            boolean register = true;

            if (item instanceof IIsNeedRegister) {
                register = ((IIsNeedRegister) item).isNeedRegister();
            }

            if (register) {
                event.getRegistry().register(item);
            }
        }
    }

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        for (Block block : InitBlocks.BLOCKS_REGISTER_BUSS) {
            boolean register = true;

            if (block instanceof IIsNeedRegister) {
                register = ((IIsNeedRegister) block).isNeedRegister();
            }

            if (register) {
                event.getRegistry().register(block);
            }
        }
    }

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event) {
        for(Item item : InitItems.ITEMS_REGISTER_BUSS) {
            if(item instanceof IHasModel) {
                boolean register = true;

                if (item instanceof IIsNeedRegister) {
                    register = ((IIsNeedRegister) item).isNeedRegister();
                }

                if (register) {
                    ((IHasModel) item).registerModels();
                }
            }
        }
        for(Block block : InitBlocks.BLOCKS_REGISTER_BUSS) {
            if(block instanceof IHasModel) {
                boolean register = true;

                if (block instanceof IIsNeedRegister) {
                    register = ((IIsNeedRegister) block).isNeedRegister();
                }

                if (register) {
                    ((IHasModel) block).registerModels();
                }
            }
        }
    }

    public static void preInit(FMLPreInitializationEvent e) {
        Registerer.registerTileEntities();
        Registerer.initNetwork();
    }

    public static void registerTileEntities() {
        for (Block block : InitBlocks.BLOCKS_REGISTER_BUSS) {
            if (block instanceof IHasTileEntity<?>) {
                GameRegistry.registerTileEntity(((IHasTileEntity<?>) block).tileEntityClass(), Objects.requireNonNull(block.getRegistryName()));
            }
        }
    }

    public static void initNetwork() {
        int i = 0;
        TowerDefence.NETWORK.registerMessage(new CPacketSyncBlocksDamage.HandlerSPC(), CPacketSyncBlocksDamage.class, i++, Side.CLIENT);
    }
}
