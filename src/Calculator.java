import java.util.Map;
import java.util.Scanner;

public class Calculator {
    Scanner scanner = new Scanner(System.in);
    String shapes = "";
    String distributorShapes = "";
    String rejoinerShapes = "";
    String VALID_SHAPES = "tsc";
    String VALID_DOUBLED_SHAPES = "tsca";
    String VALID_POSITIONS = "lmr";
    boolean isDoubled = false;
    char shapeDoubled;
    char rejoinerPosition;
    char distributorPosition;
    char dissectorPosition;

    Map<Character, String> formattedShapeDictionary = Map.of(
            'c', "circle",
            's', "square",
            't', "triangle"
    );

    Map<Character, Integer> simplifiedPositionDictionary = Map.of(
            'l', 0,
            'm', 1,
            'r', 2
    );

    Map<Character, String> formattedPositionDictionary = Map.of(
            'l', "left",
            'm', "middle",
            'r', "right"
    );

    void prompt() {
        do {
            System.out.println("Enter shape callouts (e.g tsc):");
            shapes = scanner.nextLine().toLowerCase();

            if (shapes.length() != 3)
                System.out.println("You specified the wrong amount of shapes.");

            if (!containsOnlyValidChars(shapes, VALID_SHAPES))
                System.out.println("You specified invalid shapes.");

        } while (shapes.length() != 3 || !containsOnlyValidChars(shapes, VALID_SHAPES));

        String rejoinerPositionAsString = "";

        do {
            System.out.println("Enter rejoiner position (e.g l, m, r):");
            rejoinerPosition = scanner.next().toLowerCase().charAt(0);
            rejoinerPositionAsString = String.valueOf(rejoinerPosition);

            if (!containsOnlyValidChars(rejoinerPositionAsString, VALID_POSITIONS))
                System.out.println("You didn't specify a valid position");

        } while (!containsOnlyValidChars(rejoinerPositionAsString, VALID_POSITIONS));

        String distributorPositionAsString = "";

        do {
            System.out.println("Enter distributor position (e.g l, m, r):");
            distributorPosition = scanner.next().toLowerCase().charAt(0);
            distributorPositionAsString = String.valueOf(distributorPosition);

            if (!containsOnlyValidChars(distributorPositionAsString, VALID_POSITIONS))
                System.out.println("You didn't specify a valid position");

            if (distributorPosition == rejoinerPosition)
                System.out.println("The distributor cannot be on the same position as the rejoiner");
        } while (!containsOnlyValidChars(distributorPositionAsString, VALID_POSITIONS) || (distributorPosition == rejoinerPosition));

        for (char pos : VALID_POSITIONS.toCharArray()) {
            if (pos != distributorPosition && pos != rejoinerPosition) {
                dissectorPosition = pos;
                break;
            }
        }
        boolean answerValidated;

        do {
            System.out.println("Are there any shapes doubled? (y for yes, n for no)");
            char answer = scanner.next().toLowerCase().charAt(0);
            answerValidated = true;

            if (answer == 'y')
                isDoubled = true;
            else if (answer == 'n')
                isDoubled = false;
            else {
                System.out.println("Your answer is invalid");
                answerValidated = false;
            }

        } while (!answerValidated);

        if (!isDoubled) {

            scanner.nextLine();

            do {
                System.out.println("Enter rejoiner shapes (e.g ts):");
                rejoinerShapes = scanner.nextLine().toLowerCase();

                if (rejoinerShapes.length() != 2)
                    System.out.println("You specified the wrong amount of shapes.");

                if (!containsOnlyValidChars(rejoinerShapes, VALID_SHAPES))
                    System.out.println("You specified invalid shapes.");

            } while (rejoinerShapes.length() != 2 || !containsOnlyValidChars(rejoinerShapes, VALID_SHAPES));

            do {
                System.out.println("Enter distributor shapes (e.g ts):");
                distributorShapes = scanner.nextLine().toLowerCase();

                if (distributorShapes.length() != 2)
                    System.out.println("You specified the wrong amount of shapes.");

                if (!containsOnlyValidChars(distributorShapes, VALID_SHAPES))
                    System.out.println("You specified invalid shapes.");

            } while (distributorShapes.length() != 2 || !containsOnlyValidChars(distributorShapes, VALID_SHAPES));

            return;
        }

        String shapeDoubledAsString = "";

        do {
            System.out.println("Which shapes are doubled? (A for all)");
            shapeDoubled = scanner.next().toLowerCase().charAt(0);
            shapeDoubledAsString = String.valueOf(shapeDoubled);

            if (!containsOnlyValidChars(shapeDoubledAsString, VALID_DOUBLED_SHAPES))
                System.out.println("You didn't specify a valid shape");
        } while (!containsOnlyValidChars(shapeDoubledAsString, VALID_DOUBLED_SHAPES));

    }

    void calculate() {
        if (isDoubled && shapeDoubled == 'a')
            handleAllDoubles();
        else if (isDoubled)
            handleOneDouble();
        else
            handleAllSingles();
    }

    void handleAllSingles() {
        StringBuilder sb = new StringBuilder();

        // check whose key the rejoiner sent
        for (char c : VALID_SHAPES.toCharArray()) {
            if (rejoinerShapes.indexOf(c) == -1)
                sb.append(c);
        }

        boolean distributorHasTheirKey = sb.charAt(0) == shapes.charAt(simplifiedPositionDictionary.get(distributorPosition));

        char sendToPosition = distributorHasTheirKey ? rejoinerPosition : distributorPosition;

        if (!distributorHasTheirKey)
            System.out.println("Distributor sends both shapes to " + formattedPositionDictionary.get(dissectorPosition));

        System.out.println("Dissector sends both shapes to " + formattedPositionDictionary.get(sendToPosition));

    }

    void handleAllDoubles() {
        System.out.println("Distributor sends a " + formattedShapeDictionary.get(shapes.charAt(simplifiedPositionDictionary.get(distributorPosition))) + " to " + formattedPositionDictionary.get(dissectorPosition));
        System.out.println("Dissector sends both shapes to " + formattedPositionDictionary.get(rejoinerPosition) + " and " + formattedPositionDictionary.get(distributorPosition));
    }

    void handleOneDouble() {
        char doubledPosition = VALID_POSITIONS.charAt(shapes.indexOf(shapeDoubled));

        System.out.println("Distributor sends a " + formattedShapeDictionary.get(shapes.charAt(simplifiedPositionDictionary.get(rejoinerPosition))) + " to " + formattedPositionDictionary.get(dissectorPosition));

        // if the distributor is doubled
        if (distributorPosition == doubledPosition) {
            System.out.println("Dissector sends a " + formattedShapeDictionary.get(shapes.charAt(simplifiedPositionDictionary.get(rejoinerPosition))) + " to " + formattedPositionDictionary.get(distributorPosition) +
                    " and a " + formattedShapeDictionary.get(shapes.charAt(simplifiedPositionDictionary.get(dissectorPosition))) + " to " + formattedPositionDictionary.get(rejoinerPosition));
        }
        // if the dissector is doubled
        else if (dissectorPosition == doubledPosition) {
            System.out.println("Dissector sends both shapes to " + formattedPositionDictionary.get(rejoinerPosition) + " and " + formattedPositionDictionary.get(distributorPosition));
        }
        // if the rejoiner is doubled
        else {
            System.out.println("Dissector sends a " + formattedShapeDictionary.get(shapes.charAt(simplifiedPositionDictionary.get(distributorPosition))) + " to " + formattedPositionDictionary.get(rejoinerPosition) +
                    " and a " + formattedShapeDictionary.get(shapes.charAt(simplifiedPositionDictionary.get(dissectorPosition))) + " to " + formattedPositionDictionary.get(distributorPosition));
        }
    }

    boolean containsOnlyValidChars(String input, String valid) {
        for (char c : input.toCharArray()) {
            if (valid.indexOf(c) == -1) {
                return false;
            }
        }
        return true;

    }
}