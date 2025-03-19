package com.cerbon.randomizercommand;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RandomizerCommand implements ModInitializer {
    private static final Random random = new Random();

    // Pattern for @range[min, max]
    private static final Pattern RANGE_PATTERN = Pattern.compile("@range\\[(\\d+)\\s*,\\s*(\\d+)\\]");

    // Pattern for @random[option1, option2, ...]
    private static final Pattern RANDOM_PATTERN = Pattern.compile("@random\\[(.*?)\\]");

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(Commands.literal("randomizer")
                        .then(Commands.argument("command", StringArgumentType.greedyString())
                                .executes(context -> {
                                    String command = StringArgumentType.getString(context, "command");
                                    String processedCommand = processRandomizations(command);

                                    return dispatcher.execute(processedCommand, context.getSource());
                                })
                        )
                )
        );
    }

    /**
     * Process a command string and replace all randomization patterns with random values
     * @param command The command string to process
     * @return The processed string with random values
     */
    public static String processRandomizations(String command) {
        String result = command;

        // Process all @range[min, max] patterns
        Matcher rangeMatcher = RANGE_PATTERN.matcher(result);
        StringBuilder rangeBuilder = new StringBuilder();

        while (rangeMatcher.find()) {
            int min = Integer.parseInt(rangeMatcher.group(1));
            int max = Integer.parseInt(rangeMatcher.group(2));

            if (min > max) {
                // Swap if min is greater than max
                int temp = min;
                min = max;
                max = temp;
            }

            // Generate a random number between min and max (inclusive)
            int randomValue = random.nextInt(max - min + 1) + min;
            rangeMatcher.appendReplacement(rangeBuilder, String.valueOf(randomValue));
        }
        rangeMatcher.appendTail(rangeBuilder);
        result = rangeBuilder.toString();

        // Process all @random[option1, option2, ...] patterns
        Matcher randomMatcher = RANDOM_PATTERN.matcher(result);
        StringBuilder randomBuilder = new StringBuilder();

        while (randomMatcher.find()) {
            String optionsStr = randomMatcher.group(1);
            String[] options = parseOptions(optionsStr);

            if (options.length > 0) {
                // Choose a random option
                String randomOption = options[random.nextInt(options.length)];
                // Replace any special regex characters in the replacement string
                randomOption = Matcher.quoteReplacement(randomOption);
                randomMatcher.appendReplacement(randomBuilder, randomOption);
            }
        }
        randomMatcher.appendTail(randomBuilder);
        result = randomBuilder.toString();

        return result;
    }

    /**
     * Parse a comma-separated list of options, handling possible commas inside quotes
     * @param optionsStr The string containing comma-separated options
     * @return An array of parsed options
     */
    private static String[] parseOptions(String optionsStr) {
        // Simple split for basic cases
        String[] options = optionsStr.split("\\s*,\\s*");

        // Trim each option
        for (int i = 0; i < options.length; i++) {
            options[i] = options[i].trim();
        }

        return options;
    }
}
