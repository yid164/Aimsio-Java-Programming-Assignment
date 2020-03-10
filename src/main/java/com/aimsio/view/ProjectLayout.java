package com.aimsio.view;

import com.aimsio.database.MongoDB;
import com.aimsio.database.ProjectActivityObject;
import com.aimsio.model.ProjectActivity;
import com.vaadin.server.FileDownloader;
import com.vaadin.ui.*;

import java.util.Set;

public class ProjectLayout extends VerticalLayout {

    private final Tree<ProjectActivity> tree;

    private MongoDB database;

    interface Listener {
        void addActivity(ProjectActivity projectActivity, String title);

        // delete interface
        void deleteActivity(ProjectActivity projectActivity);

        // add export interface, using FileDownloader to make it easier
        FileDownloader exportActivity();

    }
    Listener listener;
    public ProjectLayout(){
        super();
        final TextField name = new TextField();
        name.setCaption("Project Activity:");

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        tree = new Tree<>();
        tree.setCaption("Project Activities");
        tree.setWidth("300px");
        tree.setHeight("300px");
        tree.addStyleName("with-border");
        Button addButton = new Button("Add");
        addButton.addClickListener(e -> {
            if (listener != null ) {
                Set<ProjectActivity> selectedItems = tree.getSelectedItems();
                System.out.println(selectedItems.size() == 0 ? null : selectedItems.iterator().next());
                listener.addActivity(selectedItems.size() == 0 ? null : selectedItems.iterator().next(), name.getValue());

            }
        });

        Button deleteButton = new Button("Delete");
        //add listener on the delete
        deleteButton.addClickListener(e->{
            if(listener != null)
            {
                Set<ProjectActivity> selectedItems = tree.getSelectedItems();
                listener.deleteActivity(selectedItems.size() == 0 ? null : selectedItems.iterator().next());

            }
        });


        Button exportButton = new Button("Export");
        // add button lister on export
        exportButton.addClickListener(e->{
           if(listener != null)
           {
               listener.exportActivity().extend(exportButton);
           }
        });

        buttonsLayout.addComponents(addButton,deleteButton,exportButton);
        this.addComponents(name, buttonsLayout, tree);
        //this.setSizeFull();
        // This is for Question 3, put the 3 elements in center alignment
        this.setComponentAlignment(name, Alignment.TOP_CENTER);
        this.setComponentAlignment(buttonsLayout, Alignment.MIDDLE_CENTER);
        this.setComponentAlignment(tree, Alignment.MIDDLE_CENTER);


    }

    public Tree<ProjectActivity> getTree() {
        return tree;
    }

    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
