package com.fiskmods.heroes.common.interaction.key;

import com.fiskmods.heroes.FiskHeroes;
import com.fiskmods.heroes.common.hero.Hero;
import com.fiskmods.heroes.common.hero.modifier.Ability;
import com.fiskmods.heroes.common.hero.modifier.AbilityShapeShifting;
import com.fiskmods.heroes.common.interaction.InteractionType;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;

public class KeyPressShapeShift extends KeyPressBase
{
    public KeyPressShapeShift()
    {
        requireModifier(Ability.SHAPE_SHIFTING);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public KeyBinding getKey(EntityPlayer player, Hero hero)
    {
        return hero.getKey(player, AbilityShapeShifting.KEY_SHAPE_SHIFT);
    }

    @Override
    public void receive(EntityPlayer sender, EntityPlayer clientPlayer, InteractionType type, Side side, int x, int y, int z)
    {
        if (side.isClient())
        {
            sender.openGui(FiskHeroes.MODID, 1, sender.worldObj, x, y, z);
        }
    }
}
