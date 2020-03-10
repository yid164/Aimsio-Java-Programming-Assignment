## Author: Ken (Yinsheng) Dong
## Date: 2020/03/01
## Description Solution for Aimsio Java Programming Assignment

## Document Description: This document is using for description of my solution for Aimsio Java Assignment.

There have 5 question, and I solved all of them. The description is showing up what I did, I thought, my mistakes and
problems that I faced during I was solving them.

I recommend someone who is testing the program by using Java 8 (JDK 1.8) and on the Windows platform.

The reburied frameworks are Vaadin and Maven.

The bash command is also:  mvn compile; mvn jetty:run

Please let me know if you have any problem when you run it.

My email address is

yinsheng.dong@usask.ca

or

smallofyou@gmail.com

Thanks for your time.


------------------------------------------------------------------------------------------------------------------------
Question 1:

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

Finally, I added up the listener to the button and the question was solved:

                Set<ProjectActivity> selectedItems = tree.getSelectedItems();
                listener.deleteActivity(selectedItems.size() == 0 ? null : selectedItems.iterator().next());


------------------------------------------------------------------------------------------------------------------------
Question 2:

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

PS: the path to export the file is in the project repository which is /java-junior-challenge/export.txt, and the current
showing what I stored in the database.


------------------------------------------------------------------------------------------------------------------------
Question 3:

This is a UI question. I thought it is testing my UI fundamental.

What I did is set the alignment of the components, and move them to the center on the class:
ProjectLayout

        this.setComponentAlignment(name, Alignment.TOP_CENTER);
        this.setComponentAlignment(buttonsLayout, Alignment.MIDDLE_CENTER);
        this.setComponentAlignment(tree, Alignment.MIDDLE_CENTER);

Then the problem was solved.


------------------------------------------------------------------------------------------------------------------------
Question 4:

I might use JUnit to test the methods and class, this is the simplest way for doing it.

I made some Unit testing on the test folder. It is for testing my Q5's code.

I simply tested the methods that I created, and I was lucky I did get nothing wrong there.

You might test it, but because I only tested the thing that in my database, it might not pass some method if the database changed.

Additionally, there are also some UI testing, we could just run the program to see if the UI is good or not.

Also, I can use some automate tools for doing UI testing, such as Blisk, Appium, ect, but I need sometime to learn.


------------------------------------------------------------------------------------------------------------------------
Question 5:

This question cost my almost time.

Firstly, I got to choose a Database. I firstly tried MySQL, but I thought it might be a little bit tough to do a hierarchy
tree. Since I used MongoDB before, I think a NoSQL maybe a good choice, so, I used it.

For using the MongoDB, I added dependency to the Maven, then I got started.

Since the model (ProjectActivity) cannot be used in MongoDB directly, I implemented a data structure class in the
database package, which is ProjectActivityObject. According to the MongoDB official documents and the model you gave.
This ProjectActivityObject could be stored in the MongoDB. The good thing is I do not require to serialize a ID to the object.

Also, because it is a data tree, I would consider I am in a non-duplicate situation, so I did not implement how to handle the duplicate
data.

Then, I started implement the most important class which I called MongoDB in the database directory.

For using custom object in the MongoDB, I used POJO interface to deal with it.

MongoDB in Java has 2 important variables: collection and database.
    collection is for handling queries. Just like JDBC's statement, queries, and resultSet.
    database is for choose the database repositories.

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
the database on my MacBook Pro, and it shows the naming.NoInitialContextException.

I finally solved it by using my Windows Desktop. So I really recommend you guy test my code on the Windows Plat.
