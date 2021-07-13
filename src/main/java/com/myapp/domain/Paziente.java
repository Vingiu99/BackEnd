package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Paziente.
 */
@Entity
@Table(name = "paziente")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Paziente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "codice_fiscale", nullable = false)
    private String codiceFiscale;

    @NotNull
    @Column(name = "tessera_sanitaria", nullable = false)
    private String tesseraSanitaria;

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
    @Column(name = "luogo_residenza", nullable = false)
    private String luogoResidenza;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "telefono", nullable = false)
    private String telefono;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @JsonIgnoreProperties(value = { "paziente", "operatores" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Prenotazione prenotazione;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Paziente id(Long id) {
        this.id = id;
        return this;
    }

    public String getCodiceFiscale() {
        return this.codiceFiscale;
    }

    public Paziente codiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
        return this;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public String getTesseraSanitaria() {
        return this.tesseraSanitaria;
    }

    public Paziente tesseraSanitaria(String tesseraSanitaria) {
        this.tesseraSanitaria = tesseraSanitaria;
        return this;
    }

    public void setTesseraSanitaria(String tesseraSanitaria) {
        this.tesseraSanitaria = tesseraSanitaria;
    }

    public String getNome() {
        return this.nome;
    }

    public Paziente nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return this.cognome;
    }

    public Paziente cognome(String cognome) {
        this.cognome = cognome;
        return this;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public ZonedDateTime getDataNascita() {
        return this.dataNascita;
    }

    public Paziente dataNascita(ZonedDateTime dataNascita) {
        this.dataNascita = dataNascita;
        return this;
    }

    public void setDataNascita(ZonedDateTime dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getLuogoNascita() {
        return this.luogoNascita;
    }

    public Paziente luogoNascita(String luogoNascita) {
        this.luogoNascita = luogoNascita;
        return this;
    }

    public void setLuogoNascita(String luogoNascita) {
        this.luogoNascita = luogoNascita;
    }

    public String getLuogoResidenza() {
        return this.luogoResidenza;
    }

    public Paziente luogoResidenza(String luogoResidenza) {
        this.luogoResidenza = luogoResidenza;
        return this;
    }

    public void setLuogoResidenza(String luogoResidenza) {
        this.luogoResidenza = luogoResidenza;
    }

    public String getEmail() {
        return this.email;
    }

    public Paziente email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Paziente telefono(String telefono) {
        this.telefono = telefono;
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPassword() {
        return this.password;
    }

    public Paziente password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Prenotazione getPrenotazione() {
        return this.prenotazione;
    }

    public Paziente prenotazione(Prenotazione prenotazione) {
        this.setPrenotazione(prenotazione);
        return this;
    }

    public void setPrenotazione(Prenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Paziente)) {
            return false;
        }
        return id != null && id.equals(((Paziente) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Paziente{" +
            "id=" + getId() +
            ", codiceFiscale='" + getCodiceFiscale() + "'" +
            ", tesseraSanitaria='" + getTesseraSanitaria() + "'" +
            ", nome='" + getNome() + "'" +
            ", cognome='" + getCognome() + "'" +
            ", dataNascita='" + getDataNascita() + "'" +
            ", luogoNascita='" + getLuogoNascita() + "'" +
            ", luogoResidenza='" + getLuogoResidenza() + "'" +
            ", email='" + getEmail() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", password='" + getPassword() + "'" +
            "}";
    }
}
