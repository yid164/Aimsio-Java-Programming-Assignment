package com.aimsio.test;

import com.aimsio.database.MongoDB;
import com.aimsio.database.ProjectActivityObject;
import com.aimsio.model.ProjectActivity;
import com.vaadin.data.TreeData;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MongoDBTest {
    MongoDB mongoDB;
    ProjectActivityObject object;

    /**
     * Setup method
     */
    @BeforeEach
    void setUp() {
        mongoDB = new MongoDB();
    }

    @Before
    void connectMongoDatabase() {
    }


    /**
     * Test add function
     */
    @Test
    void addOneObject() {
        object = new ProjectActivityObject("hello");
        mongoDB.addOneObject(object);
        List<ProjectActivityObject> objects = mongoDB.getAllObjects();
        assertTrue(objects.contains(object), "Checking the database contains 'hello' object");
    }

    /**
     * Testing delete function
     */
    @Test
    void deleteObject() {
        object = new ProjectActivityObject("hello");
        mongoDB.deleteObject(object);
        List<ProjectActivityObject> objects = mongoDB.getAllObjects();
        assertFalse(objects.contains(object), "Checking the database delete 'hello' object");
    }

    /**
     * Testing get all objects from database
     */
    @Test
    void getAllObjects() {
        List<ProjectActivityObject> objects = mongoDB.getAllObjects();
        object = new ProjectActivityObject("a");
        assertTrue(objects.contains(object), "Checking the database contains 'a' object");
        object = new ProjectActivityObject("b");
        assertTrue(objects.contains(object), "Checking the database contains 'b' object");
        object = new ProjectActivityObject("c");
        assertTrue(objects.contains(object), "Checking the database contains 'c' object");

    }

    /**
     * Testing get all children by a object from database
     */
    @Test
    void getChildren() {

        object = new ProjectActivityObject("a");
        List<ProjectActivityObject> objects = mongoDB.getChildren(object);
        ProjectActivityObject a1 = new ProjectActivityObject("a1");
        ProjectActivityObject a2 = new ProjectActivityObject("a2");
        ProjectActivityObject a3 = new ProjectActivityObject("a3");
        a1.setParentNode(object);
        a2.setParentNode(object);
        a3.setParentNode(object);
        assertTrue(objects.contains(a1), "Checking the children nodes contains 'a1' object");
        assertTrue(objects.contains(a2), "Checking the children nodes contains 'a2' object");
        assertTrue(objects.contains(a3), "Checking the children nodes contains 'a3' object");
    }

    /**
     * Testing get the object's parent from database
     */
    @Test
    void getParent() {
        object = new ProjectActivityObject("a");
        ProjectActivityObject a1 = new ProjectActivityObject("a1");
        a1.setParentNode(object);
        assertEquals(mongoDB.getParent(a1), object);
    }

    /**
     * Testing set an object parents, since it has not been used and it will affect database, I will just skip it for now
     */
    @Test
    void setObjectParent() {
    }

    /**
     * Testing a ProjectActivity object to a ProjectActivityObject object
     */
    @Test
    void toDBObject() {
        ProjectActivity activity = new ProjectActivity(null, "hello");
        object = new ProjectActivityObject("hello");
        assertEquals(object,mongoDB.toDBObject(activity));
    }

    /**
     * Testing tree data exporting
     */
    @Test
    void exportDBTreeData() {
        TreeData<ProjectActivity> projectActivityTreeData = new TreeData<>();
        ProjectActivity a = new ProjectActivity(null, "a");
        ProjectActivity b = new ProjectActivity(null, "b");
        ProjectActivity c = new ProjectActivity(null, "c");
        ProjectActivity a1 = new ProjectActivity(a, "a1");
        ProjectActivity a2 = new ProjectActivity(a, "a2");
        ProjectActivity a3 = new ProjectActivity(a, "a3");
        ProjectActivity a11 = new ProjectActivity(a1, "a11");
        ProjectActivity a12 = new ProjectActivity(a1, "a12");
        projectActivityTreeData.addItem(null,a);
        projectActivityTreeData.addItem(null,b);
        projectActivityTreeData.addItem(null,c);
        projectActivityTreeData.addItem(a,a1);
        projectActivityTreeData.addItem(a,a2);
        projectActivityTreeData.addItem(a,a3);
        projectActivityTreeData.addItem(a1,a11);
        projectActivityTreeData.addItem(a1,a12);
        assertEquals(projectActivityTreeData.getRootItems().size(), mongoDB.exportDBTreeData().getRootItems().size());
    }
}