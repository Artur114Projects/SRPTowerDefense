package com.artur114.srptowerdefense.common.commands;

import com.artur114.bananalib.math.m2d.vec.IVec2D;
import com.artur114.bananalib.mc.BananaMC;
import com.artur114.bananalib.mc.cap.BananaCaps;
import com.artur114.srptowerdefense.common.init.InitCapabilities;
import com.artur114.srptowerdefense.common.worldstate.towerdefence.IWave;
import com.artur114.srptowerdefense.common.worldstate.towerdefence.TowerDefenceManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CommandTeleportToWave extends CommandBase {
    @Override
    public String getName() {
        return "tptowave";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/tptowave";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        if (args.length != 1) {
            player.sendMessage(new TextComponentString(TextFormatting.RED + "Error"));
        } else {
            TowerDefenceManager system = BananaCaps.capability(sender.getEntityWorld(), InitCapabilities.TOWER_DEFENCE_SYSTEM).orElse(null);
            if (system != null) {
                IVec2D vec = system.tdObjFromId(parseInt(args[0])).pos().toImmutable();
                BlockPos pos = new BlockPos(vec.x() * 16, BananaMC.findHighestBlock(player.world, vec.scale(16).floor()), vec.y() * 16);
                player.connection.setPlayerLocation(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, player.cameraYaw, player.cameraPitch);
            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        TowerDefenceManager system = BananaCaps.capability(sender.getEntityWorld(), InitCapabilities.TOWER_DEFENCE_SYSTEM).orElse(null);

        if (system != null && args.length == 1) {
            List<String> tab = system.tdObjects(IWave.class).stream().map(wave -> wave.id() + "").collect(Collectors.toList());
            return getListOfStringsMatchingLastWord(args, tab);
        }

        return Collections.emptyList();
    }
}
