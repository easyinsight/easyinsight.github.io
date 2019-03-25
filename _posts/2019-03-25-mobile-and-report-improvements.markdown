---
title: "Mobile and Report Improvements"
author: "James Boe"
---

We've been working to improve the mobile user experience for Easy Insight. We've also added multiple measure stacked column charts and dynamic column trees.<!--more-->

When viewing the report editor on a small screen, the report editor will now collapse into a form that more efficiently uses the available real estate:

<img style="max-width:300px" class="img-responsive" src="https://blog.easy-insight.com/images/report_editor_mobile.png" alt="Report Editor Mobile">

You can change the report type, toggle field selection, and toggle filter selection from these new options, and easily collapse a given section back down in order to more effectively view the entire report.

Next, we've added multiple measure stacked column charts, a frequent customer request. After choosing stacked column chart as the type, click on the 'Customize' link next to the report type and choose 'Multiple Measures':

<img style="max-width:400px" class="img-responsive" src="https://blog.easy-insight.com/images/multi_measure_stack.png" alt="Multiple Measure Stack Configuration"> 

You can then add multiple measures to define the stack instead of having to use two groupings.

Finally, we've added dynamic column trees, another frequent customer request. Choose Tree as the report type, then click on the 'Customize' link and choose 'Dynamic Column'. You'll define your hierarchy as usual, but instead of choosing a number of column fields, you'll choose one grouping and one measure. The tree will automatically create a column for each value of the measure filtered by the selected grouping:

<img style="max-width:1000px" class="img-responsive" src="https://blog.easy-insight.com/images/dynamic_column_tree.png" alt="Dynamic Column Tree">

If you have a filter on the dynamic column field, it will use that filter to restrict which columns have data.