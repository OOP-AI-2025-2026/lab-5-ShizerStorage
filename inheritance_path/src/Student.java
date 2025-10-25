public class Student extends Person {
    private String group;
    private String studentId;

    public Student(String surname, String name, int age, String group, String studentId) {
        super(surname, name, age);
        this.group = group;
        this.studentId = studentId;
    }

    // Гетери і сетери
    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @Override
    public String toString() {
        return "Студент групи " + group + ", " + getSurname() + " " + getName() + ", вік: " + getAge()
                + ". Номер студентського квитка: " + studentId;
    }
}
