# Aimsio Java Programming Assignment
This project implements a simple web application that allows the user to create a hierarchy of activities for a project (Work break down structure). The UI is created using Vaadin framework version 8.10.

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


1- Implement the delete functionality. If the user selects an item in the box in the tree, and then presses the Delete button, the item should disappear from the tree.   
 ```$xslt
My Solution (Finished):

Firstly, I added a new interface on the /view/ProjectLayout class:
        // delete interface
        void deleteActivity(ProjectActivity projectActivity);

Then, I implemented the interface on the /view/ ProjectLayout class:
    @Override
    public void deleteActivity(ProjectActivity projectActivity) {
        for(ProjectActivity activity : treeData.getChildren(projectActivity))
        {
            treeData.removeItem(activity);
        }
        treeData.removeItem(projectActivity);

        //database.deleteObject(database.toDBObject(projectActivity));
        inMemoryDataProvider.refreshAll();
    }
Since the question wants to delete the item and all the children of the item in the program, so I remove the children.

Finally, I added up the listener to the button:
                Set<ProjectActivity> selectedItems = tree.getSelectedItems();
                listener.deleteActivity(selectedItems.size() == 0 ? null : selectedItems.iterator().next());

```

2- Implement the export button to export the hierarchy into a downloadable text file. For example for the hierarchy in the screen shot above the text file should look like this:

export.txt:
```
Test
    Part 1
        Part 2
    Part A
```

```$xslt
My Solution (Finished): 

Firstly, just like Q1, I added a new interface on the /view/ProjectLayout class:
        // add export interface, using FileDownloader to make it easier
        FileDownloader exportActivity();

Then, I was trying to reach some information about FileDownloader. I found some example online, but all of them shows 
that I have to bound the button by using this funciton. It is a little bit strange, but I think it should be using I/O
interface also. I implement the FileDownloader interface then: 

     /**
      * export activity for doing the export
      */
     @Override
     public FileDownloader exportActivity() {
         // create a new file
         File file = new File("./export.txt");
         // add the printTree function to it
         try{
             FileWriter fileWriter = new FileWriter(file);
             fileWriter.write(printTree());
             fileWriter.close();
 
             //System.out.println("GOOD");
         }catch (IOException e){
             e.printStackTrace();
         }
         // return FileDownloader
         return new FileDownloader(new StreamResource((StreamResource.StreamSource) () -> {
             try{
                 return new FileInputStream(file);
             }catch (IOException e)
             {
                 e.printStackTrace();
             }
             return null;
         },null));
     }

Since question requires me to do a hierarchy layout on the file, I simply thought I could use Pre-Order Tree traversal.
However, because the TreeData is not binary, so my printTree function is just like: 
    /**
     * A recursive function for put the padded project activities to the StringBuilder
     * @param sb
     * @param padding
     * @param projectActivities
     */
    public void traverse(StringBuilder sb, String padding, List<ProjectActivity> projectActivities){

        for(ProjectActivity node : projectActivities) {
            if (node != null) {
                sb.append(padding);
                sb.append(node.toString());
                sb.append("\n");
                traverse(sb, padding + "\t", treeData.getChildren(node));
            }
        }
    }

    /**
     * Print function
     * @return the string that export activity exports to the file
     */
    public String printTree() {
        StringBuilder treePrint = new StringBuilder();
        traverse(treePrint,"", treeData.getRootItems());
        System.out.println(treePrint.toString());
        return treePrint.toString();

    }

I used a recursive function to pad the children in a tab(/t) very time, and I did half of pre-order traversal.

Finally, I added the listener on the export button, then I got the result. 

PS: the path to export the file is in the project respository which is /java-junor-challenge/export.txt, and the current
showing what I stored in the database. 
```

##### (Finished)
3- (Bonus) Update the UI so that all the components stay centered as opposed to left aligned. They should stay centered even if the window is resized.
```$xslt
My Solution (Finished)

This is a UI question. I thought it is testing my UI fundamental. 

What I did is set the alignment of the components, and move them to the center on the class:
ProjectLayout

        this.setComponentAlignment(name, Alignment.TOP_CENTER);
        this.setComponentAlignment(buttonsLayout, Alignment.MIDDLE_CENTER);
        this.setComponentAlignment(tree, Alignment.MIDDLE_CENTER);

Then the problem was solved. 
```

4- (Bonus) How would you test this code?
```$xslt
My Solution (Finished):

    I might use JUnit to test the methods and class, this is the simplest way for doing it. 
    
    I made some Unit testing on the test folder. It is for testing my Q5's code. 

    I simply tested the methods that I created, and I was lucky I did get nothing wrong there. 

    You might test it, but because I only tested the thing that in my database, it might not pass some method if the database changed. 

    Additionally, there are also some UI testing, we could just run the program to see if the UI is good or not.

    Also, I can use some automate tools for doing UI testing, such as Blisk, Appium, ect, but I need sometime to learn.
```


5- (Bonus) Use a database layer to persist the project hierarchy when user adds or deletes the activites. The web application should be able to show the same hierarchy as last session even when it is stopped and run again. 
 ```$xslt
My Solution (Finished): 

This question cost my almost time.

Firstly, I got to choose a Database. I firstly tried MySQL, but I thought it might be a little bit tough to do a hierarchy tree.
Since I used MongoDB before, I think a NoSQL maybe a good choice, so, I used it. 

For using the MongoDB, I added dependency to the Maven, then I got started. 

Since the model (ProjectActivity) cannot be used in MongoDB directly, I impletemented a data structure class in the 
database package, which is ProjectActivityObject. According to the MongoDB offical documents and the model you gave. 
This ProjectActivityObject could be stored in the MongoDB. The good thing is I do not require to serilize a ID to the object.

Also, because it is a data tree, I would consider I am in a non-duplicate situation, so I did not implemente how to handle the duplicate
data.

Then, I started implemente the most important class which I called MongoDB in the database dirctory. 

For using custom object in the MongoDB, I used POJO interface to deal with it. 

MongoDB in Java has 2 important variables: collection and database. 
    collection is for handling queris. Just like JDBC's statment, queries, and resultSet.
    database is for choose the database respositories.

There are some important methods that I made in this project for doing the database staff. 

1. connectMongoDatabase(): which is use to connect to my MongoDB, it is called in the constructor.

2. addOneObject(): which is for dealing with insert data to the database 

3. deleteObject(): for delete an object

4. getAllObjects(): return a List of all objects I saved in the database

5. getChildren(): return a List of all the children of an object

6. getParent(): return a parent object of the children object

7. toDBObject(): return a database object of the model object

8. exportDBTreeData(): return the TreeData structured object of all the data in my database.

My methods only deal with /GET/POST/DELETE, since the question does mentioned about update, I did not make it.

The exportDBTreeDate() is used to replace the treeDate variable in the ProjectLayoutPresenter. 

The most hard problem that I faced in this question was when I finished my implementation, I found I could not interact with
the database on my Macbook, and it shows the naming.NoInitialContextExpeption. 

I finally solved it by using my Windows Desktop. So I really recommend you guy test my code on the Windows Plat. 

For more information, please read the DevDoc.txt
```

## Important Notes
* Follow the design patterns used in the code and separate your logic from UI as much as possibe. 
* Make sure your code is readable and easy to follow.
* Make sure to refer to Vaadin Framework documentation as needed.
    * Hint: using `FileDownloader` in Vaadin might help for the second part of this assignment: https://vaadin.com/docs/v8/framework/articles/LettingTheUserDownloadAFile.html  
* If we need to do other steps to run your application, in addition to `mvn jetty:run`, please include them in your submission. 
