package com.jeanpiress.ProjetoBarbearia.core.security;

import com.jeanpiress.ProjetoBarbearia.domain.model.Usuario;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class AuthUser extends User {
    private static  final long serialVersionUID = 1L;

    private String nomeCompleto;
    private Long idUsuario;
    private String nomeExibicao;

    public AuthUser(Usuario usuario, Collection<? extends GrantedAuthority> authorities) {
       super(usuario.getEmail(), usuario.getSenha(), authorities);

       if(usuario.getCliente() != null) {
           this.nomeCompleto = usuario.getCliente().getNome();
           this.idUsuario = usuario.getCliente().getId();
           this.nomeExibicao = null;
       }
       if(usuario.getProfissional() != null){
           this.nomeCompleto = usuario.getProfissional().getNome();
           this.idUsuario = usuario.getProfissional().getId();
           this.nomeExibicao = usuario.getProfissional().getNome();
       }
    }
}
