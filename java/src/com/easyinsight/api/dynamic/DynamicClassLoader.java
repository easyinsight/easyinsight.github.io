package com.easyinsight.api.dynamic;

import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.util.Map;
import java.util.HashMap;

/**
 * User: James Boe
 * Date: Aug 28, 2008
 * Time: 2:36:58 PM
 */
public class DynamicClassLoader extends ClassLoader {
    private Map<String, byte[]> byteMap = new HashMap<String, byte[]>();

    public DynamicClassLoader(byte[] interfaceFile, byte[] classFile, byte[] beanFile, String beanClassName, ClassLoader parent) {
        super(parent);
        byteMap.put("IDynamicService", interfaceFile);
        byteMap.put("DynamicService", classFile);
        byteMap.put(beanClassName, beanFile);
    }

    protected Class loadClass(String name, boolean resolve)
            throws ClassNotFoundException {

        // Since all support classes of loaded class use same class loader
        // must check subclass cache of classes for things like Object

        Class c = findLoadedClass(name);
        if (c == null) {
            try {
                c = findSystemClass(name);
            } catch (Exception e) {
                // ignore...
            }
        }

        if (c == null) {

            byte data[] = byteMap.get(name);
            if (data == null) {
                c = super.loadClass(name, resolve);
            } else {
                c = defineClass(name, data, 0, data.length);
            }
            if (c == null)
                throw new ClassNotFoundException(name);
        }
        if (resolve)
            resolveClass(c);
        return c;
    }
}
