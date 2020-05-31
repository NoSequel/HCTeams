package io.github.nosequel.hcf.player.data;

import com.google.gson.JsonObject;
import io.github.nosequel.hcf.data.impl.SaveableData;
import io.github.nosequel.hcf.util.JsonBuilder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpawnProtectionData implements SaveableData {

    private long durationLeft;

    public SpawnProtectionData() {
        this.durationLeft = 0;
    }

    public SpawnProtectionData(long duration) {
        this.durationLeft = duration;
    }

    public SpawnProtectionData(JsonObject object) {
        this.durationLeft = object.get("duration").getAsLong();
    }

    @Override
    public String getSavePath() {
        return "spawn_protection";
    }

    @Override
    public JsonObject toJson() {
        return new JsonBuilder()
                .addProperty("duration", durationLeft)
                .get();
    }
}
