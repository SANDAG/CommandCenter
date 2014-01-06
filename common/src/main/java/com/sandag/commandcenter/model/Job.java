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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "job")
public class Job
{

    public enum Status
    {
        // WARNING do not change this order - values are stored in the database using this order
        //   add additional statuses to the end of the list
        QUEUED, RUNNING, COMPLETE, ARCHIVED, DELETED;
    }

    public enum Model
    {
        // WARNING do not change this order - values are stored in the database using this order
        ABM, PECAS;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Status status = Status.QUEUED;

    // default to QUEUED (Status enum position 0)
    @Column(name = "status", columnDefinition = "INT DEFAULT 0", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    public Status getStatus()
    {
        return status;
    }

    private Model model;

    @NotNull
    @Column(name = "model")
    @Enumerated(EnumType.ORDINAL)
    public Model getModel()
    {
        return model;
    }

    @NotEmpty
    @Column(name = "scenario")
    private String scenario;

    @NotEmpty
    @Column(name = "study")
    private String study;

    @NotEmpty
    @Column(name = "scenario_location")
    private String scenarioLocation;

    @NotNull
    @Min(1000) @Max(9999)
    @Column(name = "scenario_start_year")
    private Integer scenarioStartYear;

    @NotNull
    @Min(1000) @Max(9999)
    @Column(name = "scenario_end_year")
    private Integer scenarioEndYear;

    @NotEmpty
    @Column(name = "scheduling_information")
    private String schedulingInformation;

    @Column(name = "queue_position")
    @GeneratedValue
    private int queuePosition;
    
    @Column(name = "runner")
    private String runner;
    
    
    // simple getters/setters below here

    public int getQueuePosition()
    {
        return queuePosition;
    }

    public void setQueuePosition(int queuePosition)
    {
        this.queuePosition = queuePosition;
    }

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
    }

    public String getRunner()
    {
        return runner;
    }

    public void setRunner(String runner)
    {
        this.runner = runner;
    };

}
