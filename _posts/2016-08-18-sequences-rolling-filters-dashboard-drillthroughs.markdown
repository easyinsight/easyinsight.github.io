---
title: "Sequences, Rolling Filter Improvements, Dashboard Drillthroughs, Locales"
author: "James Boe"
---
We've got a few changes to walk through this week, including a way to schedule multiple refreshes and emails in a single sequence, new rolling filter options, drillthroughs into dashboards, and more<!--more-->

You can now create sequences through the Scheduling page by clicking on Create -> Sequence. Sequences allow you to choose from your data source refreshes and scheduled emails, specify an order in which they should run, and specify when the sequence should run. This way, you can ensure that a refresh completes before report emails are sent.

We've added a couple of new Custom options to rolling filter definitions. When you select the Custom option from the bottom of the rolling filter options, you can now choose "Last Full" in addition to the existing Last/Next/Before/After options, so that you can do things like "Last Full Four Months." We've also added Quarters as an option, so that you can easily do filters like "Last Six Full Quarters."

Drillthroughs now enable you to click into dashboards as well as reports, both out into another page as well as through a modal window. Simply define your drillthrough as normal through the field editor, then choose Dashboard and specify the dashboard you want to drill into. For example, you might have a project dashboard showing a few different reports tied to your Basecamp or Teamwork project. Your users could click into that dashboard from a project summary list to see a few different reports specific to that project.

Finally, we've added several new Account Locale options under Account Settings, as well as made a few fixes to our charts to ensure that they render with the proper number formatting for locales.