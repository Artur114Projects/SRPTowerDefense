package com.artur114.srptowerdefense.common.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

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

    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return null;//args.length == 1 ? getListOfStringsMatchingLastWord(args, ) : Collections.emptyList();
    }
}
