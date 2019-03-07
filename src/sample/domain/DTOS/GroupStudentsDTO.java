package sample.domain.DTOS;

public class GroupStudentsDTO {

    private Integer number;
    private Integer group;

    public GroupStudentsDTO(Integer number, Integer group) {
        this.number = number;
        this.group = group;
    }

    public Integer getNumber() {
        return number;
    }

    public Integer getGroup() {
        return group;
    }

}
