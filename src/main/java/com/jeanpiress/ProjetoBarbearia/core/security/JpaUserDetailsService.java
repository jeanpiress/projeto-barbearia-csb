package com.jeanpiress.ProjetoBarbearia.core.security;

import com.jeanpiress.ProjetoBarbearia.domain.model.Usuario;
import com.jeanpiress.ProjetoBarbearia.domain.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado"));

        return new AuthUser(usuario, getAuthorities(usuario));
    }

    private Collection<GrantedAuthority> getAuthorities(Usuario usuario) {
        return usuario.getPermissoes().stream()
                .map(permissao -> new SimpleGrantedAuthority(permissao.getNome().toUpperCase()))
                .collect(Collectors.toSet());

    }
}
