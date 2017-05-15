---
title: "Freshdesk SLA Reporting, Testing Joins, Hubspot Connection"
author: "James Boe"
---
We've got steps for adding Freshdesk SLA tracking to your reporting, a new way to make sure your joins are correctly defined, and a new connection to Hubspot!<!--more-->

If you need to report on your Freshdesk SLA data, you can now make a few changes to your configuration and you'll have reporting on whether or not your SLAs are being met. First, open up the "Ticket Calculations" report in the report editor.

Create a custom grouping for "SLA Compliance" by clicking on Custom Field -> Add Custom Grouping. Label the field as SLA Compliance, then add a calculation of:

if ([Due By] == null() || [Resolved At] <= [Due By] || nowdate() <= [Due By], "SLA Compliance", "SLA Violation")

Save the field and add the field to the report. Next, click on Custom Field -> Copy Field and choose "Ticket Count" from under the "Tickets" folder.

Rename the new copied field to "SLA Violations", create a pattern match filter on "SLA Compliance" from under "Report Custom Fields" and put in a value of "SLA Violation" (matching what we did in the previous step).

Save the field and add the field to the report as well. By adding these two fields to your calculation report, they become available to the rest of your reporting. Click on the Save button and save the report. If you create a new report or modify an existing report, you will now be able to add those fields from under the Ticket Calculations folder to create reports on your SLA status.

<img src="/images/sla_compliance.png" alt="SLA Compliance Field" class="img img-responsive"/>



Next, we've added new functionality for helping to test the validity of a join when you're in the setup of a combined data source. After you've selected the data sources/fields in the join, you can click on the "Test Join" button as shown below to see an analysis of the join. If the results show a low value of matched records relative to unmatched records, your join may not be properly defined.

<img src="/images/test_joins.png" alt="Test Joins" class="img img-responsive"/>



Finally, we have a beta version of the Hubspot connection available now under the Connections page. There aren't any prebuilt reports/dashboards available at this point, but this connection should help you get started with integrating Hubspot into your other Easy Insight reporting. Enjoy!