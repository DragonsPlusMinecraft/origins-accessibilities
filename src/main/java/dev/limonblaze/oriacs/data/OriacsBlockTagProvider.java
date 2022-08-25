package dev.limonblaze.oriacs.data;

import dev.limonblaze.oriacs.common.Oriacs;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class OriacsBlockTagProvider extends BlockTagsProvider {
    
    public OriacsBlockTagProvider(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, Oriacs.ID, existingFileHelper);
    }
    
    @Override
    protected void addTags() {
    
    }
    
}
