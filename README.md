# phone-usage

To run the application you need to set up the following environment variables:

DB_URL=jdbc:mysql://localhost:3306/phone_usage;DB_PASSWORD=root;DB_USERNAME=root;REPORT_YEAR=2018

You also need a MySQL database running in the port you specified in the DB_URL environment variable.
Then you need to create the database schema with the following commands:

DROP SCHEMA IF EXISTS phone_usage;

CREATE SCHEMA phone_usage;

USE phone_usage;

If you run the application now, the tables will be automatically created for you. It will throw an exception
because it tries to generate the report but there's no data yet, so we need to populate the tables. 

The following is needed to be able to have permissions of loading a csv file into our db. You also need to run the
connection with OPT_LOCAL_INFILE=1, which is achieved following the button click flow provided. 

SET GLOBAL local_infile=1;

Database -> Manage Connections -> Advanced -> Others -> add OPT_LOCAL_INFILE=1

Now, load the csv data sources into the db with the following 2 commands:

LOAD DATA LOCAL INFILE 'C:/Users/josue/Code/phone-usage/src/main/resources/CellPhone.csv'
INTO TABLE cell_phones FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n'
IGNORE 1 ROWS (employee_id, employee_name, purchase_date, model);

LOAD DATA LOCAL INFILE 'C:/Users/josue/Code/phone-usage/src/main/resources/CellPhoneUsageByMonth.csv'
INTO TABLE phone_usage_by_month FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n'
IGNORE 1 ROWS (cell_phone_employee_id, date, total_minutes, total_data);

Now you are all set, just run the application again and each time an excel file report will be created under 
src/main/resources/reports. 




# Technical decisions and assumptions

I wasn't sure if I needed to create the db or just use the csv data straightaway. I went with the DB option but I could've
also parsed the csvs directly and go from there.

Another interesting thing is that for the summary header, I don't filter phones by year purchased, since I assummed a 
phone might have been purchased in an older year but we still want to see its usage in the year specified to get the
report for.

I didn't complete the printing locally functionality because I already invested like 4 hours and I read your comment 
that it should be fine. I think the main feature is processing the data and generating the excel file, which is working
correctly (I will attach a video too for your facility) and we can print it from Microsoft Excel or Google Sheets. In any
case, java.awt provides an API to print documents and it could be used for that.

To improve this project I would ideally use docker to prevent needing to run MySQL dependency separately, and automate the commands
needed to set it up. I would also add unit and integration tests to make sure everything works fine. There's no logging at all
which I would also add in a real project, and also error handling. Finally, I would also improve the fact that the first time
it throws an exception because it creates the tables and there's no data for the report. 

