package fr.utc.onzzer.client.data.impl;

import fr.utc.onzzer.client.data.DataUserServices;
import fr.utc.onzzer.common.dataclass.ModelUpdateTypes;
import fr.utc.onzzer.common.dataclass.TrackLite;
import fr.utc.onzzer.common.dataclass.Track;
import fr.utc.onzzer.common.dataclass.User;
import fr.utc.onzzer.common.dataclass.UserLite;
import fr.utc.onzzer.common.services.Listenable;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DataUserServicesImpl extends Listenable implements DataUserServices, Serializable {

    private final DataRepository dataRepository;

    public DataUserServicesImpl(final DataRepository dataRepository) {
        this.dataRepository = dataRepository;

        //Vérifier que le dossier data/profiles existe, sinon le créer
        String profilesDirectory = "data";
        File directory = new File(profilesDirectory);
        if (!directory.exists()) {
            directory.mkdir();
        }
        profilesDirectory = "data/profiles";
        directory = new File(profilesDirectory);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }
    @Override
    public User importProfile(String filePath) throws Exception {
        // Fonction permettant de recuperer un profil utilisateur à partir d'un fichier, de l'ajouter au dosssier profiles et de retourner l'utilisateur correspondant
        User user = null;
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            Object obj = objectInputStream.readObject();
            if (obj instanceof User) {
                user = (User) obj;
                this.createProfile(user);
            }
        } catch (Exception ex) {
            throw new Exception("Erreur lors de l'import du profil : " + ex.getMessage());
        }

        return user;
    }
    @Override
    public void createProfile(User newClient) throws Exception {
        if (!(newClient instanceof Serializable)) {
            throw new IllegalArgumentException("User object must implement Serializable");
        }
        String profilesDirectory = "data/profiles";
        File directory = new File(profilesDirectory);

        // Si le dossier n'existe pas, créez-le
        if (!directory.exists()) {
            directory.mkdir();
        }
        String filePath = profilesDirectory + File.separator + newClient.getId() + ".ser";

        try (FileOutputStream fileOut = new FileOutputStream(filePath);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

            objectOut.writeObject(newClient); // Écriture de l'objet User sérialisé dans le fichier
        } catch (Exception ex) {
            throw new Exception("Erreur lors de la création du profil : " + ex.getMessage());
        }

    }
    @Override
    public Boolean checkCredentials(String user, String pw) throws Exception {
        String directoryPath = "data/profiles";
        File directory = new File(directoryPath);

        if (directory.isDirectory()) {
            File[] files = directory.listFiles(); // Liste des fichiers du répertoire

            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        // Manipulation de chaque fichier
                        try (FileInputStream fileInputStream = new FileInputStream(file)) {
                            ObjectInputStream objStream = new ObjectInputStream(fileInputStream);
                            Object obj = objStream.readObject();
                            if (obj instanceof User) {
                                User userFromFile = (User) obj;
                                if (userFromFile.getPassword().equals(pw) && userFromFile.getUsername().equals(user)) {
                                    this.dataRepository.user = userFromFile;
                                    this.dataRepository.connectedUsers.put(userFromFile.toUserLite(),null);
                                    //On lit les fichiers de tracks pour ajouter les tracks de l'utilisateur à la liste des tracks, si il existe pas on le créer
                                    String tracksDirectory = "data/tracks"+File.separator+userFromFile.getId();
                                    File directoryTracks = new File(tracksDirectory);
                                    if (!directoryTracks.exists()) {
                                        directoryTracks.mkdir();
                                    }
                                    if (directoryTracks.isDirectory()) {
                                        File[] filesTracks = directory.listFiles(new FilenameFilter() {
                                            public boolean accept(File dir, String name) {
                                                return name.toLowerCase().endsWith(".ser");
                                            }
                                        });
                                        if (filesTracks != null) {
                                            for (File fileTrack : filesTracks) {
                                                if (fileTrack.isFile()) {
                                                    // Manipulation de chaque fichier
                                                    try (FileInputStream fileInputStreamTrack = new FileInputStream(fileTrack)) {
                                                        ObjectInputStream objStreamTrack = new ObjectInputStream(fileInputStreamTrack);
                                                        Object objTrack = objStreamTrack.readObject();
                                                        if (objTrack instanceof Track) {
                                                            Track trackFromFile = (Track) objTrack;
                                                            this.dataRepository.tracks.add(trackFromFile);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void exportProfile(User user, String filePath) throws Exception {
        // Fonction permettant d'exporter un profil utilisateur dans un fichier
        if (!(user instanceof Serializable)) {
            throw new IllegalArgumentException("User object must implement Serializable");
        }
        try (FileOutputStream fileOut = new FileOutputStream(filePath);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

            objectOut.writeObject(user); // Écriture de l'objet User sérialisé dans le fichier
        } catch (Exception ex) {
            throw new Exception("Erreur lors de l'export du profil : " + ex.getMessage());
        }
    }
    @Override
    public User getUser() throws Exception {
        return this.dataRepository.getUser();
    }

    @Override
    public HashMap<UserLite, List<TrackLite>> getConnectedUsers() {
        return this.dataRepository.getConnectedUsers();
    }

    @Override
    public void addUser(UserLite userLite) throws Exception {
        this.dataRepository.getConnectedUsers().put(userLite,null);
        this.notify(userLite, UserLite.class, ModelUpdateTypes.NEW_USER);
    }

    @Override
    public void setConnectedUsers(List<UserLite> newConnectedUsers) {
        final HashMap<UserLite, List<TrackLite>> connectedUsers = this.dataRepository.getConnectedUsers();
        connectedUsers.clear();

        for (final UserLite userLite: newConnectedUsers) {
            connectedUsers.put(userLite, null);
        }

//        connectedUsers.put((UserLite) newConnectedUsers, null);

        // TODO change here, to be able to notify without data OR to notify with an array
        final UserLite u = new UserLite(UUID.randomUUID(), "");
        this.notify(u, UserLite.class, ModelUpdateTypes.NEW_USERS);
    }


    @Override
    public void deleteUser(UserLite userLite) throws Exception {
        this.dataRepository.getConnectedUsers().remove(userLite);
        // Si le fichier existe dans le dossier profiles alors on le supprime
        String profilesDirectory = "data/profiles";
        File directory = new File(profilesDirectory);
        if (directory.isDirectory()) {
            File[] files = directory.listFiles(); // Liste des fichiers du répertoire
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        // Manipulation de chaque fichier
                        if (file.getName().equals(userLite.getId() + ".ser")) {
                            file.delete();
                        }
                    }
                }
            }
        }
        //Si l'utilisateur supprimé est connecté, alors on le déconnecte
        if (this.dataRepository.getUser().getId().equals(userLite.getId())) {
            this.dataRepository.user = null;
        }
        this.notify(userLite, UserLite.class, ModelUpdateTypes.DELETE_USER);
    }
    @Override
    public void updateUser(User user) throws Exception {
        // Récupérer l'utilisateur à modifier dans les fichiers du dossier profiles
        String profilesDirectory = "data/profiles";
        File directory = new File(profilesDirectory);
        if (directory.isDirectory()) {
            File[] files = directory.listFiles(); // Liste des fichiers du répertoire
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        // Manipulation de chaque fichier
                        if (file.getName().equals(user.getId() + ".ser")) {
                            // Suppression de l'ancien fichier
                            file.delete();
                            // Création du nouveau fichier
                            this.createProfile(user);
                            this.dataRepository.user = user;
                        }
                    }
                }
            }
        }
        this.notify(user, User.class, ModelUpdateTypes.UPDATE_USER);

    }
    @Override
    public void deleteAllUsers() {
        this.dataRepository.getConnectedUsers().clear();
        String profilesDirectory = "data/profiles";
        File directory = new File(profilesDirectory);
        if (directory.isDirectory()) {
            File[] files = directory.listFiles(); // Liste des fichiers du répertoire
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        // Manipulation de chaque fichier
                        file.delete();
                        dataRepository.user = null;

                    }
                }
            }
        }

    }

    @Override
    public void logOut() {
        this.dataRepository.user = null;
        this.dataRepository.getConnectedUsers().clear();
        this.dataRepository.tracks.clear();

    }

    @Override
    public void removeUser(UserLite userLite) throws Exception {
        this.dataRepository.getConnectedUsers().remove(userLite);
    }

}
