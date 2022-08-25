package dev.limonblaze.oriacs.data;

import dev.limonblaze.oriacs.common.registry.OriacsItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
public class OriacsRecipeProvider extends RecipeProvider {
    
    public OriacsRecipeProvider(DataGenerator generator) {
        super(generator);
    }
    
    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(OriacsItems.UMBRELLA.get())
            .define('1', Items.STICK).define('m', Items.PHANTOM_MEMBRANE)
            .pattern(" m ")
            .pattern("m1m")
            .pattern(" 1 ")
            .unlockedBy("has_phantom_membrane", has(Items.PHANTOM_MEMBRANE))
            .save(consumer);
        ShapedRecipeBuilder.shaped(OriacsItems.DIVING_HELMET.get())
            .define('g', Items.GLASS).define('c', Items.COPPER_INGOT).define('k', Items.DRIED_KELP)
            .pattern("ccc")
            .pattern("cgc")
            .pattern("kkk")
            .unlockedBy("enter_water", insideOf(Blocks.WATER))
            .save(consumer);
        ShapedRecipeBuilder.shaped(OriacsItems.LANDWALKING_HELMET.get())
            .define('g', Items.GLASS).define('c', Items.COPPER_INGOT).define('k', Items.DRIED_KELP).define('w', Items.WATER_BUCKET)
            .pattern("ccc")
            .pattern("cgc")
            .pattern("kwk")
            .unlockedBy("enter_water", insideOf(Blocks.WATER))
            .save(consumer);
        ShapedRecipeBuilder.shaped(OriacsItems.CHAINMEMBRANE_HELMET.get())
            .define('c', Items.CHAIN).define('m', Items.PHANTOM_MEMBRANE)
            .pattern("ccc")
            .pattern("m m")
            .unlockedBy("has_phantom_membrane", has(Items.PHANTOM_MEMBRANE))
            .save(consumer);
        ShapedRecipeBuilder.shaped(OriacsItems.CHAINMEMBRANE_CHESTPLATE.get())
            .define('c', Items.CHAIN).define('m', Items.PHANTOM_MEMBRANE)
            .pattern("c c")
            .pattern("mcm")
            .pattern("mcm")
            .unlockedBy("has_phantom_membrane", has(Items.PHANTOM_MEMBRANE))
            .save(consumer);
        ShapedRecipeBuilder.shaped(OriacsItems.CHAINMEMBRANE_LEGGINGS.get())
            .define('c', Items.CHAIN).define('m', Items.PHANTOM_MEMBRANE)
            .pattern("cmc")
            .pattern("c c")
            .pattern("m m")
            .unlockedBy("has_phantom_membrane", has(Items.PHANTOM_MEMBRANE))
            .save(consumer);
        ShapedRecipeBuilder.shaped(OriacsItems.CHAINMEMBRANE_BOOTS.get())
            .define('c', Items.CHAIN).define('m', Items.PHANTOM_MEMBRANE)
            .pattern("m m")
            .pattern("c c")
            .unlockedBy("has_phantom_membrane", has(Items.PHANTOM_MEMBRANE))
            .save(consumer);
        ShapedRecipeBuilder.shaped(Items.CHAINMAIL_HELMET)
            .define('c', Items.CHAIN).define('i', Items.IRON_NUGGET)
            .pattern("ccc")
            .pattern("i i")
            .unlockedBy("has_chain", has(Items.CHAIN))
            .save(consumer, "oriacs:chainmail_helmet");
        ShapedRecipeBuilder.shaped(Items.CHAINMAIL_CHESTPLATE)
            .define('c', Items.CHAIN).define('i', Items.IRON_NUGGET)
            .pattern("c c")
            .pattern("ici")
            .pattern("ici")
            .unlockedBy("has_chain", has(Items.CHAIN))
            .save(consumer, "oriacs:chainmail_chestplate");
        ShapedRecipeBuilder.shaped(Items.CHAINMAIL_LEGGINGS)
            .define('c', Items.CHAIN).define('i', Items.IRON_NUGGET)
            .pattern("cic")
            .pattern("c c")
            .pattern("i i")
            .unlockedBy("has_chain", has(Items.CHAIN))
            .save(consumer, "oriacs:chainmail_leggings");
        ShapedRecipeBuilder.shaped(Items.CHAINMAIL_BOOTS)
            .define('c', Items.CHAIN).define('i', Items.IRON_NUGGET)
            .pattern("i i")
            .pattern("c c")
            .unlockedBy("has_chain", has(Items.CHAIN))
            .save(consumer, "oriacs:chainmail_boots");
    }
    
}
