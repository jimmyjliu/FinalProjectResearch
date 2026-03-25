package oop.project.research.scenarios;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParserException;

import java.time.LocalDate;
import java.util.Map;

public final class ArgumentScenarios {

    public static Map<String, Object> parse(String command) throws ArgumentParserException {
        // Recall argparse4j requires a String[] arguments. Space-splitting the
        // input is sufficient but doesn't support quoted arguments.
        var split = command.split(" ", 2);
        var arguments = split.length == 2 ? split[1].split(" ") : new String[] {};
        return switch (split[0]) {
            case "add" -> add(arguments);
            case "sub" -> sub(arguments);
            case "fizzbuzz" -> fizzbuzz(arguments);
            case "difficulty" -> difficulty(arguments);
            case "date" -> date(arguments);
            default -> throw new AssertionError();
        };
    }

    public static Map<String, Object> add(String[] arguments) throws ArgumentParserException {
        var parser = ArgumentParsers.newFor("add").build();
        parser.addArgument("left").type(int.class);
        parser.addArgument("right").type(int.class);
        var namespace = parser.parseArgs(arguments);
        //Note: namespace.getAttrs() returns a Map directly, but a major
        //part of this problem is to ensure we can safely get arguments with
        //the correct static type for use in a real program!
        var left = namespace.getInt("left"); //uses getInt to return int
        int right = namespace.get("right"); //uses type inference - clever but risky!
        return Map.of("left", left, "right", right);
    }

    public static Map<String, Object> sub(String[] arguments) throws ArgumentParserException {
        var parser = ArgumentParsers.newFor("sub").build();
        parser.addArgument("left").type(double.class);
        parser.addArgument("right").type(double.class);
        var namespace = parser.parseArgs(arguments);
        var left = namespace.getDouble("left");
        var right = namespace.getDouble("right");
        return Map.of("left", left, "right", right);
    }

    public static Map<String, Object> fizzbuzz(String[] arguments) throws ArgumentParserException {
        var parse = ArgumentParsers.newFor("fizzbuzz").build();
        parse.addArgument("number").type(int.class).choices(Arguments.range(1,100));
        var namespace = parse.parseArgs(arguments);
        var number = namespace.getInt("number");
        return Map.of("number", number);
    }

    // consider also using an enum for difficulty
    public static Map<String, Object> difficulty(String[] arguments) throws ArgumentParserException {
        var parser = ArgumentParsers.newFor("difficulty").build();
        parser.addArgument("difficulty").type(String.class).choices("easy", "normal", "hard", "peaceful");
        var namespace = parser.parseArgs(arguments);
        var difficulty = namespace.getString("difficulty");
        return Map.of("difficulty", difficulty);
    }

    public static Map<String, Object> date(String[] arguments) throws ArgumentParserException {
        var parser = ArgumentParsers.newFor("date").build();
        parser.addArgument("date").type(String.class);

        var namespace = parser.parseArgs(arguments);
        var dateString = namespace.getString("date");

        LocalDate date;

        try {
            date = LocalDate.parse(dateString);
        } catch (Exception e) {
            throw new ArgumentParserException("Invalid date format. Expected YYYY-MM-DD.", parser);
        }

        return Map.of("date", date);
    }

}
