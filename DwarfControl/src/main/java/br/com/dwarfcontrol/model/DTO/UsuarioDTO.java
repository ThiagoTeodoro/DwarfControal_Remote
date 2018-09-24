package br.com.dwarfcontrol.model.DTO;

import br.com.dwarfcontrol.model.entitys.Usuario;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "nome",
        "email",
        "senha",
        "nivel",
        "ativo"
})
public class UsuarioDTO {

    @JsonProperty("nome")
    private String nome;

    @JsonProperty("email")
    private String email;

    @JsonProperty("senha")
    private String senha;

    @JsonProperty("nivel")
    private int nivel;

    @JsonProperty("ativo")
    private boolean ativo;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return "UsuarioDTO{" +
                "nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                ", nivel=" + nivel +
                '}';
    }

    /***
     * Método responsável por converter um UsuarioDTO para um Usuario
     *
     * @return
     */
    public Usuario toUsuario(){

        Usuario usuario = new Usuario();
        usuario.setNome(this.nome);
        usuario.setSenha(this.senha);
        usuario.setNivel(this.nivel);
        usuario.setEmail(this.email);
        usuario.setAtivo(this.ativo);

        return usuario;

    }
}
