package com.pagamento.Usuario.Controller;

import com.pagamento.Usuario.DTO.RegisterRequest;
import com.pagamento.Usuario.Payload.AuthenticationRequest;
import com.pagamento.Usuario.Payload.AuthenticationResponse;
import com.pagamento.Usuario.Security.JwtUtil;
import com.pagamento.Usuario.Service.MyUserDetailsService;
import com.pagamento.Usuario.Service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MyUserDetailsService usuarioService;

    @Autowired
    private UsuarioService userService;

    @PostMapping("/autenticacao")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsuario(), authenticationRequest.getSenha())
        );
        final UserDetails userDetails = usuarioService.loadUserByUsername(authenticationRequest.getUsuario());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> criarUsuario(@Valid @RequestBody RegisterRequest registerRequest) throws Exception {
        RegisterRequest usuarioSalvo = userService.saveUser(registerRequest);
        final String jwt = jwtUtil.generateToken(registerRequest.username());
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}