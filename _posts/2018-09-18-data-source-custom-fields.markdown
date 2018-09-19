---
title: "Data Source Custom Fields"
author: "James Boe"
---

You can now extend your connections with even more powerful custom fields. For example, you could add a Project Category to your Projects or match your support users up to customer records in your CRM system.<!--more-->

From your data source, you can click on Configure the Data Source -> Add Custom Fields to get started.

Depending on your data source, you may or may not get some suggested fields to start with. For example, Basecamp will suggest custom fields on top of your projects or todos:

<img style="max-width:800px;margin-top:30px;margin-bottom:30px" src="/images/custom_field1.png" alt="Collapsed Sidebar" class="img img-responsive"/>

By choosing Projects, we get the option to use a template such as Project Category or Project Start Date. We can also click on one of the types to the right to choose a custom field type ourselves. If we choose one of the templates, the type and name are populated for us. We can use the suggested name or rename it, then create the field:

<img style="max-width:800px;margin-top:30px;margin-bottom:30px" src="/images/custom_field_template.png" alt="Expanded Sidebar" class="img img-responsive"/>

After creating the field, you can assign values through the automatically generated editor report. Simply click on the value you wish to assign or change and start typing, and the value will be set. Once assigned, you can use this field like any other field on your connection.

<img style="max-width:800px;margin-top:30px;margin-bottom:30px" src="/images/custom_field_assigning_value.png" alt="Expanded Sidebar" class="img img-responsive"/>

If you don't have the option to use a template on your connection or simply want to create your own custom field, the configuration will be slightly different. First, you'll need to choose the field you wish to base your custom field on--for example, if you're adding custom fields to your Deals, you might choose Deal ID. If you're adding custom fields to your products, you might choose Product Code. In this example, we're doing Opportunity ID:

<img style="max-width:800px;margin-top:30px;margin-bottom:30px" src="/images/custom_field_custom_configuration_start.png" alt="Expanded Sidebar" class="img img-responsive"/>

You can choose whether or not to include a label field as part of the configuration. For our above example, we're using Opportunity ID, but we also want to include Deal Title so that when we're assigning values, we know which deal we're mapping the data into. We'll need to choose a name for the custom field data source, choose a type for the field, and name the field. In the above example, we're labeling the data source as Deal Extensions and choosing a Measure as the type. Once generated, the field is editable in exactly the same way as the Basecamp example.

<img style="max-width:800px;margin-top:30px;margin-bottom:30px" src="/images/custom_field_custom_configuration.png" alt="Expanded Sidebar" class="img img-responsive"/>