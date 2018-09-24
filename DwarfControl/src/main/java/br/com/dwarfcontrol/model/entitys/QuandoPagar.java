package br.com.dwarfcontrol.model.entitys;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name="tb_quandopagar")
public class QuandoPagar {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Usuario usuario;

    @Column(nullable=false, length=500)
    private String descricao;

    //Precision e Scale é a maneira de tornar um numero Decimal no Banco
    @Column(nullable=false, precision=10, scale=2)
    private float valor;

    @Temporal(TemporalType.DATE)
    @Column(nullable=false)
    private Calendar data;

    @Column(nullable=false)
    private boolean status = false;

    //Transiente é um campo que não existe no banco mais existe na classe.
    @Transient
    private boolean vencido;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public Calendar getData() {
        return data;
    }

    public void setData(Calendar data) {
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isVencido() {
        return vencido;
    }

    public void setVencido(boolean vencido) {
        this.vencido = vencido;
    }

    @Override
    public String toString() {
        return "QuandoPagar{" +
                "id=" + id +
                ", usuario=" + usuario +
                ", descricao='" + descricao + '\'' +
                ", valor=" + valor +
                ", data=" + data +
                ", status=" + status +
                ", vencido=" + vencido +
                '}';
    }
}
