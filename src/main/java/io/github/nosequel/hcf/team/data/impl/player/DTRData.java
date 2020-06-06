package io.github.nosequel.hcf.team.data.impl.player;

import com.google.gson.JsonObject;
import io.github.nosequel.hcf.team.data.impl.SaveableTeamData;
import io.github.nosequel.hcf.util.JsonBuilder;
import io.github.nosequel.hcf.util.NumberUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;

@Getter
@Setter
public class DTRData extends SaveableTeamData {

    private double dtr;
    private double maxDtr;
    private long lastRegen;

    public DTRData() {
    }

    /**
     * Constructor for generating a new DTRData object
     *
     * @param dtr the default dtr
     */
    public DTRData(double dtr) {
        this.dtr = dtr;
        this.maxDtr = dtr;
    }

    /**
     * Constructor for loading a DTRData object from a JsonObject
     * {@link JsonObject}
     *
     * @param object the JsonObject
     */
    public DTRData(JsonObject object) {
        this.dtr = object.get("dtr").getAsDouble();
        this.maxDtr = object.get("maxDtr").getAsDouble();
    }

    /**
     * Set the dtr of the object
     *
     * @param dtr the new dtr
     */
    public void setDtr(double dtr) {
        this.dtr = NumberUtil.round(dtr, 1);
    }

    /**
     * Get the current dtr
     *
     * @return the dtr
     */
    public double getDtr() {
        return NumberUtil.round(dtr, 1);
    }

    /**
     * Format the dtr to a string
     *
     * @return the formatted dtr strin
     */
    public String formatDtr() {

        final String dtrSymbol = this.getDtr() == maxDtr ? "⯈" : "⯅";
        final ChatColor dtrColor = this.getDtr() >= 1.1 ? ChatColor.GREEN : this.getDtr() < 1.1 ? ChatColor.YELLOW : ChatColor.DARK_RED;

        return dtrColor + String.valueOf(this.getDtr()) + dtrSymbol;
    }

    public boolean isRaidable() {
        return this.getDtr() <= 0.0D;
    }

    @Override
    public String getSavePath() {
        return "dtr";
    }

    @Override
    public JsonObject toJson() {
        return new JsonBuilder()
                .addProperty("dtr", dtr)
                .addProperty("maxDtr", maxDtr).get();
    }
}
