package com.jeanpiress.ProjetoBarbaria.api.dtosModel.input;

import com.jeanpiress.ProjetoBarbaria.api.dtosModel.resumo.PermissaoId;
import com.jeanpiress.ProjetoBarbaria.domain.model.Cliente;
import com.jeanpiress.ProjetoBarbaria.domain.model.Permissao;
import com.jeanpiress.ProjetoBarbaria.domain.model.Profissional;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UsuarioInput {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String senha;

    private Cliente cliente;

    private Profissional profissional;

    private Long permissao;

    private String nome;


}
