---
title: "Join Cardinality"
author: "James Boe"
---
You can now specify One to One, One to Many, Many to One, or Many to Many as cardinality on each join you define.<!--more--> For example, a project might have a budget, while having a one to many relationship to time entries. You don't want it to multiply out the budget for each time entry, so defining the One to Many join will ensure that the budget value is properly handled throughout the various report processing.