package oop.project.research.scenarios;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParserException;

import java.util.Map;

public final class CommandScenarios {

    public static Map<String, Object> mul(String[] arguments) throws ArgumentParserException {
        var parser = ArgumentParsers.newFor("mul").build();
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

    public static Map<String, Object> div(String[] arguments) throws ArgumentParserException {
        var parser = ArgumentParsers.newFor("div").build();
        parser.addArgument("--left").required(true).type(Double.class);
        parser.addArgument("--right").required(true).type(Double.class);

        var namespace = parser.parseArgs(arguments);
        var left = namespace.getDouble("left");
        var right = namespace.getDouble("right");
        return Map.of("left", left, "right", right);
    }

    public static Map<String, Object> echo(String[] arguments) throws ArgumentParserException {
        var parser = ArgumentParsers.newFor("echo").build();
        parser.addArgument("message")
                .nargs("?")
                .type(String.class)
                .setDefault("echo,echo,echo...");
        var namespace = parser.parseArgs(arguments);
        var message = namespace.getString("message");
        return Map.of("message", message);
    }

    public static Map<String, Object> search(String[] arguments) throws ArgumentParserException {
        var parser = ArgumentParsers.newFor("search").build();
        parser.addArgument("term").type(String.class);
        parser.addArgument("--case-insensitive", "-i")
                .type(Arguments.booleanType())
                .nargs("?") // checks for number of arguments
                .setConst(true)
                .setDefault(false);

        var namespace = parser.parseArgs(arguments);
        var term = namespace.getString("term");
        boolean caseInsensitive = namespace.getBoolean("case_insensitive");
        return Map.of("term", term, "case-insensitive", caseInsensitive);
    }

    public static Map<String, Object> dispatch(String[] arguments) throws ArgumentParserException {
        var parser = ArgumentParsers.newFor("search").build();
        parser.addArgument("type").type(String.class).choices("static", "dynamic").required(true);
        parser.addArgument("value").required(true);

        var namespace = parser.parseArgs(arguments);
        var type = namespace.getString("type");
        var value = namespace.getString("value");

        if (type.equals("static")) {
            int convert;
            try {
                convert = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new ArgumentParserException("Invalid value given. Expected integer.", parser);
            }
            return Map.of("type", type, "value", convert);
        }
        return Map.of("type", type, "value", value);
    }

}
