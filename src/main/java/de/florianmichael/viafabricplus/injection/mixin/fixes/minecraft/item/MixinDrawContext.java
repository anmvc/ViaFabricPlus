/*
 * This file is part of ViaFabricPlus - https://github.com/FlorianMichael/ViaFabricPlus
 * Copyright (C) 2021-2024 FlorianMichael/EnZaXD <florian.michael07@gmail.com> and RK_01/RaphiMC
 * Copyright (C) 2023-2024 contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.florianmichael.viafabricplus.injection.mixin.fixes.minecraft.item;

import com.viaversion.viaversion.protocols.v1_10to1_11.Protocol1_10To1_11;
import de.florianmichael.viafabricplus.util.ItemUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DrawContext.class)
public abstract class MixinDrawContext {

    @Unique
    private static final String viaFabricPlus$vvIdentifier = "VV|" + Protocol1_10To1_11.class.getSimpleName(); // ItemRewriter#nbtTagName

    @Redirect(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getCount()I"))
    private int handleNegativeItemCount(ItemStack instance) {
        final NbtCompound tag = ItemUtil.getTagOrNull(instance);
        if (tag != null && tag.contains(viaFabricPlus$vvIdentifier)) {
            return tag.getInt(viaFabricPlus$vvIdentifier);
        } else {
            return instance.getCount();
        }
    }

    @Redirect(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Ljava/lang/String;valueOf(I)Ljava/lang/String;", remap = false))
    private String makeTextRed(int count) {
        if (count <= 0) {
            return Formatting.RED.toString() + count;
        } else {
            return String.valueOf(count);
        }
    }

}
