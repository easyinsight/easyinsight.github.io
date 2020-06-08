---
title: "PDF Enhancements and Duration Formatting"
author: "James Boe"
---

We've overhauled our PDF rendering and added new options for formatting durations!<!--more-->



We've redesigned Easy Insight's PDF generation. In particular, the new generation code should handle dashboard output much more cleanly. You can turn on the new PDF rendering by going to Account Settings -> PDF and toggling the 'New' PDF style to active.

We'll swap everyone over to this new rendering style in the near future, but wanted to make it available for anyone who wants to try it out now while we continue to test and polish the new rendering.



We've also added new options for formatting duration values. When you configure a measure, you'll now see that you have options under Formatting including Milliseconds, Seconds, Minutes, Hours, and Days:

<img style="max-width:400px;margin-top:30px;margin-bottom:30px" src="https://blog.easy-insight.com/images/formatting_config.png" alt="Formatting Configuration" class="img img-responsive"/>

Your selection needs to match up with whatever unit of the measure the field represents--if it's a calculation of:

daysbetween([Date Created], [Date Won])

You'd want to use Days as your formatting. If it's a calculation of:

minutesbetween([Date Created], [Solved At])

You'd want to use Minutes as your formatting. With the formatting configured, the next step is to define the formatting output:

<img style="max-width:400px;margin-top:30px;margin-bottom:30px" src="https://blog.easy-insight.com/images/formatting_duration.png" alt="Formatting Configuration" class="img img-responsive"/>

You can use the following format options:

y = Year
M = Month
d = Day
m = Minute
s = Second

For example, if you define a format of:

d:h:m

The resulting report will look the following:

<img style="max-width:600px;margin-top:30px;margin-bottom:30px" src="https://blog.easy-insight.com/images/formatting_result.png" alt="Formatting Configuration" class="img img-responsive"/>