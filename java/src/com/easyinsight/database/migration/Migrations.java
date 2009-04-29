package com.easyinsight.database.migration;

/**
 * User: James Boe
 * Date: Apr 27, 2009
 * Time: 11:45:57 AM
 */
public class Migrations {
    public static final Migration[] migrations = new Migration [] { new Migrate60to61() };

    public void migrate() {
        for (Migration migration : migrations) {
            if (migration.needToRun()) {
                migration.migrate();
            }
        }
    }
}
