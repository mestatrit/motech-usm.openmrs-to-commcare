package org.motechproject.commcare.domain;

import org.apache.commons.lang.ObjectUtils;

/**
 * Domain class that represents a series of case informations to be converted into case XML for upload to CommCareHQ.
 */
public class CaseTask {
    private CreateTask createTask;
    private UpdateTask updateTask;
    private IndexTask indexTask;
    private CloseTask closeTask;
    private String caseId;
    private String userId;
    private String dateModified;
    private String xmlns;

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CaseTask)) {
            return false;
        }
        CaseTask other = (CaseTask) obj;
        return ObjectUtils.equals(createTask, other.createTask) && ObjectUtils.equals(updateTask, other.updateTask)
                && ObjectUtils.equals(indexTask, other.indexTask) && ObjectUtils.equals(closeTask, other.closeTask)
                && ObjectUtils.equals(caseId, other.caseId) && ObjectUtils.equals(userId, other.userId)
                && ObjectUtils.equals(dateModified, other.dateModified) && ObjectUtils.equals(xmlns, other.xmlns);
    }
    
    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + ObjectUtils.hashCode(createTask);
        hash = hash * 31 + ObjectUtils.hashCode(updateTask);
        hash = hash * 31 + ObjectUtils.hashCode(indexTask);
        hash = hash * 31 + ObjectUtils.hashCode(closeTask);
        hash = hash * 31 + ObjectUtils.hashCode(caseId);
        hash = hash * 31 + ObjectUtils.hashCode(userId);
        hash = hash * 31 + ObjectUtils.hashCode(dateModified);
        hash = hash * 31 + ObjectUtils.hashCode(xmlns);
        
        return hash;
    }

    public CreateTask getCreateTask() {
        return this.createTask;
    }

    public void setCreateTask(CreateTask createTask) {
        this.createTask = createTask;
    }

    public UpdateTask getUpdateTask() {
        return this.updateTask;
    }

    public void setUpdateTask(UpdateTask updateTask) {
        this.updateTask = updateTask;
    }

    public IndexTask getIndexTask() {
        return this.indexTask;
    }

    public void setIndexTask(IndexTask indexTask) {
        this.indexTask = indexTask;
    }

    public CloseTask getCloseTask() {
        return this.closeTask;
    }

    public void setCloseTask(CloseTask closeTask) {
        this.closeTask = closeTask;
    }

    public String getCaseId() {
        return this.caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDateModified() {
        return this.dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public String getXmlns() {
        return this.xmlns;
    }

    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }
}
