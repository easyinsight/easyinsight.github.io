/*  This software code is made available "AS IS" without warranties of any 
	kind.  You may copy, display, modify and redistribute the software
	code either by itself or as incorporated into your code; provided that
	you do not remove any proprietary notices.  Your use of this software
	code is at your own risk and you waive any claim against Amazon
	Web Services LLC or its affiliates with respect to your use of this 
	software code. (c) Amazon Web Services LLC or its affiliates.*/

package com.easyinsight.billing;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FormatTimestamp {
	
	public static String formatTimestamp() {
		Calendar cal = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		cal.setTimeInMillis(cal.getTimeInMillis() - cal.get(Calendar.ZONE_OFFSET) -
				cal.get(Calendar.DST_OFFSET));
		return df.format(cal.getTime());
	}

}
