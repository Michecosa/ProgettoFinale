package com.example.final_project.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.final_project.Model.Carrello;
import com.example.final_project.Model.Utente;
import com.example.final_project.Repository.CarrelloRepository;
import com.example.final_project.Repository.UtenteRepository;
import com.example.final_project.Security.JwtService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder criptatore;
    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private CarrelloRepository carrelloRepository;
    @Autowired
    private AuthenticationManager authManager;

    @PostMapping("/register")
    public void register(@RequestBody Utente utente) {
        utente.setPassword(criptatore.encode(utente.getPassword()));
        utente.setRoles("ROLE_USER");
        utente = utenteRepository.save(utente);

        Carrello c = new Carrello();
        c.setUtente(utente);
        carrelloRepository.save(c);
    }

    @PostMapping("/login")
    public String login(@RequestBody Utente utente) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(utente.getUsername(), utente.getPassword()));
        return jwtService.generateToken(auth);
    }

    @GetMapping("/test")
    public String dammiNomeUtente(Authentication auth) {
        return auth.getName();
    }
}
