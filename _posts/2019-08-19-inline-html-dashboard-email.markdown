---
title: "Auto Complete Filters, FTP/SFTP Source Refresh, Excel Sheet Selection, Teamwork milestone and comment edits, and Basecamp schedule events!"
author: "James Boe"
---

We've added three new changes for this week. You can create auto complete filters, refresh your flat file data sources from FTP or SFTP sites, and email dashboards as inline HTML.<!--more-->

You can now create auto complete filters to make it easy to quickly filter down a report or dashboard into a particular value:

<img style="max-width:600px;margin-top:30px;margin-bottom:30px" src="https://blog.easy-insight.com/images/auto_complete_setup.png" alt="Auto Complete Filter Setup" class="img img-responsive"/>

When you create a filter on a grouping, you'll have a new option to the right of pattern match filter for creating an auto complete filter:

<img style="max-width:400px;margin-top:30px;margin-bottom:30px" src="https://blog.easy-insight.com/images/auto_complete_example.png" alt="Auto Complete Filter Example" class="img img-responsive"/>

By default, the auto complete filter waits for two characters typed before it pops up the list of possible options. You can change this behavior by editing the filter and adjusting the "Minimum Characters for Auto Complete" setting.

You can now automate your flat file data sources to refresh off of FTP, FTPS, and SFTP sites. After you've uploaded your data and set up your data source, you can click on Configure the Data Source -> All Configuration Options -> Flat File Configuration. You can specify the credentials for FTP retrieval, the FTP site URL, the path for the file, and the protocol. Keep in mind that FTPS and SFTP are different. You'll want to select the appropriate protocol for your server:

<img style="max-width:800px;margin-top:30px;margin-bottom:30px" src="https://blog.easy-insight.com/images/flat_file_configuration.png" alt="Flat File Configuration" class="img img-responsive"/>

After you've set up the configuration, you can schedule the source to refresh off of the Scheduling page like any other data source.

We've also added a couple of enhancements to Excel uploads. When you upload a workbook containing multiple sheets, you can choose which sheet you want to use for creating the data source. If your Excel data starts on a row other than the first row, you can also specify a row offset to start processing data from.

If you're using two way functionality, you can now create and edit Teamwork milestones and comments directly from your Easy Insight reports. You can edit the milestone deadline, description, tags, assignees, and completion status, and you can create or edit comments:

<img style="max-width:800px;margin-top:30px;margin-bottom:30px" src="https://www.easy-insight.com/images/help/milestone_list.png" alt="Teamwork Two Way Milestone Edits" class="img img-responsive"/>

Finally, Your Basecamp 3 connection will now pull over schedule events: 

<img style="max-width:200px;margin-top:30px;margin-bottom:30px" src="https://blog.easy-insight.com/images/basecamp_schedule_entries.png" alt="Basecamp Schedule Entries" class="img img-responsive"/>