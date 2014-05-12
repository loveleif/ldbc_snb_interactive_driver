package com.ldbc.driver.workloads.ldbc.socnet.interactive;

public class LdbcQuery11Result {
    public static final int DEFAULT_LIMIT = 10;
    private final long personId;
    private final String personFirstName;
    private final String personLastName;
    private final String organizationName;
    private final int organizationWorkFromYear;

    public LdbcQuery11Result(long personId, String personFirstName, String personLastName, String organizationName, int organizationWorkFromYear) {
        this.personId = personId;
        this.personFirstName = personFirstName;
        this.personLastName = personLastName;
        this.organizationName = organizationName;
        this.organizationWorkFromYear = organizationWorkFromYear;
    }

    public long personId() {
        return personId;
    }

    public String personFirstName() {
        return personFirstName;
    }

    public String personLastName() {
        return personLastName;
    }

    public String organizationName() {
        return organizationName;
    }

    public int organizationWorkFromYear() {
        return organizationWorkFromYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LdbcQuery11Result that = (LdbcQuery11Result) o;

        if (organizationWorkFromYear != that.organizationWorkFromYear) return false;
        if (personId != that.personId) return false;
        if (organizationName != null ? !organizationName.equals(that.organizationName) : that.organizationName != null)
            return false;
        if (personFirstName != null ? !personFirstName.equals(that.personFirstName) : that.personFirstName != null)
            return false;
        if (personLastName != null ? !personLastName.equals(that.personLastName) : that.personLastName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (personId ^ (personId >>> 32));
        result = 31 * result + (personFirstName != null ? personFirstName.hashCode() : 0);
        result = 31 * result + (personLastName != null ? personLastName.hashCode() : 0);
        result = 31 * result + (organizationName != null ? organizationName.hashCode() : 0);
        result = 31 * result + organizationWorkFromYear;
        return result;
    }

    @Override
    public String toString() {
        return "LdbcQuery11Result{" +
                "personId=" + personId +
                ", personFirstName='" + personFirstName + '\'' +
                ", personLastName='" + personLastName + '\'' +
                ", organizationName='" + organizationName + '\'' +
                ", organizationWorkFromYear=" + organizationWorkFromYear +
                '}';
    }
}