package com.foodie.monolith.service;

import com.foodie.monolith.data.NewUserInformation;
import com.foodie.monolith.data.UserInformation;
import com.foodie.monolith.exception.UserNotFoundException;
import com.foodie.monolith.model.AssignedRole;
import com.foodie.monolith.model.User;
import com.foodie.monolith.model.UserProfile;
import com.foodie.monolith.repository.AssignedRoleRepository;
import com.foodie.monolith.repository.UserProfileRepository;
import com.foodie.monolith.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserProfileRepository userProfileRepository;
    @Autowired
    AssignedRoleRepository assignedRoleRepository;



    @Transactional
    public List<User> getUsers() throws UserNotFoundException {
        List<User>  users = new ArrayList<User>();
        if(userRepository.findAll().isEmpty()){
            throw new UserNotFoundException("No Users to return");
        } else {
            List<User> dbUsers = userRepository.findAll();
            for(User user: dbUsers){
                users.add(user);
            }
            return users;
        }
    }

    @Transactional
    public List<UserInformation> getAllUserInformation() throws Exception {
        List<UserInformation> userInformation = new ArrayList<>();

        if(userProfileRepository.findAll().isEmpty()){
            throw new Exception("Something went wrong, please try again.");
        } else {
            for(UserProfile dbUser: userProfileRepository.findAll()){
                userInformation.add(getUserInformation(dbUser.getUsersId()));
            }
        }

        return userInformation;
    }

    @Transactional
    public UserInformation getUserById(Integer userId) throws UserNotFoundException {
        UserInformation userInformation = null;
        if(userRepository.findById(userId).isEmpty()){
            throw new UserNotFoundException("Location with Id: " + userId + " does not exists. Please try again.");
        } else {
            userInformation = getUserInformation(userId);
        }
        return userInformation;
    }

    @Transactional
    public String createUser(NewUserInformation newUserInformation) {

        System.out.println("newUserInformation");
        System.out.println(newUserInformation);
//        System.out.println(newUserInformation.getUserName());
//        System.out.println(newUserInformation.getPassword());
//        System.out.println(newUserInformation.getRoleId());
//        System.out.println(newUserInformation.getEmail());
//        System.out.println(newUserInformation.getIsActive());
//        System.out.println(newUserInformation.getPhone());
//        System.out.println(newUserInformation.getFirstName());
//        System.out.println(newUserInformation.getLastName());


        User newUser = new User();
        User savedUser = new User();
        newUser.setUsername(newUserInformation.getUserName());
        newUser.setPassword(newUserInformation.getPassword());
        newUser.setActive(newUserInformation.getIsActive());
        savedUser = userRepository.saveAndFlush(newUser);

        if(userRepository.findById(savedUser.getUserId()).isEmpty()){
            return "Something went wrong. Please try again";
        } else {

            // Assign Role to new user;
            AssignedRole newAssignedRole = new AssignedRole();
            AssignedRole savedAssignedRole = new AssignedRole();
            newAssignedRole.setUsersId(savedUser.getUserId());
            newAssignedRole.setRolesId(newUserInformation.getRoleId());

            System.out.println("newAssignedRole");
            System.out.println(newAssignedRole);
            System.out.println("newAssignedRole.RoleId");
            System.out.println(newAssignedRole.getRolesId());
            System.out.println("newAssignedRole.UserId");
            System.out.println(newAssignedRole.getUsersId());

            savedAssignedRole = assignedRoleRepository.saveAndFlush(newAssignedRole);
            if(assignedRoleRepository.findByUsersId(savedAssignedRole.getUsersId()).isEmpty()){
                System.out.println("Role could not be assigned to this user. Please try again.");
            } else {
                System.out.println("Role assigned successfully.");
            }


            // Create Profile to user;
            UserProfile savedUserProfile = new UserProfile();
            UserProfile newUserProfile = new UserProfile();
            newUserProfile.setEmail(newUserInformation.getEmail());
            newUserProfile.setPhone(newUserInformation.getPhone());
            newUserProfile.setFirstName(newUserInformation.getFirstName());
            newUserProfile.setLastName(newUserInformation.getLastName());
            newUserProfile.setUsersId(savedUser.getUserId());
            savedUserProfile = userProfileRepository.saveAndFlush(newUserProfile);
            System.out.println("New User created with Id: " + savedUser.getUserId());
            if(userProfileRepository.findById(savedUserProfile.getUsersId()).isEmpty()){
                return "Something went wrong with User Profile. Please try again";
            } else {
                return "User Profile created with Id: " + savedUserProfile.getUsersId();
            }
        }
    }

//    @Transactional
//    public String createUserProfile(UserProfile newUserProfile) throws UserNotFoundException {
//        UserProfile savedUserProfile = new UserProfile();
//        if(userRepository.findById(newUserProfile.getUsersId()).isEmpty()){
//            throw new UserNotFoundException("User not found for userId: " + newUserProfile.getUsersId() + ". Could not create UserProfile");
//        } else {
//            savedUserProfile = userProfileRepository.saveAndFlush(newUserProfile);
//            if(userProfileRepository.findById(newUserProfile.getUsersId()).isEmpty()){
//                throw new UserNotFoundException("UserProfile not found for userId: " + newUserProfile.getUsersId() + ". Please try again.");
//            } else {
//                return "New UserProfile created from userId: " + savedUserProfile.getUsersId();
//            }
//        }
//    }

    @Transactional
    public String updateUser(Integer userId, User updateUser) throws UserNotFoundException {
        User dbUser = userRepository.findById(userId).orElse(null);;

        if(dbUser == null){
            throw new UserNotFoundException("User with Id: " + userId + "does not exists. Please try again.");
        } else {
            dbUser.setPassword(updateUser.getPassword());

            return "User has been updated successfully";
        }
    }

    @Transactional
    public String setUserActive(UserInformation userInformation) throws UserNotFoundException {
        User userToUpdate = userRepository.findById(userInformation.getUsersId()).orElse(null);

        if(userRepository.findById(userInformation.getUsersId()).isEmpty()) {
            throw new UserNotFoundException("Something went wrong. Please try again");
        } else {
            User updateUser = userRepository.getById(userInformation.getUsersId());
            updateUser.setUserId(userInformation.getUsersId());
            updateUser.setPassword(userToUpdate.getPassword());
            updateUser.setUsername(userInformation.getUserName());
            updateUser.setActive(userInformation.getIsActive());

            System.out.println("setUserActive data");
            System.out.println(updateUser.getUserId());
            System.out.println(updateUser.getUsername());
            System.out.println(updateUser.isActive());


            userRepository.save(updateUser);
            return "User has been updated successfully";
        }
    }

    @Transactional
    public String deleteUser(Integer userId) throws UserNotFoundException {
        User dbUser = userRepository.findById(userId).orElse(null);

        if(dbUser == null){
            throw new UserNotFoundException("User with Id: " + userId + " does not exists. Please try again.");
        } else {
            userRepository.delete(dbUser);
            return "User has been deleted successfully";
        }
    }

    @Transactional
    public UserInformation login(User user) throws UserNotFoundException {
        User dbUser = userRepository.findTopByUsername(user.getUsername()).orElse(null);
        UserInformation userInformation = null;

        if(dbUser == null){
            throw new UserNotFoundException("#1 LOGIN FAILED");
        } else {
            if(!dbUser.getPassword().equals(user.getPassword())){
                System.out.println("found user values");
                System.out.println(dbUser.getUsername());
                System.out.println(dbUser.getPassword());

                System.out.println("submitted user values");
                System.out.println(user.getUsername());
                System.out.println(user.getPassword());


                throw new UserNotFoundException("#2 LOGIN FAILED ");
            } else {
                System.out.println("User logged in ! Sending Token");

                userInformation = getUserInformation(dbUser.getUserId());
                return userInformation;
            }
        }
    }

    @Transactional
    public UserInformation getUserInformation(Integer userId){
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.get();

        Optional<UserProfile> optionalUserProfile = userProfileRepository.findById(userId);
        UserProfile userProfile = optionalUserProfile.get();

        UserInformation userInformation = new UserInformation();

        // set user information
        userInformation.setUsersId(user.getUserId());
        userInformation.setUserName(user.getUsername());
        userInformation.setIsActive(user.isActive());

        // set user profile information
        userInformation.setFirstName(userProfile.getFirstName());
        userInformation.setLastName(userProfile.getLastName());
        userInformation.setEmail(userProfile.getEmail());
        userInformation.setPhone(userProfile.getPhone());

        // set user role information
        if(user.getUserRole() != null){
            System.out.println("User is assigned a role");

            userInformation.setRoleName(user.getUserRole().getRoleName());

//            userInformation.setRoleName(user.getUserRoles()
//                    .stream()
//                    .map(role -> role.getRoleName())
//                    .toString());
        } else {
            System.out.println("no roles for this user");
        }

        // set user image information
        if(userProfile.getProfileImage() != null){
            System.out.println("User is assigned a role");

            userInformation.setProfileImage(userProfile.getProfileImage());

//            userInformation.setRoleName(user.getUserRoles()
//                    .stream()
//                    .map(role -> role.getRoleName())
//                    .toString());
        } else {
            System.out.println("no profile image for this user");
        }

        return userInformation;

    }

}