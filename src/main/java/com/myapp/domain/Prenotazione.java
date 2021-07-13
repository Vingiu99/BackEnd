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
 * A Prenotazione.
 */
@Entity
@Table(name = "prenotazione")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Prenotazione implements Serializable {

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
    @Column(name = "luogo_vaccino", nullable = false)
    private String luogoVaccino;

    @NotNull
    @Column(name = "data_vaccino", nullable = false)
    private ZonedDateTime dataVaccino;

    @JsonIgnoreProperties(value = { "prenotazione" }, allowSetters = true)
    @OneToOne(mappedBy = "prenotazione")
    private Paziente paziente;

    @ManyToMany(mappedBy = "prenotaziones")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "prenotaziones" }, allowSetters = true)
    private Set<Operatore> operatores = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Prenotazione id(Long id) {
        this.id = id;
        return this;
    }

    public String getCodiceFiscale() {
        return this.codiceFiscale;
    }

    public Prenotazione codiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
        return this;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public String getTesseraSanitaria() {
        return this.tesseraSanitaria;
    }

    public Prenotazione tesseraSanitaria(String tesseraSanitaria) {
        this.tesseraSanitaria = tesseraSanitaria;
        return this;
    }

    public void setTesseraSanitaria(String tesseraSanitaria) {
        this.tesseraSanitaria = tesseraSanitaria;
    }

    public String getNome() {
        return this.nome;
    }

    public Prenotazione nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return this.cognome;
    }

    public Prenotazione cognome(String cognome) {
        this.cognome = cognome;
        return this;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public ZonedDateTime getDataNascita() {
        return this.dataNascita;
    }

    public Prenotazione dataNascita(ZonedDateTime dataNascita) {
        this.dataNascita = dataNascita;
        return this;
    }

    public void setDataNascita(ZonedDateTime dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getLuogoNascita() {
        return this.luogoNascita;
    }

    public Prenotazione luogoNascita(String luogoNascita) {
        this.luogoNascita = luogoNascita;
        return this;
    }

    public void setLuogoNascita(String luogoNascita) {
        this.luogoNascita = luogoNascita;
    }

    public String getLuogoResidenza() {
        return this.luogoResidenza;
    }

    public Prenotazione luogoResidenza(String luogoResidenza) {
        this.luogoResidenza = luogoResidenza;
        return this;
    }

    public void setLuogoResidenza(String luogoResidenza) {
        this.luogoResidenza = luogoResidenza;
    }

    public String getEmail() {
        return this.email;
    }

    public Prenotazione email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Prenotazione telefono(String telefono) {
        this.telefono = telefono;
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getLuogoVaccino() {
        return this.luogoVaccino;
    }

    public Prenotazione luogoVaccino(String luogoVaccino) {
        this.luogoVaccino = luogoVaccino;
        return this;
    }

    public void setLuogoVaccino(String luogoVaccino) {
        this.luogoVaccino = luogoVaccino;
    }

    public ZonedDateTime getDataVaccino() {
        return this.dataVaccino;
    }

    public Prenotazione dataVaccino(ZonedDateTime dataVaccino) {
        this.dataVaccino = dataVaccino;
        return this;
    }

    public void setDataVaccino(ZonedDateTime dataVaccino) {
        this.dataVaccino = dataVaccino;
    }

    public Paziente getPaziente() {
        return this.paziente;
    }

    public Prenotazione paziente(Paziente paziente) {
        this.setPaziente(paziente);
        return this;
    }

    public void setPaziente(Paziente paziente) {
        if (this.paziente != null) {
            this.paziente.setPrenotazione(null);
        }
        if (paziente != null) {
            paziente.setPrenotazione(this);
        }
        this.paziente = paziente;
    }

    public Set<Operatore> getOperatores() {
        return this.operatores;
    }

    public Prenotazione operatores(Set<Operatore> operatores) {
        this.setOperatores(operatores);
        return this;
    }

    public Prenotazione addOperatore(Operatore operatore) {
        this.operatores.add(operatore);
        operatore.getPrenotaziones().add(this);
        return this;
    }

    public Prenotazione removeOperatore(Operatore operatore) {
        this.operatores.remove(operatore);
        operatore.getPrenotaziones().remove(this);
        return this;
    }

    public void setOperatores(Set<Operatore> operatores) {
        if (this.operatores != null) {
            this.operatores.forEach(i -> i.removePrenotazione(this));
        }
        if (operatores != null) {
            operatores.forEach(i -> i.addPrenotazione(this));
        }
        this.operatores = operatores;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Prenotazione)) {
            return false;
        }
        return id != null && id.equals(((Prenotazione) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Prenotazione{" +
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
            ", luogoVaccino='" + getLuogoVaccino() + "'" +
            ", dataVaccino='" + getDataVaccino() + "'" +
            "}";
    }
}
