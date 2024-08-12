package com.pagamento.Usuario.Service;

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

    public RegisterRequest saveUser(RegisterRequest registerRequest) throws ServiceException {
        if(registerRequest == null)
            throw new ServiceException("Requisição vazia");

        List<Usuario> user = userRepository.findByUsername(registerRequest.username());
        if (!user.isEmpty())  {
            throw new ServiceException("Usuário já cadastrado: " + registerRequest.username());
        }

        Usuario usuario = toEntity(registerRequest);
        try {
            usuario = userRepository.save(usuario);
        } catch (Exception e) {
            throw new ServiceException("Erro ao inserir o usuário: " + e.getMessage());
        }
        return toRegisterRequest(usuario);
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

    public RegisterRequest toRegisterRequest(Usuario usuario) {
        return new RegisterRequest(
                usuario.getUsername(),
                usuario.getPassword(), 
                usuario.getCliente());
    }

    public Usuario toEntity(RegisterRequest registerRequest) {
        Usuario usuario = new Usuario();
        usuario.setUsername(registerRequest.username());
        usuario.setPassword(passwordEncoder.encode(registerRequest.password()));
        usuario.setCliente(registerRequest.cliente());

        return usuario;
    }
}

