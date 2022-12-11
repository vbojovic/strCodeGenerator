package com.timanaga.streamCodeGenerator.helpers.databases.dbModels;

public class DatabaseSequence {
    private String name;
    private boolean cycle;
    private long increment;
    private long minvalue ;
    private long maxvalue ;
    private String DDL;
    private String comment;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCycle() {
        return cycle;
    }

    public void setCycle(boolean cycle) {
        this.cycle = cycle;
    }

    public long getIncrement() {
        return increment;
    }

    public void setIncrement(long increment) {
        this.increment = increment;
    }

    public long getMinvalue() {
        return minvalue;
    }

    public void setMinvalue(long minvalue) {
        this.minvalue = minvalue;
    }

    public long getMaxvalue() {
        return maxvalue;
    }

    public void setMaxvalue(long maxvalue) {
        this.maxvalue = maxvalue;
    }





    public String getDDL() {
        return DDL;
    }

    public void setDDL(String DDL) {
        this.DDL = DDL;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
