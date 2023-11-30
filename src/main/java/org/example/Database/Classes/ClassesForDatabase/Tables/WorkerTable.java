package org.example.Database.Classes.ClassesForDatabase.Tables;

public class WorkerTable {
    private int idWorkers;
    private String FIO;
    private String address;
    private String workerPosition;
    private Double salary;
    private Double rating;
    private String phone;
    private int experience;

    public WorkerTable(int idWorkers, String FIO, String address, String workerPosition, double salary, double rating, String phone, int experience) {
        this.idWorkers = idWorkers;
        this.FIO = FIO;
        this.address = address;
        this.workerPosition = workerPosition;
        this.salary = salary;
        this.rating = rating;
        this.phone = phone;
        this.experience = experience;
    }

    public int getIdWorkers() {
        return this.idWorkers;
    }

    public void setIdWorkers(int idWorkers) {
        this.idWorkers = idWorkers;
    }

    public String getFIO() {
        return this.FIO;
    }

    public void setFIO(String FIO) {
        this.FIO = FIO;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWorkerPosition() {
        return this.workerPosition;
    }

    public void setWorkerPosition(String workerPosition) {
        this.workerPosition = workerPosition;
    }

    public Double getSalary() {
        return this.salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Double getRating() {
        return this.rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getExperience() {
        return this.experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }
}
