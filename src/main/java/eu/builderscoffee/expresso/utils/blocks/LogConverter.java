package eu.builderscoffee.expresso.utils.blocks;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.material.MaterialData;

import java.util.Arrays;

public class LogConverter {

    /***
     * Retourne le LogType via le shortId(Data) d'un block
     * @param shortId
     * @return
     */
    public static LogType getLogTypeByShort(int shortId) {
        return Arrays.stream(LogType.values())
                .findFirst()
                .filter(logType -> logType.getShortIdFacingWestEast() == shortId || logType.getShortIdFacingNorthSouth() == shortId)
                .get();
    }

    /***
     * Retourne la face du log
     * @param shortId
     * @return
     */
    public static LogFacing getLogFacing(int shortId) {
        for (LogType logType : LogType.values()) {
            if (logType.getShortIdFacingNorthSouth() == shortId) {
                return LogFacing.NORTH_SOUTH;
            } else if (logType.getShortIdFacingWestEast() == shortId) {
                return LogFacing.EAST_WEST;
            }
        }
        return null;
    }

    /***
     * On récupère l'ancienne data du block posée & la nouvelle data du block à poser
     * On convertit l'ancienne data dans le nouveau type de block en indiquant la BlockFace
     * du nouveau block et on la retourne
     * @param oldLogType - Le type de blocs posé
     * @param newLogType -  Le type de blocs que l'on veut replacer
     * @return
     */
    public static MaterialData ConvertLogType(LogType oldLogType, LogType newLogType) {
        LogFacing oldlogFacing = getLogFacing(oldLogType.getDataId());
        switch (oldlogFacing) {
            case NORTH_SOUTH:
                return new MaterialData(17, (byte) newLogType.getShortIdFacingNorthSouth());
            case EAST_WEST:
                return new MaterialData(17, (byte) newLogType.getShortIdFacingWestEast());
        }
        return null;
    }

    public enum LogType {
        OAK(0, 4, 8),
        SPRUCE(1, 5, 9),
        BIRCH(2, 6, 10),
        JUNGLE(3, 7, 11);

        @Getter
        public int Id;
        @Getter
        @Setter
        public int DataId;
        @Getter
        public int shortIdFacingWestEast;
        @Getter
        public int shortIdFacingNorthSouth;

        LogType(int id, int shortIdFacingWestEast, int shortIdFacingNorthSouth) {
            this.Id = id;
            this.shortIdFacingWestEast = shortIdFacingWestEast;
            this.shortIdFacingNorthSouth = shortIdFacingNorthSouth;
        }
    }

    public enum LogFacing {
        NORTH_SOUTH,
        EAST_WEST,
    }
}
