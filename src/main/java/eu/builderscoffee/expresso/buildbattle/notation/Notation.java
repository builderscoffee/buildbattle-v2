package eu.builderscoffee.expresso.buildbattle.notation;

import lombok.Data;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
public class Notation {
    private UUID UUID;
    private Map<NotationType, Integer> notes = new HashMap<>();

    public Notation(UUID uuid) {
        this.UUID = uuid;
        Arrays.stream(NotationType.values()).forEach(notationType -> notes.put(notationType, 0));
    }

    public enum NotationType {

        Beauty(30, 1, 5),
        Creative(22, 1, 5),
        Amenagement(22, 1, 5),
        Folklore(22, 1, 5),
        Fun(4, 1, 5);

        @Getter
        private final int maxValue; // Valeur maximum d'une note
        @Getter
        private final int normalClickValue; // Valeur d'un clicks
        @Getter
        private final int shiftClickValue; // Valeur d'un shifts clicks

        NotationType(int maxValue, int normalClickValue, int shiftClickValue) {
            this.maxValue = maxValue;
            this.normalClickValue = normalClickValue;
            this.shiftClickValue = shiftClickValue;
        }
    }

}
