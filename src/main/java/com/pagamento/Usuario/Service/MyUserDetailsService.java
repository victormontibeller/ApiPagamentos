package com.pagamento.Usuario.Service;

import com.pagamento.Usuario.Model.Usuario;
import com.pagamento.Usuario.Repository.UserRepository;
import com.pagamento.Usuario.Security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> user = userRepository.findByUsername(username).stream().findFirst();
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Usuário não encontrado com username: " + username);
        }
        return new MyUserDetails(user.get());
    }
}
