package com.aimsio.model;

public class ProjectActivity {
    ProjectActivity parentNode;

    public ProjectActivity(ProjectActivity parentNode, String title) {
        this.parentNode = parentNode;
        this.title = title;
    }

    String title;
    float hours;

    public ProjectActivity getParentNode() {
        return parentNode;
    }

    public void setParentNode(ProjectActivity parentNode) {
        this.parentNode = parentNode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getHours() {
        return hours;
    }

    public void setHours(float hours) {
        this.hours = hours;
    }

    @Override
    public String toString() {
        return title;
    }
}
