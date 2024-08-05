package net.launchers.mod.initializer;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.word;

public final class LMCommands {
    private static final String LAUNCHER_ID = "l";
    private static final String P_LAUNCHER_ID = "p";
    private static final String E_LAUNCHER_ID = "e";

    public static void initialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, environment) -> {
            Lcalc.define(dispatcher);
        });
    }

    private static float getHeightFromForce(float force) {
        return (float) (7 * Math.pow(force, 1.6F) - Math.pow(force, 1.25F));
    }

    private final static class Lcalc {
        public static void define(CommandDispatcher<ServerCommandSource> dispatcher) {
            ArrayList<String> launchersList = new ArrayList<>();
            dispatcher.register(CommandManager.literal("lcalc").then(CommandManager.argument("first", word()).suggests(suggestedStrings()).executes(ctx -> {
                launchersList.add(getString(ctx, "first"));
                return checkIdsAndPrintForce(launchersList, ctx);
            }).then(CommandManager.argument("second", word()).suggests(suggestedStrings()).executes(ctx -> {
                launchersList.add(getString(ctx, "first"));
                launchersList.add(getString(ctx, "second"));
                return checkIdsAndPrintForce(launchersList, ctx);
            }).then(CommandManager.argument("third", word()).suggests(suggestedStrings()).executes(ctx -> {
                launchersList.add(getString(ctx, "first"));
                launchersList.add(getString(ctx, "second"));
                launchersList.add(getString(ctx, "third"));
                return checkIdsAndPrintForce(launchersList, ctx);
            }).then(CommandManager.argument("fourth", word()).suggests(suggestedStrings()).executes(ctx -> {
                launchersList.add(getString(ctx, "first"));
                launchersList.add(getString(ctx, "second"));
                launchersList.add(getString(ctx, "third"));
                launchersList.add(getString(ctx, "fourth"));
                return checkIdsAndPrintForce(launchersList, ctx);
            }))))).executes(ctx -> {
                return checkIdsAndPrintForce(launchersList, ctx);
            }));
        }

        private static boolean areLauncherIdValid(ArrayList<String> ids) {
            for (String current : ids) {
                if (!current.equals("l") && !current.equals("p") && !current.equals("e")) {
                    return false;
                }
            }
            return !ids.isEmpty();
        }

        private static float getStackForceByLauncherId(String id) {
            return switch (id) {
                case LAUNCHER_ID -> LMBlock.LAUNCHER_BLOCK.stackMultiplier;
                case P_LAUNCHER_ID -> LMBlock.POWERED_LAUNCHER_BLOCK.stackMultiplier;
                case E_LAUNCHER_ID -> LMBlock.EXTREME_LAUNCHER_BLOCK.stackMultiplier;
                default -> 0F;
            };
        }

        private static float getBaseForceByLauncherId(String id) {
            return switch (id) {
                case LAUNCHER_ID -> LMBlock.LAUNCHER_BLOCK.baseMultiplier;
                case P_LAUNCHER_ID -> LMBlock.POWERED_LAUNCHER_BLOCK.baseMultiplier;
                case E_LAUNCHER_ID -> LMBlock.EXTREME_LAUNCHER_BLOCK.baseMultiplier;
                default -> 0F;
            };
        }

        private static float getForce(ArrayList<String> ids) {
            float base = getBaseForceByLauncherId(ids.get(0));
            float multipier = 1F;
            for (int i = 1; i < ids.size(); i++) {
                multipier += getStackForceByLauncherId(ids.get(i));
            }
            return base * multipier;
        }

        private static int checkIdsAndPrintForce(ArrayList<String> launchersList, CommandContext<ServerCommandSource> context) {
            if (!areLauncherIdValid(launchersList)) {
                context.getSource().sendFeedback(() -> Text.of("\nOne or more parameters are not correct.\n" + "Usage:\n" + "l: Launcher\n" + "p: Powered Launcher\n" + "e: Extreme Launcher\n"), false);
                launchersList.clear();
                return 0;
            }
            StringBuilder printString = new StringBuilder("\nStack: \n");
            for (String s : launchersList) {
                printString.append(s).append("\n");
            }
            float force = getForce(launchersList);
            printString.append("Force: ").append(force);
            printString.append(", height in blocks (approx): ").append(getHeightFromForce(force));
            context.getSource().sendFeedback(() -> Text.of(printString.toString()), false);
            launchersList.clear();
            return 1;
        }

        public static SuggestionProvider<ServerCommandSource> suggestedStrings() {
            ArrayList<String> suggestions = new ArrayList<>();
            suggestions.add(LAUNCHER_ID);
            suggestions.add(P_LAUNCHER_ID);
            suggestions.add(E_LAUNCHER_ID);
            return (ctx, builder) -> getSuggestionsBuilder(builder, suggestions);
        }

        private static CompletableFuture<Suggestions> getSuggestionsBuilder(SuggestionsBuilder builder, List<String> list) {
            String remaining = builder.getRemaining().toLowerCase(Locale.ROOT);

            if (list.isEmpty()) { // If the list is empty then return no suggestions
                return Suggestions.empty(); // No suggestions
            }

            for (String str : list) { // Iterate through the supplied list
                if (str.toLowerCase(Locale.ROOT).startsWith(remaining)) {
                    builder.suggest(str); // Add every single entry to suggestions list.
                }
            }
            return builder.buildFuture(); // Create the CompletableFuture containing all the suggestions
        }
    }
}
