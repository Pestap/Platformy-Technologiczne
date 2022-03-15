public class Job {
    private int jobId;
    private int precision;
    public Job(int precision, int jobId) {
        this.precision = precision;
        this.jobId = jobId;
    }
    public int getJobId(){
        return jobId;
    }
    public int getPrecision(){
        return  precision;
    }
}
