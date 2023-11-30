package fr.utc.onzzer.client.data; // Assurez-vous que le fichier de test est dans le même package ou un package similaire

import fr.utc.onzzer.client.data.impl.DataRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;

// importer les librairie de onzzer-common
import fr.utc.onzzer.common.dataclass.TrackLite;
import fr.utc.onzzer.common.dataclass.User;

//importer l'implémentaion de l'interface à tester
import fr.utc.onzzer.client.data.impl.DataUserServicesImpl;

public class DataUserServicesTest {

    private DataUserServices dataUserServices; // Votre interface à tester

    @BeforeEach
    void setUp() {
        // Initialisez votre interface avant chaque test si nécessaire
        // Par exemple, vous pouvez initialiser l'implémentation de cette interface si elle est déjà implémentée
        DataRepository dataRepository = new DataRepository();
        dataUserServices = new DataUserServicesImpl(dataRepository);
    }

    @Test
    void testCreateProfile() {
        // Création d'un nouvel utilisateur pour le test
        User user = new User(UUID.randomUUID(), "username", "mail", "password");

        try {
            // Appel de la méthode createProfile avec l'utilisateur créé
            dataUserServices.createProfile(user);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la création du profil : " + e.getMessage());
        }
    }
    @Test
    void testCheckCredentials() {
        // Création d'un nouvel utilisateur pour le test
        User user = new User(UUID.randomUUID(), "username", "mail", "password");

        try {
            // Appel de la méthode createProfile avec l'utilisateur créé
            dataUserServices.createProfile(user);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la création du profil : " + e.getMessage());
        }

        try {
            // Appel de la méthode checkCredentials avec l'utilisateur créé
            Boolean IsConnected = dataUserServices.checkCredentials(user.getUsername(), user.getPassword());
            Assertions.assertTrue(IsConnected);

        } catch (Exception e) {
            Assertions.fail("Erreur lors de la vérification des identifiants : " + e.getMessage());
        }
    }

    @Test
    void testGetUser() {
        // Création d'un nouvel utilisateur pour le test
        User user = new User(UUID.randomUUID(), "Malo", "mail", "passwrong");

        try {
            // Appel de la méthode createProfile avec l'utilisateur créé
            dataUserServices.createProfile(user);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la création du profil : " + e.getMessage());
        }
        try {
            dataUserServices.checkCredentials(user.getUsername(), user.getPassword());
        }catch (Exception e) {
            Assertions.fail("Erreur lors de la vérification des identifiants : " + e.getMessage());
        }

        try {
            // Appel de la méthode getUser avec l'utilisateur créé
            User userGet = dataUserServices.getUser();
            Assertions.assertEquals(user.getUsername(), userGet.getUsername());

        } catch (Exception e) {
            Assertions.fail("Erreur lors de la récupération de l'utilisateur : " + e.getMessage());
        }
    }

    @Test
    void testDeleteUser() {
        // Création d'un nouvel utilisateur pour le test
        User user = new User(UUID.randomUUID(), "username", "mail", "password");

        try {
            // Appel de la méthode createProfile avec l'utilisateur créé
            dataUserServices.createProfile(user);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la création du profil : " + e.getMessage());
        }

        try {
            dataUserServices.checkCredentials(user.getUsername(), user.getPassword());
        }catch (Exception e) {
            Assertions.fail("Erreur lors de la vérification des identifiants : " + e.getMessage());
        }

        try {
            // Appel de la méthode deleteUser avec l'utilisateur créé
            dataUserServices.deleteUser(user.toUserLite());
            Assertions.assertNull(dataUserServices.getUser());
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la suppression du profil : " + e.getMessage());
        }
    }

}

