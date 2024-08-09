package com.pagamento.Usuario.Service;

import com.pagamento.Cliente.Model.Endereco;
import com.pagamento.Exception.ResourceNotFoundException;
import com.pagamento.Exception.ServiceException;
import com.pagamento.Usuario.DTO.RegisterRequest;
import com.pagamento.Usuario.Model.Usuario;
import com.pagamento.Usuario.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public RegisterRequest saveUser(Usuario registerRequest) throws ResourceNotFoundException {

        List<Usuario> user = userRepository.findByUsername(registerRequest.getUsername());
        if (!user.isEmpty())  {
            throw new ResourceNotFoundException("Usuário já cadastrado: " + registerRequest.getUsername());
        }

        Usuario usuario = toEntity(registerRequest);
        try {
            usuario = userRepository.save(usuario);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Erro ao inserir o usuário: " + e.getMessage());
        }
        return toDTO(usuario);
    }

    public List<Usuario> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<Usuario> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Usuario updateUser(Long id, Usuario userDetails) {
        Usuario user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário nao encontrado com o id: " + id));
        user.setUsername(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        Usuario user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário nao encontrado com o id: " + id));
        userRepository.delete(user);
    }

    public RegisterRequest toDTO(Usuario usuario) {
        return new RegisterRequest(
                usuario.getUsername(),
                usuario.getPassword());
    }

    public Usuario toEntity(Usuario registerRequest) {
        Usuario usuario = new Usuario();
        usuario.setUsername(registerRequest.getUsername());
        usuario.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        return usuario;
    }

    public Optional<Usuario> findByUsername(String usuario) {
        return null;
    }
}

