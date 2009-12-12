package com.easyinsight.reportpackage;

import com.easyinsight.logging.LogClass;

import java.util.List;

/**
 * User: jamesboe
 * Date: Dec 10, 2009
 * Time: 9:18:34 PM
 */
public class ReportPackageService {
    private ReportPackageStorage reportPackageStorage = new ReportPackageStorage();

    public long saveReportPackage(ReportPackage reportPackage) {
        try {
            return reportPackageStorage.saveReportPackage(reportPackage);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public ReportPackage getReportPackage(long packageID) {
        try {
            return reportPackageStorage.getReportPackage(packageID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public ReportPackageResponse openPackageIfPossible(long packageID) {
        try {
            return reportPackageStorage.openPackageIfPossible(packageID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void deleteReportPackage(long packageID) {
        try {
            reportPackageStorage.deleteReportPackage(packageID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void keepPackage(long packageID) {
        try {
            reportPackageStorage.keepPackage(packageID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<ReportPackageDescriptor> getReportPackagesForUser() {
        try {
            return reportPackageStorage.getReportPackagesForUser();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
