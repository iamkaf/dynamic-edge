package com.iamkaf.dynamicedge.neoforge;

import com.iamkaf.dynamicedge.DynamicEdge;
import net.neoforged.fml.common.Mod;

@Mod(DynamicEdge.MOD_ID)
public final class DynamicEdgeNeoForge {
    public DynamicEdgeNeoForge() {
        // Run our common setup.
        DynamicEdge.init();
    }
}
