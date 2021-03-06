package com.github.atomicblom.shearmadness.api.events;

import net.minecraft.command.CommandBase;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;

public class RegisterShearMadnessCommandEvent extends Event {

    public RegisterShearMadnessCommandEvent(List<CommandBase> subCommands) {
        this.subCommands = subCommands;
    }

    private List<CommandBase> subCommands;

    public void addCommand(CommandBase childCommand) {
        subCommands.add(childCommand);
    }
}
