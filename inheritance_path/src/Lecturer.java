public class Lecturer extends Person {
    private String department;
    private double salary;

    public Lecturer(String surname, String name, int age, String department, double salary) {
        super(surname, name, age);
        this.department = department;
        this.salary = salary;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        String salaryStr = String.format("%.2f", salary);
        return "Викладач кафедри " + department + ", " + getSurname() + " " + getName() + ", вік: " + getAge()
                + ". Зарплата: " + salaryStr;
    }
}
