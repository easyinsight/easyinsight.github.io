package com.easyinsight.icons;

import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.Account;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * User: James Boe
 * Date: Jan 8, 2009
 * Time: 9:33:37 PM
 */
public class IconService {
    
    public List<Icon> getIcons() {
        SecurityUtil.authorizeAccountTier(Account.BASIC);
        File iconFolder = new File("../webapps/app/assets/icons/32x32");
        File[] iconFiles = iconFolder.listFiles();
        List<Icon> icons = new ArrayList<Icon>(); 
        for (File iconFile : iconFiles) {
            String fileName = iconFile.getName();
            if (fileName.endsWith("png")) {
                //String path = "/DMS/assets/icons/32x32/" + fileName;
                //String path = fileName;
                Icon icon = new Icon();
                icon.setName(fileName.substring(0, fileName.length() - 4));
                icon.setPath(fileName);
                icons.add(icon);
            }
        }
        Collections.sort(icons, new Comparator<Icon>() {

            public int compare(Icon o1, Icon o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return icons;
    }
}
