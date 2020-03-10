package com.aimsio.database;

import com.aimsio.model.ProjectActivity;
import com.mongodb.*;
import com.mongodb.MongoClient;
import com.mongodb.client.*;
import com.mongodb.client.result.UpdateResult;
import com.vaadin.data.TreeData;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


public class MongoDB {
    /**
     * database config
     */
    private MongoDatabase database;

    private MongoCollection<ProjectActivityObject> collection;

    /**
     * Construct
     */
    public MongoDB() {
        database = null;
        collection = null;
        connectMongoDatabase();
    }


    /**
     * Connect to MongoDB
     */
    public void connectMongoDatabase() {
        try {
            // url of my MongoDB
            MongoClientURI uri = new MongoClientURI(
                    "mongodb+srv://ken1234:cnSDeDK8cZ7TTDdH@project-fge7j.mongodb.net/test?retryWrites=true&w=majority"
            );

            MongoClient mongoClient = new MongoClient(uri);
            CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                    fromProviders(PojoCodecProvider.builder().automatic(true).build()));
            database = mongoClient.getDatabase("test").withCodecRegistry(pojoCodecRegistry);
            System.out.println("Database connected");
            collection = database.getCollection("project", ProjectActivityObject.class);
            System.out.println("Collection connected");

        } catch (Exception e) {
            System.out.println(e.getCause().toString());
        }
        //return database;

    }


    /**
     * Add a object
     *
     * @param object
     */
    public void addOneObject(ProjectActivityObject object) {
        collection.insertOne(object);
    }

    /**
     * Delete a object
     *
     * @param object
     */
    public void deleteObject(ProjectActivityObject object) {
        try {

            collection.deleteMany(eq("parentNode.title", object.getTitle()));
            collection.deleteOne(eq("title", object.getTitle()));
        } catch (Exception e) {
            System.out.println(e.getCause().toString());
        }
    }

    /**
     * Get all objects and return them in a list
     */
    public List<ProjectActivityObject> getAllObjects() {
        List<ProjectActivityObject> list = new ArrayList<>();

        try {
            if (database != null) {
                System.out.println("Connect to database successfully");
            }
            Consumer<ProjectActivityObject> printBlock = new Consumer<ProjectActivityObject>() {
                @Override
                public void accept(final ProjectActivityObject projectActivityObject) {
                    //System.out.println(projectActivityObject);
                }
            };
            collection.find().forEach(printBlock);
            FindIterable<ProjectActivityObject> iterable = collection.find();
            MongoCursor<ProjectActivityObject> cursor = iterable.iterator();
            while (cursor.hasNext()) {
                list.add(cursor.next());
            }


        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return list;

    }

    /**
     * Get all children
     *
     * @return a list of children
     */
    public List<ProjectActivityObject> getChildren(ProjectActivityObject object) {
        List<ProjectActivityObject> list = new ArrayList<>();
        try {
            FindIterable<ProjectActivityObject> iterable = collection.find(eq("parentNode.title", object.getTitle()));
            MongoCursor<ProjectActivityObject> cursor = iterable.iterator();
            while (cursor.hasNext()) {
                ProjectActivityObject o = cursor.next();
                list.add(o);
                // used a recursive function to traverse it
                list.addAll(getChildren(o));
            }

        } catch (Exception e) {
            System.out.println(e.getCause().toString());
        }
        return list;

    }

    /**
     * get parent
     *
     * @param object ProjectActivityObject
     * @return it's parent
     */
    public ProjectActivityObject getParent(ProjectActivityObject object) {
        ProjectActivityObject parent = collection.find(eq("title", object.getParentNode().getTitle())).first();
        if (parent == null) {
            return null;
        }
        return parent;
    }

    /**
     * Setup the object parent in the database
     *
     * @param object
     * @param parent
     */
    public void setObjectParent(ProjectActivityObject object, ProjectActivityObject parent) {
        try {

            Bson filter = eq("title", object.getTitle());
            Bson updateOperation = set("parentNode", parent);
            UpdateResult updateResult = collection.updateOne(filter, updateOperation);
            System.out.println(updateResult.toString());
        } catch (Exception e) {
            System.out.println(e.getCause().toString());
        }
    }

    /**
     * Convert a ProjectActivity to ProjectActivityObject
     *
     * @param activity
     * @return object
     */
    public ProjectActivityObject toDBObject(ProjectActivity activity) {
        ProjectActivityObject object = new ProjectActivityObject(activity.getTitle());
        if (activity.getParentNode() != null) {
            object.setParentNode(new ProjectActivityObject(activity.getParentNode().getTitle()));
        }
        return object;
    }

    /**
     * @param parentActivities
     * @param childObject
     * @return true if list contains the child object'parent and add the node to the list otherwise false
     */
    private boolean containsParent(List<ProjectActivity> parentActivities, ProjectActivityObject childObject) {
        for (ProjectActivity activity : parentActivities) {
            if (childObject.getParentNode().getTitle().equals(activity.getTitle())) {
                parentActivities.add(new ProjectActivity(activity, childObject.getTitle()));
                return true;
            }
        }
        return false;
    }

    /**
     * This TreeDate is order to sync the ProjectLayoutPresenter's TreeDate, so it could directly be used to Layout function
     * This function might be slow because you defined the data type which is TreeData, so it has to convert database object
     * to the TreeData
     *
     * @return TreeData list of ProjectActivity in Database
     */
    public TreeData<ProjectActivity> exportDBTreeData() {
        TreeData<ProjectActivity> activityTreeData = new TreeData<>();
        List<ProjectActivityObject> objects = getAllObjects();
        List<ProjectActivity> rootItems = new ArrayList<>();

        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).getParentNode() == null) {
                System.out.println("GOOD");
                rootItems.add(new ProjectActivity(null, objects.get(i).getTitle()));
                objects.set(i, null);
                System.out.println(rootItems);
            }
        }
        objects.removeIf(Objects::isNull);
        System.out.println(objects);


        while (!objects.isEmpty()) {
            for (int i = 0; i < objects.size(); i++) {
                if (containsParent(rootItems, objects.get(i))) {
                    objects.set(i, null);
                }
            }
            objects.removeIf(Objects::isNull);
        }
        System.out.println(rootItems);
        for (ProjectActivity activity : rootItems) {
            System.out.println(activity);
            activityTreeData.addItem(activity.getParentNode(), activity);
        }
        return activityTreeData;
    }
}

//    /**
//     * Main for testing
//     * @param args
//     */

//    public static void main(String [] args)
//    {
//        MongoDB db = new MongoDB();
//        ProjectActivity activity1 = new ProjectActivity(null, "Hello");
////        ProjectActivity activity2 = new ProjectActivity(activity1, "world");
//        ProjectActivityObject object = db.toDBObject(activity1);
//        db.addOneObject(object);

//        try{
//            db.getCollection().insertOne(projectActivityObject1);
//        }catch (Exception e){
//            System.out.println(e.getCause().toString());
//
//        db.addOneObject(projectActivityObject);
//        db.addOneObject(projectActivityObject1);
//        db.addOneObject(projectActivityObject2);

//        db.getAllActivities();
//        db.deleteActivities(projectActivityObject);
//db.deleteOjbect(projectActivityObject);
// db.deleteOjbect(projectActivityObject1);
//        db.getAllActivities();
//System.out.println(projectActivityObject1.hashCode());
//        System.out.println(db.getAllObjects());
//        System.out.println(db.getChildren(projectActivityObject));
//        System.out.println(db.getParent(projectActivityObject2));
//    }
//
//
//}
