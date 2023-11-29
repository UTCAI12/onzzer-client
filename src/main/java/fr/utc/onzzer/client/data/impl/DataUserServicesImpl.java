package fr.utc.onzzer.client.data.impl;

import fr.utc.onzzer.client.data.DataUserServices;
import fr.utc.onzzer.common.dataclass.TrackLite;
import fr.utc.onzzer.common.dataclass.User;
import fr.utc.onzzer.common.dataclass.UserLite;
import fr.utc.onzzer.common.services.Listenable;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DataUserServicesImpl extends Listenable implements DataUserServices, Serializable {

    private DataRepository dataRepository;

    public DataUserServicesImpl(final DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }
    @Override
    public User importProfile(String filePath) throws Exception {
        try (FileInputStream fileIn = new FileInputStream(filePath);
             ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {

            User importedUser = (User) objectIn.readObject();
            System.out.println("Profil de l'utilisateur importé avec succès depuis : " + filePath);
            return importedUser;

        } catch (Exception ex) {
            throw new Exception("Erreur lors de l'importation du profil : " + ex.getMessage());
        }
    }
    @Override
    public void createProfile(User newClient) throws Exception {
        if (!(newClient instanceof Serializable)) {
            throw new IllegalArgumentException("User object must implement Serializable");
        }
        String profilesDirectory = "profiles";
        File directory = new File(profilesDirectory);

        // Si le dossier n'existe pas, créez-le
        if (!directory.exists()) {
            directory.mkdir();
        }
        String filePath = profilesDirectory + File.separator + newClient.getId() + ".ser";

        try (FileOutputStream fileOut = new FileOutputStream(filePath);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

            objectOut.writeObject(newClient); // Écriture de l'objet User sérialisé dans le fichier
            System.out.println("Le profil de l'utilisateur a été créé avec succès à l'emplacement : " + filePath);
        } catch (Exception ex) {
            throw new Exception("Erreur lors de la création du profil : " + ex.getMessage());
        }

    }
    @Override
    public Boolean checkCredentials(String user, String pw) throws Exception {
        String directoryPath = "profiles";
        File directory = new File(directoryPath);

        if (directory.isDirectory()) {
            File[] files = directory.listFiles(); // Liste des fichiers du répertoire

            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        // Manipulation de chaque fichier
                        System.out.println("Nom du fichier : " + file.getName());
                        try (FileInputStream fileInputStream = new FileInputStream(file)) {
                            ObjectInputStream objStream = new ObjectInputStream(fileInputStream);
                            Object obj = objStream.readObject();
                            if (obj instanceof User) {
                                User userFromFile = (User) obj;
                                if (userFromFile.getPassword().equals(pw) && userFromFile.getUsername().equals(user)) {
                                    this.dataRepository.user = userFromFile;
                                    this.dataRepository.connectedUsers.put(userFromFile.toUserLite(),null);
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
        this.dataRepository.getConnectedUsers().put(userLite.getId(),);
        this.notify(userLite, UserLite.class, ModelUpdateTypes.NEW_USER);
    }

    @Override
    public void setConnectedUsers(List<UserLite> newConnectedUsers) {
        final HashMap<UserLite, List<TrackLite>> connectedUsers = this.dataRepository.getConnectedUsers();
        connectedUsers.clear();
        connectedUsers.put(newConnectedUsers);

        // TODO change here, to be able to notify without data OR to notify with an array
        final UserLite u = new UserLite(UUID.randomUUID(), "");
        this.notify(u, UserLite.class, ModelUpdateTypes.NEW_USERS);
    }


    @Override
    public void deleteUser(UserLite userLite) throws Exception {
        this.dataRepository.getConnectedUsers().remove(userLite);
        this.notify(userLite, UserLite.class, ModelUpdateTypes.DELETE_USER);
    }
    @Override
    public void updateUser(User user) throws Exception {}
    @Override
    public void deleteAllUsers() {}

 */
}
