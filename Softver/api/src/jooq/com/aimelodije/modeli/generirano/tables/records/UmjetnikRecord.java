/*
 * This file is generated by jOOQ.
 */
package com.aimelodije.modeli.generirano.tables.records;


import com.aimelodije.modeli.generirano.tables.Umjetnik;

import java.time.LocalDate;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record10;
import org.jooq.Row10;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class UmjetnikRecord extends UpdatableRecordImpl<UmjetnikRecord> implements Record10<Long, String, String, String, String, String, String, String, String, LocalDate> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.umjetnik.id</code>.
     */
    public UmjetnikRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.umjetnik.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.umjetnik.rola</code>.
     */
    public UmjetnikRecord setRola(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.umjetnik.rola</code>.
     */
    public String getRola() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.umjetnik.korime</code>.
     */
    public UmjetnikRecord setKorime(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.umjetnik.korime</code>.
     */
    public String getKorime() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.umjetnik.email</code>.
     */
    public UmjetnikRecord setEmail(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>public.umjetnik.email</code>.
     */
    public String getEmail() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.umjetnik.lozinka</code>.
     */
    public UmjetnikRecord setLozinka(String value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>public.umjetnik.lozinka</code>.
     */
    public String getLozinka() {
        return (String) get(4);
    }

    /**
     * Setter for <code>public.umjetnik.ime</code>.
     */
    public UmjetnikRecord setIme(String value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>public.umjetnik.ime</code>.
     */
    public String getIme() {
        return (String) get(5);
    }

    /**
     * Setter for <code>public.umjetnik.prezime</code>.
     */
    public UmjetnikRecord setPrezime(String value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>public.umjetnik.prezime</code>.
     */
    public String getPrezime() {
        return (String) get(6);
    }

    /**
     * Setter for <code>public.umjetnik.opis</code>.
     */
    public UmjetnikRecord setOpis(String value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>public.umjetnik.opis</code>.
     */
    public String getOpis() {
        return (String) get(7);
    }

    /**
     * Setter for <code>public.umjetnik.slika</code>.
     */
    public UmjetnikRecord setSlika(String value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>public.umjetnik.slika</code>.
     */
    public String getSlika() {
        return (String) get(8);
    }

    /**
     * Setter for <code>public.umjetnik.datum_registracije</code>.
     */
    public UmjetnikRecord setDatumRegistracije(LocalDate value) {
        set(9, value);
        return this;
    }

    /**
     * Getter for <code>public.umjetnik.datum_registracije</code>.
     */
    public LocalDate getDatumRegistracije() {
        return (LocalDate) get(9);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record10 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row10<Long, String, String, String, String, String, String, String, String, LocalDate> fieldsRow() {
        return (Row10) super.fieldsRow();
    }

    @Override
    public Row10<Long, String, String, String, String, String, String, String, String, LocalDate> valuesRow() {
        return (Row10) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return Umjetnik.UMJETNIK.ID;
    }

    @Override
    public Field<String> field2() {
        return Umjetnik.UMJETNIK.ROLA;
    }

    @Override
    public Field<String> field3() {
        return Umjetnik.UMJETNIK.KORIME;
    }

    @Override
    public Field<String> field4() {
        return Umjetnik.UMJETNIK.EMAIL;
    }

    @Override
    public Field<String> field5() {
        return Umjetnik.UMJETNIK.LOZINKA;
    }

    @Override
    public Field<String> field6() {
        return Umjetnik.UMJETNIK.IME;
    }

    @Override
    public Field<String> field7() {
        return Umjetnik.UMJETNIK.PREZIME;
    }

    @Override
    public Field<String> field8() {
        return Umjetnik.UMJETNIK.OPIS;
    }

    @Override
    public Field<String> field9() {
        return Umjetnik.UMJETNIK.SLIKA;
    }

    @Override
    public Field<LocalDate> field10() {
        return Umjetnik.UMJETNIK.DATUM_REGISTRACIJE;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getRola();
    }

    @Override
    public String component3() {
        return getKorime();
    }

    @Override
    public String component4() {
        return getEmail();
    }

    @Override
    public String component5() {
        return getLozinka();
    }

    @Override
    public String component6() {
        return getIme();
    }

    @Override
    public String component7() {
        return getPrezime();
    }

    @Override
    public String component8() {
        return getOpis();
    }

    @Override
    public String component9() {
        return getSlika();
    }

    @Override
    public LocalDate component10() {
        return getDatumRegistracije();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getRola();
    }

    @Override
    public String value3() {
        return getKorime();
    }

    @Override
    public String value4() {
        return getEmail();
    }

    @Override
    public String value5() {
        return getLozinka();
    }

    @Override
    public String value6() {
        return getIme();
    }

    @Override
    public String value7() {
        return getPrezime();
    }

    @Override
    public String value8() {
        return getOpis();
    }

    @Override
    public String value9() {
        return getSlika();
    }

    @Override
    public LocalDate value10() {
        return getDatumRegistracije();
    }

    @Override
    public UmjetnikRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public UmjetnikRecord value2(String value) {
        setRola(value);
        return this;
    }

    @Override
    public UmjetnikRecord value3(String value) {
        setKorime(value);
        return this;
    }

    @Override
    public UmjetnikRecord value4(String value) {
        setEmail(value);
        return this;
    }

    @Override
    public UmjetnikRecord value5(String value) {
        setLozinka(value);
        return this;
    }

    @Override
    public UmjetnikRecord value6(String value) {
        setIme(value);
        return this;
    }

    @Override
    public UmjetnikRecord value7(String value) {
        setPrezime(value);
        return this;
    }

    @Override
    public UmjetnikRecord value8(String value) {
        setOpis(value);
        return this;
    }

    @Override
    public UmjetnikRecord value9(String value) {
        setSlika(value);
        return this;
    }

    @Override
    public UmjetnikRecord value10(LocalDate value) {
        setDatumRegistracije(value);
        return this;
    }

    @Override
    public UmjetnikRecord values(Long value1, String value2, String value3, String value4, String value5, String value6, String value7, String value8, String value9, LocalDate value10) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached UmjetnikRecord
     */
    public UmjetnikRecord() {
        super(Umjetnik.UMJETNIK);
    }

    /**
     * Create a detached, initialised UmjetnikRecord
     */
    public UmjetnikRecord(Long id, String rola, String korime, String email, String lozinka, String ime, String prezime, String opis, String slika, LocalDate datumRegistracije) {
        super(Umjetnik.UMJETNIK);

        setId(id);
        setRola(rola);
        setKorime(korime);
        setEmail(email);
        setLozinka(lozinka);
        setIme(ime);
        setPrezime(prezime);
        setOpis(opis);
        setSlika(slika);
        setDatumRegistracije(datumRegistracije);
    }

    /**
     * Create a detached, initialised UmjetnikRecord
     */
    public UmjetnikRecord(com.aimelodije.modeli.generirano.tables.pojos.Umjetnik value) {
        super(Umjetnik.UMJETNIK);

        if (value != null) {
            setId(value.getId());
            setRola(value.getRola());
            setKorime(value.getKorime());
            setEmail(value.getEmail());
            setLozinka(value.getLozinka());
            setIme(value.getIme());
            setPrezime(value.getPrezime());
            setOpis(value.getOpis());
            setSlika(value.getSlika());
            setDatumRegistracije(value.getDatumRegistracije());
        }
    }
}
