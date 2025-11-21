import java.lang.annotation.*;
import java.lang.reflect.*;

//Аннотация
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Iterate {
    int value();
}

//Класс с различными методами
class MyClass {
    public void publicMethod1() {
        System.out.println("Public method 1");
    }

    protected void protectedMethod1() {
        System.out.println("Protected method 1");
    }

    @Iterate(3)
    protected void protectedMethod2(String msg) {
        System.out.println("Protected method 2: " + msg);
    }

    @Iterate(2)
    private void privateMethod1(int x) {
        System.out.println("Private method 1: " + x);
    }

    private void privateMethod2() {
        System.out.println("Private method 2");
    }
}

//Другой класс (демонстрация)
public class ReflectionDemo {
    public static void main(String[] args) throws Exception {
        MyClass obj = new MyClass();
        Class<?> cls = obj.getClass();

        for (Method m : cls.getDeclaredMethods()) { //пробегаем по всем методам класса
            if (m.isAnnotationPresent(Iterate.class)) { //отбираем с аннотациями
                Iterate itr = m.getAnnotation(Iterate.class);
                m.setAccessible(true); //доступ к private/protected методам

                System.out.println("\nВызов метода: " + m.getName());
                for (int i = 0; i < itr.value(); i++) {
                    if (m.getParameterCount() == 1) {
                        Class<?> param = m.getParameterTypes()[0];
                        if (param == String.class)
                            m.invoke(obj, "Hello"); //вызываем метод, передавая строку
                        else if (param == int.class)
                            m.invoke(obj, 123); //вызываем метод, передавая целое число
                    } else {
                        m.invoke(obj); //если параметров нет, просто вызываем метод
                    }
                }
            }
        }
    }
}
