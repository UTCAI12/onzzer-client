package fr.utc.onzzer.client.data; // Assurez-vous que le fichier de test est dans le même package ou un package similaire

import fr.utc.onzzer.client.data.impl.DataRepository;
import fr.utc.onzzer.common.dataclass.UserLite;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
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
    // Test d'updateUser
    @Test
    void testUpdateUser() {
        // Création d'un nouvel utilisateur pour le test
        User user = new User(UUID.randomUUID(), "name", "mail", "password");

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
            // Appel de la méthode updateUser avec l'utilisateur créé
            user.setPassword("newPassword");
            dataUserServices.updateUser(user);
            Assertions.assertEquals(user, dataUserServices.getUser());
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la mise à jour du profil : " + e.getMessage());
        }
    }

    @Test
    void testDeleteAllUsers() {
        // Création d'un nouvel utilisateur pour le test
        User user1 = new User(UUID.randomUUID(), "username", "mail", "password");
        User user2 = new User(UUID.randomUUID(), "username2", "mail@testeur_parfait.com", "passpassword");
        try {
            // Appel de la méthode createProfile avec l'utilisateur créé
            dataUserServices.createProfile(user1);
            dataUserServices.createProfile(user2);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la création du profil : " + e.getMessage());
        }

        try {
            dataUserServices.checkCredentials(user1.getUsername(), user1.getPassword());
        }catch (Exception e) {
            Assertions.fail("Erreur lors de la vérification des identifiants : " + e.getMessage());
        }

        try {
            // Appel de la méthode deleteUser avec l'utilisateur créé
            dataUserServices.deleteAllUsers();
            Assertions.assertNull(dataUserServices.getUser());
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la suppression du profil : " + e.getMessage());
        }
    }

    @Test
    void testImportProfile() {
        try {
            // Appel de la méthode importProfile avec l'utilisateur créé
            User userImported = dataUserServices.importProfile("/home/malo/Documents/projets/ai12/" + "f5cf4e36-6717-4150-b9c5-4120ca8042bc"  + ".ser");
        } catch (Exception e) {
            Assertions.fail("Erreur lors de l'import du profil : " + e.getMessage());
        }
    }

    @Test
    void testExportUser(){
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
            // Appel de la méthode exportProfile avec l'utilisateur créé
            dataUserServices.exportProfile(user, "/home/malo/Documents/" + user.getId() + ".ser");
        } catch (Exception e) {
            Assertions.fail("Erreur lors de l'export du profil : " + e.getMessage());
        }
    }

    // Test de la méthode getConnectedUsers
    @Test
    void testGetConnectedUsers(){
        // Création d'un nouvel utilisateur pour le test
        User user1 = new User(UUID.randomUUID(), "username", "mail", "password");
        User user2 = new User(UUID.randomUUID(), "username2", "mail@testeur_parfait.com", "passpassword");
        try {
            // Appel de la méthode createProfile avec l'utilisateur créé
            dataUserServices.createProfile(user1);
            dataUserServices.createProfile(user2);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la création du profil : " + e.getMessage());
        }

        try {
            dataUserServices.checkCredentials(user1.getUsername(), user1.getPassword());
            dataUserServices.checkCredentials(user2.getUsername(), user2.getPassword());
        }catch (Exception e) {
            Assertions.fail("Erreur lors de la vérification des identifiants : " + e.getMessage());
        }

        try {
            HashMap<UserLite, List<TrackLite>> ConnectedUser = dataUserServices.getConnectedUsers();
            System.out.println(ConnectedUser);
            Assertions.assertEquals(2, ConnectedUser.size());

        } catch (Exception e) {
            Assertions.fail("Erreur lors de la récupération des utilisateurs connectés : " + e.getMessage());
        }
        try {
            dataUserServices.deleteAllUsers();
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la suppression des utilisateurs : " + e.getMessage());
        }
    }

    @Test
    //test de la fonction logout
    void testLogOut(){
        // Création d'un nouvel utilisateur pour le test
        User user1 = new User(UUID.randomUUID(), "username", "mail", "password");
        User user2 = new User(UUID.randomUUID(), "username2", "mail@testeur_parfait.com", "passpassword");
        try {
            // Appel de la méthode createProfile avec l'utilisateur créé
            dataUserServices.createProfile(user1);
            dataUserServices.createProfile(user2);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la création du profil : " + e.getMessage());
        }

        try {
            dataUserServices.checkCredentials(user1.getUsername(), user1.getPassword());
            dataUserServices.checkCredentials(user2.getUsername(), user2.getPassword());
        }catch (Exception e) {
            Assertions.fail("Erreur lors de la vérification des identifiants : " + e.getMessage());
        }

        try {
            dataUserServices.logOut();
            Assertions.assertNull(dataUserServices.getUser());
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la déconnexion : " + e.getMessage());
        }
    }

}

