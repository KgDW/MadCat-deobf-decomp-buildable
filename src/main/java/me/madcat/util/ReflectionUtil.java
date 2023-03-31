package me.madcat.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.Objects;

public class ReflectionUtil {
    public static <F, T extends F> void copyOf(F f, T t, boolean bl) throws NoSuchFieldException, IllegalAccessException {
        Objects.requireNonNull(f);
        Objects.requireNonNull(t);
        Class<?> clazz = f.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            ReflectionUtil.makePublic(field);
            if (ReflectionUtil.isStatic(field)) continue;
            if (bl && ReflectionUtil.isFinal(field)) {
                continue;
            }
            ReflectionUtil.makeMutable(field);
            field.set(t, field.get(f));
        }
    }

    public static boolean isStatic(Member member) {
        return (member.getModifiers() & 8) != 0;
    }

    public static <F, T extends F> void copyOf(F f, T t) throws IllegalAccessException, NoSuchFieldException {
        ReflectionUtil.copyOf(f, t, false);
    }

    public static void makeAccessible(AccessibleObject accessibleObject, boolean bl) {
        Objects.requireNonNull(accessibleObject);
        accessibleObject.setAccessible(bl);
    }

    public static void makeMutable(Member member) throws IllegalAccessException, NoSuchFieldException {
        Objects.requireNonNull(member);
        Field field = Field.class.getDeclaredField("modifiers");
        ReflectionUtil.makePublic(field);
        field.setInt(member, member.getModifiers() & 0xFFFFFFEF);
    }

    public static boolean isFinal(Member member) {
        return (member.getModifiers() & 0x10) != 0;
    }

    public static void makePublic(AccessibleObject accessibleObject) {
        ReflectionUtil.makeAccessible(accessibleObject, true);
    }
}

