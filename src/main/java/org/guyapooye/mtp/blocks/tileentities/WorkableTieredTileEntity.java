package org.guyapooye.mtp.blocks.tileentities;

import org.guyapooye.mtp.items.MTBlockItem;
import org.guyapooye.mtp.recipes.RecipeMap;

import java.util.function.Function;

public class WorkableTieredTileEntity extends TieredTileEntity{
    //protected final RecipeLogicEnergy workable;
    protected final RecipeMap<?> recipeMap;
    //private final Function<Integer, Integer> tankScalingFunction;

    public final boolean handlesRecipeOutputs;


    public WorkableTieredTileEntity(MTBlockItem base, RecipeMap<?> recipeMap, int tier
    //                                    Function<Integer, Integer> tankScalingFunction
    ) {
        this(base, recipeMap, tier//, tankScalingFunction
                ,true);
    }

    public WorkableTieredTileEntity(MTBlockItem base, RecipeMap<?> recipeMap, int tier,
                                    //                                    Function<Integer, Integer> tankScalingFunction,
                                    boolean handlesRecipeOutputs) {
        super(base, tier);
        this.handlesRecipeOutputs = handlesRecipeOutputs;
        //this.workable = createWorkable(recipeMap);
        this.recipeMap = recipeMap;
        //this.tankScalingFunction = tankScalingFunction;
        //reinitializeEnergyContainer();
    }
}
