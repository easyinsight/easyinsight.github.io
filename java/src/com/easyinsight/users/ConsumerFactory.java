package com.easyinsight.users;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.step2.ConsumerHelper;
import org.openid4java.consumer.ConsumerAssociationStore;
import org.openid4java.consumer.InMemoryConsumerAssociationStore;

/**
 * User: jamesboe
 * Date: 10/17/12
 * Time: 4:00 PM
 */
public class ConsumerFactory {
    protected ConsumerHelper helper;

    public ConsumerFactory() {
        this(new InMemoryConsumerAssociationStore());
    }

    public ConsumerFactory(ConsumerAssociationStore store) {
        Injector injector = Guice.createInjector(new GuiceModule(store));
        helper = injector.getInstance(ConsumerHelper.class);
    }

    public ConsumerHelper getConsumerHelper() {
        return helper;
    }
}
