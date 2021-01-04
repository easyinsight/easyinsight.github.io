---
title: "Parameter Queries and Connection Tweaks"
author: "James Boe"
---

Have a complex database query that you want to run live with parameters from your report? We've added live query parameters as a new type of database connection as well as making some small tweaks to other connections.<!--more-->

<h2 class="productHeader">Live Query Parameter Sources</h2>

We've seen customers who have complex queries across multiple tables that they want to run directly against the database, as opposed to recreating in a variety of joins across different data sources within Easy Insight. For example, take the query:

select order_number, customer_name from order, customer where order.customer_id = customer.id and order_date >= '2020-01-01' and order_date <= '2020-12-31'

When creating your database connection, you can click on the 'Create a Parameter Query' link. For the above example, you would specify a query of:

select order_number, customer_name from order, customer where order.customer_id = customer.id and order_date >= {OrderDate} and order_date <= {OrderDate}

Click on 'Add Live Query Parameter' and add a parameter with a name of OrderDate and a type of Date. Create the connection as normal.

When you go to build a report on this new connection, you'll be prompted to automatically create a date filter on the OrderDate parameter. Without the date filter, the report can't run, since it needs to populate that filter to pull the live data out of the remote database.

You can also set up single, multi, or auto complete filters for the live query, but you'll need to use them in the context of a combined data source. For more information on live query parameter sources, see <a href="https://www.easy-insight.com/docs/data_sources/databases.html">https://www.easy-insight.com/docs/data_sources/databases.html</a>. You'll need an Enterprise account in order to use this functionality. 

<h2 class="productHeader">Connection Tweaks</h2>

<ul>
<li>We've added the various product task fields to Everhour so that you can directly join data to its matching task in Basecamp, JIRA, Asana, Trello, or ClickUp.</li>
<li>You can now pull prepayment information from DEAR Systems.</li>
<li>QuickBooks Online now consolidates all transaction and line information into Transaction and Line tables, combining everything from Bills, Sales Orders, Invoices, Purchases, Purchase Orders, and Credit Memos.</li>
</ul>