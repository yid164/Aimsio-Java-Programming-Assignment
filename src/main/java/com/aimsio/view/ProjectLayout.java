package com.aimsio.view;

import com.aimsio.model.ProjectActivity;
import com.vaadin.ui.*;

import java.util.Set;

public class ProjectLayout extends VerticalLayout {

    private final Tree<ProjectActivity> tree;

    interface Listener {
        void addActivity(ProjectActivity projectActivity, String title);
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
                listener.addActivity(selectedItems.size() == 0 ? null : selectedItems.iterator().next(), name.getValue());
            }
        });

        Button deleteButton = new Button("Delete");


        Button exportButton = new Button("Export");

        buttonsLayout.addComponents(addButton,deleteButton,exportButton);
        this.addComponents(name, buttonsLayout, tree);
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
