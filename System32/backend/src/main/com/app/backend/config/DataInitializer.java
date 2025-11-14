package com.app.backend.config;

import com.app.backend.model.User;
import com.app.backend.model.Category;
import com.app.backend.repository.UserRepository;
import com.app.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Ejecutando DataInitializer");

        //Eliminar y recrear usuarios para asegurar contraseñas correctas 

        if(userRepository.existsByUsername("admin")) {
            User exitingAdmin = userRepository.findByUsername("admin").orElse(null);
            if (exitingAdmin != null) {
                userRepository.delete(exitingAdmin);
                System.out.println("usuario AdDMIN existente eliminado");
            }
        }

         if(userRepository.existsByUsername("coordinador")) {
            User exitingCoord = userRepository.findByUsername("coordinador").orElse(null);
            if (exitingCoord != null) {
                userRepository.delete(exitingCoord);
                System.out.println("usuario COORDINADOR existente eliminado");
            }
        }

        //Crear usuaio admin 
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setEmail("admin@app.com");
        admin.setRole(User.Role.ADMIN);
        admin.setActive(true);
        userRepository.save(admin);
        System.out.println("Usuario ADMIN creado - username: admin, password: admin123");
    

        //Crear usuaio COORDINADOR
        User coordinador = new User();
        coordinador.setUsername("coordinador");
        coordinador.setPassword(passwordEncoder.encode("coord123"));
        coordinador.setEmail("coordinador@app.com");
        coordinador.setRole(User.Role.COORDINADOR);
        coordinador.setActive(true);
        userRepository.save(coordinador);
        System.out.println("Usuario COORDINADOR creado - username: coordinador, password: coord123");

        System.out.println("DataInitializer completado exitosamente");
    }



}
