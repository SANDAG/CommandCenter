package com.sandag.commandcenter.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "job")
public class Job extends BaseEntity
{

    public enum Status
    {
        QUEUED, RUNNING, CANCELLED, DELETED, FINISHED, ARCHIVED;
    }

    public enum ExitStatus
    {
        SUCCESS, FAILURE;
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
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private Status status = Status.QUEUED;

    @Column(name = "status", nullable = false)    
    public Status getStatus()
    {
        return status;
    }

    @Enumerated(EnumType.STRING)
    private ExitStatus exitStatus;

    @Column(name = "exit_status")    
    public ExitStatus getExitStatus()
    {
        return exitStatus;
    }

    @Enumerated(EnumType.STRING)
    private Model model;

    @NotNull
    @Column(name = "model")    
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

    @Column(name = "description")
    private String description;

    @Column(name = "queue_position")
    @GeneratedValue
    private int queuePosition;
    
    @Column(name = "runner")
    private String runner;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "started")
    private Date started;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "finished")
    private Date finished;
    
    @Column(name = "archived_notes")
    private String archivedNotes;

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

    public void setExitStatus(ExitStatus exitStatus)
    {
        this.exitStatus = exitStatus;
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

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
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
    }

    public Date getStarted()
    {
        return started;
    }

    public void setStarted(Date started)
    {
        this.started = started;
    }

    public Date getFinished()
    {
        return finished;
    }

    public void setFinished(Date finished)
    {
        this.finished = finished;
    }

    public String getArchivedNotes()
    {
        return archivedNotes;
    }

    public void setArchivedNotes(String archivedNotes)
    {
        this.archivedNotes = archivedNotes;
    };

}
