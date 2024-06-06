package studentmanagementsystem;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Класс для установления соединения с базой данных.
 */
public class database {

    /**
     * Метод для установления соединения с базой данных.
     * @return Объект Connection для работы с базой данных
     */
    public static Connection connectDb(){

        try{
            // Загрузка драйвера JDBC
            Class.forName("com.mysql.jdbc.Driver");

            // Установление соединения с базой данных "studentdata" на локальном хосте, используя имя пользователя "root" и пустой пароль
            return DriverManager.getConnection("jdbc:mysql://localhost/studentdata", "root", "");
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
