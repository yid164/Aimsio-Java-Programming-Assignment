package com.aimsio.database;

import org.bson.types.ObjectId;

/**
 * Create the project activity object to store in the database
 */

public final class ProjectActivityObject{


    private ProjectActivityObject parentNode;

    private String title;

    private float hours;

    //private List<ProjectActivityObject> childrenNodes;

    private ObjectId id;

    /**
     * Initialize
     */
    public ProjectActivityObject(){

    }


    public ProjectActivityObject(final String title)
    {
        this.title = title;
        //this.childrenNodes = new ArrayList<>();

    }

    /**
     * Get ID
     * @return
     */

    public ObjectId getId() {
        return id;
    }

    /**
     * Get title
     * @return string title
     */
    public String getTitle()
    {
        return this.title;
    }

    /**
     * Get Hours
     * @return float hours
     */
    public float getHours() {
        return hours;
    }


    /**
     * Get Children Nodes
     * @return List children
     */
//    public List<ProjectActivityObject> getChildrenNodes() {
//        return childrenNodes;
//    }

    /**
     * Get parent node
     * @return this.parentNode
     */
    public ProjectActivityObject getParentNode() {
        return parentNode;
    }

    /**
     * Set ID
     * @param id
     */
    public void setId(ObjectId id) {
        this.id = id;
    }

    /**
     * Set title
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Set hours
     * @param hours
     */
    public void setHours(float hours) {
        this.hours = hours;
    }

    /**
     * Set parent node
     * @param parentNode
     */
    public void setParentNode(ProjectActivityObject parentNode) {
        this.parentNode = parentNode;
        //this.parentNode.childrenNodes.add(this);
    }

    /**
     * Set children nodes
     * @param
     */
//    public void setChildrenNodes(List<ProjectActivityObject> childrenNodes){
//        this.childrenNodes = childrenNodes;
//    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        ProjectActivityObject projectActivityObject = (ProjectActivityObject) obj;

        if (!getTitle().equals(projectActivityObject.getTitle())) {
            return false;
        }
        if (getId() != null ? !getId().equals(projectActivityObject.getId()) : projectActivityObject.getId() != null) {
            return false;
        }
        if (getParentNode() != null ? !getParentNode().equals(projectActivityObject.getParentNode()) : projectActivityObject.getParentNode() != null) {
            return false;
        }
//        if (getChildrenNodes() != null ? !getChildrenNodes().equals(projectActivityObject.getChildrenNodes()) : projectActivityObject.getChildrenNodes() != null) {
//            return false;
//        }

        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return this.title;
    }

}
