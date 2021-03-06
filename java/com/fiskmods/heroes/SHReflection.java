package com.fiskmods.heroes;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class SHReflection
{
    // Client
    private static GenericMethod<Object, Void> renderNametagMethod;
    private static GenericMethod<Object, Void> clearResourcesMethod;

    public static GenericField<Object, String> splashTextField;
    public static GenericField<Object, Map> mapTextureObjectsField;
    public static GenericField<Object, Float> thirdPersonDistanceField;
    public static GenericField<Object, List> defaultResourcePacksField;

    // Common
    private static GenericMethod<Entity, Void> setSizeMethod;
    private static GenericMethod<EntityLivingBase, Integer> getArmSwingAnimationEndMethod;
    private static GenericMethod<Entity, Boolean> canTriggerWalkingMethod;
    private static GenericMethod<EntityCreeper, Void> creeperExplodeMethod;

    public static GenericField<Entity, Integer> nextStepDistanceField;
    public static GenericField<NBTTagList, List> tagListField;
    public static GenericField<WorldGenMinable, Block> genMineableOreField;
    public static GenericField<WorldGenMinable, Integer> genMineableMetaField;
    public static GenericField<WorldGenMinable, Integer> genMineableNumField;
    public static GenericField<WorldGenMinable, Block> genMineableStoneField;

    @SideOnly(Side.CLIENT)
    public static void client()
    {
        renderNametagMethod = MethodBuilder.in(RenderPlayer.class, void.class).with(EntityLivingBase.class, double.class, double.class, double.class, String.class, float.class, double.class).find("func_96449_a");
        clearResourcesMethod = MethodBuilder.in(SimpleReloadableResourceManager.class, void.class).with().find("func_110543_a", "clearResources");

        splashTextField = new GenericField(GuiMainMenu.class, String.class, "field_73975_c", "splashText");
        mapTextureObjectsField = new GenericField(TextureManager.class, Map.class, "field_110585_a", "mapTextureObjects");
        thirdPersonDistanceField = new GenericField(EntityRenderer.class, float.class, "field_78490_B", "thirdPersonDistance");
        defaultResourcePacksField = new GenericField(Minecraft.class, List.class, "field_110449_ao", "defaultResourcePacks");
    }

    public static void common()
    {
        setSizeMethod = MethodBuilder.in(Entity.class, void.class).with(float.class, float.class).find("func_70105_a", "setSize");
        getArmSwingAnimationEndMethod = MethodBuilder.in(EntityLivingBase.class, int.class).with().find("func_82166_i", "getArmSwingAnimationEnd");
        canTriggerWalkingMethod = MethodBuilder.in(Entity.class, boolean.class).with().find("func_70041_e_", "canTriggerWalking");
        creeperExplodeMethod = MethodBuilder.in(EntityCreeper.class, void.class).with().find("func_146077_cc");

        nextStepDistanceField = new GenericField(Entity.class, int.class, "field_70150_b", "nextStepDistance");
        tagListField = new GenericField(NBTTagList.class, List.class, "field_74747_a", "tagList");
        genMineableOreField = new GenericField(WorldGenMinable.class, Block.class, "field_150519_a");
        genMineableMetaField = new GenericField(WorldGenMinable.class, int.class, "mineableBlockMeta");
        genMineableNumField = new GenericField(WorldGenMinable.class, int.class, "field_76541_b", "numberOfBlocks");
        genMineableStoneField = new GenericField(WorldGenMinable.class, Block.class, "field_150518_c");
    }

    public static void renderNametag(RenderPlayer instance, EntityLivingBase entity, double x, double y, double z, String username, float p_96449_9_, double p_96449_10_)
    {
        renderNametagMethod.invoke(instance, entity, x, y, z, username, p_96449_9_, p_96449_10_);
    }

    public static void clearResources(SimpleReloadableResourceManager instance)
    {
        clearResourcesMethod.invoke(instance);
    }

    public static void setSize(Entity instance, float f, float f1)
    {
        setSizeMethod.invoke(instance, f, f1);
    }

    public static int getArmSwingAnimationEnd(EntityLivingBase instance)
    {
        return getArmSwingAnimationEndMethod.invoke(instance);
    }

    public static boolean canTriggerWalking(Entity instance)
    {
        return canTriggerWalkingMethod.invoke(instance);
    }

    public static void creeperExplode(EntityCreeper instance)
    {
        creeperExplodeMethod.invoke(instance);
    }

    public static class GenericField<C, T>
    {
        private final Field theField;

        public GenericField(Class<C> parent, Class<T> type, String... names)
        {
            for (String name : names)
            {
                for (Field field : parent.getDeclaredFields())
                {
                    if (field.getName().equals(name) && field.getType() == type)
                    {
                        field.setAccessible(true);
                        theField = field;
                        return;
                    }
                }
            }

            throw new RuntimeException(String.format("Unable to locate field of type %s in %s: %s", type.getName(), parent.getName(), Arrays.asList(names)));
        }

        public T get(C instance)
        {
            try
            {
                return (T) theField.get(instance);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }

        public T getStatic()
        {
            return get(null);
        }

        public void set(C instance, T value)
        {
            try
            {
                theField.set(instance, value);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }

        public void setStatic(T value)
        {
            set(null, value);
        }
    }

    public static class MethodBuilder<C, T>
    {
        private final Class<C> parentClass;
        private final Class<T> returnType;

        private Class[] parameters;

        private MethodBuilder(Class<C> parent, Class<T> type)
        {
            parentClass = parent;
            returnType = type;
        }

        public static <C, T> MethodBuilder in(Class<C> parent, Class<T> type)
        {
            return new MethodBuilder(parent, type);
        }

        public MethodBuilder with(Class... params)
        {
            parameters = params;
            return this;
        }

        public GenericMethod find(String... names)
        {
            List<String> list = Arrays.asList(names);
            String print = getParameterPrint(parameters);

            for (Method method : parentClass.getDeclaredMethods())
            {
                if (method.getReturnType() == returnType && list.contains(method.getName()) && print.equals(getParameterPrint(method.getParameterTypes())))
                {
                    method.setAccessible(true);
                    return new GenericMethod(method);
                }
            }

            throw new RuntimeException(String.format("Unable to locate method of type %s in %s: %s", returnType.getName(), parentClass.getName(), list));
        }

        public static String getParameterPrint(Class[] params)
        {
            if (params != null)
            {
                StringBuilder sb = new StringBuilder();

                for (Class c : params)
                {
                    sb.append(c.getCanonicalName()).append(";");
                }

                return sb.toString();
            }

            return "";
        }
    }

    public static class GenericMethod<C, T>
    {
        private final Method theMethod;

        public GenericMethod(Method method)
        {
            theMethod = method;
        }

        public T invoke(C instance, Object... args)
        {
            try
            {
                return (T) theMethod.invoke(instance, args);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }

        public T invokeStatic(Object... args)
        {
            return invoke(null, args);
        }
    }
}
