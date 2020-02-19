# Aimsio Java Programming Assignment
This project implements a simple web application that allows the user to create a hierarchy of activities for a project (Work break down structure).

## Running the web application
To run the project, make sure you have JDK and maven installed on your machine and then run the following command in the root directory of the project:
```bash
mvn compile; mvn jetty:run
```
Then you can open a browser and access the web application through this URL: http://localhost:8080

## Using the web application
You can type a title for your project in the _Project Activity_ text box and press _Add_ button. The item will be added to the box labled **Project Activities** below. In order to add a sub activity, before pressing the Add button, select an item in the box below. 

**Project Actitivies** is a Tree structure that shows the hierarchy of project activities. 
![Screenshot](imgs/screenshot1.png)

# Assignment
We want you to make the following changes to this web application:
1. Implement the delete functionality. If the user selects an item in the box in the tree, and then presses the Delete button, the item should disappear from the tree.   
2. Implement the export button to export the hierarchy into a downloadable text file:

