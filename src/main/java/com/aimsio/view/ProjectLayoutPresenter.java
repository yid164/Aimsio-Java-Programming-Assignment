package com.aimsio.view;

import com.aimsio.database.MongoDB;
import com.aimsio.model.ProjectActivity;
import com.vaadin.data.TreeData;
import com.vaadin.data.provider.TreeDataProvider;
import com.vaadin.server.*;

import java.io.*;
import java.util.List;


public class ProjectLayoutPresenter implements ProjectLayout.Listener {
    private final ProjectLayout layout;
    private final TreeDataProvider<ProjectActivity> inMemoryDataProvider;
    private final TreeData<ProjectActivity> treeData;
    private MongoDB database;


    public ProjectLayoutPresenter(ProjectLayout layout, MongoDB database){
        this.database = new MongoDB();
        this.layout = layout;
        this.layout.setListener(this);
//        treeData = new TreeData<>();
        this.database = new MongoDB();
        treeData = database.exportDBTreeData();
        inMemoryDataProvider = new TreeDataProvider<>(treeData);
        this.layout.getTree().setDataProvider(inMemoryDataProvider);
    }


    @Override
    public void addActivity(ProjectActivity projectActivity, String title) {

        treeData.addItem(projectActivity, new ProjectActivity(projectActivity, title));

        //ProjectActivity activity = new ProjectActivity(projectActivity, title);
        inMemoryDataProvider.refreshAll();
        System.out.println(database.getAllObjects());
        //database.addOneObject(database.toDBObject(activity));

    }

//    @Override
//    public void addToDatabase(ProjectActivity projectActivity, String title){
//        ProjectActivity activity = new ProjectActivity(projectActivity, title);
//        ProjectActivityObject object = database.toDBObject(activity);
//        database.addOneObject(object);
//    }

    /**
     * delete activity for doing the delectation
     * @param projectActivity
     */
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



}
