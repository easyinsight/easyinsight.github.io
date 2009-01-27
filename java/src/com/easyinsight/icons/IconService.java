package com.easyinsight.icons;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

/**
 * User: James Boe
 * Date: Jan 8, 2009
 * Time: 9:33:37 PM
 */
public class IconService {
    
    public List<Icon> getIcons() {
        File iconFolder = new File("../webapps/DMS/assets/icons/32x32");
        File[] iconFiles = iconFolder.listFiles();
        List<Icon> icons = new ArrayList<Icon>(); 
        for (File iconFile : iconFiles) {
            String fileName = iconFile.getName();
            if (fileName.endsWith("png")) {
                String path = "/DMS/assets/icons/32x32/" + fileName;
                Icon icon = new Icon();
                icon.setName(fileName.substring(0, fileName.length() - 4));
                icon.setPath(path);
                icons.add(icon);
            }
        }
        return icons;
    }
}
