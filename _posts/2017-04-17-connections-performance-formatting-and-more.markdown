---
title: "Connection Improvements, Performance Tweaks, Dashboard Versioning, and More!"
author: "James Boe"
---
We have a large number of smaller changes for this week--improvements to the Basecamp, Teamwork, and Zendesk connections, performance improvements to resource allocation reporting, dashboard version history, and more! <!--more-->

First, we've got several small connection improvements. For Basecamp 2, we've added Project Creator as a new field. For Teamwork, the data source should now refresh its data dramatically faster. And for Zendesk, Ticket Description results should have much better formatting around line breaks.

Next, we've made a major performance improvements to the calculation used in the Resource Allocation tab of project management dashboards. The weeklyaverage(), monthlyaverage(), and quarterlyaverage() functions should all run dramatically faster now.

When you save changes to a dashboard, the changes will now be tracked just like the changes to reports have been. If you need to revert back to a previous version of the dashboard, you can go to Configuration -> Version History to restore a previous version.

Finally, if you want to reorder the filters on a dashboard, report, or filter set, you can now click on Quick Actions and choose "Reorder Filters". This action will pull up a window where you can drag the filters around to reorder them.