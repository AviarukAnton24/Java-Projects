package studentmanagementsystem;

/**
 * Класс для хранения данных о курсе.
 */
public class courseData {

    private final String course; // Название курса
    private final String description; // Описание курса
    private final String degree; // Степень курса

    /**
     * Конструктор класса courseData.
     * @param course Название курса
     * @param description Описание курса
     * @param degree Степень курса
     */
    public courseData(String course, String description, String degree){
        this.course = course;
        this.description = description;
        this.degree = degree;
    }

    /**
     * Метод для получения названия курса.
     * @return Название курса
     */
    public String getCourse(){
        return course;
    }

    /**
     * Метод для получения описания курса.
     * @return Описание курса
     */
    public String getDescription(){
        return description;
    }

    /**
     * Метод для получения степени курса.
     * @return Степень курса
     */
    public String getDegree(){
        return degree;
    }

}