/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JoshuaDuPlessis;
import com.github.javafaker.Faker;
import java.util.Date;
import java.util.Random;



/**
 *
 * @author Josh
 */
public class DataGenerator {
    private Faker faker;
    private Random random;

    public DataGenerator() {
        faker = new Faker();
        random = new Random();
    }

    public Account generateFakeAccount() {
        Account account = new Account("username", "password", new Date(), 25, true);

        account.setUsername(faker.name().firstName() + faker.name().lastName());
        account.setPassword(faker.internet().password());
       
        account.setAge(random.nextInt(100) + 1); // Generate a random age between 1 and 100
 int randomNumber = faker.random().nextInt(1, 11);

    // Set the teacher flag based on the random number
    account.setTeacher(randomNumber <= 1);
        // Set other account properties using faker methods
        return account;
    }
}