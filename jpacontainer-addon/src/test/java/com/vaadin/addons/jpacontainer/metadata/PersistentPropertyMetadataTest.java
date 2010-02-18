/*
 * JPAContainer
 * Copyright (C) 2009 Oy IT Mill Ltd
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
package com.vaadin.addons.jpacontainer.metadata;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.persistence.Version;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test case for {@link PersistentPropertyMetadata}.
 *
 * @author Petter Holmström (IT Mill)
 * @since 1.0
 */
public class PersistentPropertyMetadataTest {

    @Version
    public int dummyField;

    @Version
    public Integer getDummyField() {
        return dummyField;
    }

    public void setDummyField(Integer dummyField) {
        this.dummyField = dummyField;
    }

    @Test
    public void testFieldProperty() throws Exception {
        Field field = getClass().getDeclaredField("dummyField");
        PersistentPropertyMetadata prop = new PersistentPropertyMetadata("dummy", Integer.class, PersistentPropertyMetadata.PropertyKind.SIMPLE, field);
        assertEquals("dummy", prop.getName());
        assertSame(Integer.class, prop.getType());
        assertNull(prop.getTypeMetadata());
        assertEquals(PersistentPropertyMetadata.PropertyKind.SIMPLE, prop.getPropertyKind());
        assertEquals(PersistentPropertyMetadata.AccessType.FIELD, prop.getAccessType());
        assertTrue(prop.isWritable());
        assertSame(field, prop.field);
        assertNull(prop.getter);
        assertNull(prop.setter);
        assertArrayEquals(field.getAnnotations(), prop.getAnnotations());
        assertNotNull(prop.getAnnotation(Version.class));
    }

    @Test
    public void testMethodProperty() throws Exception {
        Method getter = getClass().getDeclaredMethod("getDummyField");
        Method setter = getClass().getDeclaredMethod("setDummyField", Integer.class);
        PersistentPropertyMetadata prop = new PersistentPropertyMetadata("dummy", Integer.class, PersistentPropertyMetadata.PropertyKind.COLLECTION, getter, setter);
        assertEquals("dummy", prop.getName());
        assertSame(Integer.class, prop.getType());
        assertNull(prop.getTypeMetadata());
        assertEquals(PersistentPropertyMetadata.PropertyKind.COLLECTION, prop.getPropertyKind());
        assertEquals(PersistentPropertyMetadata.AccessType.METHOD, prop.getAccessType());
        assertTrue(prop.isWritable());
        assertNull(prop.field);
        assertSame(getter, prop.getter);
        assertSame(setter, prop.setter);
        assertArrayEquals(getter.getAnnotations(), prop.getAnnotations());
        assertNotNull(prop.getAnnotation(Version.class));
    }

    @Test
    public void testFieldPropertyWithMetadata() throws Exception {
        Field field = getClass().getDeclaredField("dummyField");
        ClassMetadata<Integer> cmd = new ClassMetadata<Integer>(Integer.class);
        // It does not matter that Integer is not embeddable nor a reference, it will work anyway
        PersistentPropertyMetadata prop = new PersistentPropertyMetadata("dummy", cmd, PersistentPropertyMetadata.PropertyKind.REFERENCE, field);
        assertEquals("dummy", prop.getName());
        assertSame(Integer.class, prop.getType());
        assertSame(cmd, prop.getTypeMetadata());
        assertEquals(PersistentPropertyMetadata.PropertyKind.REFERENCE, prop.getPropertyKind());
        assertEquals(PersistentPropertyMetadata.AccessType.FIELD, prop.getAccessType());
        assertTrue(prop.isWritable());
        assertSame(field, prop.field);
        assertNull(prop.getter);
        assertNull(prop.setter);
        assertArrayEquals(field.getAnnotations(), prop.getAnnotations());
        assertNotNull(prop.getAnnotation(Version.class));
    }

    @Test
    public void testMethodPropertyWithMetadata() throws Exception {
        Method getter = getClass().getDeclaredMethod("getDummyField");
        Method setter = getClass().getDeclaredMethod("setDummyField", Integer.class);
        ClassMetadata<Integer> cmd = new ClassMetadata<Integer>(Integer.class);
        // It does not matter that Integer is not embeddable nor a reference, it will work anyway
        PersistentPropertyMetadata prop = new PersistentPropertyMetadata("dummy", cmd, PersistentPropertyMetadata.PropertyKind.EMBEDDED, getter, setter);
        assertEquals("dummy", prop.getName());
        assertSame(Integer.class, prop.getType());
        assertSame(cmd, prop.getTypeMetadata());
        assertEquals(PersistentPropertyMetadata.PropertyKind.EMBEDDED, prop.getPropertyKind());
        assertEquals(PersistentPropertyMetadata.AccessType.METHOD, prop.getAccessType());
        assertTrue(prop.isWritable());
        assertNull(prop.field);
        assertSame(getter, prop.getter);
        assertSame(setter, prop.setter);
        assertArrayEquals(getter.getAnnotations(), prop.getAnnotations());
        assertNotNull(prop.getAnnotation(Version.class));
    }

    // TODO Add test for equals() and hashCode() + serialization
}
