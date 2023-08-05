package com.example.Group_project.Configuration;
// import
import com.example.Group_project.REpository.UserREpository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.Group_project.entities.User;
// user details service is implemented
    public class UserDetailsServiceImpl implements UserDetailsService {
//        inject userRepository
        @Autowired
        private UserREpository userREpository;
//load by username
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

            //get user from database
// load by user name
            User user= userREpository.getUserByUserName(username);
// if not found throw
            if (user== null){
                throw new UsernameNotFoundException("could not found user");
            }
            CustomUserDetails customUserDetails = new CustomUserDetails(user);//..
            return customUserDetails;// return details
        }
    }


