package use_case.clear_users;

// TODO Complete me

import java.util.Set;

public interface ClearUserDataAccessInterface {

    Set<String> getAllUsernames();
    void deleteUser(Set<String> usernames);

}