---
title: "Tree/Summary Report Rework"
author: "James Boe"
tags: ['report functionality']
---
We've reworked the internals of Tree and Summary reports to correctly recalculate calculations and conditional formatting on the upper levels of the tree and summary.<!--more--> For example, in a Google Analytics report, you might have a calculation of New Visits / Visits * 100 as a percentage. Previously, it would aggregate those results into the upper level, giving an incorrect result. It'll now recalculate the calculation per level, giving the correct value for every level of the hierarchy. This change involved quite a bit of rework on the Tree report in particular, so please let us know if you encounter any issues with these report types.