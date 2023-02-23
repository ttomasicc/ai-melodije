/*
 * This file is generated by jOOQ.
 */
package com.aimelodije.modeli.generirano.tables;


import com.aimelodije.modeli.generirano.Keys;
import com.aimelodije.modeli.generirano.Public;
import com.aimelodije.modeli.generirano.tables.records.MelodijaRecord;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function6;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row6;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Melodija extends TableImpl<MelodijaRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.melodija</code>
     */
    public static final Melodija MELODIJA = new Melodija();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<MelodijaRecord> getRecordType() {
        return MelodijaRecord.class;
    }

    /**
     * The column <code>public.melodija.id</code>.
     */
    public final TableField<MelodijaRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.melodija.autor_id</code>.
     */
    public final TableField<MelodijaRecord, Long> AUTOR_ID = createField(DSL.name("autor_id"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.melodija.zanr_id</code>.
     */
    public final TableField<MelodijaRecord, Long> ZANR_ID = createField(DSL.name("zanr_id"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.melodija.audio</code>.
     */
    public final TableField<MelodijaRecord, String> AUDIO = createField(DSL.name("audio"), SQLDataType.VARCHAR(50).nullable(false), this, "");

    /**
     * The column <code>public.melodija.naziv</code>.
     */
    public final TableField<MelodijaRecord, String> NAZIV = createField(DSL.name("naziv"), SQLDataType.VARCHAR(100).nullable(false).defaultValue(DSL.field("'Moja nova melodija'::character varying", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>public.melodija.datum_dodano</code>.
     */
    public final TableField<MelodijaRecord, LocalDate> DATUM_DODANO = createField(DSL.name("datum_dodano"), SQLDataType.LOCALDATE.nullable(false).defaultValue(DSL.field("CURRENT_DATE", SQLDataType.LOCALDATE)), this, "");

    private Melodija(Name alias, Table<MelodijaRecord> aliased) {
        this(alias, aliased, null);
    }

    private Melodija(Name alias, Table<MelodijaRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.melodija</code> table reference
     */
    public Melodija(String alias) {
        this(DSL.name(alias), MELODIJA);
    }

    /**
     * Create an aliased <code>public.melodija</code> table reference
     */
    public Melodija(Name alias) {
        this(alias, MELODIJA);
    }

    /**
     * Create a <code>public.melodija</code> table reference
     */
    public Melodija() {
        this(DSL.name("melodija"), null);
    }

    public <O extends Record> Melodija(Table<O> child, ForeignKey<O, MelodijaRecord> key) {
        super(child, key, MELODIJA);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public Identity<MelodijaRecord, Long> getIdentity() {
        return (Identity<MelodijaRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<MelodijaRecord> getPrimaryKey() {
        return Keys.MELODIJA_PKEY;
    }

    @Override
    public List<ForeignKey<MelodijaRecord, ?>> getReferences() {
        return Arrays.asList(Keys.MELODIJA__MELODIJA_AUTOR_ID_FKEY, Keys.MELODIJA__MELODIJA_ZANR_ID_FKEY);
    }

    private transient Umjetnik _umjetnik;
    private transient Zanr _zanr;

    /**
     * Get the implicit join path to the <code>public.umjetnik</code> table.
     */
    public Umjetnik umjetnik() {
        if (_umjetnik == null)
            _umjetnik = new Umjetnik(this, Keys.MELODIJA__MELODIJA_AUTOR_ID_FKEY);

        return _umjetnik;
    }

    /**
     * Get the implicit join path to the <code>public.zanr</code> table.
     */
    public Zanr zanr() {
        if (_zanr == null)
            _zanr = new Zanr(this, Keys.MELODIJA__MELODIJA_ZANR_ID_FKEY);

        return _zanr;
    }

    @Override
    public Melodija as(String alias) {
        return new Melodija(DSL.name(alias), this);
    }

    @Override
    public Melodija as(Name alias) {
        return new Melodija(alias, this);
    }

    @Override
    public Melodija as(Table<?> alias) {
        return new Melodija(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Melodija rename(String name) {
        return new Melodija(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Melodija rename(Name name) {
        return new Melodija(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Melodija rename(Table<?> name) {
        return new Melodija(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row6 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row6<Long, Long, Long, String, String, LocalDate> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function6<? super Long, ? super Long, ? super Long, ? super String, ? super String, ? super LocalDate, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function6<? super Long, ? super Long, ? super Long, ? super String, ? super String, ? super LocalDate, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}