# AppointmentScheduler

This is a simple application to create appointments between customers and users. The application keeps track of appointments and customer info. Upon logging in, the user is notified if they have an appointment coming up within the next 15 minutes. All information on the application is pulled from a database that can be edited from within the application. Time within the database is in UTC, while times entered by the user are in their local time zone. All times entered are checked against EST because that is the business hours of the company. 
