package com.easyinsight.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 1/19/11
 * Time: 4:52 PM
 */
public class RolePrioritySet<K extends EIDescriptor> {

    private Map<K, K> map = new HashMap<K, K>();

    public List<K> values() {
        return new ArrayList<K>(map.values());
    }

    public void add(K newDescriptor) {
        EIDescriptor existing = map.get(newDescriptor);
        if (existing == null || existing.getRole() > newDescriptor.getRole()) {
            map.put(newDescriptor, newDescriptor);
        }
    }
}
