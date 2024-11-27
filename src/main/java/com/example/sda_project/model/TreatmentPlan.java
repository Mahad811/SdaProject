package com.example.sda_project.model;


public class TreatmentPlan {

    // Public attributes
    private int counsellorId;
    private int inmateId;
    private String type;
    private String description;
    private boolean active;

    // Constructor
    public TreatmentPlan(int counsellorId, int inmateId, String type, String description) {
        this.counsellorId = counsellorId;
        this.inmateId = inmateId;
        this.type = type;
        this.description = description;
        this.active = true;
    }

    // Getters and setters (optional if you want to provide access to private fields)
    public int getCounsellorId() {
        return counsellorId;
    }

    public void setCounsellorId(int counsellorId) {
        this.counsellorId = counsellorId;
    }

    public int getInmateId() {
        return inmateId;
    }

    public void setInmateId(int inmateId) {
        this.inmateId = inmateId;
    }

    public String getType() {
        return type;
    }
    public Boolean getActive() {
        return active;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public void setActive(Boolean active) {
        this.active = active;
    }

    // toString method for easy representation
    @Override
    public String toString() {
        return "TreatmentPlan{" +
                "counsellorId=" + counsellorId +
                ", inmateId=" + inmateId +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", active='" + active + '\'' +
                '}';
    }
}
