package com.aimsio.view;

import com.aimsio.model.ProjectActivity;
import com.vaadin.data.TreeData;
import com.vaadin.data.provider.TreeDataProvider;

public class ProjectLayoutPresenter implements ProjectLayout.Listener {
    private final ProjectLayout layout;
    private final TreeDataProvider<ProjectActivity> inMemoryDataProvider;
    private final TreeData<ProjectActivity> treeData;

    public ProjectLayoutPresenter(ProjectLayout layout){
        this.layout = layout;
        this.layout.setListener(this);

        treeData = new TreeData<>();
        inMemoryDataProvider = new TreeDataProvider<>(treeData);
        this.layout.getTree().setDataProvider(inMemoryDataProvider);
    }


    @Override
    public void addActivity(ProjectActivity projectActivity, String title) {
        treeData.addItem(projectActivity, new ProjectActivity(projectActivity, title));
        inMemoryDataProvider.refreshAll();
    }
}
