package com.iamkaf.dynamicedge.api.event;

import com.iamkaf.dynamicedge.augment.Augment;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

// Note to future self.
// When you're ready to trigger this event, use:
// `AugmentEvent.AUGMENT_PROGRESS.invoker().progress(stack, player, augment);`

public interface AugmentEvent {
    Event<Progress> AUGMENT_PROGRESS = EventFactory.createLoop();

    // TODO: add these events: AUGMENT_INTRODUCED, AUGMENT_ACTIVATED

    interface Progress {
        /**
         * Invoked when a piece of gear gains augment progress.
         *
         * @param gear    The piece of gear.
         * @param player  The player carrying the item.
         * @param augment The augment that gained progress.
         * @param progressAmount The amount of progress acquired.
         */
        void progress(ItemStack gear, Player player, Augment augment, Integer progressAmount);
    }
}
