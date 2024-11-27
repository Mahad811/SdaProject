package com.example.sda_project.model;


import java.util.ArrayList;
import java.util.List;

public class Prison {
    // Public attributes
    private int id;
    private String name;
    private String location;
    private List<Guard> guards;
    private List<Inmate> inmates;


    // Constructor
    public Prison(int id, String name, String location, List<Guard> guards, List<Inmate> inmates) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.inmates = new ArrayList<>();
        this.guards = new ArrayList<>();
        for(Inmate i: inmates)
        {
            if (i.getPrisonID() == this.id)
                this.inmates.add(i);
        }
        for(Guard g: guards)
        {
            if (g.getPrisonid() == this.id)
                this.guards.add(g);
        }

    }
    public String getName()
    {
        return name;
    }
    // toString method
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Prison{")
                .append("id=").append(id)
                .append(", name='").append(name).append('\'')
                .append(", location='").append(location).append('\'');

        // Adding information about guards
        if (guards != null && !guards.isEmpty()) {
            sb.append(", guards=[");
            for (Guard guard : guards) {
                sb.append(guard.toString()).append(", ");
            }
            sb.setLength(sb.length() - 2); // Remove trailing comma and space
            sb.append(']');
        } else {
            sb.append(", guards=[]");
        }

        // Adding information about inmates
        if (inmates != null && !inmates.isEmpty()) {
            sb.append(", inmates=[");
            for (Inmate inmate : inmates) {
                sb.append(inmate.toString()).append(", ");
            }
            sb.setLength(sb.length() - 2); // Remove trailing comma and space
            sb.append(']');
        } else {
            sb.append(", inmates=[]");
        }

        sb.append('}');
        return sb.toString();
    }

    public void set_sentence(int inmateid, Sentence sent)
    {
         for(Inmate i : inmates){
            if (i.getId() == inmateid) {
                i.setSentence(sent);
            }
        }

    }
    public void set_behavior(int inmateid,Behavior behav)
    {
        for(Inmate i : inmates){
            if (i.getId() == inmateid) {
                i.addBehavior(behav);
            }
        }

    }

    public int getPrisonID() {
        return id;
    }
    // Getter and Setter for Guards
    public List<Guard> getGuards() {
        return guards;
    }

    public void setGuards(List<Guard> guards) {
        this.guards = guards;
    }

    public void addGuard(Guard guard) {
        if (this.guards == null) {
            this.guards = new ArrayList<>();
        }
        this.guards.add(guard);
    }

    // Getter and Setter for Inmates
    public List<Inmate> getInmates() {
        return inmates;
    }

    public void setInmates(List<Inmate> inmates) {
        this.inmates = inmates;
    }

    public void addInmate(Inmate inmate) {
        if (this.inmates == null) {
            this.inmates = new ArrayList<>();
        }
        this.inmates.add(inmate);
    }
    public void removeGuard(Guard g)
    {
        this.guards.remove(g);
    }
}
