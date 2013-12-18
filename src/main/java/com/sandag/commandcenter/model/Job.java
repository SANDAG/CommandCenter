package com.sandag.commandcenter.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;

@Entity
@Table(name = "job")
public class Job
{
  
    public enum Status
    {
        QUEUED, RUNNING, COMPLETE, ARCHIVED, DELETED;
    }

    public enum Model
    {
        ABM, PECAS;
    }
    
    @Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;    
    
    private Status status;
    
    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    public Status getStatus()
    {
        return status;
    }

    private Model model;
    
    @Column(name = "model")
    @Enumerated(EnumType.ORDINAL)
    public Model getModel()
    {
        return model;
    }
    
    @Column(name = "scenario")
    private String scenario;
    
    @Column(name = "study")
    private String study;
    
    @Column(name = "scenario_location")
    private String scenarioLocation;
    
    @Digits(integer = 4, fraction = 0)
    @Column(name = "scenario_start_year")
    private Integer scenarioStartYear;
    
    @Digits(integer = 4, fraction = 0)
    @Column(name = "scenario_end_year")
    private Integer scenarioEndYear;
    
    @Column(name = "scheduling_information")
    private String schedulingInformation;
    

    // simple getters/setters below here
    
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public String getScenario()
    {
        return scenario;
    }

    public void setScenario(String scenario)
    {
        this.scenario = scenario;
    }

    public String getStudy()
    {
        return study;
    }

    public void setStudy(String study)
    {
        this.study = study;
    }

    public String getScenarioLocation()
    {
        return scenarioLocation;
    }

    public void setScenarioLocation(String scenarioLocation)
    {
        this.scenarioLocation = scenarioLocation;
    }

    public Integer getScenarioStartYear()
    {
        return scenarioStartYear;
    }

    public void setScenarioStartYear(Integer scenarioStartYear)
    {
        this.scenarioStartYear = scenarioStartYear;
    }

    public Integer getScenarioEndYear()
    {
        return scenarioEndYear;
    }

    public void setScenarioEndYear(Integer scenarioEndYear)
    {
        this.scenarioEndYear = scenarioEndYear;
    }

    public String getSchedulingInformation()
    {
        return schedulingInformation;
    }

    public void setSchedulingInformation(String schedulingInformation)
    {
        this.schedulingInformation = schedulingInformation;
    }

    public void setModel(Model model)
    {
        this.model = model;
    };

}
