---
title: "My Data Filters
author: "James Boe"
---

You can now give your users a 'My Data' option to let them automatically filter reports and dashboards down to their particular tasks, deals, or tickets, while still allowing them to see other data.<!--more-->

Personas help you to limit what particular data a user can see, but in many cases, you want a user to be able to see his or her own data by default, while allowing the user to see the rest of the data if necessary. 

To get started, create or update an existing persona through Account Settings -> Personas. Create a filter on the field you wish to use for the restriction. For example, you might do Task Assignee, Sales Rep, or Agent Name. Once you've created the filter, click on the filter name and choose 'Change to My Data' as shown below.

<img style="max-width:800px;margin-top:30px;margin-bottom:30px" src="/images/my_data_persona_config1.png" alt="My Data Configuration Step 1" class="img img-responsive"/>

After clicking on 'Change to My Data', the filter will move to the opposite section of the configuration screen:

<img style="max-width:800px;margin-top:30px;margin-bottom:30px" src="/images/my_data_persona_config2.png" alt="My Data Configuration Step 2" class="img img-responsive"/>

Go ahead and save your persona. Next, go to the user configuration screen for the user you wish to set up with the persona. Set the persona and populate the filter. For example, in the screenshot below, the user is being set to the Sales Rep persona, with the filter assigned to a sales rep of Jim:

<img style="max-width:800px;margin-top:30px;margin-bottom:30px" src="/images/my_data_user_configuration.png" alt="My Data Configuration Step 3" class="img img-responsive"/>

There's one last optional configuration step if you want the filter to default to My Data for users with a valid persona configured. Open up a report or dashboard with a filter on the same field as used in the persona. Edit the filter and check the 'Default to My Data If Available' option:

<img style="max-width:800px;margin-top:30px;margin-bottom:30px" src="/images/my_data_filter_configuration.png" alt="My Data Configuration Step 4" class="img img-responsive"/>

When the user now loads the report or dashboard, the filter will default to a My Data option:

<img style="max-width:800px;margin-top:30px;margin-bottom:30px" src="/images/my_data_end_filter.png" alt="My Data Configuration Step 5" class="img img-responsive"/>

With the report showing the appropriate values tied to that user's configuration:

<img style="max-width:800px;margin-top:30px;margin-bottom:30px" src="/images/my_data_filtered.png" alt="My Data Configuration Step 6" class="img img-responsive"/>