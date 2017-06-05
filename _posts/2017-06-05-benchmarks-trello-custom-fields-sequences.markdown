---
title: "Benchmark Reports, Trello Custom Fields, Sequence Improvements"
author: "James Boe"
---
We've added a new Benchmark report type, Trello custom field reporting, and Run Now for sequences!<!--more-->

The new Benchmark report enables you to easily compare one value of a grouping against all of the other values in your data. For example, you might compare different metrics for a sales rep against the rest of your sales team, or for a particular agent against the rest of your support organization.

To start with, choose the "Benchmark" report type from the report editor type selector. You'll need to choose the grouping used for the comparison and any number of metrics for comparison purpose. Each metric will represent one row of data in the report.

<img src="/images/benchmark_report.png" alt="Benchmark Report" class="img img-responsive"/>

You'll want to create a filter on your comparison grouping, then choose a value from that in order to get values for the "Your Value" and "Rank" options. For the "Rank", you can adjust the "High Value is Good" setting under Edit Field Properties -> KPI in order to change the direction of the ranking.

<hr>

Next, we've added integration with Trello custom fields to pull over all types of Trello custom fields for you to use in your reporting. You'll find your custom fields for each board under a new folder labeled as (Board Name) Custom Fields. Joins are automatically defined between the new folder and the main data, so you can just add the fields to your report and they should immediately work for you.

<hr>

Finally, we've added a "Run Now" option to Sequences under Scheduling. If you need to trigger a sequence to run manually, you can edit the sequence, scroll to the bottom of the page, and click on the "Run Now" button.