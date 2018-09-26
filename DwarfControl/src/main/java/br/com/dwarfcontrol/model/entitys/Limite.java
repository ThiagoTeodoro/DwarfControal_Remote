package br.com.dwarfcontrol.model.entitys;

import javax.persistence.*;

@Entity
@Table(name = "tb_limite")
public class Limite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private Double valor;

    @OneToOne
    private Usuario usuario;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Limite{" +
                "id=" + id +
                ", valor=" + valor +
                ", usuario=" + usuario.toString() +
                '}';
    }
}
