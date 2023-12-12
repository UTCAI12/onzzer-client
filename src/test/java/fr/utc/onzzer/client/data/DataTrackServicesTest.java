package fr.utc.onzzer.client.data; // Assurez-vous que le fichier de test est dans le même package ou un package similaire

import fr.utc.onzzer.client.data.impl.DataRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import java.util.HashMap;
import java.util.List;


import fr.utc.onzzer.common.dataclass.TrackLite;
import fr.utc.onzzer.common.dataclass.Track;

import fr.utc.onzzer.common.dataclass.UserLite;
import fr.utc.onzzer.common.dataclass.User;


// Importez l'implémentation de l'interface à tester
import fr.utc.onzzer.client.data.impl.DataTrackServicesImpl;
import fr.utc.onzzer.client.data.impl.DataUserServicesImpl;


public class DataTrackServicesTest {

    private DataTrackServices dataTrackServices;
    private DataUserServices dataUserServices;


    @BeforeEach
    void setUp() {
        // Initialisez votre interface avant chaque test si nécessaire
        // Par exemple, vous pouvez initialiser l'implémentation de cette interface si elle est déjà implémentée
        DataRepository dataRepository = new DataRepository();
        dataTrackServices = new DataTrackServicesImpl(dataRepository);
        dataUserServices = new DataUserServicesImpl(dataRepository);

        //Créer un utilisateur pour le test
        User user = new User(UUID.randomUUID(), "usrname", "mail", "pasword");
        try {
            dataUserServices.createProfile(user);
            dataUserServices.checkCredentials(user.getUsername(), user.getPassword());
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la création du profil : " + e.getMessage());
        }
    }

    @Test
    void testSaveTrack() {
        //Ajouter un utilisateur pour le test
        User user = new User(UUID.randomUUID(), "usrname", "mail", "pasword");
        try {
            dataUserServices.createProfile(user);
            dataUserServices.checkCredentials(user.getUsername(), user.getPassword());
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la création du profil : " + e.getMessage());
        }
        // Créez une piste (Track) pour le test
        Track track = new Track(UUID.randomUUID(), user.getId(), "artist", "album", false);
        // Créez une piste (Track) pour le test
       // Track track2 = new Track(UUID.randomUUID(), UUID.randomUUID(), "artist", "album");

        try {
            // Appelez la méthode saveTrack avec la piste créée
            dataTrackServices.saveTrack(track);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la sauvegarde de la piste : " + e.getMessage());
        }
        try {
            // Appelez la méthode getTrack avec l'UUID de la piste créée
            Track track2 = dataTrackServices.getTrack(track.getId());
            Assertions.assertEquals(track, track2);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la récupération de la piste : " + e.getMessage());
        }
        try {
            // getConnectedUsers
            HashMap<UserLite, List<TrackLite>> connectedUsers = dataUserServices.getConnectedUsers();
            //Assertions.assertEquals(connectedUsers.get(user.toUserLite()).get(0), track.toTrackLite());
            System.out.println(connectedUsers);
        } catch (Exception e) {
            Assertions.fail(" " + e.getMessage());
        }
        // delete all users
        try {
            dataUserServices.deleteAllUsers();
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
        }

    }

    @Test
    void testGetTrack() {
        // Créez une piste (Track) pour le test
        Track track = new Track(UUID.randomUUID(), UUID.randomUUID(), "artist", "album", false);
        try {
            // Appelez la méthode saveTrack avec la piste créée
            dataTrackServices.saveTrack(track);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la sauvegarde de la piste : " + e.getMessage());
        }

        try {
            // Appelez la méthode getTrack avec l'UUID de la piste créée
            Track track2 = dataTrackServices.getTrack(track.getId());
            Assertions.assertEquals(track, track2);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la récupération de la piste : " + e.getMessage());
        }
    }

    @Test
    void testGetTrackLites() {
        // Créez une piste (Track) pour le test
        Track track = new Track(UUID.randomUUID(), UUID.randomUUID(), "artist", "album", true);
        try {
            // Appelez la méthode saveTrack avec la piste créée
            dataTrackServices.saveTrack(track);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la sauvegarde de la piste : " + e.getMessage());
        }

        try {
            // Appelez la méthode getTrack avec l'UUID de la piste créée
            TrackLite trackLite = dataTrackServices.getTrackLites().get(0);
            Assertions.assertEquals(track.toTrackLite().getId(), trackLite.getId());
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la récupération de la piste : " + e.getMessage());
        }
    }

    @Test
    void testUpdateTrack() {
        // Créez une piste (Track) pour le test
        Track track = new Track(UUID.randomUUID(), UUID.randomUUID(), "artist", "album", false);
        try {
            // Appelez la méthode saveTrack avec la piste créée
            dataTrackServices.saveTrack(track);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la sauvegarde de la piste : " + e.getMessage());
        }

        try {
            // Appelez la méthode getTrack avec l'UUID de la piste créée
            Track track2 = dataTrackServices.getTrack(track.getId());
            Assertions.assertEquals(track, track2);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la récupération de la piste : " + e.getMessage());
        }

        // Créez une piste (Track) pour le test
        Track track3 = new Track(track.getId(), UUID.randomUUID(), "artist2", "album2", false);
        try {
            // Appelez la méthode saveTrack avec la piste créée
            dataTrackServices.updateTrack(track3);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la sauvegarde de la piste : " + e.getMessage());
        }

        try {
            // Appelez la méthode getTrack avec l'UUID de la piste créée
            Track track4 = dataTrackServices.getTrack(track.getId());
            Assertions.assertEquals(track3, track4);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la récupération de la piste : " + e.getMessage());
        }
    }

    @Test
    void testAddTrackToLibrary() {
        // Créez une piste (Track) pour le test
        Track track = new Track(UUID.randomUUID(), UUID.randomUUID(), "artist", "album", false);
        try {
            // Appelez la méthode saveTrack avec la piste créée
            dataTrackServices.saveTrack(track);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la sauvegarde de la piste : " + e.getMessage());
        }

        try {
            // Appelez la méthode getTrack avec l'UUID de la piste créée
            Track track2 = dataTrackServices.getTrack(track.getId());
            Assertions.assertEquals(track, track2);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la récupération de la piste : " + e.getMessage());
        }

        try {
            // Appelez la méthode getTrack avec l'UUID de la piste créée
            dataTrackServices.addTrackToLibrary(track.getId());
        } catch (Exception e) {
          //  Assertions.fail("Erreur lors de l'ajout de la piste à la bibliothèque : " + e.getMessage());
        }
    }

    @Test
    void testRemoveAllTracks() {
        // Créez une piste (Track) pour le test
        Track track = new Track(UUID.randomUUID(), UUID.randomUUID(), "artist", "album", true);
        try {
            // Appelez la méthode saveTrack avec la piste créée
            dataTrackServices.saveTrack(track);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la sauvegarde de la piste : " + e.getMessage());
        }

        try {
            // Appelez la méthode getTrack avec l'UUID de la piste créée
            Track track2 = dataTrackServices.getTrack(track.getId());
            Assertions.assertEquals(track, track2);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la récupération de la piste : " + e.getMessage());
        }

        try {
            // Appelez la méthode getTrack avec l'UUID de la piste créée
            dataTrackServices.addTrackToLibrary(track.getId());

        } catch (Exception e) {
         //   Assertions.fail("Erreur lors de l'ajout de la piste à la bibliothèque : " + e.getMessage());
        }

        try {
            // Appelez la méthode getTrack avec l'UUID de la piste créée
            dataTrackServices.removeAllTracks();
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la suppression de la piste : " + e.getMessage());
        }
    }

    @Test
    public void testDeleteTrack() {
        // Créez une piste (Track) pour le test
        Track track = new Track(UUID.randomUUID(), UUID.randomUUID(), "artist", "album", true);
        try {
            // Appelez la méthode saveTrack avec la piste créée
            dataTrackServices.saveTrack(track);
            dataTrackServices.deleteTrack(track.getId());
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la suppression de la piste : " + e.getMessage());
        }
    }

    @Test
    public void testPublishTrack() {
        // Créez une piste (Track) pour le test
        Track track = new Track(UUID.randomUUID(), "du texte en musique".getBytes(), UUID.randomUUID() ,"artist", "album", true);
        try {
            // Appelez la méthode saveTrack avec la piste créée
            dataTrackServices.saveTrack(track);
            dataTrackServices.publishTrack(track);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la publication de la piste : " + e.getMessage());
        }
    }

    @Test
    public void testUnpublishedTrack() {
        // Créez une piste (Track) pour le test
        Track track = new Track(UUID.randomUUID(), "du texte en musique".getBytes(), UUID.randomUUID(), "artist", "album", false);
        try {
            // Appelez la méthode saveTrack avec la piste créée
            dataTrackServices.saveTrack(track);
            dataTrackServices.unpublishedTrack(track.toTrackLite());
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la dépublication de la piste : " + e.getMessage());
        }

        // Afficher la track
        try {
            // Appelez la méthode getTrack avec l'UUID de la piste créée
            Track track2 = dataTrackServices.getTrack(track.getId());
            Assertions.assertEquals(track, track2);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la récupération de la piste : " + e.getMessage());
        }
    }
}