package io.github.pigaut.rpg.command;

import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.core.command.*;
import io.github.pigaut.rpg.*;
import io.github.pigaut.rpg.core.command.*;

public class OrestackParameters {

    private static final RpgMakerPlugin plugin = RpgMakerPlugin.getInstance();

    public static final CommandParameter GENERATOR_NAME = CommandParameter.create("generator-name",
            (commandSender, strings) -> plugin.getGeneratorTemplates().getAllNames());

    public static final CommandParameter GENERATOR_GROUP = CommandParameter.create("generator-group",
            (commandSender, strings) -> plugin.getGeneratorTemplates().getAllGroups());

    public static final CommandParameter GATE_NAME = CommandParameter.create("gate-name",
            (commandSender, strings) -> plugin.getGateTemplates().getAllNames());

    public static final CommandParameter GATE_GROUP = CommandParameter.create("gate-group",
            (commandSender, strings) -> plugin.getGateTemplates().getAllGroups());

    public static final CommandParameter COLLECTION_NAME = CommandParameter.create("collection-name",
            (sender, args) -> plugin.getCollectionTemplates().getAllNames());

    public static final CommandParameter SKILL_NAME = CommandParameter.create("skill-name",
            (sender, args) -> plugin.getSkillTemplates().getAllNames());

}
