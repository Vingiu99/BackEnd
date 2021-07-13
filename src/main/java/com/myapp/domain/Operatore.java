package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Operatore.
 */
@Entity
@Table(name = "operatore")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Operatore implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "id_asl_operatore", nullable = false)
    private String idAslOperatore;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull
    @Column(name = "cognome", nullable = false)
    private String cognome;

    @NotNull
    @Column(name = "data_nascita", nullable = false)
    private ZonedDateTime dataNascita;

    @NotNull
    @Column(name = "luogo_nascita", nullable = false)
    private String luogoNascita;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "telefono", nullable = false)
    private String telefono;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_operatore__prenotazione",
        joinColumns = @JoinColumn(name = "operatore_id"),
        inverseJoinColumns = @JoinColumn(name = "prenotazione_id")
    )
    @JsonIgnoreProperties(value = { "paziente", "operatores" }, allowSetters = true)
    private Set<Prenotazione> prenotaziones = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Operatore id(Long id) {
        this.id = id;
        return this;
    }

    public String getIdAslOperatore() {
        return this.idAslOperatore;
    }

    public Operatore idAslOperatore(String idAslOperatore) {
        this.idAslOperatore = idAslOperatore;
        return this;
    }

    public void setIdAslOperatore(String idAslOperatore) {
        this.idAslOperatore = idAslOperatore;
    }

    public String getNome() {
        return this.nome;
    }

    public Operatore nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return this.cognome;
    }

    public Operatore cognome(String cognome) {
        this.cognome = cognome;
        return this;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public ZonedDateTime getDataNascita() {
        return this.dataNascita;
    }

    public Operatore dataNascita(ZonedDateTime dataNascita) {
        this.dataNascita = dataNascita;
        return this;
    }

    public void setDataNascita(ZonedDateTime dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getLuogoNascita() {
        return this.luogoNascita;
    }

    public Operatore luogoNascita(String luogoNascita) {
        this.luogoNascita = luogoNascita;
        return this;
    }

    public void setLuogoNascita(String luogoNascita) {
        this.luogoNascita = luogoNascita;
    }

    public String getEmail() {
        return this.email;
    }

    public Operatore email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Operatore telefono(String telefono) {
        this.telefono = telefono;
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPassword() {
        return this.password;
    }

    public Operatore password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Prenotazione> getPrenotaziones() {
        return this.prenotaziones;
    }

    public Operatore prenotaziones(Set<Prenotazione> prenotaziones) {
        this.setPrenotaziones(prenotaziones);
        return this;
    }

    public Operatore addPrenotazione(Prenotazione prenotazione) {
        this.prenotaziones.add(prenotazione);
        prenotazione.getOperatores().add(this);
        return this;
    }

    public Operatore removePrenotazione(Prenotazione prenotazione) {
        this.prenotaziones.remove(prenotazione);
        prenotazione.getOperatores().remove(this);
        return this;
    }

    public void setPrenotaziones(Set<Prenotazione> prenotaziones) {
        this.prenotaziones = prenotaziones;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Operatore)) {
            return false;
        }
        return id != null && id.equals(((Operatore) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Operatore{" +
            "id=" + getId() +
            ", idAslOperatore='" + getIdAslOperatore() + "'" +
            ", nome='" + getNome() + "'" +
            ", cognome='" + getCognome() + "'" +
            ", dataNascita='" + getDataNascita() + "'" +
            ", luogoNascita='" + getLuogoNascita() + "'" +
            ", email='" + getEmail() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", password='" + getPassword() + "'" +
            "}";
    }
}
