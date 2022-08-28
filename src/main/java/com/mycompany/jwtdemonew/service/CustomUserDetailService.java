package com.mycompany.jwtdemonew.service;

import com.mycompany.jwtdemonew.entity.RoleEntity;
import com.mycompany.jwtdemonew.entity.UserEntity;
import com.mycompany.jwtdemonew.model.RoleModel;
import com.mycompany.jwtdemonew.model.UserModel;
import com.mycompany.jwtdemonew.repository.RoleRepository;
import com.mycompany.jwtdemonew.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Lazy//in order to avoid BeanCurrentlyInCreationException
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserModel register(UserModel userModel){

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userModel, userEntity);//it does not do a deep copy

        Set<RoleEntity> roleEntities = new HashSet<>();
        //fetch every role from DB based on role id and then set this role to user entity roles
        for(RoleModel rm: userModel.getRoles()){
            Optional<RoleEntity> optRole = roleRepository.findById(rm.getId());
            if( optRole.isPresent()){
                roleEntities.add(optRole.get());
            }
        }
        userEntity.setRoles(roleEntities);
        userEntity.setPassword(this.passwordEncoder.encode(userEntity.getPassword()));
        userEntity = userRepository.save(userEntity);

        BeanUtils.copyProperties(userEntity, userModel);

        //convert RoleEntities to RoleModels
        Set<RoleModel> roleModels = new HashSet<>();
        RoleModel rm = null;
        for(RoleEntity re: userEntity.getRoles()){
            rm = new RoleModel();
            //BeanUtils.copyProperties(re, rm);
            rm.setRoleName(re.getRoleName());
            rm.setId(re.getId());
            roleModels.add(rm);
        }
        userModel.setRoles(roleModels);
        return userModel;
    }

    //this method actually does the validation for user existence
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByUsername(userName);

        if(userEntity == null){//here we can make a DB call with the help of repository and do the validation
            throw new UsernameNotFoundException("User does not exists!");
        }

        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userEntity, userModel);

        //convert RoleEntities to RoleModels
        Set<RoleModel> roleModels = new HashSet<>();
        RoleModel rm = null;
        for(RoleEntity re: userEntity.getRoles()){
            rm = new RoleModel();
            //BeanUtils.copyProperties(re, rm);
            rm.setRoleName(re.getRoleName());
            rm.setId(re.getId());
            roleModels.add(rm);
        }

        userModel.setRoles(roleModels);
        return userModel;
    }
}
