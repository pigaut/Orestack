package io.github.pigaut.rpg.module.mob.model;

import com.ticxo.modelengine.api.*;
import com.ticxo.modelengine.api.model.*;
import org.bukkit.entity.*;
import org.jetbrains.annotations.*;

public class MobModelTemplate {

    public static final MobModelTemplate EMPTY = new MobModelTemplate(null);

    private final String modelId;

    public MobModelTemplate(@Nullable String modelId) {
        this.modelId = modelId;
    }

    public void apply(@NotNull LivingEntity entity) {
        if (modelId == null) {
            return;
        }

        ModeledEntity modeledEntity = ModelEngineAPI.createModeledEntity(entity);
        if (modeledEntity != null) {
            ActiveModel activeModel = ModelEngineAPI.createActiveModel(modelId);
            if (activeModel != null) {
                modeledEntity.addModel(activeModel, true);
                modeledEntity.setBaseEntityVisible(false);
            }
        }
    }

}
