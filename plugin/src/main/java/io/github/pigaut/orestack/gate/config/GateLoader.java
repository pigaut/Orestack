package io.github.pigaut.orestack.gate.config;

import io.github.pigaut.orestack.*;
import io.github.pigaut.orestack.gate.*;
import io.github.pigaut.orestack.gate.template.*;
import io.github.pigaut.voxel.plugin.manager.*;
import io.github.pigaut.yaml.*;
import io.github.pigaut.yaml.configurator.load.*;
import org.bukkit.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class GateLoader implements ConfigLoader<GateTemplate> {

    private final OrestackPlugin plugin;

    public GateLoader(OrestackPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getErrorDescription() {
        return "invalid gate";
    }

    @Override
    public @NotNull GateTemplate loadFromScalar(ConfigScalar scalar) throws InvalidConfigException {
        String gateName = scalar.toString();
        GateTemplate gateTemplate = plugin.getGateTemplate(gateName);
        if (gateTemplate == null) {
            throw new InvalidConfigException(scalar, "Could not find gate with name: '" + gateName + "'");
        }
        return gateTemplate;
    }

    @Override
    public @NotNull GateTemplate loadFromSequence(@NotNull ConfigSequence sequence) throws InvalidConfigException {
        if (!(sequence instanceof ConfigRoot root)) {
            throw new InvalidConfigException(sequence, "Gate can only be loaded from a root configuration sequence");
        }

        String name = root.getName();
        String group = Group.byFile(root.getFile(), "gates", true);

        List<GatePhase> gatePhases = sequence.getAllRequired(GatePhase.class);
        GatePhase lastPhase = gatePhases.get(gatePhases.size() - 1);
        boolean multiBlock = false;
        Double maxHealth = null;
        Material gateItem = lastPhase.getStructureTemplate().getMostCommonMaterial();

        for (GatePhase phase : gatePhases) {
            if (phase.getStructureTemplate().hasMultipleBlocks()) {
                multiBlock = true;
            }
            Double phaseHealth = phase.getMaxHealth();
            if (phaseHealth != null) {
                maxHealth = maxHealth != null ? maxHealth + phaseHealth : phaseHealth;
            }
        }

        return new GateTemplate(name, group, gatePhases, multiBlock, maxHealth, gateItem);
    }

}