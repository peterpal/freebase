/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package sk.stuba.fiit.freebase.hadoop;  
@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class TopicAvro extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"TopicAvro\",\"namespace\":\"sk.stuba.fiit.freebase.hadoop\",\"fields\":[{\"name\":\"title\",\"type\":\"string\"},{\"name\":\"types\",\"type\":{\"type\":\"array\",\"items\":\"string\"}},{\"name\":\"alts\",\"type\":{\"type\":\"array\",\"items\":\"string\"}}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  @Deprecated public java.lang.CharSequence title;
  @Deprecated public java.util.List<java.lang.CharSequence> types;
  @Deprecated public java.util.List<java.lang.CharSequence> alts;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>. 
   */
  public TopicAvro() {}

  /**
   * All-args constructor.
   */
  public TopicAvro(java.lang.CharSequence title, java.util.List<java.lang.CharSequence> types, java.util.List<java.lang.CharSequence> alts) {
    this.title = title;
    this.types = types;
    this.alts = alts;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return title;
    case 1: return types;
    case 2: return alts;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: title = (java.lang.CharSequence)value$; break;
    case 1: types = (java.util.List<java.lang.CharSequence>)value$; break;
    case 2: alts = (java.util.List<java.lang.CharSequence>)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'title' field.
   */
  public java.lang.CharSequence getTitle() {
    return title;
  }

  /**
   * Sets the value of the 'title' field.
   * @param value the value to set.
   */
  public void setTitle(java.lang.CharSequence value) {
    this.title = value;
  }

  /**
   * Gets the value of the 'types' field.
   */
  public java.util.List<java.lang.CharSequence> getTypes() {
    return types;
  }

  /**
   * Sets the value of the 'types' field.
   * @param value the value to set.
   */
  public void setTypes(java.util.List<java.lang.CharSequence> value) {
    this.types = value;
  }

  /**
   * Gets the value of the 'alts' field.
   */
  public java.util.List<java.lang.CharSequence> getAlts() {
    return alts;
  }

  /**
   * Sets the value of the 'alts' field.
   * @param value the value to set.
   */
  public void setAlts(java.util.List<java.lang.CharSequence> value) {
    this.alts = value;
  }

  /** Creates a new TopicAvro RecordBuilder */
  public static sk.stuba.fiit.freebase.hadoop.TopicAvro.Builder newBuilder() {
    return new sk.stuba.fiit.freebase.hadoop.TopicAvro.Builder();
  }
  
  /** Creates a new TopicAvro RecordBuilder by copying an existing Builder */
  public static sk.stuba.fiit.freebase.hadoop.TopicAvro.Builder newBuilder(sk.stuba.fiit.freebase.hadoop.TopicAvro.Builder other) {
    return new sk.stuba.fiit.freebase.hadoop.TopicAvro.Builder(other);
  }
  
  /** Creates a new TopicAvro RecordBuilder by copying an existing TopicAvro instance */
  public static sk.stuba.fiit.freebase.hadoop.TopicAvro.Builder newBuilder(sk.stuba.fiit.freebase.hadoop.TopicAvro other) {
    return new sk.stuba.fiit.freebase.hadoop.TopicAvro.Builder(other);
  }
  
  /**
   * RecordBuilder for TopicAvro instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<TopicAvro>
    implements org.apache.avro.data.RecordBuilder<TopicAvro> {

    private java.lang.CharSequence title;
    private java.util.List<java.lang.CharSequence> types;
    private java.util.List<java.lang.CharSequence> alts;

    /** Creates a new Builder */
    private Builder() {
      super(sk.stuba.fiit.freebase.hadoop.TopicAvro.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(sk.stuba.fiit.freebase.hadoop.TopicAvro.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.title)) {
        this.title = data().deepCopy(fields()[0].schema(), other.title);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.types)) {
        this.types = data().deepCopy(fields()[1].schema(), other.types);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.alts)) {
        this.alts = data().deepCopy(fields()[2].schema(), other.alts);
        fieldSetFlags()[2] = true;
      }
    }
    
    /** Creates a Builder by copying an existing TopicAvro instance */
    private Builder(sk.stuba.fiit.freebase.hadoop.TopicAvro other) {
            super(sk.stuba.fiit.freebase.hadoop.TopicAvro.SCHEMA$);
      if (isValidValue(fields()[0], other.title)) {
        this.title = data().deepCopy(fields()[0].schema(), other.title);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.types)) {
        this.types = data().deepCopy(fields()[1].schema(), other.types);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.alts)) {
        this.alts = data().deepCopy(fields()[2].schema(), other.alts);
        fieldSetFlags()[2] = true;
      }
    }

    /** Gets the value of the 'title' field */
    public java.lang.CharSequence getTitle() {
      return title;
    }
    
    /** Sets the value of the 'title' field */
    public sk.stuba.fiit.freebase.hadoop.TopicAvro.Builder setTitle(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.title = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'title' field has been set */
    public boolean hasTitle() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'title' field */
    public sk.stuba.fiit.freebase.hadoop.TopicAvro.Builder clearTitle() {
      title = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'types' field */
    public java.util.List<java.lang.CharSequence> getTypes() {
      return types;
    }
    
    /** Sets the value of the 'types' field */
    public sk.stuba.fiit.freebase.hadoop.TopicAvro.Builder setTypes(java.util.List<java.lang.CharSequence> value) {
      validate(fields()[1], value);
      this.types = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'types' field has been set */
    public boolean hasTypes() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'types' field */
    public sk.stuba.fiit.freebase.hadoop.TopicAvro.Builder clearTypes() {
      types = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /** Gets the value of the 'alts' field */
    public java.util.List<java.lang.CharSequence> getAlts() {
      return alts;
    }
    
    /** Sets the value of the 'alts' field */
    public sk.stuba.fiit.freebase.hadoop.TopicAvro.Builder setAlts(java.util.List<java.lang.CharSequence> value) {
      validate(fields()[2], value);
      this.alts = value;
      fieldSetFlags()[2] = true;
      return this; 
    }
    
    /** Checks whether the 'alts' field has been set */
    public boolean hasAlts() {
      return fieldSetFlags()[2];
    }
    
    /** Clears the value of the 'alts' field */
    public sk.stuba.fiit.freebase.hadoop.TopicAvro.Builder clearAlts() {
      alts = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    @Override
    public TopicAvro build() {
      try {
        TopicAvro record = new TopicAvro();
        record.title = fieldSetFlags()[0] ? this.title : (java.lang.CharSequence) defaultValue(fields()[0]);
        record.types = fieldSetFlags()[1] ? this.types : (java.util.List<java.lang.CharSequence>) defaultValue(fields()[1]);
        record.alts = fieldSetFlags()[2] ? this.alts : (java.util.List<java.lang.CharSequence>) defaultValue(fields()[2]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}