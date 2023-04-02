package com.foodie.monolith.service;

import com.foodie.monolith.data.NewUserInformation;
import com.foodie.monolith.data.UserInformation;
import com.foodie.monolith.exception.*;
import com.foodie.monolith.model.*;
import com.foodie.monolith.payload.request.SignupRequest;
import com.foodie.monolith.payload.response.MessageResponse;
import com.foodie.monolith.repository.AssignedRoleRepository;
import com.foodie.monolith.repository.RoleRepository;
import com.foodie.monolith.repository.UserProfileRepository;
import com.foodie.monolith.repository.UserRepository;
import com.foodie.monolith.security.jwt.JwtUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    AssignedRoleRepository assignedRoleRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserProfileRepository userProfileRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;



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
    public UserInformation getUserById(String foodieCookie, Long userId) throws NotCurrentUserException, UserNotFoundException {
        // User: id, username, password, isActive, userRoles
        // UserInformation: usersId, userName, isActive, roles, email, phone, firstName, lastName, profileImage
        // Note: profileImage is empty by default, user has the option to upload profile image after successful signup

        // 1. Check if user exists in database.
        // 2. Check current cookie is valid for user.
        // 3. Return ResponseEntity.OK and userInformation if successful.

        // Capturing user to verify userName matches cookie userName
        User dbUser = userRepository.getById(userId);
        UserInformation userInformation = new UserInformation();

        // Check user exists.
        if(userRepository.findById(userId).isEmpty())
            throw new UserNotFoundException("User with Id: " + userId + " does not exists.");

        System.out.println("username: ....");
        System.out.println(dbUser.getUsername());
        System.out.println("cookie userName: ....");
        System.out.println(jwtUtils.getUserNameFromJwtToken(foodieCookie));

        // Check user has currently active jwtCookie
        if(!dbUser.getUsername().equals(jwtUtils.getUserNameFromJwtToken(foodieCookie)) ) {
            throw new NotCurrentUserException("User cookie not valid.");
        } else {
            System.out.println("User cookie was valid.");
        }

        System.out.println("grabbing user by id: " + userId);
        userInformation = getUserInformation(userId);

        return userInformation;
    }

//    @Transactional
//    public String createUser(NewUserInformation newUserInformation) {
//
//        System.out.println("newUserInformation");
////        System.out.println(newUserInformation);
////        System.out.println(newUserInformation.getUserName());
////        System.out.println(newUserInformation.getPassword());
////        System.out.println(newUserInformation.getRoleId());
////        System.out.println(newUserInformation.getEmail());
////        System.out.println(newUserInformation.getIsActive());
////        System.out.println(newUserInformation.getPhone());
////        System.out.println(newUserInformation.getFirstName());
////        System.out.println(newUserInformation.getLastName());
//
//
//        User newUser = new User();
//        User savedUser = new User();
//        newUser.setUsername(newUserInformation.getUserName());
//        newUser.setPassword(newUserInformation.getPassword());
//        newUser.setActive(newUserInformation.getIsActive());
//        savedUser = userRepository.saveAndFlush(newUser);
//
//        if(userRepository.findById(savedUser.getUserId()).isEmpty()){
//            return "Something went wrong. Please try again";
//        } else {
//
//            // Assign Role to new user;
//            AssignedRole newAssignedRole = new AssignedRole();
//            AssignedRole savedAssignedRole = new AssignedRole();
//            newAssignedRole.setUsersId(savedUser.getUserId());
//            newAssignedRole.setRolesId(newUserInformation.getRoleId());
//
////            System.out.println("newAssignedRole");
////            System.out.println(newAssignedRole);
////            System.out.println("newAssignedRole.RoleId");
////            System.out.println(newAssignedRole.getRolesId());
////            System.out.println("newAssignedRole.UserId");
////            System.out.println(newAssignedRole.getUsersId());
//
//            savedAssignedRole = assignedRoleRepository.saveAndFlush(newAssignedRole);
//            if(assignedRoleRepository.findByUsersId(savedAssignedRole.getUsersId()).isEmpty()){
//                System.out.println("Role could not be assigned to this user. Please try again.");
//            } else {
//                System.out.println("Role assigned successfully.");
//            }
//
//
//            // Create Profile to user;
//            UserProfile savedUserProfile = new UserProfile();
//            UserProfile newUserProfile = new UserProfile();
//            newUserProfile.setEmail(newUserInformation.getEmail());
//            newUserProfile.setPhone(newUserInformation.getPhone());
//            newUserProfile.setFirstName(newUserInformation.getFirstName());
//            newUserProfile.setLastName(newUserInformation.getLastName());
//            newUserProfile.setUsersId(savedUser.getUserId());
//            savedUserProfile = userProfileRepository.saveAndFlush(newUserProfile);
//            System.out.println("New User created with Id: " + savedUser.getUserId());
//            if(userProfileRepository.findById(savedUserProfile.getUsersId()).isEmpty()){
//                return "Something went wrong with User Profile. Please try again";
//            } else {
//                return "User Profile created with Id: " + savedUserProfile.getUsersId();
//            }
//        }
//    }

    @Transactional
    public String registerUser(SignupRequest signupRequest) throws EmailTakenException, PhoneTakenException, RoleNotFoundException, UsernameTakenException, UserNotFoundException, UserProfileNotFoundException {
        // User: id, username, password, isActive, userRoles
        // UserProfile: usersId, email, phone, firstName, lastName, profileImage
        // Note: profileImage is empty by default, user has the option to upload profile image after successful signup

        // 1. Check if signupRequest username, email, or phone already exists in our database, since these should all be unique.
        // 2. Check if role(s) from signupRequest exists in our database.
        // 3. Save User to database if all checks pass.
        // 4. Check if user was saved to database.
        // 5. Create UserProfile using the new user id.
        // 6. Check if userProfile was saved to database and return ResponseEntity.OK if successful.

        System.out.println("signUpRequest");
        System.out.println(signupRequest.getUsername());
        System.out.println(signupRequest.getPassword());
        System.out.println(signupRequest.getRole());
        System.out.println(signupRequest.getEmail());
        System.out.println(signupRequest.getPhone());
        System.out.println(signupRequest.getFirstName());
        System.out.println(signupRequest.getLastName());

        // Create new user using signUpRequest username and password
        User user = new User(signupRequest.getUsername(),
                encoder.encode(signupRequest.getPassword()));
        UserProfile userProfile = new UserProfile();

        // Will be used further down to check if our user and profile save was successful.
        User savedUser = new User();
        UserProfile savedUserProfile = new UserProfile();


        // Will be used to save all signup request roles to user after roles are verified.
        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();


        System.out.println("Checking signup requests for uniqueness");

        // Check if unique user information already exists in our database
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            System.out.println("Username already exists!");
            throw new UsernameTakenException("Username is already taken!");
        } else {
            System.out.println("Username does not exists in database");
        }

        if(userProfileRepository.existsByEmail(signupRequest.getEmail())) {
            System.out.println("Email already exists!");
            throw new EmailTakenException("Email is already taken!");
        } else {
            System.out.println("Email does not exists in database");
        }

        if(userProfileRepository.existsByPhone(signupRequest.getPhone())) {
            System.out.println("Phone already exists!");
            throw new PhoneTakenException("Phone is already taken!");
        } else {
            System.out.println("Phone does not exists in database");
        }


        System.out.println("get all ERoles");
//        System.out.println(roleRepository.findAll());
        roleRepository.findAll().forEach(role -> {
            System.out.println("roleId: " + role.getRoleId());
            System.out.println("roleName: " + role.getRoleName());
        });


        if (strRoles == null) {
            Role userRole = roleRepository.findByRoleName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RoleNotFoundException("Error: Role is not found because null."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                System.out.println("role");
                System.out.println(role);

                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByRoleName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RoleNotFoundException("Error: Role admin is not found."));
                        roles.add(adminRole);

                        break;
                    case "owner":
                        Role ownerRole = roleRepository.findByRoleName(ERole.ROLE_OWNER)
                                .orElseThrow(() -> new RoleNotFoundException("Error: Role owner is not found."));
                        roles.add(ownerRole);

                        break;
                    default:

                        System.out.println("default role switch, role not found");
                        System.out.println(roleRepository.findByRoleName(ERole.ROLE_USER));

                        Role userRole = roleRepository.findByRoleName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RoleNotFoundException("Error: Role user is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setUserRoles(roles);
        user.setActive(true);


        // capture the newly created user
        savedUser = userRepository.saveAndFlush(user);
//        savedUser.setUserId(100L);

        // check saved user exists in database
        if (userRepository.findById(savedUser.getUserId()).isEmpty())
            throw new UserNotFoundException("User was not saved successfully!");

        System.out.println("New User created with Id: " + savedUser.getUserId());

        // verify user was correctly assigned roles
        if(assignedRoleRepository.findByUsersId(savedUser.getUserId()).isEmpty())
            throw new RoleNotFoundException("User was not assigned a role");


        // populate userProfile for newly created user
        // uses the new users id
        userProfile.setUsersId(savedUser.getUserId());
        userProfile.setEmail(signupRequest.getEmail());
        userProfile.setPhone(signupRequest.getPhone());
        userProfile.setFirstName(signupRequest.getFirstName());
        userProfile.setLastName(signupRequest.getLastName());

        // capture newly created userProfile
        savedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // check saved profile exists in database and return message to user
        if(userProfileRepository.findById(savedUserProfile.getUsersId()).isEmpty()){
            throw new UserProfileNotFoundException("User profile was not saved successfully!");
        } else {
            return "User registered successfully!";
        }
    }



    @Transactional
    public String updateUser(Long userId, User updateUser) throws UserNotFoundException {
        User dbUser = userRepository.findById(userId).orElse(null);;

        if(dbUser == null){
            throw new UserNotFoundException("User with Id: " + userId + "does not exists. Please try again.");
        } else {
            dbUser.setPassword(updateUser.getPassword());

            return "User has been updated successfully";
        }
    }
    @Transactional
    public String setUserActive(String foodieCookie, UserInformation updateUser) throws NotCurrentUserException, UserNotFoundException {
        // User: id, username, password, isActive, userRoles

        // 1. Check if user exists in database.
        // 2. Check current cookie is valid for user.
        // 3. Save updated user active status to database.
        // 4. Return ResponseEntity.OK and success message.

        System.out.println("setUserActive data");
        // System.out.println(updateUser.getUserId());
        // System.out.println(updateUser.getUsername());
        // System.out.println(updateUser.isActive());

        // Capturing user to verify userName matches cookie UserName and update user
        User dbUser = userRepository.findById(updateUser.getUsersId()).orElse(null);

        // Check user exists.
        if(userRepository.findById(updateUser.getUsersId()).isEmpty())
            throw new UserNotFoundException("User with Id: " +  updateUser.getUsersId() + " does not exists.");

        // Check user has currently active jwtCookie
        if(!dbUser.getUsername().equals(jwtUtils.getUserNameFromJwtToken(foodieCookie)) ) {
            throw new NotCurrentUserException("User cookie not valid.");
        } else {
            System.out.println("User cookie was valid.");
        }

        dbUser.setActive(updateUser.getIsActive());

        userRepository.save(dbUser);
        return "User active statue updated successfully";
    }

    @Transactional
    public String deleteUser(Long userId) throws UserNotFoundException {
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
//        User dbUser = userRepository.findTopByUsername(user.getUsername()).orElse(null);
        User dbUser = userRepository.findByUsername(user.getUsername()).orElse(null);
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

//                userInformation = getUserInformation(dbUser.getUserId());
                return userInformation;
            }
        }
    }

    @Transactional
    public UserInformation getUserInformation(Long userId){
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
        if(user.getUserRoles() != null){
            System.out.println("User is assigned a role");

            userInformation.setRoles(user.getUserRoles());
        } else {
            System.out.println("no roles for this user");
        }

        // set user image information
        if(userProfile.getProfileImage() != null){
            System.out.println("User is assigned a role");

            userInformation.setProfileImage(userProfile.getProfileImage());
        } else {
            System.out.println("no profile image for this user");
        }
        return userInformation;
//        return null;
    }
}