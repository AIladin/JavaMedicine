How do I import an SQL Dump file into MySQL database?
1. Using Import SQL Wizard

Previously, you can import and *.sql file to use with your database in TablePlus by dragging the file into the query editor and execute it. It works just fine for the small sized file, but for a huge file, it gives the app a hard time executing it.

Now TablePlus has already supported Import SQL Dump.

Note:

You have to update the app to the latest version to make sure the app runs properly.
Careful: Importing a *.sql file may overwrite your existing data!
To import an SQL dump file:

Connect to your MySQL database
Choose Import > From SQL Dump… from the File menu.
This will bring up a dialog box, select the file on your file system that you would like to import, then click Import.
Your database will now be updated. Click the Refresh button (Cmd + R) if needed.