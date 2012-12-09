package com.easyinsight.pipeline;

import java.util.List;

/**
 * User: jamesboe
 * Date: 12/5/12
 * Time: 1:08 PM
 */
public interface INestedComponent extends IComponent {
    public void add(IComponent component);
    public void addAll(List<IComponent> components);
}
