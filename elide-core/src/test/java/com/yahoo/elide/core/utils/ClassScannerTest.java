/*
 * Copyright 2019, Yahoo Inc.
 * Licensed under the Apache License, Version 2.0
 * See LICENSE file in project root for terms.
 */
package com.yahoo.elide.core.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.yahoo.elide.annotation.Include;
import org.junit.jupiter.api.Test;

import jakarta.persistence.Entity;

import java.util.Set;

public class ClassScannerTest {

    private final ClassScanner scanner;

    public ClassScannerTest() {
        scanner = new DefaultClassScanner();
    }

    @Test
    public void testGetAllClasses() {
        Set<Class<?>> classes = scanner.getAllClasses("com.yahoo.elide.core.utils");
        assertEquals(39, classes.size());
        assertTrue(classes.contains(ClassScannerTest.class));
    }

    @Test
    public void testGetAnnotatedClasses() {
        Set<Class<?>> classes = scanner.getAnnotatedClasses("example", Include.class);
        assertEquals(32, classes.size(), "Actual: " + classes);
        classes.forEach(cls -> assertTrue(cls.isAnnotationPresent(Include.class)));
    }

    @Test
    public void testGetAllAnnotatedClasses() {
        Set<Class<?>> classes = scanner.getAnnotatedClasses(Include.class);
        assertEquals(44, classes.size(), "Actual: " + classes);
        classes.forEach(cls -> assertTrue(cls.isAnnotationPresent(Include.class)));
    }

    @Test
    public void testGetAnyAnnotatedClasses() {
        Set<Class<?>> classes = scanner.getAnnotatedClasses(Include.class, Entity.class);
        assertEquals(55, classes.size());
        for (Class<?> cls : classes) {
            assertTrue(cls.isAnnotationPresent(Include.class)
                    || cls.isAnnotationPresent(Entity.class));
        }
    }
}
