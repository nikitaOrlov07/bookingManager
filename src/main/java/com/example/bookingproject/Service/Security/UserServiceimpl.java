package com.example.bookingproject.Service.Security;

import com.example.bookingproject.Dto.security.RegistrationDto;
import com.example.bookingproject.Model.Comment;

import com.example.bookingproject.Model.Security.RoleEntity;
import com.example.bookingproject.Model.Security.UserEntity;
import com.example.bookingproject.Repository.MessageRepository;
import com.example.bookingproject.Repository.Security.RoleRepository;
import com.example.bookingproject.Repository.Security.UserRepository;
import com.example.bookingproject.Security.SecurityUtil;
import com.example.bookingproject.Service.ChatService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceimpl implements UserService{
    private UserRepository userRepository; private RoleRepository roleRepository;  // implements methods from repositories
    private PasswordEncoder passwordEncoder;  private MessageRepository messageRepository; private ChatService chatService;
    @Autowired
    public UserServiceimpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, MessageRepository messageRepository , ChatService chatService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.messageRepository = messageRepository;
        this.chatService=chatService;
    }

    @Override
    public void saveUser(RegistrationDto registrationDto) {
        UserEntity userEntity = new UserEntity(); // we cant save RegistrationDto to the database because it`s totally different entity
        // create something like mapper
        userEntity.setUsername(registrationDto.getUsername());
        userEntity.setEmail(registrationDto.getEmail());
        userEntity.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        userEntity.setTown(registrationDto.getTown());
        userEntity.setCompanyName(registrationDto.getCompanyName());
        userEntity.setPhoneNumber(registrationDto.getPhoneNumber());
        // User account creation date
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd:MM:yyyy");
        userEntity.setCreationDate(date.format(formatter));
        RoleEntity role = roleRepository.findByName("USER");// по факту "USER"  записывается в переменную role (- В этой строке мы ищем объект RoleEntity, представляющий роль "USER" в системе.)
        userEntity.setRoles(Arrays.asList(role));// даем нашему userEntity (юзеру) роль "USER" (мы назначаем найденную роль "USER" пользователю, устанавливая список ролей пользователя в качестве списка,
        //----------------------------------------------------------------
        userRepository.save(userEntity);
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    @Transactional
    @Override
    public UserEntity findByUsername(String username) {
       return  userRepository.findByUsername(username);
    }

    @Override
    public UserEntity findById(Long userId) {
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        return optionalUser.orElse(null);
    }




    @Override
    public List<UserEntity> search(String query, String type) {
       return null;
    }

    @Override
    public void save(UserEntity currentUser) {
        userRepository.save(currentUser);
    }


    @Override
    public List<UserEntity> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<UserEntity> findAllByLikedComments(Comment comment) {
        return   userRepository.findAllByLikedComments(comment);
    }

    @Override
    public List<UserEntity> findAllByDislikedComments(Comment comment) {
        return userRepository.findAllByDislikedComments(comment);
    }

    @Override
    public void actionComment(String action, Comment comment) {
        UserEntity user = findByUsername(SecurityUtil.getSessionUser());
        if(action.equals("like"))
        {
            if(!user.getLikedComments().contains(comment) && !user.getDislikedComments().contains(comment)) {     // if user did not like this news +
                comment.setLikes(comment.getLikes() + 1);
                log.info("User did not like this comment");
                user.getLikedComments().add(comment);
                userRepository.save(user);
            }
            else if(user.getLikedComments().contains(comment) && !user.getDislikedComments().contains(comment)) { // if user liked this news before
                comment.setLikes(comment.getLikes() - 1);
                log.info("User liked this news");
                user.getLikedComments().remove(comment);
                userRepository.save(user);
            }
            else if(user.getDislikedComments().contains(comment))  // if user disliked this news before
            {
                // remove this news from disliked news
                comment.setDislikes(comment.getDislikes() - 1);
                user.getDislikedComments().remove(comment);
                log.info("remove user dislike");
                // Add this news to liked news
                comment.setLikes(comment.getLikes() + 1);
                user.getLikedComments().add(comment);
                log.info("add user like");
                userRepository.save(user);
            }
        }
        else if(action.equals("dislike"))
        {

            if(!user.getDislikedComments().contains(comment) && !user.getLikedComments().contains(comment)) {
                comment.setDislikes(comment.getDislikes() + 1);
                user.getDislikedComments().add(comment);
                log.info("User did not dislike this news");
                userRepository.save(user);
            }
            else if(user.getDislikedComments().contains(comment) && !user.getLikedComments().contains(comment))
            {
                comment.setDislikes(comment.getDislikes() - 1);
                user.getDislikedComments().remove(comment);
                log.info("User disliked this news");
                userRepository.save(user);
            }
            else if(user.getLikedComments().contains(comment)) // if user liked this news этот метод не происходит
            {
                comment.setLikes(comment.getLikes() - 1);
                user.getLikedComments().remove(comment);
                log.info("remove user like");
                comment.setDislikes(comment.getDislikes() + 1);
                user.getDislikedComments().add(comment);
                log.info("add user dislike");
                userRepository.save(user);
            }
        }
    }

}
