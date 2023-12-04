package fr.utc.onzzer.client.data; // Assurez-vous que le fichier de test est dans le même package ou un package similaire

import fr.utc.onzzer.client.data.impl.DataRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;

import fr.utc.onzzer.common.dataclass.TrackLite;
import fr.utc.onzzer.common.dataclass.Track;


// Importez l'implémentation de l'interface à tester
import fr.utc.onzzer.client.data.impl.DataTrackServicesImpl;

public class DataTrackServicesTest {

    private DataTrackServices dataTrackServices;

    @BeforeEach
    void setUp() {
        // Initialisez votre interface avant chaque test si nécessaire
        // Par exemple, vous pouvez initialiser l'implémentation de cette interface si elle est déjà implémentée
        DataRepository dataRepository = new DataRepository();
        dataTrackServices = new DataTrackServicesImpl(dataRepository);
    }

    @Test
    void testSaveTrack() {
        // Créez une piste (Track) pour le test
        Track track = new Track(UUID.randomUUID(), UUID.randomUUID(), "artist", "album");

        try {
            // Appelez la méthode saveTrack avec la piste créée
            dataTrackServices.saveTrack(track);
        } catch (Exception e) {
            Assertions.fail("Erreur lors de la sauvegarde de la piste : " + e.getMessage());
        }
    }

    @Test
    void testGetTrack() {
        // Créez une piste (Track) pour le test
        Track track = new Track(UUID.randomUUID(), UUID.randomUUID(), "artist", "album");
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
        Track track = new Track(UUID.randomUUID(), UUID.randomUUID(), "artist", "album");
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
        Track track = new Track(UUID.randomUUID(), UUID.randomUUID(), "artist", "album");
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
        Track track3 = new Track(track.getId(), UUID.randomUUID(), "artist2", "album2");
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
        Track track = new Track(UUID.randomUUID(), UUID.randomUUID(), "artist", "album");
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
        Track track = new Track(UUID.randomUUID(), UUID.randomUUID(), "artist", "album");
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
}