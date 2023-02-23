/*
 * This file is generated by jOOQ.
 */
package com.aimelodije.modeli.generirano.tables;


import com.aimelodije.modeli.generirano.Keys;
import com.aimelodije.modeli.generirano.Public;
import com.aimelodije.modeli.generirano.tables.records.AlbumRecord;

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
public class Album extends TableImpl<AlbumRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.album</code>
     */
    public static final Album ALBUM = new Album();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<AlbumRecord> getRecordType() {
        return AlbumRecord.class;
    }

    /**
     * The column <code>public.album.id</code>.
     */
    public final TableField<AlbumRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.album.umjetnik_id</code>.
     */
    public final TableField<AlbumRecord, Long> UMJETNIK_ID = createField(DSL.name("umjetnik_id"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.album.slika</code>.
     */
    public final TableField<AlbumRecord, String> SLIKA = createField(DSL.name("slika"), SQLDataType.VARCHAR(50), this, "");

    /**
     * The column <code>public.album.naziv</code>.
     */
    public final TableField<AlbumRecord, String> NAZIV = createField(DSL.name("naziv"), SQLDataType.VARCHAR(100).nullable(false).defaultValue(DSL.field("'Moj novi album'::character varying", SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>public.album.datum_dodano</code>.
     */
    public final TableField<AlbumRecord, LocalDate> DATUM_DODANO = createField(DSL.name("datum_dodano"), SQLDataType.LOCALDATE.nullable(false).defaultValue(DSL.field("CURRENT_DATE", SQLDataType.LOCALDATE)), this, "");

    /**
     * The column <code>public.album.opis</code>.
     */
    public final TableField<AlbumRecord, String> OPIS = createField(DSL.name("opis"), SQLDataType.CLOB, this, "");

    private Album(Name alias, Table<AlbumRecord> aliased) {
        this(alias, aliased, null);
    }

    private Album(Name alias, Table<AlbumRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.album</code> table reference
     */
    public Album(String alias) {
        this(DSL.name(alias), ALBUM);
    }

    /**
     * Create an aliased <code>public.album</code> table reference
     */
    public Album(Name alias) {
        this(alias, ALBUM);
    }

    /**
     * Create a <code>public.album</code> table reference
     */
    public Album() {
        this(DSL.name("album"), null);
    }

    public <O extends Record> Album(Table<O> child, ForeignKey<O, AlbumRecord> key) {
        super(child, key, ALBUM);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public Identity<AlbumRecord, Long> getIdentity() {
        return (Identity<AlbumRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<AlbumRecord> getPrimaryKey() {
        return Keys.ALBUM_PKEY;
    }

    @Override
    public List<ForeignKey<AlbumRecord, ?>> getReferences() {
        return Arrays.asList(Keys.ALBUM__ALBUM_UMJETNIK_ID_FKEY);
    }

    private transient Umjetnik _umjetnik;

    /**
     * Get the implicit join path to the <code>public.umjetnik</code> table.
     */
    public Umjetnik umjetnik() {
        if (_umjetnik == null)
            _umjetnik = new Umjetnik(this, Keys.ALBUM__ALBUM_UMJETNIK_ID_FKEY);

        return _umjetnik;
    }

    @Override
    public Album as(String alias) {
        return new Album(DSL.name(alias), this);
    }

    @Override
    public Album as(Name alias) {
        return new Album(alias, this);
    }

    @Override
    public Album as(Table<?> alias) {
        return new Album(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Album rename(String name) {
        return new Album(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Album rename(Name name) {
        return new Album(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Album rename(Table<?> name) {
        return new Album(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row6 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row6<Long, Long, String, String, LocalDate, String> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function6<? super Long, ? super Long, ? super String, ? super String, ? super LocalDate, ? super String, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function6<? super Long, ? super Long, ? super String, ? super String, ? super LocalDate, ? super String, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
