package com.avis.reporting.dataObjects;

public class TCLevelInfoDTO {

    int tcStatus;
    String methodName = null;
    Boolean isProductionBug = false;
    String packageName = null;
    Object[] parameters;
    String className = null;
    Throwable exception = null;
    String description = null;
    String tcReportingCategory = null;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TCLevelInfoDTO withDescription(String description){
        this.description = description;
        return this;
    }

    public String getTcReportingCategory() {
        return tcReportingCategory;
    }

    public void setTcReportingCategory(String tcReportingCategory) {
        this.tcReportingCategory = tcReportingCategory;
    }

    public TCLevelInfoDTO withTcReportingCategory(String tcReportingCategory){
        this.tcReportingCategory = tcReportingCategory;
        return this;
    }

    public int getTcStatus() {
        return tcStatus;
    }

    public void setTcStatus(int tcStatus) {
        this.tcStatus = tcStatus;
    }

    public TCLevelInfoDTO withTcStatus(int tcStatus){
        this.tcStatus = tcStatus;
        return this;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public TCLevelInfoDTO withMethodName(String methodName){
        this.methodName = methodName;
        return this;
    }

    public Boolean getProductionBug() {
        return isProductionBug;
    }

    public void setProductionBug(Boolean productionBug) {
        isProductionBug = productionBug;
    }

    public TCLevelInfoDTO withProductionBug(Boolean productionBug){
        this.isProductionBug = productionBug;
        return this;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public TCLevelInfoDTO withPackageName(String packageName){
        this.packageName = packageName;
        return this;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public TCLevelInfoDTO withParameters(Object[] parameters){
        this.parameters = parameters;
        return this;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public TCLevelInfoDTO withClassName(String className){
        this.className = className;
        return this;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public TCLevelInfoDTO withException(Throwable exception){
        this.exception = exception;
        return this;
    }
}
