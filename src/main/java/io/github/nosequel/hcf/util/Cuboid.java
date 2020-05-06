package io.github.nosequel.hcf.util;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Map;

@Data
public class Cuboid {

    private String worldName;
    private int maxX, maxY, maxZ, minX, minY, minZ;

    /**
     * the main constructor for a Cuboid
     *
     * @param location1 the first location of the cuboid
     * @param location2 the second location of the cuboid
     */
    public Cuboid(Location location1, Location location2) {
        if (location1.getWorld() != location2.getWorld()) {
            throw new IllegalArgumentException("world of location1 does not equal location2's world");
        }

        this.maxX = Math.max(location1.getBlockX(), location2.getBlockX());
        this.maxY = Math.max(location1.getBlockX(), location2.getBlockX());
        this.maxZ = Math.max(location1.getBlockX(), location2.getBlockX());

        this.minX = Math.min(location1.getBlockX(), location2.getBlockX());
        this.minY = Math.min(location1.getBlockX(), location2.getBlockX());
        this.minZ = Math.min(location1.getBlockX(), location2.getBlockX());

        this.worldName = location1.getWorld().getName();
    }

    /**
     * Load a cuboid from a Map<String,Object>
     *
     * @param map the map
     */
    public Cuboid(Map<String, Object> map) {
        this(
                new Location(Bukkit.getWorld((String) map.get("worldName")), (int) map.get("x1"), (int) map.get("y1"), (int) map.get("z1")),
                new Location(Bukkit.getWorld((String) map.get("worldName")), (int) map.get("x2"), (int) map.get("y2"), (int) map.get("z2"))
        );
    }

    /**
     * checks if location is in cuboid
     *
     * @param location the location to be checked
     * @return whether the location is in the cuboid
     */
    public boolean isLocationInCuboid(Location location) {
        return (minX <= location.getBlockX() && minY <= location.getBlockY() && minZ <= location.getBlockZ()) && (maxX >= location.getBlockX() && maxY >= location.getBlockY() && maxZ >= location.getBlockZ());
    }
}