/*
 * This file is generated by jOOQ.
 */
package com.aimelodije.modeli.generirano.tables;


import com.aimelodije.modeli.generirano.Keys;
import com.aimelodije.modeli.generirano.Public;
import com.aimelodije.modeli.generirano.tables.records.AlbumMelodijaRecord;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function2;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row2;
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
public class AlbumMelodija extends TableImpl<AlbumMelodijaRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.album_melodija</code>
     */
    public static final AlbumMelodija ALBUM_MELODIJA = new AlbumMelodija();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<AlbumMelodijaRecord> getRecordType() {
        return AlbumMelodijaRecord.class;
    }

    /**
     * The column <code>public.album_melodija.album_id</code>.
     */
    public final TableField<AlbumMelodijaRecord, Long> ALBUM_ID = createField(DSL.name("album_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.album_melodija.melodija_id</code>.
     */
    public final TableField<AlbumMelodijaRecord, Long> MELODIJA_ID = createField(DSL.name("melodija_id"), SQLDataType.BIGINT.nullable(false), this, "");

    private AlbumMelodija(Name alias, Table<AlbumMelodijaRecord> aliased) {
        this(alias, aliased, null);
    }

    private AlbumMelodija(Name alias, Table<AlbumMelodijaRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.album_melodija</code> table reference
     */
    public AlbumMelodija(String alias) {
        this(DSL.name(alias), ALBUM_MELODIJA);
    }

    /**
     * Create an aliased <code>public.album_melodija</code> table reference
     */
    public AlbumMelodija(Name alias) {
        this(alias, ALBUM_MELODIJA);
    }

    /**
     * Create a <code>public.album_melodija</code> table reference
     */
    public AlbumMelodija() {
        this(DSL.name("album_melodija"), null);
    }

    public <O extends Record> AlbumMelodija(Table<O> child, ForeignKey<O, AlbumMelodijaRecord> key) {
        super(child, key, ALBUM_MELODIJA);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public UniqueKey<AlbumMelodijaRecord> getPrimaryKey() {
        return Keys.ALBUM_MELODIJA_PKEY;
    }

    @Override
    public List<ForeignKey<AlbumMelodijaRecord, ?>> getReferences() {
        return Arrays.asList(Keys.ALBUM_MELODIJA__ALBUM_MELODIJA_ALBUM_ID_FKEY, Keys.ALBUM_MELODIJA__ALBUM_MELODIJA_MELODIJA_ID_FKEY);
    }

    private transient Album _album;
    private transient Melodija _melodija;

    /**
     * Get the implicit join path to the <code>public.album</code> table.
     */
    public Album album() {
        if (_album == null)
            _album = new Album(this, Keys.ALBUM_MELODIJA__ALBUM_MELODIJA_ALBUM_ID_FKEY);

        return _album;
    }

    /**
     * Get the implicit join path to the <code>public.melodija</code> table.
     */
    public Melodija melodija() {
        if (_melodija == null)
            _melodija = new Melodija(this, Keys.ALBUM_MELODIJA__ALBUM_MELODIJA_MELODIJA_ID_FKEY);

        return _melodija;
    }

    @Override
    public AlbumMelodija as(String alias) {
        return new AlbumMelodija(DSL.name(alias), this);
    }

    @Override
    public AlbumMelodija as(Name alias) {
        return new AlbumMelodija(alias, this);
    }

    @Override
    public AlbumMelodija as(Table<?> alias) {
        return new AlbumMelodija(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public AlbumMelodija rename(String name) {
        return new AlbumMelodija(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public AlbumMelodija rename(Name name) {
        return new AlbumMelodija(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public AlbumMelodija rename(Table<?> name) {
        return new AlbumMelodija(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row2<Long, Long> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function2<? super Long, ? super Long, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function2<? super Long, ? super Long, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}