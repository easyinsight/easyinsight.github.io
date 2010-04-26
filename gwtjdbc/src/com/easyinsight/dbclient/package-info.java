@TypeDef(
        name="encryptedString",
        typeClass= EncryptedStringType.class,
        parameters= {
            @Parameter(name="encryptorRegisteredName", value="myHibernateStringEncryptor")
        }
    )

package com.easyinsight.dbclient;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.TypeDef;
import org.jasypt.hibernate.type.EncryptedStringType;

