package model;

public class Model {
    private Integer id;
    private String nama;
    private Integer age;
    private Integer balanced;
    private Integer previousBalanced;
    private Integer averageBalanced;
    private Integer freeTransfer;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getBalanced() {
        return balanced;
    }

    public void setBalanced(Integer balanced) {
        this.balanced = balanced;
    }

    public Integer getPreviousBalanced() {
        return previousBalanced;
    }

    public void setPreviousBalanced(Integer previousBalanced) {
        this.previousBalanced = previousBalanced;
    }

    public Integer getAverageBalanced() {
        return averageBalanced;
    }

    public void setAverageBalanced(Integer averageBalanced) {
        this.averageBalanced = averageBalanced;
    }

    public Integer getFreeTransfer() {
        return freeTransfer;
    }

    public void setFreeTransfer(Integer freeTransfer) {
        this.freeTransfer = freeTransfer;
    }
}
