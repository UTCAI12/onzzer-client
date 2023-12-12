package fr.utc.onzzer.client.data; // Assurez-vous que le fichier de test est dans le même package ou un package similaire

import fr.utc.onzzer.client.data.impl.DataRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
    private User user;


    @BeforeEach
    void setUp() {
        // Initialisez votre interface avant chaque test si nécessaire
        // Par exemple, vous pouvez initialiser l'implémentation de cette interface si elle est déjà implémentée
        DataRepository dataRepository = new DataRepository();
        dataTrackServices = new DataTrackServicesImpl(dataRepository);
        dataUserServices = new DataUserServicesImpl(dataRepository);

        //Créer un utilisateur pour le test
        user = new User(UUID.randomUUID(), "usrname", "mail", "pasword");
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
        Track track = new Track(UUID.randomUUID(), "new Byte[]".getBytes() ,user.getId(), "artist", "album", false);
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
        Track track = new Track(UUID.randomUUID(),"dfqzd".getBytes() , UUID.randomUUID(), "artist", "album", false);
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
    void testGetTracks(){
        // Créez une piste (Track) pour le test
        Track track = new Track(UUID.randomUUID(), "dfqzd".getBytes(), UUID.randomUUID(), "artist", "album", false);
        try {
            // Appelez la méthode saveTrack avec la piste créée
            dataTrackServices.saveTrack(track);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la sauvegarde de la piste : " + e.getMessage());
        }
        ArrayList<Track> tracks = dataTrackServices.getTracks();
        System.out.println(tracks);
        try {
            //Si le son est present dans la liste des tracks alors assert = true
            boolean assert1 = false;
            for (Track track1 : tracks) {
                if (track1.getId().equals(track.getId())) {
                    assert1 = true;
                }
            }
            Assertions.assertTrue(assert1);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la récupération de la piste : " + e.getMessage());
        }

    }
    @Test
    void testGetMyTrackLites(){
        // Créez une piste (Track) pour le test
        Track track = new Track(UUID.randomUUID(), "dfqzd".getBytes(), UUID.randomUUID(), "artist", "album", false);
        try {
            // Appelez la méthode saveTrack avec la piste créée
            dataTrackServices.saveTrack(track);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la sauvegarde de la piste : " + e.getMessage());
        }
        ArrayList<TrackLite> tracks = dataTrackServices.getMyTrackLites();
        System.out.println(tracks);
        try {
            //Si le son est present dans la liste des tracks alors assert = true
            boolean assert1 = false;
            for (TrackLite track1 : tracks) {
                if (track1.getId().equals(track.getId())) {
                    assert1 = true;
                }
            }
            Assertions.assertTrue(assert1);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la récupération de la piste : " + e.getMessage());
        }
    }
    //Test getOtherTracklites
    @Test
    void testGetOtherTrackLites(){
        //récupérer l'utilisateur
        User user;
        try {
            user = dataUserServices.getUser();
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la récupération de l'utilisateur : " + e.getMessage());
            return;
        }
        //Ajouter l'utilisateur à ceux connecter
        try{
            dataUserServices.addUser(user.toUserLite());
        } catch (Exception e) {
            Assertions.fail("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
            return;
        }
        Track track = new Track(UUID.randomUUID(), "dfqzd".getBytes(), user.getId(), "artist", "album", false);
        try {
            dataTrackServices.publishedTrack(track.toTrackLite());
            // Appelez la méthode saveTrack avec la piste créée
            dataTrackServices.addTrackToLibrary(track.getId());
            dataTrackServices.updateTrack(track);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la sauvegarde de la piste : " + e.getMessage());
        }
        ArrayList<TrackLite> tracks = dataTrackServices.getOtherTrackLites();
        System.out.println(tracks);
        try {
            //Si le son est present dans la liste des tracks alors assert = true
            boolean assert1 = false;
            for (TrackLite track1 : tracks) {
                if (track1.getId().equals(track.getId())) {
                    assert1 = true;
                }
            }
            Assertions.assertTrue(assert1);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la récupération de la piste : " + e.getMessage());
        }
    }

    @Test
    void testGetTrackLites() {
        // Créez une piste (Track) pour le test
        Track track = new Track(UUID.randomUUID(), "bfzuqfz".getBytes(),UUID.randomUUID(), "artist", "album", true);
        try {
            // Appelez la méthode saveTrack avec la piste créée
            dataTrackServices.saveTrack(track);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la sauvegarde de la piste : " + e.getMessage());
        }
        System.out.println(track.getId());
        ArrayList<TrackLite> traclites = dataTrackServices.getTrackLites();
        System.out.println(traclites);
        try {
            //Si le son est present dans la liste des traclites alors assert = true
            boolean assert1 = false;
            for (TrackLite traclite : traclites) {
                System.out.println(traclite.getId());
                if (traclite.getId().equals(track.getId())) {
                    assert1 = true;
                }
            }
            Assertions.assertTrue(assert1);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la récupération de la piste : " + e.getMessage());
        }
    }

    @Test
    void testUpdateTrack() {
        // Créez une piste (Track) pour le test
        Track track = new Track(UUID.randomUUID(),"QZFFZQF".getBytes(), UUID.randomUUID(), "artist", "album", false);
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
        Track track3 = new Track(track.getId(), "QDZDZ".getBytes(),UUID.randomUUID(), "artist2", "album2", false);
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
        Track track = new Track(UUID.randomUUID(),"FQQSFES".getBytes(), UUID.randomUUID(), "artist", "album", false);
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
        Track track = new Track(UUID.randomUUID(),"SFUGFEU".getBytes(), UUID.randomUUID(), "artist", "album", true);
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
        Track track = new Track(UUID.randomUUID(),"adQFE".getBytes(), UUID.randomUUID(), "artist", "album", true);
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
        Track track = new Track(UUID.randomUUID(), "dfqzd".getBytes(), UUID.randomUUID(), "artist", "album", true);
        //sauvegarder la track
        try {
            dataTrackServices.saveTrack(track);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la sauvegarde de la piste : " + e.getMessage());
        }
        //publish la track
        try {
            dataTrackServices.publishTrack(track);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la publication de la piste : " + e.getMessage());
        }
        try {
            //assert false
            Assertions.assertFalse(dataTrackServices.getTrack(track.getId()).getPrivateTrack());
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la récupération de la piste : " + e.getMessage());
        }
    }

    @Test
    public void testUnpublishTrack() {
        Track track = new Track(UUID.randomUUID(), "dfqzd".getBytes(), UUID.randomUUID(), "artist", "album", false);
        //sauvegarder la track
        try {
            dataTrackServices.saveTrack(track);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la sauvegarde de la piste : " + e.getMessage());
        }
        //publish la track
        try {
            dataTrackServices.unpublishTrack(track);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la dépublication de la piste : " + e.getMessage());
        }
        try {
            //assert true
            Assertions.assertTrue(dataTrackServices.getTrack(track.getId()).getPrivateTrack());
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la récupération de la piste : " + e.getMessage());
        }
    }

    @Test
    public void testPublishedTrack(){

        //Ajouter un utilisateur pour le test
        User userDistant = new User(UUID.randomUUID(), "usrname", "mail", "pasword");
        // Créez une piste (Track) pour le test
        Track track = new Track(UUID.randomUUID(), "du texte en musique".getBytes(), userDistant.getId(), "artist", "album", false);
        //Ajouter l'utilisateur Distant
        try{
            dataUserServices.addUser(userDistant.toUserLite());
        } catch (Exception e) {
            Assertions.fail("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
            return;
        }

        //Ajouter la track à la hashmap via publishedTrack
        try {
            dataTrackServices.publishedTrack(track.toTrackLite());
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la publication de la piste : " + e.getMessage());
        }
        //Récupérer la hashmap via getConnectedUsers
        HashMap<UserLite, List<TrackLite>> connectedUsers = dataUserServices.getConnectedUsers();
        //Comparer la hashmap avec la track
        try {
            //Si le son est present dans la liste des tracks alors assert = true
            boolean assert1 = false;
            for (TrackLite track1 : connectedUsers.get(userDistant.toUserLite())) {
                if (track1.getId().equals(track.getId())) {
                    assert1 = true;
                }
            }
            Assertions.assertTrue(assert1);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la récupération de la piste : " + e.getMessage());
        }
    }
    @Test
    public void testUnpublishedTrack() {

        //Ajouter un utilisateur pour le test
        User userDistant = new User(UUID.randomUUID(), "usrname", "mail", "pasword");
        // Créez une piste (Track) pour le test
        Track track = new Track(UUID.randomUUID(), "du texte en musique".getBytes(), userDistant.getId(), "artist", "album", false);
        //Ajouter l'utilisateur Distant
        try{
            dataUserServices.addUser(userDistant.toUserLite());
        } catch (Exception e) {
            Assertions.fail("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
            return;
        }

        //Ajouter la track à la hashmap via publishedTrack
        try {
            dataTrackServices.publishedTrack(track.toTrackLite());
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la publication de la piste : " + e.getMessage());
        }
        //Récupérer la hashmap via getConnectedUsers
        HashMap<UserLite, List<TrackLite>> connectedUsers = dataUserServices.getConnectedUsers();
        //Comparer la hashmap avec la track
        try {
            //Si le son est present dans la liste des tracks alors assert = true
            boolean assert1 = false;
            for (TrackLite track1 : connectedUsers.get(userDistant.toUserLite())) {
                if (track1.getId().equals(track.getId())) {
                    assert1 = true;
                }
            }
            Assertions.assertTrue(assert1);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la récupération de la piste : " + e.getMessage());
        }

        //Dépublier la track
        try {
            dataTrackServices.unpublishedTrack(track.toTrackLite());
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la dépublication de la piste : " + e.getMessage());
        }
        //Récupérer la hashmap via getConnectedUsers
        HashMap<UserLite, List<TrackLite>> connectedUsers2 = dataUserServices.getConnectedUsers();
        //Comparer la hashmap avec la track
        try {
            //Si le son est present dans la liste des tracks alors assert = false
            boolean assert1 = false;
            for (TrackLite track1 : connectedUsers2.get(userDistant.toUserLite())) {
                if (track1.getId().equals(track.getId())) {
                    assert1 = true;
                }
            }
            Assertions.assertFalse(assert1);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la récupération de la piste : " + e.getMessage());
        }
    }


}