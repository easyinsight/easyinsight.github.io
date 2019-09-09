---
title: "Scripting auto complete and warnings"
author: "James Boe"
---

It's easier than ever to create custom fields or apply conditional formatting with our new changes to support auto complete and warnings.<!--more-->

When you create a custom field, you can now hit the Tab key after you start typing in a field name. For example, if you have:

<img style="max-width:600px;margin-top:30px;margin-bottom:30px" src="https://blog.easy-insight.com/images/auto_complete_field_start.png" alt="Auto Complete" class="img img-responsive"/>

You can hit Tab after the Curr in the calculation to automatically complete the field name as Current Stage. If it can't find a single auto complete match, for example with a shorter Cu as the field name you've typed in, it'll pop up a search box:

<img style="max-width:600px;margin-top:30px;margin-bottom:30px" src="https://blog.easy-insight.com/images/auto_complete_field.png" alt="Auto Complete" class="img img-responsive"/>

If you have a grouping comparison, you can also auto complete to retrieve the set of possible values for the field. For example, with the calculation of:

<img style="max-width:600px;margin-top:30px;margin-bottom:30px" src="https://blog.easy-insight.com/images/auto_complete_value_start.png" alt="Auto Complete" class="img img-responsive"/>

You can hit tab after the " in the calculation to pop up the list of possible values, making it easier to get comparisons right and quickly build your custom fields:

<img style="max-width:600px;margin-top:30px;margin-bottom:30px" src="https://blog.easy-insight.com/images/auto_complete_value.png" alt="Auto Complete" class="img img-responsive"/>

You can use this same functionality from Conditional Formatting as well. You can start typing out your calculation and use Tab to pull up the list of fields or values:

<img style="max-width:600px;margin-top:30px;margin-bottom:30px" src="https://blog.easy-insight.com/images/auto_complete_conditional_format.png" alt="Auto Complete" class="img img-responsive"/>

We've also added warnings for common errors in scripting. For example, if you're missing a closing parenthesis, a warning message will pop up below the calculation input:

<img style="max-width:600px;margin-top:30px;margin-bottom:30px" src="https://blog.easy-insight.com/images/auto_complete_warning.png" alt="Auto Complete" class="img img-responsive"/>

